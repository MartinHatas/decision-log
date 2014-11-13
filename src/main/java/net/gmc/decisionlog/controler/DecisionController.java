package net.gmc.decisionlog.controler;

import net.gmc.decisionlog.controler.dto.DecisionSearchResponse;
import net.gmc.decisionlog.data.ElasticSearchStore;
import net.gmc.decisionlog.model.Decision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping(value="")
public class DecisionController {

    @Autowired
    private ElasticSearchStore elasticSearchStore;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String getListDecisionsView(Model model) {
        model.addAttribute("decision", elasticSearchStore.listAllDecision());
        return "index";
    }

    @RequestMapping(value="/search/{keyWord}", method= RequestMethod.GET)
    public String search(@PathVariable String keyWord, Model model) {
        DecisionSearchResponse searchResponse = elasticSearchStore.searchDecisions(keyWord);
        model.addAttribute("decision", searchResponse.decisions);
        model.addAttribute("nextScrollId", searchResponse.nextScrollId);
        return "index";
    }

    @RequestMapping(value="/{deciscionId}", method= RequestMethod.GET)
    public String getDecisionById(@PathVariable String decisionId, Model model) {
        model.addAttribute("decision", elasticSearchStore.getDecision(decisionId));
        return "decision";
    }

    @RequestMapping(value="/add-decision", method= RequestMethod.GET)
    public String getAddDecisionView(Model model) {
        return "add";
    }

    @RequestMapping(value="/add-decision", method= RequestMethod.POST)
    public ResponseEntity<String> addDecision(@RequestBody Decision decision) {
        elasticSearchStore.saveDecision(decision);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value="/decision-list", method= RequestMethod.GET, produces = "application/json")
    public List<Decision> getDecision() {
        List<Decision> decisions = elasticSearchStore.listAllDecision();
        return decisions;
    }


}
