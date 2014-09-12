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

/**
 * Created by Mario Viviani on 10/07/2014.
 */
public abstract class TeleportService extends WearableListenerService{

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "TeleportService";


    //    private AsyncTask<?,?,?> asyncTask;
    private OnSyncDataItemTask onSyncDataItemTask;
    private OnSyncDataItemTask.Builder onSyncDataItemTaskBuilder;
    private OnSyncDataItemCallback onSyncDataItemCallback;
    private OnGetMessageTask onGetMessageTask;
    private OnGetMessageTask.Builder onGetMessageTaskBuilder;
    private OnGetMessageCallback onGetMessageCallback;

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

                boolean flagHandled = false;
                if (onSyncDataItemTaskBuilder != null) {
                    onSyncDataItemTaskBuilder.build().execute(dataMap);
                    flagHandled = true;
                }
                if (!flagHandled && onSyncDataItemCallback != null) {
                    onSyncDataItemCallback.onDataSync(dataMap);
                    flagHandled = true;
                }
                if (!flagHandled && onSyncDataItemTask != null) {
                    onSyncDataItemTask.execute(dataMap);
                    flagHandled = true;
                }

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

    /**
     * Get the TeleportTask that will be executed when a DataItem is synced
     *
     */
    public OnSyncDataItemTask getOnSyncDataItemTask() {
        return onSyncDataItemTask;
    }


    /**
     * Set the TeleportTask to be executed when a DataItem is synced
     *
     * @param onSyncDataItemTask task to be executed. Keep in mind it will be executed only once, so you might need to reset it.
     */
    public void setOnSyncDataItemTask(OnSyncDataItemTask onSyncDataItemTask) {
        this.onSyncDataItemTask = onSyncDataItemTask;
    }




    public abstract static class OnSyncDataItemTask extends AsyncTask<DataMap, Void, DataMap> {

        public abstract static class Builder {

            public abstract OnSyncDataItemTask build();

        }

        protected DataMap doInBackground(DataMap... param) {

            //DataMap dataMap = DataMap.fromByteArray((byte[]) param[0]);

            return param[0];

            //return param[0];
        }

        protected abstract void onPostExecute(DataMap result);
    }

    public abstract static class OnSyncDataItemCallback {

        abstract public void onDataSync(DataMap dataMap);
    }

    /**
     * Set a Builder to be called in order to have an AsyncTask Handling the DataItem syncing
     * Keep in mind that you shall not use this unless you have multiple Syncing event coming in a short timelapse, thus requiring multiple AsyncTask to handle them.
     *
     * @param onSyncDataItemTaskBuilder A Builder for a task that extends TeleportTask that should be executed when a DataItem is Synced. Keep in mind it will be executed only once, so you might need to reset it.
     */
    public void setOnSyncDataItemTaskBuilder(OnSyncDataItemTask.Builder onSyncDataItemTaskBuilder) {
        this.onSyncDataItemTaskBuilder = onSyncDataItemTaskBuilder;
    }

    /**
     * Set the Callback to be executed when a DataItem is synced
     *
     * @param onSyncDataItemCallback A Task that extends TeleportTask that should be executed when a DataItem is Synced. Keep in mind it will be executed only once, so you might need to reset it.
     */
    public void setOnSyncDataItemCallback(OnSyncDataItemCallback onSyncDataItemCallback) {
        this.onSyncDataItemCallback = onSyncDataItemCallback;
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

        boolean flagHandled = false;

        if(onGetMessageTaskBuilder != null) {
            String path = messageEvent.getPath();
            onGetMessageTaskBuilder.build().execute(path);
            flagHandled = true;
        }

        if(!flagHandled && onGetMessageCallback != null) {
            String messagePath = messageEvent.getPath();
            onGetMessageCallback.onCallback(messagePath);
            flagHandled = true;
        }

        if (!flagHandled && onGetMessageTask != null) {
            String messagePath = messageEvent.getPath();
            onGetMessageTask.execute(messagePath);
            flagHandled = true;
        }

    }

    /**
     * AsyncTask that will be executed when a Message is received You should extend this task and implement the onPostExecute() method when implementing your Service.
     *
     * */
    public abstract static class OnGetMessageTask extends AsyncTask<String, Void, String> {

        public abstract static class Builder {

            public abstract OnGetMessageTask build();

        }

        protected String doInBackground(String... path) {

            return path[0];


        }

        protected abstract void onPostExecute(String path);
    }

    public abstract static class OnGetMessageCallback {

        abstract public void onCallback(String dataMap);
    }


    public OnGetMessageTask getOnGetMessageTask() {
        return onGetMessageTask;
    }

    public void setOnGetMessageTask(OnGetMessageTask onGetMessageTask) {
        this.onGetMessageTask = onGetMessageTask;
    }

    /**
     * Set a Builder to be called in order to have an AsyncTask Handling Message
     * Keep in mind that you shall not use this unless you have multiple Message event coming in a short timelapse, thus requiring multiple AsyncTask to handle them.
     *
     * @param onGetMessageTaskBuilder A Builder for a task that extends TeleportTask that should be executed when a DataItem is Synced. Keep in mind it will be executed only once, so you might need to reset it.
     */
    public void setOnGetMessageTaskBuilder(OnGetMessageTask.Builder onGetMessageTaskBuilder) {
        this.onGetMessageTaskBuilder = onGetMessageTaskBuilder;
    }

    public void setOnGetMessageCallback(OnGetMessageCallback onGetMessageCallback) {
        this.onGetMessageCallback = onGetMessageCallback;
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

//    /**
//     * Loads Bitmap from Asset
//     *
//     * @param asset Asset to be converted to Bitmap
//     */
//    public Bitmap loadBitmapFromAsset(Asset asset) {
//        if (asset == null) {
//            throw new IllegalArgumentException("Asset must be non-null");
//        }
//
//        // convert asset into a file descriptor and block until it's ready
//        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
//                mGoogleApiClient, asset).await().getInputStream();
//
//
//        if (assetInputStream == null) {
//            Log.w(TAG, "Requested an unknown Asset.");
//            return null;
//        }
//        // decode the stream into a bitmap
//        return BitmapFactory.decodeStream(assetInputStream);
//    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }



}
