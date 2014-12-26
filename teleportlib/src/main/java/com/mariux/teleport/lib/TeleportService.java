package com.mariux.teleport.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Mario Viviani on 10/07/2014.
 */
public abstract class TeleportService extends WearableListenerService{

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "TeleportService";

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();

    }

    //--------------SYNC DATAITEM ------------------//

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);

        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {

                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                DataMap dataMap = dataMapItem.getDataMap();

                EventBus.getDefault().post(dataMap);

            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.d("DataItem Deleted", event.getDataItem().toString());
            }
        }
    }

    //sync String

    public void syncString(String key, String item) {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/" + key);
        putDataMapRequest.getDataMap().putString(key, item);
        syncDataItem(putDataMapRequest);
    }

    public void syncInt(String key, int item) {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/" + key);
        putDataMapRequest.getDataMap().putInt(key, item);
        syncDataItem(putDataMapRequest);
    }

    public void syncLong(String key, long item) {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/" + key);
        putDataMapRequest.getDataMap().putLong(key, item);
        syncDataItem(putDataMapRequest);
    }

    public void syncBoolean(String key, boolean item) {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/" + key);
        putDataMapRequest.getDataMap().putBoolean(key, item);
        syncDataItem(putDataMapRequest);
    }

    public void syncByteArray(String key, byte[] item) {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/" + key);
        putDataMapRequest.getDataMap().putByteArray(key, item);
        syncDataItem(putDataMapRequest);
    }

    public void syncByte(String key, byte item) {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/" + key);
        putDataMapRequest.getDataMap().putByte(key, item);
        syncDataItem(putDataMapRequest);
    }

    public void syncAsset(String key, Asset item) {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/" + key);
        putDataMapRequest.getDataMap().putAsset(key, item);
        syncDataItem(putDataMapRequest);
    }

    public void syncAll(DataMap item) {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/dataMap");
        putDataMapRequest.getDataMap().putAll(item);
        syncDataItem(putDataMapRequest);
    }

    //General method to sync data in the Data Layer
    public void syncDataItem(PutDataMapRequest putDataMapRequest) {

        PutDataRequest request = putDataMapRequest.asPutDataRequest();

        Log.d(TAG, "Generating DataItem: " + request);
        if (!mGoogleApiClient.isConnected()) {
            return;
        }

        //let's send the dataItem to the DataLayer API
        Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        if (!dataItemResult.getStatus().isSuccess()) {
                            Log.e(TAG, "ERROR: failed to putDataItem, status code: "
                                    + dataItemResult.getStatus().getStatusCode());
                        }

                    }
                });
    }

    //-----------------MESSAGING------------------//

    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }

        return results;
    }

    //Task to send messages to nodes
    private class StartTeleportMessageTask extends AsyncTask<Object, Void, Object> {


        @Override
        protected Void doInBackground(Object... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                propagateMessageToNodes(node, (String) args[0], (byte[]) args[1]);
            }
            return null;
        }
    }

    //propagate message to nodes
    private void propagateMessageToNodes(String node, String path, byte[] payload) {
        Wearable.MessageApi.sendMessage(
                mGoogleApiClient, node, path, payload).setResultCallback(

                new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Failed to send message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                    }
                }
        );

    }

    public void sendMessage(String path, byte[] payload) {
        //Start a StartTeleportMessageTask with proper Path and Payload
        new StartTeleportMessageTask().execute(path, payload);
    }


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived() A message from watch was received:" + messageEvent.getRequestId() + " " + messageEvent.getPath());

        EventBus.getDefault().post(messageEvent.getPath());
    }

    /***
     * Task to elaborate image from an Asset. You must pass the Asset and the getGoogleApiClient()
     */
    public abstract static class ImageFromAssetTask extends AsyncTask<Object, Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(Object... params) {
            InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                    (GoogleApiClient)params[1], (Asset)params[0]).await().getInputStream();
            Bitmap bitmap=  BitmapFactory.decodeStream(assetInputStream);
            return bitmap;

        }

        @Override
        protected abstract void onPostExecute(Bitmap bitmap);

    };

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }
}
