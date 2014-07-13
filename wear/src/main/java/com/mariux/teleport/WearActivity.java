package com.mariux.teleport;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.DataMap;
import com.mariux.teleport.lib.TeleportClient;

public class WearActivity extends Activity {

    private TextView mTextView;
    TeleportClient mTeleportClient;
    TeleportClient.OnSyncDataItemTask mOnSyncDataItemTask;
    TeleportClient.OnGetMessageTask mMessageTask;



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

        //instantiate the TeleportClient with the application Context
        mTeleportClient = new TeleportClient(this);

        //Create and initialize task
        mOnSyncDataItemTask = new ShowToastOnSyncDataItemTask();
        mMessageTask = new ShowToastFromOnGetMessageTask();


        //let's set the two task to be executed when an item is synced or a message is received
        mTeleportClient.setOnSyncDataItemTask(mOnSyncDataItemTask);
        mTeleportClient.setOnGetMessageTask(mMessageTask);




    }

    @Override
    protected void onStart() {
        super.onStart();
        mTeleportClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTeleportClient.disconnect();

    }

    //Task to show the String from DataMap with key "string" when a DataItem is synced
    public class ShowToastOnSyncDataItemTask extends TeleportClient.OnSyncDataItemTask {

        protected void onPostExecute(DataMap dataMap) {

            String s = dataMap.getString("string");

            Toast.makeText(getApplicationContext(),"DataItem - "+s,Toast.LENGTH_SHORT).show();

            mTeleportClient.setOnSyncDataItemTask(new ShowToastOnSyncDataItemTask());
        }
    }

    //Task that shows the path of a received message
    public class ShowToastFromOnGetMessageTask extends TeleportClient.OnGetMessageTask {

        @Override
        protected void onPostExecute(String  path) {

            Toast.makeText(getApplicationContext(),"Message - "+path,Toast.LENGTH_SHORT).show();

            //let's reset the task (otherwise it will be executed only once)
            mTeleportClient.setOnGetMessageTask(new ShowToastFromOnGetMessageTask());
        }
    }




}
