package net.gmc.decisionlog.model;


import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Decision {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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
        if (conclusions != null) {
            return conclusions.replaceAll("\\n", "<br/>");
        }else {
            return conclusions;
        }
    }

    public void setConclusions(String conclusions) {
        this.conclusions = conclusions;
    }

    public String getDate() {
        String stringDate = null;
        if (date != null) {
             stringDate = DATE_FORMAT.format(date);
        }else {
            stringDate = DATE_FORMAT.format(new Date());
        }
        return stringDate;
    }

    public String getFormattedDate(){
        String date1 = getDate();
        if (date1 != null) {
            return date1.replaceAll("-", "&#8209;");
        }
        return "";
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTimestamp(){
        return date;
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

    public String getFormattedAttendees(){
        StringBuilder sb = new StringBuilder();
        for (String attendee : attendees) {
            sb.append("<span>" + attendee + "</span>");
        }
        return sb.toString();
    }

    public String getFormattedTags(){
        StringBuilder sb = new StringBuilder();
        for (String tag : tags) {
            sb.append("<span>" + tag + "</span>");
        }
        return sb.toString();
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

    public String getSkypeMessage() throws UnknownHostException {
        return String.format("New decision record about '%s' has been saved. You can check it here. http://%s:/%s", subject, InetAddress.getLocalHost().getHostName(), id);
    }
}
