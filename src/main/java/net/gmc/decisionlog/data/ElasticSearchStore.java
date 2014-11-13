package net.gmc.decisionlog.data;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gmc.decisionlog.controler.dto.DecisionSearchResponse;
import net.gmc.decisionlog.model.Decision;
import net.gmc.decisionlog.notification.publishing.DecisionEventPublisher;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Component
public class ElasticSearchStore {

    private static final Logger logger = Logger.getLogger(ElasticSearchStore.class);

    public static final String DECISION_TYPE_NAME = "decision";
    private static final String DECISIONS_INDEX_NAME = "decisions";
    private static final String DECISION_LOG = "decisionlog";
    private ObjectMapper objectMapper = new ObjectMapper();

    private static Node node;

    @Value("${demo}")
    private boolean isDemo;

    @Autowired
    private DecisionEventPublisher publisher;

    @PostConstruct
    private void init() throws InterruptedException {
        logger.info("Constructing and running ElasticSearch storage.");
        buildNode();
        createIndex();
        waitUntilElasticSearchIsStarted();
        createSampleData();
    }

    private void waitUntilElasticSearchIsStarted() {
        try {

            ClusterStateResponse clusterState = getClusterState();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ClusterStateResponse getClusterState() throws ExecutionException, InterruptedException {
        return node.client().admin().cluster().prepareState().execute().get();
    }


    private void buildNode() {
        node = nodeBuilder()
                .local(false)
                .clusterName(DECISION_LOG)
                .build().start();
    }

    private void createIndex() {

   //     String mapping = getMappingString();
        try {
            if (!node.client().admin().indices().prepareExists(DECISIONS_INDEX_NAME).execute().get().isExists()) {
                logger.debug("Creating index.");
                node.client().admin().indices().prepareCreate(DECISIONS_INDEX_NAME).execute().get();
                //    node.client().admin().indices().preparePutMapping(DECISIONS_INDEX_NAME).setSource(mapping).setType("_default_").execute().get();
            }
        } catch (Exception e) {
            logger.error("Failed to create index for fulltext search.", e);
        }
    }

    public List<Decision> listAllDecision() {
        SearchResponse searchResponse = getSearchResponse();
        return deserializeDecisionsResponse(searchResponse);
    }

    private List<Decision> deserializeDecisionsResponse(SearchResponse searchResponse) {
        List<Decision> decisions = new ArrayList<Decision>();
        for(SearchHit hit : searchResponse.getHits()){
            String sourceAsString = hit.getSourceAsString();
            Decision decision = deserialize(sourceAsString);
            if (decision != null) {
                decision.setRelevance(hit.getScore());
                decision.setId(hit.getId());
                decisions.add(decision);
            }
        }
        return decisions;
    }

    private Decision deserialize(String sourceAsString) {
        try {
            Decision decision = objectMapper.readValue(sourceAsString, Decision.class);
            return decision;
        } catch (IOException e) {
            logger.error(String.format("Failed to deserialize desicion '%s'", sourceAsString), e);
        }
        return null;
    }


    private SearchResponse getSearchResponse() {
        return node.client().prepareSearch(DECISIONS_INDEX_NAME)
                    .setTypes(DECISION_TYPE_NAME)
                    .setQuery(QueryBuilders.matchAllQuery())
                    .execute()
                    .actionGet();
    }

    public void saveDecision(Decision decision){
        logger.info(String.format("Going to save new decision record: '%s'", decision.toString()));
        String decisionJsonString = convertEntityToJson(decision);
        saveJson(decisionJsonString);
        publisher.publishDecisionAdded(decision);
    }

    private void saveJson(String decisionJsonString) {
        IndexResponse indexResponse = node.client().prepareIndex(DECISIONS_INDEX_NAME, DECISION_TYPE_NAME)
                .setSource(decisionJsonString)
                .execute()
                .actionGet();
    }

    private String convertEntityToJson(Decision decision) {
        String decisionJsonString = null;
        try {
            decisionJsonString = objectMapper.writeValueAsString(decision);
        } catch (JsonProcessingException e) {
            logger.error(String.format("Failed"), e);
        }
        return decisionJsonString;
    }

    @PreDestroy
    private void destroy(){
        logger.info("Gracefully shutting down ElasticSearch node.");
        node.close();
    }

    private void createSampleData() throws InterruptedException {
        if (isDemo) {
            Thread.sleep(5000);
            if (listAllDecision().isEmpty()) {
                Decision sampleDecision1 = new Decision();
                sampleDecision1.setSubject("Long transactions");
                sampleDecision1.setDate(new Date());
                sampleDecision1.setReason("In Interactive Plus we have long transactions which causes performance problem and deadlocks.");
                sampleDecision1.setConclusions("" +
                        "1)\tVašek se podívá na zjednodušení volání SQL při schvalování. Všechny data by se měla načíst EAGER jedním SELECTEM pomocí JOINů přes ALIASY.\n" +
                        "2)\tPetr mrkne na kaskády v databázi, které využijeme při mazání.\n" +
                        "3)\tMartin H. se podívá na optimalizaci operací s DB, při vytváření snapshotů, taky aby se bloby kopírovaly přímo v DB a ne v Javě.\n" +
                        "4)\tOndra koukne na optimalizaci při mazání draftů – budou pouze označeny jako deleted a z DB se smažou později.\n");
                sampleDecision1.setAttendees(new String[]{"m.hatas", "m.jankovsky", "v.stolin", "h.steinmetz", "m.kosar"});
                sampleDecision1.setTags(new String[]{"#performance", "#transactions", "#database", "#deadlock", "#peak"});

                saveDecision(sampleDecision1);
            }
        }

    }

    public DecisionSearchResponse searchDecisions(String keyWord) {
        return new DecisionSearchResponse();
    }

    public Decision getDecision(String decisionId) {
        Decision decision = null;
        try {
            GetResponse response = node.client().prepareGet(DECISIONS_INDEX_NAME, DECISION_TYPE_NAME, decisionId).execute().get();
            String sourceAsString = response.getSourceAsString();
            decision = deserialize(sourceAsString);
            if (decision != null) {
                //decision.setRelevance(response.getScore());
                decision.setId(response.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decision;
    }
}
