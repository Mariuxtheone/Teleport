#Send and Receive Messages

##Send Messages

To send a Message to another device, you can use the method `sendMessage(String path, byte[] payload)` 

provided by `TeleportClient` and `TeleportService`.


*Example:* if you want to send a "startActivity" message, you can do it this way

 ``` java
 
      mTeleportClient.sendMessage("startActivity", null);
 ```

You can use the `payload` argument to attach a payload (100KB max) to your message (at the moment the Payload is not used by Teleport)

##Listen and react to Messages 

`TeleportClient` and `TeleportService` provide you a custom async tasks `OnGetMessageTask` that react  to received Messages

**You need to *extend* these abstract Task inside your Activity, implementing the `OnPostExecute()` method.**


###OnSyncDataItemTask

`OnGetMessageTask` allows you to easily get the path of a received Message.
 
**Example:**, let's say we want to start an Activity when the String with the `path` "startActivity" is synced: 

We just need to extend `OnSyncDataItemTask` like this:

``` java

    private static final String STARTACTIVITY = "startActivity";

    public class StartActivityTask extends TeleportClient.OnGetMessageTask {

        @Override
        protected void onPostExecute(String path) {
            
           if (path.equals(STARTACTIVITY)){           
                       Intent startIntent = new Intent(getBaseContext(), WearActivity.class);
                       startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(startIntent);
                    }

         }
    }
```

Easy, right? ;-)

*NOTE: Remember that a AsyncTask will be executed only once, so you might need to **reset the task** if you want to perform it again.*

###Set the OnGetMessageTask

After we`ve implemented our Task, we need to **set it in our TeleportClient/Service**.

`TeleportClient/Service` provide you with a method `setOnGetMessageTask(OnGetMessageTask onGetMessageTask)` to set up the task.

You can add it where you want, for example to your Activity `onCreate()` like this (following the above example):

``` java

         mTeleportClient.setOnGetMessageTask(new StartActivityTask());
```

If you need more advanced usage, you can use AsyncTask Factory or Callbacks. Learn more here: [Advanced Usage](/doc/ADVANCEDUSAGE.md)