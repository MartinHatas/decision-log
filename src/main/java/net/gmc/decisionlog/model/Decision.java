package net.gmc.decisionlog.model;


import java.util.Arrays;
import java.util.Date;

public class Decision {

    private String name;
    private String meeting;
    private Date date;
    private String[] attendees;
    private String[] tags;
    private String conclusions;

    public Decision() {
    }

    public Decision(String name, String meeting, Date date, String[] attendees, String[] tags, String conclusions) {
        this.name = name;
        this.meeting = meeting;
        this.date = date;
        this.attendees = attendees;
        this.tags = tags;
        this.conclusions = conclusions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeeting() {
        return meeting;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
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

    public String getConclusions() {
        return conclusions;
    }

    public void setConclusions(String conclusions) {
        this.conclusions = conclusions;
    }

    @Override
    public String toString() {
        return "Decision{" +
                "name='" + name + '\'' +
                ", meeting='" + meeting + '\'' +
                ", date=" + date +
                ", attendees=" + Arrays.toString(attendees) +
                ", tags=" + Arrays.toString(tags) +
                ", conclusions='" + conclusions + '\'' +
                '}';
    }
}
