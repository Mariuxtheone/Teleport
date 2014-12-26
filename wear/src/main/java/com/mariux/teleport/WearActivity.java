package com.mariux.teleport;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.wearable.DataMap;

import de.greenrobot.event.EventBus;

public class WearActivity extends Activity {
    private static final String TAG = "WearActivity";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //For Message API receiving
    public void onEvent(final String messageContent) {
        Log.d(TAG, "onEvent message: " + messageContent);
        if(mTextView != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText("Received message: " + messageContent);
                }
            });
        }
    }

    //For DataItem API changes
    public void onEvent(DataMap dataMap) {
        final String string = dataMap.getString("string");
        Log.d(TAG, "onPostExecute String from map: " + string);
        if(mTextView != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText("Received String from map: " + string);
                }
            });
        }
    }
}
