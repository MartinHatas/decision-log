package net.gmc.decisionlog.notification;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;
import net.gmc.decisionlog.model.Decision;
import net.gmc.decisionlog.notification.publishing.DecisionAddedMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class SkypeNotifier implements ApplicationListener<DecisionAddedMessage> {

    private static final Logger logger = Logger.getLogger(SkypeNotifier.class);

    @Value("${skype.chatnames}")
    private String[] skypeChatNames;

    @Value("${skype.notification.enabled}")
    private boolean skypeNotificationEnabled;

    @Value("${server.port}")
    private String port;


    @Value("${server.contextPath}")
    private String contextPath;


    private List<Chat> chats = new ArrayList<Chat>();

    @PostConstruct
    private void init() throws SkypeException {

        if (skypeNotificationEnabled) {
            Skype.setDaemon(false);
            Chat[] allChats = Skype.getAllChats();
//            for (Chat chat : allChats) {
//                for (String skypeChatName: skypeChatNames) {
//                    if (chat.getId().equals(skypeChatName)) {
//                        logger.info(String.format("Registering: chat with id '%s' has been found.", skypeChatName));
//                        chats.add(chat);
//                    }
//                }
//            }

            if (Skype.isInstalled()){
                initSkypeDaemon();
            } else {
                logger.error("It seem that you have not installed Skype.");
            }
        } else {
            logger.info("Skype notifier is disabled. If you want to be notified via Skype turn this feature on in application.properties.");
        }
    }

    private void initSkypeDaemon() {
        logger.info("Constructing Skype message notifier");

        waitWhileSkypeIsRunning();
    }


    @Override
    public void onApplicationEvent(DecisionAddedMessage decisionAddedMessage) {
        if (skypeNotificationEnabled) {
            notifyAboutNewDecisionAdded(decisionAddedMessage);
        }
    }

    private void notifyAboutNewDecisionAdded(DecisionAddedMessage decisionAddedMessage) {
        Decision decision = decisionAddedMessage.getDecision();
        for (String skypeChatId : skypeChatNames) {
            sendDecisionSkypeMessage(decision, skypeChatId);
        }
    }

    private void sendDecisionSkypeMessage(Decision decision, String skypeChatId) {
        try {
            Skype.chat(skypeChatId.trim()).send(String.format(decision.getSkypeMessage(), port, contextPath));
//            for (Chat chat: chats) {
//                chat.send(String.format(decision.getSkypeMessage(), port, contextPath));
//            }
        } catch (Exception e) {
            logger.error(String.format("Failed to send skype message to the chat with id '%s'", skypeChatId), e);
        }
    }

    private void waitWhileSkypeIsRunning(){
        try {
            while (Skype.isRunning()) {
                logger.info("Cannot hook up on the skype process. It seem that Skype is not running. Waiting 30 sec.");
                Thread.sleep(30000);
            }
        } catch (Exception e) {
            logger.error("Failed attempt to hook up on the skype process.", e);
        }
    }
}
