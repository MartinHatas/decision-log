package net.gmc.decisionlog.data;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gmc.decisionlog.model.Decision;
import net.gmc.decisionlog.notification.publishing.DecisionEventPublisher;
import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Component
public class ElasticSearchStore {

    private static final Logger logger = Logger.getLogger(ElasticSearchStore.class);

    public static final String DECISION_TYPE_NAME = "decision";
    private static final String DECISIONS_INDEX_NAME = "decisions";
    private static final String DECISION_LOG = "decisionlog";
    private ObjectMapper objectMapper = new ObjectMapper();

    private static Node node;

    @Autowired
    private DecisionEventPublisher publisher;

    @PostConstruct
    private void init(){
        logger.info("Constructing and running ElasticSearch storage.");
        buildNode();
        createIndex();
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
            deserialize(decisions, sourceAsString);

        }
        return decisions;
    }

    private void deserialize(List<Decision> decisions, String sourceAsString) {
        try {
            Decision decision = objectMapper.readValue(sourceAsString, Decision.class);
            decisions.add(decision);
        } catch (IOException e) {
            logger.error(String.format("Failed to deserialize desicion '%s'", sourceAsString), e);
        }
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

}
