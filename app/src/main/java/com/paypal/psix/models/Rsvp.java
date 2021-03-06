package com.paypal.psix.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Rsvps")
public class Rsvp extends Model {

    @Column(name = "Amount")
    public int amount;

    // 'not_replied', 'unsure', 'attending', or 'declined'
    @Column(name = "RsvpStatus")
    public String status;

    @Column(name = "Event", index = true, onDelete = Column.ForeignKeyAction.CASCADE)
    public Event event;

    @Column(name = "User", index = true, onDelete = Column.ForeignKeyAction.CASCADE)
    public User user;

    public Rsvp() {
        super();
    }

    public Rsvp(Event event, User user) {
        this(-1, "not_replied", event, user);
    }

    public Rsvp(int amount, String status, Event event, User user) {
        super();
        this.amount = amount;
        this.status = status;
        this.event = event;
        this.user = user;
    }

    public String getFormattedStatus() {
        String[] splitted = status.toLowerCase().split("_");
        StringBuffer formattedStatus = new StringBuffer();
        for(String word : splitted) {
            formattedStatus
                    .append(Character.toUpperCase(word.charAt(0)) + word.substring(1))
                    .append(" ");
        }
        return formattedStatus.toString().trim();
    }

    public String getFormattedAmount() {
        return String.format("$%d", amount);
    }

    public boolean hasPaid() {
        return amount != -1;
    }
}
