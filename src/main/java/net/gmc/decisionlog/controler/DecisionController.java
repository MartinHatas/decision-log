package net.gmc.decisionlog.controler;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.Skype;
import com.skype.SkypeException;
import net.gmc.decisionlog.data.ElasticSearchStore;
import net.gmc.decisionlog.model.Decision;
import net.gmc.decisionlog.model.Search;
import net.gmc.decisionlog.model.SkypeChatInfo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(DecisionController.class);

    @Autowired
    private ElasticSearchStore elasticSearchStore;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String getListDecisionsView(Model model) {
        model.addAttribute("savedSuccessfully", false);
        model.addAttribute("showRelevancy", false);
        model.addAttribute("search", new Search());
        model.addAttribute("decisions", elasticSearchStore.listAllDecision());
        return "index";
    }

    @RequestMapping(value="/", method= RequestMethod.POST)
    public String search(@ModelAttribute("search") Search search, Model model) {
        model.addAttribute("savedSuccessfully", false);
        model.addAttribute("showRelevancy", true);
        List<Decision> decisions = elasticSearchStore.searchDecisions(search.getText());
        model.addAttribute("decisions", decisions);
        model.addAttribute("search", new Search(search.getText(), search.getTag()));
        return "index";
    }

    @RequestMapping(value="/{decisionId}", method= RequestMethod.DELETE)
    public String search(@PathVariable String decisionId, Model model) {
        model.addAttribute("savedSuccessfully", false);
        model.addAttribute("showRelevancy", true);
        elasticSearchStore.deleteDecision(decisionId);
        List<Decision> decisions = elasticSearchStore.listAllDecision();
        model.addAttribute("decisions", decisions);
        return "index";
    }

    @RequestMapping(value="/{decisionId}", method= RequestMethod.GET)
    public String getDecisionById(@PathVariable String decisionId, Model model) {
        Decision decision = elasticSearchStore.getDecision(decisionId);
        List<Decision> decisionList = new ArrayList<Decision>();
        decisionList.add(decision);
        model.addAttribute("decisions", decisionList);
        return "detail";
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
        List<Decision> decisions = elasticSearchStore.listAllDecision();
        decisions.add(0, decision);
        model.addAttribute("decisions", decisions);
        model.addAttribute("savedSuccessfully", savedSuccessfully);
        model.addAttribute("showRelevancy", false);
        model.addAttribute("search", new Search());
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


    @ResponseBody
    @RequestMapping(value="/skype", method= RequestMethod.GET, produces = "application/json")
    public List<SkypeChatInfo> getRecentChats(Model model) {
        return getSkypeChatInfos();
    }

    private List<SkypeChatInfo> getSkypeChatInfos() {
        List<SkypeChatInfo> skypeChatInfos = new ArrayList<SkypeChatInfo>();
        try {
            Chat[] allRecentChats = Skype.getAllRecentChats();
            for (Chat recentChat : allRecentChats) {
                SkypeChatInfo skypeChatInfo = new SkypeChatInfo();
                skypeChatInfo.id = recentChat.getId();
                skypeChatInfo.status = recentChat.getStatus();
                skypeChatInfo.windowTitle = recentChat.getWindowTitle();
                ChatMessage[] recentChatMessages = recentChat.getRecentChatMessages();
                if (recentChatMessages.length > 0) {
                    skypeChatInfo.lastMessage = recentChatMessages[recentChatMessages.length - 1].getContent();
                }
                skypeChatInfos.add(skypeChatInfo);
            }
        } catch (SkypeException e) {
            logger.error("Failed to obtain all recent chat names.", e);
        }
        return skypeChatInfos;
    }

}
