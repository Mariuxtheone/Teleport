package com.mariux.teleport;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.wearable.DataMap;
import com.mariux.teleport.lib.TeleportClient;


public class MobileActivity extends Activity {

    TeleportClient mTeleportClient;
    EditText syncDataItemEditText;
    EditText sendMessageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);

        syncDataItemEditText = (EditText) findViewById(R.id.syncDataItemEditText);
        sendMessageEditText = (EditText) findViewById(R.id.sendMessageEditText);

        mTeleportClient = new TeleportClient(this);

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mTeleportClient.connect();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mTeleportClient.disconnect();
//
//    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mobile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void syncDataItem(View v) {

        //set the AsyncTask to execute when the Data is Synced
        mTeleportClient.setOnSyncDataItemTask(new ShowToastOnSyncDataItemTask());

        //Let's sync a String!
        mTeleportClient.syncString("string", syncDataItemEditText.getText().toString());

    }

    public void sendMessage(View v) {

        mTeleportClient.setOnGetMessageTask(new ShowToastFromOnGetMessageTask());

        mTeleportClient.sendMessage(sendMessageEditText.getText().toString(), null);
    }

    public void sendStartActivityMessage(View v) {

        mTeleportClient.setOnGetMessageTask(new ShowToastFromOnGetMessageTask());

        mTeleportClient.sendMessage("startActivity", null);
    }


    public class ShowToastOnSyncDataItemTask extends TeleportClient.OnSyncDataItemTask {

        @Override
        protected void onPostExecute(DataMap dataMap) {

            String s = dataMap.getString("string");


            Toast.makeText(getApplicationContext(),"DataItem - "+s,Toast.LENGTH_SHORT).show();

            //let's reset the task (otherwise it will be executed only once)
            mTeleportClient.setOnSyncDataItemTask(new ShowToastOnSyncDataItemTask());
        }
    }


    public class ShowToastFromOnGetMessageTask extends TeleportClient.OnGetMessageTask {

        @Override
        protected void onPostExecute(String path) {


            Toast.makeText(getApplicationContext(),"Message - "+path,Toast.LENGTH_SHORT).show();

            //let's reset the task (otherwise it will be executed only once)
            mTeleportClient.setOnGetMessageTask(new ShowToastFromOnGetMessageTask());
        }
    }


}
