package net.gmc.decisionlog.controler;

import net.gmc.decisionlog.data.ElasticSearchStore;
import net.gmc.decisionlog.model.Decision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@Controller
@RequestMapping(value="")
public class DecisionController {

    @Autowired
    private ElasticSearchStore elasticSearchStore;

    @RequestMapping(value="/", method= RequestMethod.GET, produces = "text/html; charset=UTF-8")
    public String listDecisions(Model model) {
        List<Decision> decisions = elasticSearchStore.listAllDecision();
        model.addAttribute("decisions", decisions);
        return "index";
    }
}
