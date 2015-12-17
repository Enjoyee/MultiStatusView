package com.grimmer.multistatusview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private KooniaoStatusView mStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStatusView = (KooniaoStatusView) findViewById(R.id.statusView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_loading) {
            mStatusView.setViewStatus(MultiStatusView.ViewStatus.STATUS_LOADING);
            return true;
        } else if (id == R.id.action_err) {
            mStatusView.setViewStatus(MultiStatusView.ViewStatus.STATUS_ERR);
            return true;
        } else if (id == R.id.action_empty) {
            mStatusView.setViewStatus(MultiStatusView.ViewStatus.STATUS_EMPTY);
            return true;
        } else if (id == R.id.action_content) {
            mStatusView.setViewStatus(MultiStatusView.ViewStatus.STATUS_SUCCESS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
