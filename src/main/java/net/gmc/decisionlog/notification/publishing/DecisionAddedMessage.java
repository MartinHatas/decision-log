package net.gmc.decisionlog.notification.publishing;


import net.gmc.decisionlog.model.Decision;
import org.springframework.context.ApplicationEvent;

public class DecisionAddedMessage extends ApplicationEvent {

    private Decision decision;

    public DecisionAddedMessage(Object source) {
        super(source);
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }
}
