package net.gmc.decisionlog.notification.publishing;

import net.gmc.decisionlog.model.Decision;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;


@Component
public class DecisionEventPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void publishDecisionAdded(Decision decision) {
        DecisionAddedMessage decisionAddedMessage = new DecisionAddedMessage(this);
        decisionAddedMessage.setDecision(decision);
        publisher.publishEvent(decisionAddedMessage);
    }
}
