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

import de.greenrobot.event.EventBus;


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

    @Override
    protected void onStart() {
        super.onStart();
        mTeleportClient.connect();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTeleportClient.disconnect();
        EventBus.getDefault().unregister(this);
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

        //Let's sync a Custom Object - how cool is that?
        mTeleportClient.syncString("sting", syncDataItemEditText.getText().toString());
    }

    public void sendMessage(View v) {
        mTeleportClient.sendMessage(sendMessageEditText.getText().toString(), null);
    }

    public void sendStartActivityMessage(View v) {
        mTeleportClient.sendMessage("startActivity", null);
    }
    
    //For DataItem API changes
    public void onEvent(DataMap dataMap) {
        final String s = dataMap.getString("string");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"DataItem - "+s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //For Message API receiving
    public void onEvent(final String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Message - " + path, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
