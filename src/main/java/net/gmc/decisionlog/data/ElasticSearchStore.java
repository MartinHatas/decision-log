package net.gmc.decisionlog.data;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gmc.decisionlog.model.Decision;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Component
public class ElasticSearchStore {

    public static final String DECISION_TYPE_NAME = "decision";
    private static final String DECISIONS_INDEX_NAME = "decisions";
    private ObjectMapper objectMapper = new ObjectMapper();

    private static Node node;

    @PostConstruct
    private void init(){

        node = nodeBuilder()
                .local(false)
                .clusterName("decisionlog")
                .build().start();
    }

    public List<Decision> listAllDecision() {

//        SearchResponse searchResponse = node.client().prepareSearch(DECISIONS_INDEX_NAME)
//                .setTypes(DECISION_TYPE_NAME)
//                .setQuery(QueryBuilders.matchAllQuery())
//                .execute()
//                .actionGet();
//
//        for(SearchHit hit : searchResponse.getHits()){
//            String sourceAsString = hit.getSourceAsString();
//            //objectMapper.readValue(sourceAsString, Decision.class);
//        }

        ArrayList<Decision> decisions = new ArrayList<Decision>();
        decisions.add(new Decision("Transakce", "Dlouhé transakce", new Date(), new String[]{"m.hatas", "v.stolin"}, new String[]{"#performance", "#database"}, "Zrychlit to"));
        decisions.add(new Decision("Transakce", "Dlouhé transakce", new Date(), new String[]{"m.hatas", "v.stolin"}, new String[]{"#performance", "#database"}, "Zrychlit to"));
        decisions.add(new Decision("Transakce", "Dlouhé transakce", new Date(), new String[]{"m.hatas", "v.stolin"}, new String[]{"#performance", "#database"}, "Zrychlit to"));

        return decisions;

    }

    public void saveDecision(Decision decision){

        String decisionJsonString = null;
        try {
            decisionJsonString = objectMapper.writeValueAsString(decision);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        IndexResponse indexResponse = node.client().prepareIndex(DECISIONS_INDEX_NAME, DECISION_TYPE_NAME)
                .setSource(decisionJsonString)
                .execute()
                .actionGet();


    }

}
