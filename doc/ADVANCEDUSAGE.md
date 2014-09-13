#ADVANCED USAGE

## Using AsyncTask Factory

AsyncTask are single shot.

Most of the time it will be okay with the basic use. But if you are using Message or DataSyncing with at a **certain rate or running on a low end phone**, you might be running into a corner case.

Sometime, the event come and the AsyncTask is still currently running and it ends in a Exception.

There you should not use classical setters but use the `Builder` setter.

Each AsyncTask is provided with an appropriate builder. The main purpose is that every time a new event is triggered, a new AsyncTask will be instantiated automatically.

```java
mTeleportClient.setOnGetMessageTaskBuilder (
    new OnGetMessageTask.Builder () {
        @Override
        public void build() {
            return new ShowToastAsyncTask();
        }
    }
 );


mTeleportClient.setOnSyncDataItemTaskBuilder (
    new OnSyncDataItemTask.Builder () {
        @Override
        public void build() {
            return new ShowToastAsyncTask();
        }
    }
 );
```

##Using Callbacks

If you want to manage yourself the response of Data sync or Message received, you can use Callbacks. There are `OnGetMessageCallback` and `OnSyncDataItemCallback` you can set in your `TeleportClient` and `TeleportService`.

Remember: in this case you need to **manage the threading yourself!**. Callbacks are not asynchronous like AsyncTask and AsyncTask Factory.

``` java
        //OnGetMessageCallback
        mTeleportClient.setOnGetMessageCallback(new TeleportClient.OnGetMessageCallback() {
            @Override
            public void onCallback(String dataMap) {
                //your callback here...
            }
        });
        
        //OnGetMessageCallback
        mTeleportClient.setOnSyncDataItemCallback(new TeleportClient.OnSyncDataItemCallback() {
            @Override
            public void onDataSync(DataMap dataMap) {
                //your callback here...
            }
        });
```
