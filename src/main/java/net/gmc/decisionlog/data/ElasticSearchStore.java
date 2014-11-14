package net.gmc.decisionlog.data;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gmc.decisionlog.model.Decision;
import net.gmc.decisionlog.notification.publishing.DecisionEventPublisher;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
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
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        buildNode();
        createIndex();;
        createSampleData();
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
            getDecisionsFromHit(decisions, hit);
        }
        return decisions;
    }

    private void getDecisionsFromHit(List<Decision> decisions, SearchHit hit) {
        String sourceAsString = hit.getSourceAsString();
        Decision decision = deserialize(sourceAsString);
        if (decision != null) {
            decision.setRelevance(hit.getScore());
            decision.setId(hit.getId());
            decisions.add(decision);
        }
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
                    .addSort("timestamp", SortOrder.DESC)
                    .execute()
                    .actionGet();
    }

    public void saveDecision(Decision decision){
        logger.info(String.format("Going to save new decision record: '%s'", decision.toString()));
        String decisionJsonString = convertEntityToJson(decision);
        String id = saveJson(decisionJsonString);
        decision.setId(id);
        publisher.publishDecisionAdded(decision);
    }

    private String saveJson(String decisionJsonString) {
        String response = null;
        try {
            IndexResponse indexResponse = node.client().prepareIndex(DECISIONS_INDEX_NAME, DECISION_TYPE_NAME)
                    .setSource(decisionJsonString.getBytes("UTF-8"))
                    .execute()
                    .actionGet();
            return indexResponse.getId();
        } catch (UnsupportedEncodingException e) {
            logger.debug("Should never happened, every JVM must support UTF8");
        }
        return response;
    }

    private String convertEntityToJson(Decision decision) {
        String decisionJsonString = null;
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(decision);
            decisionJsonString = new String(bytes, "UTF-8");
        } catch (Exception e) {
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
    //        if (listAllDecision().isEmpty()) {
                Decision sampleDecision1 = new Decision();
                sampleDecision1.setSubject("Long transactions");
                sampleDecision1.setDate(new Date());
                sampleDecision1.setReason("In Interactive Plus we have long transactions which causes performance problem and deadlocks.");
                sampleDecision1.setConclusions(
                        "1)\tVašek se podívá na zjednodušení volání SQL při schvalování. Všechny data by se měla načíst EAGER jedním SELECTEM pomocí JOINů přes ALIASY.\n" +
                                "2)\tPetr mrkne na kaskády v databázi, které využijeme při mazání.\n" +
                                "3)\tMartin H. se podívá na optimalizaci operací s DB, při vytváření snapshotů, taky aby se bloby kopírovaly přímo v DB a ne v Javě.\n" +
                                "4)\tOndra koukne na optimalizaci při mazání draftů – budou pouze označeny jako deleted a z DB se smažou později.\n");
                sampleDecision1.setAttendees(new String[]{"Martin Hátaš", "Michal Jankovský", "Václav Stolín", "Hynek Steinmetz", "Martin Kosař"});
                sampleDecision1.setTags(new String[]{"#performance", "#transactions", "#database", "#deadlock", "#peak"});

                Decision sampleDecision2 = new Decision();
                sampleDecision2.setSubject("User activity tracking");
                sampleDecision2.setDate(new Date());
                sampleDecision2.setReason("We want log some user actions at frontend to the license library so we can achieve better UX.");
                sampleDecision2.setConclusions(
                        "1)\tMartin Viktorin a Tomáš Sychra vyvíjejí JavaScriptový logger, který si includneme do klienta a budem přes něj logovat uživatelské akce, které upřesní UX. (pure javascript, singleton, odesílá jednou za čas/po naplnění zásobníku/po ukončení framu, obecné schéma logované zprávy)\n" +
                                "2)\tMartin Viktorin a Tomáš Sychra vyvíjejí maven modul, který obsahuje servlet, který umí příjmat a deserializovat zprávy z JS loggeru a volá metodu interface, který si v I+ na serveru naimplementujeme tak, aby zprávu poslal do licencační knihovny.\n" +
                                "3)\tKonzultanti za I+ byli demokraticky zvoleni Lukáš a Standa.\n");
                sampleDecision2.setAttendees(new String[]{"Stanislav Hacker", "Lukáš Fíla", "Lenka Kreibichová", "Radek Špelda", "Martin Kosař"});
                sampleDecision2.setTags(new String[]{"#UX", "#frontend", "#javascript", "#logging", "#license"});



                Decision sampleDecision3 = new Decision();
                sampleDecision3.setSubject("Choosing framework for Inspire Interactive backend");
                sampleDecision3.setDate(new Date());
                sampleDecision3.setReason("We want to build our application on top of framework that supports MVC pattern, can be easily integrated with cloud and have support for business intelligence addons.");
                sampleDecision3.setConclusions(
                        "We selet this because of");
                sampleDecision3.setAttendees(new String[]{"Mark Zuckie", "Bill Doors", "Steve Bending"});
                sampleDecision3.setTags(new String[]{"#framework", "#backend", "#cloud", "#BI", "#MVC"});

                saveDecision(sampleDecision1);
                saveDecision(sampleDecision2);
                saveDecision(sampleDecision3);
   //         }
        }

    }

    public List<Decision> searchDecisions(String keyWord) {

        SearchResponse response = node.client().prepareSearch(DECISIONS_INDEX_NAME)
                .setTypes(DECISION_TYPE_NAME)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.simpleQueryString(keyWord).field("subject").field("reason").field("conclusions").field("attendees").field("tags"))
                .execute()
                .actionGet();

        List<Decision> decisionList = new ArrayList<Decision>();
        for (SearchHit hit : response.getHits()) {
            getDecisionsFromHit(decisionList, hit);
        }

        return decisionList;
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
