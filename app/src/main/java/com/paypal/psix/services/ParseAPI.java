package com.paypal.psix.services;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by shay on 3/11/15.
 */
public interface ParseAPI {

    static String APP_ID  = "3VYFRciY4tMgYwEwVYq2veNGbqq7CjJvQUp85IAo";
    static String API_KEY = "LTNEwupT7LOtVvWoVVVkFahN2H76oDU0qbEJSL3b";


    // EVENT FETCHING

    @Headers({
        "X-Parse-Application-Id: " + APP_ID,
        "X-Parse-REST-API-Key: " + API_KEY,
    })
    @GET("/1/classes/Event")
    void fetchUserEvents(@Query("where") String query, Callback<ParseEventResults> cb);


    // PAYMENT FETCHING
    @Headers({
            "X-Parse-Application-Id: " + APP_ID,
            "X-Parse-REST-API-Key: " + API_KEY,
    })
    @GET("/1/classes/Payment")
    void fetchEventPayments(@Query("where") String fbId, Callback<ParsePaymentsResults> cb);


    // EVENT CREATION

    @Headers({
        "X-Parse-Application-Id: " + APP_ID,
        "X-Parse-REST-API-Key: " + API_KEY,
        "Content-Type: application/json"
    })
    @POST("/1/classes/Event")
    void createEvent(@Body ParseEventCreateParams params,
                     Callback<ParseEvent> cb);

    public static class ParseEventCreateParams {
        public String fbId;
        public String fbUserId;
        public String description;
        public String name;
        public int amountPer;

        public ParseEventCreateParams(String fbId, String fbUserId, String description, String name, int amountPer) {
            this.fbId = fbId;
            this.fbUserId = fbUserId;
            this.description = description;
            this.name = name;
            this.amountPer = amountPer;
        }

    }

    public class ParseEvent {
        public String fbId;
    }

    public class ParseEventResults {
        public List<ParseEvent> results;
    }


    public class ParsePayment {
        public int amount;
        public String userFbId;
    }

    public class ParsePaymentsResults {
        public List<ParsePayment> results;
    }

}