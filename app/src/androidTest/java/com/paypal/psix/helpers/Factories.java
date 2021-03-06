package com.paypal.psix.helpers;

import com.paypal.psix.models.User;

import org.fluttercode.datafactory.impl.DataFactory;

/**
 * Created by shay on 3/3/15.
 */
public class Factories {

    public final static int FB_ID_LENGTH = 10;
    public final static DataFactory rndData = new DataFactory();

    public static User createRandomUser() {
        return createUser(rndData.getNumberText(FB_ID_LENGTH), rndData.getName(), rndData.getName());
    }

    public static User createUser(String fbUserId, String firstName, String lastName) {
        User user = new User();
        user.fbUserId = fbUserId;
        user.firstName = firstName;
        user.lastName = lastName;
        user.save();
        return user;
    }
}
