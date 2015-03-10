package com.paypal.psix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.paypal.psix.R;

public class EventsActivity extends PSixActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        setupActionBar();
    }

    private void setupActionBar() {
        setTitle(" " + getString(R.string.your_events));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
