package com.paypal.psix.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.paypal.psix.R;
import com.paypal.psix.models.User;
import com.paypal.psix.services.UserSession;
import com.paypal.psix.utils.EmailValidator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    private static final String LOG_TAG = PreferenceActivity.class.getSimpleName();

    Preference paypalPref;
    Preference signOutPref;
    Preference loggedInUserPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        setupToolbar();
        setupPaypalAccount();
        setupSignOut();
        setupUserIdentity();
    }

    private void setupUserIdentity() {
        loggedInUserPref = findPreference(getString(R.string.pref_user_name));
        loggedInUserPref.setTitle(UserSession.getUser().getFullName());
        new UpdateUserProfilePic().execute();
    }

    private void setupToolbar() {
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar)LayoutInflater.from(this).inflate(R.layout.settings_bar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                fadeOutSettings();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fadeOutSettings();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == paypalPref) {
            return validatePaypalAccount(String.valueOf(newValue));
        }
        return true;
    }

    private void setupPaypalAccount() {
        paypalPref = findPreference(getString(R.string.pref_paypal_key));
        paypalPref.setOnPreferenceChangeListener(this);
    }

    private void setupSignOut() {
        signOutPref = findPreference(getString(R.string.pref_signout_key));
        signOutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference pref) {
                signOut();
                return true;
            }
        });
    }

    private boolean validatePaypalAccount(String email) {
        boolean isValid = EmailValidator.validate(email);
        if (!isValid) {
            Toast.makeText(SettingsActivity.this, getString(R.string.toast_invalid_email), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }

    private void signOut() {
        Toast.makeText(SettingsActivity.this, getString(R.string.toast_sign_out), Toast.LENGTH_SHORT).show();
        UserSession.logout(this);
    }

    private void fadeOutSettings() {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private class UpdateUserProfilePic extends AsyncTask<Void, Void, Drawable> {

        @Override
        protected Drawable doInBackground(Void... params) {
            try {
                User loggedInUser = UserSession.getUser();
                Bitmap x;
                HttpURLConnection connection = (HttpURLConnection) new URL(loggedInUser.getAvatarURL()).openConnection();
                connection.setRequestProperty("User-agent", "Mozilla/4.0");
                connection.connect();
                InputStream input = connection.getInputStream();

                x = BitmapFactory.decodeStream(input);
                return new BitmapDrawable(x);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            loggedInUserPref.setIcon(drawable);
        }

    }

}