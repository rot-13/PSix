package com.paypal.psix.services;

import android.os.Bundle;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Session;

public class FacebookService {

    private static String FB_EVENT_FIELDS = "cover,name,id,start_time,description";

    public static void getUserCreatedFutureEvents(Request.Callback callback) {
        Bundle params = new Bundle();
        params.putString("fields", FB_EVENT_FIELDS);
        params.putString("since", Long.toString(System.currentTimeMillis() / 1000L));

        new Request(
            Session.getActiveSession(),
            "/me/events/created",
            params,
            HttpMethod.GET,
            callback
        ).executeAsync();
    }

    public static Request getInviteesForEventRequest(String fbEventId, Request.Callback callback) {
        return new Request(
            Session.getActiveSession(),
            "/" + fbEventId + "/invited",
            null,
            HttpMethod.GET,
            callback
        );
    }

    public static void postInEvent(String fbEventId, String message, Request.Callback callback) {
        Bundle params = new Bundle();
        params.putString("message", message);
        new Request(
            Session.getActiveSession(),
            "/" + fbEventId + "/feed",
            params,
            HttpMethod.POST,
            callback
        ).executeAsync();
    }
}
