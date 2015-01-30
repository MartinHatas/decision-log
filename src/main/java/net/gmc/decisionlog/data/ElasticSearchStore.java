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
import org.elasticsearch.index.shard.DocsStats;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticSearchStore {

    private static final Logger logger = Logger.getLogger(ElasticSearchStore.class);

    public static final String DECISION_TYPE_NAME = "decision";
    private static final String DECISIONS_INDEX_NAME = "decisionlog";
    private ObjectMapper objectMapper = new ObjectMapper();
    private static Node node;

    @Value("${demo}")
    private boolean isDemo;

    @Value("${store.index.name}")
    private String DECISION_LOG;

    @Autowired
    private DecisionEventPublisher publisher;

    private boolean isPopulated = false;

    @PostConstruct
    private void init() throws InterruptedException {
        logger.info("Constructing and running ElasticSearch storage.");
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        buildNode();
        createIndex();
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
            } else {
                DocsStats docs = node.client().admin().indices().prepareStats(DECISIONS_INDEX_NAME).execute().get().getTotal().getDocs();
                if (docs == null) {
                    isPopulated = false;
                    return;
                }
                long count = docs.getCount();
                if (count > 0) {
                    isPopulated = true;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to create index for fulltext search.", e);
        }
    }

    public List<Decision> listAllDecision() {
        if (isPopulated) {
            SearchResponse searchResponse = getSearchResponse();
            return deserializeDecisionsResponse(searchResponse);
        } else {
            return new ArrayList<Decision>();
        }
    }

    private List<Decision> deserializeDecisionsResponse(SearchResponse searchResponse) {
        List<Decision> decisions = new ArrayList<Decision>();
        for(SearchHit hit : searchResponse.getHits()){
            getDecisionsFromHit(decisions, hit, searchResponse.getHits().getMaxScore());
        }
        return decisions;
    }

    private void getDecisionsFromHit(List<Decision> decisions, SearchHit hit, float maxScore) {
        String sourceAsString = hit.getSourceAsString();
        Decision decision = deserialize(sourceAsString);
        if (decision != null) {

            float score;
            if (hit.getScore() != 0f && maxScore != 0f) {
                score = hit.getScore() / maxScore;
            } else {
                score = hit.getScore();
            }

            decision.setRelevance(score);
            decision.setId(hit.getId());
            decisions.add(decision);
        }
    }

    private Decision deserialize(String sourceAsString) {
        try {
            Decision decision = objectMapper.readValue(sourceAsString, Decision.class);
            return decision;
        } catch (IOException e) {
            logger.error(String.format("Failed to deserialize decision '%s'", sourceAsString), e);
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
        isPopulated = true;
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
            Date date = new Date();

            Calendar c2 = Calendar.getInstance();
            c2.setTime(date);
            c2.add(Calendar.YEAR, -1);
            c2.add(Calendar.MONTH, -8);

            Thread.sleep(5000);
            //        if (listAllDecision().isEmpty()) {
            Decision sampleDecision1 = new Decision();
            sampleDecision1.setSubject("Long transactions");
            sampleDecision1.setDate(c2.getTime());
            sampleDecision1.setReason("In Interactive Plus we have long transactions which causes performance problem and deadlocks.");
            sampleDecision1.setConclusions(
                        "1)\tVašek se podívá na zjednodušení volání SQL při schvalování. Všechny data by se měla načíst EAGER jedním SELECTEM pomocí JOINů přes ALIASY.\n" +
                                "2)\tPetr mrkne na kaskády v databázi, které využijeme při mazání.\n" +
                                "3)\tMartin H. se podívá na optimalizaci operací s DB, při vytváření snapshotů, taky aby se bloby kopírovaly přímo v DB a ne v Javě.\n" +
                                "4)\tOndra koukne na optimalizaci při mazání draftů – budou pouze označeny jako deleted a z DB se smažou později.\n");
            sampleDecision1.setAttendees(new String[]{"Martin Hátaš", "Michal Jankovský", "Václav Stolín", "Hynek Steinmetz", "Martin Kosař"});
            sampleDecision1.setTags(new String[]{"#performance", "#transactions", "#database", "#deadlock", "#peak"});

            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            c1.add(Calendar.YEAR, -1);
            c1.add(Calendar.MONTH, -5);

            Decision sampleDecision2 = new Decision();
            sampleDecision2.setSubject("User activity tracking");
            sampleDecision2.setDate(c1.getTime());
            sampleDecision2.setReason("We want log some user actions at frontend to the license library so we can achieve better UX.");
            sampleDecision2.setConclusions(
                        "1)\tMartin Viktorin a Tomáš Sychra vyvíjejí JavaScriptový logger framework, který si includneme do klienta a budem přes něj logovat uživatelské akce, které upřesní UX. (pure javascript, singleton, odesílá jednou za čas/po naplnění zásobníku/po ukončení framu, obecné schéma logované zprávy)\n" +
                                "2)\tMartin Viktorin a Tomáš Sychra vyvíjejí maven modul, který obsahuje servlet, který umí příjmat a deserializovat zprávy z JS loggeru a volá metodu interface, který si v I+ na serveru naimplementujeme tak, aby zprávu poslal do licencační knihovny.\n" +
                                "3)\tKonzultanti za I+ byli demokraticky zvoleni Lukáš a Standa.\n");
            sampleDecision2.setAttendees(new String[]{"Stanislav Hacker", "Lukáš Fíla", "Lenka Kreibichová", "Radek Špelda", "Martin Kosař"});
            sampleDecision2.setTags(new String[]{"#UX", "#frontend", "#javascript", "#logging", "#license"});



            Calendar c3 = Calendar.getInstance();
            c3.setTime(date);
            c3.add(Calendar.YEAR, -1);
            c3.add(Calendar.MONTH, -5);

            Decision sampleDecision4 = new Decision();
            sampleDecision4.setSubject("Modularization - Architecture change");
            sampleDecision4.setDate(c3.getTime());
            sampleDecision4.setReason("We want to split our monolithic application into a modules so that we support code ownership that bring us more code quality a faster development.");
            sampleDecision4.setConclusions(
                    "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Duis aute irure dolor in " +
                            "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Nam sed tellus " +
                            "id magna elementum tincidunt. Maecenas aliquet accumsan leo. Mauris dolor felis, sagittis at," +
                            " luctus sed, aliquam non, tellus. framework Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris" +
                            " nisi ut aliquip ex ea commodo consequat. Phasellus faucibus molestie nisl. Nulla quis diam. Class aptent " +
                            "taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos." +
                            " Fusce suscipit libero eget elit. Donec quis nibh at felis congue commodo.");
            sampleDecision4.setAttendees(new String[]{"Denis van Gaal", "Don Ruiz", "Frank Buchar"});
            sampleDecision4.setTags(new String[]{"#modularization", "#server", "#ownership", "#quality"});


            Calendar c4 = Calendar.getInstance();
            c4.setTime(date);
            c4.add(Calendar.MONTH, -3);

            Decision sampleDecision5 = new Decision();
            sampleDecision5.setSubject("What database we should use as internal datastorage");
            sampleDecision5.setDate(c4.getTime());
            sampleDecision5.setReason("We are choosing database to run in memory for non-production data. We need good performance and stability");
            sampleDecision5.setConclusions(
                    "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Duis aute irure dolor in " +
                            "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Nam sed tellus " +
                            "id magna elementum tincidunt. Maecenas aliquet accumsan leo. Mauris dolor felis, sagittis at," +
                            " luctus sed, aliquam non, tellus. framework Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris" +
                            " nisi ut aliquip ex ea commodo consequat. Phasellus faucibus molestie nisl. Nulla quis diam. Class aptent " +
                            "taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos." +
                            " Fusce suscipit libero eget elit. Donec quis nibh at felis congue commodo.");
            sampleDecision5.setAttendees(new String[]{"Denis van Gaal", "Don Ruiz", "Frank Buchar"});
            sampleDecision5.setTags(new String[]{"#modularization", "#server", "#ownership", "#quality"});




            Decision sampleDecision3 = new Decision();
            sampleDecision3.setSubject("Choosing framework for Inspire Interactive backend");

            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.YEAR, -2);
            c.add(Calendar.MONTH, -2);

            sampleDecision3.setDate(c.getTime());
                sampleDecision3.setReason("We want to build our application on top of framework that supports MVC pattern, can be easily integrated with cloud and have support for business intelligence addons.");
                sampleDecision3.setConclusions(
                        "We were choosing among several frameworks namely Spring, Play, Akka and J2EE. \n" +
                                "Finally we have chosen Spring framework because of its plugins and addons that provide us much more functionality than we even need.\n" +
                                "It core of our project. It can´t be easily changed in future.");
                sampleDecision3.setAttendees(new String[]{"Browsil Pillow", "Bill Doors", "Steve Bender"});
                sampleDecision3.setTags(new String[]{"#framework", "#backend", "#cloud", "#BI", "#MVC", "#Spring"});

                saveDecision(sampleDecision1);
                saveDecision(sampleDecision2);
                saveDecision(sampleDecision3);
                saveDecision(sampleDecision4);
                saveDecision(sampleDecision5);
   //         }
        }

    }

    public List<Decision> searchDecisions(String keyWord) {

        SearchResponse response = node.client().prepareSearch(DECISIONS_INDEX_NAME)
                .setTypes(DECISION_TYPE_NAME)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.simpleQueryString(keyWord).field("subject").field("reason").field("conclusions").field("attendees").field("tags", 2.0f))
                .execute()
                .actionGet();


        List<Decision> decisionList = new ArrayList<Decision>();
        for (SearchHit hit : response.getHits()) {
            getDecisionsFromHit(decisionList, hit, response.getHits().getMaxScore());
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
