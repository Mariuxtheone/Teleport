#TeleportClient in Activity

##Set up TeleportClient

Inside both your mobile and wear `Activity` you can use `TeleportClient` to easily create an "endpoint" to send/receive messages

Instantiate your `TeleportClient` with your activity `Context`

So your Activity `onCreate()` will look something like this:

``` java
    
    private  TeleportClient mTeleportClient;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);

        mTeleportClient = new TeleportClient(this);

    }
```

Then, connect the TeleportClient in the `onStart()` of your Activity, and disconnect it in `onStop()`

``` java

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
```

At this point your activity is **ready to sync data and send and receive messages**.

To Learn how to implement Teleport inside a Service proceed to [TeleportService](/doc/TELEPORTSERVICE.md)

To Learn how to Sync Data across devices continue to [Sync Data](/doc/SYNCDATA.md)






