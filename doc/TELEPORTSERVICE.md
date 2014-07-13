#TeleportService

`TeleportService` is a full-fledged `WearableListenerService`, with all the connection layer already setup and ready for use.

It encapsulates all the functions of `TeleportClient`, like the `sync...()` and `sendMessage()` and the AsyncTasks `OnSyncDataItemTask` and `OnSendMessageTask`.

To understand how to implement these features head to [Sync Data](/doc/SYNCDATA.md) and [Send and Retrieve Message](/doc/MESSAGE.md).

##Set up a TeleportService

`TeleportService` is an abstract class so you need to **extend it**.
 
 **Example:** Let`s say we want a Service in our Wear app. We can do it like this:

        public class WearService extends TeleportService {
        
        }

Once you have extended `TeleportService`, you need to add it to the Android Manifest of the app where you have extended it.

You need to put it under the *<application>* tag in the Manifest.

**Example:** Let`s add the WearService to the Manifest of our **wear** app:

```xml

        <service
            android:name=".WearService" >
            <intent-filter>
                <action android:name="com.google.android.gms.wearable.BIND_LISTENER" />
            </intent-filter>
        </service>
        
```

#Set the Task

Once you`ve added it to the Manifest:
 
1) Extend the Tasks you need, `OnSyncDataItemTask` and `OnSendMessageTask` like is described in [Sync Data](/doc/SYNCDATA.md) and [Send and Retrieve Message](/doc/MESSAGE.md).

2) Add the task in your Service `onCreate()`

**Example:** let`s create a Task which starts and Activity when a Message "startActivity" is received.

```java
    
    public class WearService extends TeleportService{
    
     private static final String STARTACTIVITY = "startActivity";
    
    @Override
    public void onCreate() {
        super.onCreate();

        setOnGetMessageTask(new StartActivityTask());

    }

    //Task that shows the path of a received message
    public class StartActivityTask extends TeleportService.OnGetMessageTask {

        @Override
        protected void onPostExecute(String  path) {

       if (path.equals(STARTACTIVITY)){

            Intent startIntent = new Intent(getBaseContext(), WearActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
         }

            //let`s reset the task (otherwise it will be executed only once)
            setOnGetMessageTask(new StartActivityTask());
        }
    }
```
