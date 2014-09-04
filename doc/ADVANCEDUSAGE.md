# Using AsyncTask Factory

AsyncTask are single shot.

Most of the time it will be okay with the basic use. But if you are using Message or DataSyncing with at a certain rate or running on a low end phone, you might be running into a corner case.

Sometime, the event come and the AsyncTask is still currently running and it ends in a Exception.

There you should not use classical setters but use the Builder setter.

Each asyntask is provided with an appropriate builder. The main purpose is that every time a new event is triggered, a new AsyncTask will be instantiated automatically.

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