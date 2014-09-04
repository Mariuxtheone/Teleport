#Sync Data across Devices

##Sync Data

To Sync Data across devices, you can use one of the commodity *"sync...()"* methods provided by `TeleportClient` and `TeleportService`.

The sync methods are `syncInt(), syncLong(), syncBoolean(), syncString()` and much more.
The first argument of these methods is a `String key` value that will be used as *path and identifier* for this data. 

*Example:* if you want to sync a String "Hello, World!", you can do it this way

 ``` java
 
     mTeleportClient.syncString("hello", "Hello, World!");
 ```

You can use the `key` to easily retrieve the data in your receiver Activity.

##Listen to Data Sync 

`TeleportClient` and `TeleportService` provide you a custom async tasks `OnSyncDataItemTask` that react  to DataItem Sync.

**You need to *extend* these abstract Task inside your Activity, implementing the `OnPostExecute()` method.**


###OnSyncDataItemTask

`OnSyncDataItemTask` allows you to easily get a `DataMap` of synced `DataItem`.
 
**Example:**, let's say we want to show a Toast message when the String with the `key` "hello" is synced: 

We just need to extend `OnSyncDataItemTask` like this:

``` java

    public class ShowToastOnSyncDataItemTask extends TeleportClient.OnSyncDataItemTask {

        @Override
        protected void onPostExecute(DataMap dataMap) {
            
            //let`s get the String from the DataMap, using its identifier key
            String string = dataMap.getString("hello");

            //let`s create a pretty Toast with our string!
            Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();

         }
    }
```

Easy, right? ;-)

*NOTE: Remember that a AsyncTask will be executed only once, so you might need to **reset the task** if you want to perform it again.*

###Set the OnSyncDataItemTask

After we`ve implemented our Task, we need to *set it in our TeleportClient/Service*.

`TeleportClient/Service` provide you with a method `setOnSyncDataItemTask(OnSyncDataItemTask onSyncDataItemTask)` to set up the task.

You can add it where you want, for example to your Activity `onCreate()` like this for a TeleportClient (but you can do it the same for TeleportService):

``` java

         mTeleportClient.setOnSyncDataItemTask(new ShowToastFromOnSyncDataTask());
```

If you need more advanced usage, you can use AsyncTask Factory or Callbacks. Learn more here: [Advanced Usage](/doc/ADVANCEDUSAGE.md)

To learn how to send Messages to another device continue to [Send and Receive Messages](/doc/MESSAGE.md)