package net.gmc.decisionlog.model;


import java.util.Arrays;
import java.util.Date;

public class Decision {

    private String id;
    private String subject;
    private String reason;
    private String conclusions;
    private Date date;
    private String[] attendees;
    private String[] tags;
    private Float relevance;

    public Decision() {
    }

    public Decision(String id, String subject, String reason, String conclusions, Date date, String[] attendees, String[] tags, Float relevance) {
        this.id = id;
        this.subject = subject;
        this.reason = reason;
        this.conclusions = conclusions;
        this.date = date;
        this.attendees = attendees;
        this.tags = tags;
        this.relevance = relevance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getConclusions() {
        return conclusions;
    }

    public void setConclusions(String conclusions) {
        this.conclusions = conclusions;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String[] getAttendees() {
        return attendees;
    }

    public void setAttendees(String[] attendees) {
        this.attendees = attendees;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public Float getRelevance() {
        return relevance;
    }

    public void setRelevance(Float relevance) {
        this.relevance = relevance;
    }

    @Override
    public String toString() {
        return "Decision{" +
                "subject='" + subject + '\'' +
                ", reason='" + reason + '\'' +
                ", conclusions='" + conclusions + '\'' +
                ", date=" + date +
                ", attendees=" + Arrays.toString(attendees) +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}
