package net.gmc.decisionlog.controler;

import net.gmc.decisionlog.data.ElasticSearchStore;
import net.gmc.decisionlog.model.Decision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Controller
@RequestMapping(value="")
public class DecisionController {

    private static final Random R = new Random();

    @Autowired
    private ElasticSearchStore elasticSearchStore;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String getListDecisionsView(Model model) {
        model.addAttribute("savedSuccessfully", true);
        model.addAttribute("showRelevancy", false);
        model.addAttribute("decisions", elasticSearchStore.listAllDecision());
        return "index";
    }

    @RequestMapping(value="/search/{keyWord}", method= RequestMethod.GET)
    public String search(@PathVariable String keyWord, Model model) {
        sleep();
        model.addAttribute("savedSuccessfully", true);
        model.addAttribute("showRelevancy", true);
        List<Decision> decisions = elasticSearchStore.searchDecisions(keyWord);
        model.addAttribute("decision", decisions);
        return "index";
    }



    @RequestMapping(value="/{deciscionId}", method= RequestMethod.GET)
    public String getDecisionById(@PathVariable String decisionId, Model model) {
        model.addAttribute("decision", elasticSearchStore.getDecision(decisionId));
        return "decision";
    }

    @RequestMapping(value="/add-decision", method= RequestMethod.GET)
    public String getAddDecisionView(Model model) {
        model.addAttribute("decision", new Decision());
        return "add";
    }

    @RequestMapping(value="/add-decision", method= RequestMethod.POST)
    public String addDecision(@ModelAttribute("decision") Decision decision, Model model) {
        boolean savedSuccessfully = true;
        decision.setDate(new Date());
        try {
            elasticSearchStore.saveDecision(decision);
        } catch (Exception e) {
            savedSuccessfully = false;
        }
        List<Decision> decisionList = new ArrayList<Decision>();
        decisionList.add(decision);
        model.addAttribute("decisions", elasticSearchStore.listAllDecision());
        model.addAttribute("savedSuccessfully", savedSuccessfully);
        model.addAttribute("showRelevancy", false);
        return "index";
    }

    @ResponseBody
    @RequestMapping(value="/decision-list", method= RequestMethod.GET, produces = "application/json")
    public List<Decision> getDecision() {
        List<Decision> decisions = elasticSearchStore.listAllDecision();
        return decisions;
    }

    @ResponseBody
    @RequestMapping(value="/search1/{keyWord}", method= RequestMethod.GET, produces = "application/json")
    public List<Decision> search1(@PathVariable String keyWord, Model model) {
        return elasticSearchStore.searchDecisions(keyWord);
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
