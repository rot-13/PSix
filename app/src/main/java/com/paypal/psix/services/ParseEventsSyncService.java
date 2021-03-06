package com.paypal.psix.services;

import com.paypal.psix.models.Event;
import com.paypal.psix.models.User;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by shay on 3/11/15.
 */
public class ParseEventsSyncService {

    public static void getPaymentsPerEvent(Event event, Callback<ParseAPI.ParsePaymentsResults> cb) {
        String query = "{\"event\":{\"$inQuery\":{\"where\":{\"fbId\":\"" + event.fbEventId + "\"},\"className\":\"Event\"}}}";
        client().fetchEventPayments(query, cb);
    }

    public static void getEventsOfUser(User user, Callback<ParseAPI.ParseEventResults> cb) {
        String query = "{\"fbUserId\":" + "\"" + user.fbUserId + "\"}";
        client().fetchUserEvents(query, cb);
    }

    public static void pluginEvent(final Event event, Callback<ParseAPI.ParseEvent> cb) {
        client().createEvent(new ParseAPI.ParseEventCreateParams(event.fbEventId, UserSession.getUser().fbUserId, event.paymentDescription, event.name, event.amountPerUser), cb);
    }

    private static ParseAPI client() {
        RestAdapter restAdapter = new RestAdapter.Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setEndpoint("https://api.parse.com")
            .build();

        return restAdapter.create(ParseAPI.class);
    }
}
