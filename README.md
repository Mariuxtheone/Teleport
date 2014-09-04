# Teleport - Data Sync & Messaging Library for Android Wear

![Screen](/doc/images/teleport_256.png)

Teleport is a library to easily setup and manage Data Syncronization and Messaging on Android Wearables.

*The library is thought for Android Studio.*



##Quick Overview

You can see Teleport as an Android Wear "plugin" you can add to your Activities and Services.

Teleport provides you **commodity classes** to easily establish a communication between a mobile handheld device and an Android Wear device.

*  `TeleportClient` provides you "endpoints" you can put inside your Activities, both in Mobile and Wear.
*  `TeleportService` is a full-fledged, already set-up 'WearableListenerService'. 

Both these classes incapsulates all the `GoogleApiClient` setup required to establish a connection between Mobile and Wear.

`TeleportClient` and `TeleportService` also provide you two `AsyncTask` you can extend to easily perform operations with the synced DataItems and received Messages:

* `OnSyncDataItemTask` provides you a complete `DataMap` of synced data
* `OnGetMessageTask` provides you access to a received Message `path` in form of `String`.

You just need to *extend* these tasks inside your Activity/Service and you're good to go!

To Sync Data and send Messages, you can use commodity methods like

* `sync<ItemType>(String key, <ItemType> item)` to Sync Data across devices
* `sendMessage(String path, byte[] payload)` to send a Message to another device.

##Summary

* [Library Set Up:](/doc/SETUP.md) How to import Teleport library in your project.
* [TeleportClient in Activity:](/doc/TELEPORTCLIENT.md) How to setup a TeleportClient.
* [TeleportService:](/doc/TELEPORTSERVICE.md) How to setup a TeleportService.
* [Sync Data:](/doc/SYNCDATA.md) How to Sync Data
* [Send and Receive Messages:](/doc/MESSAGE.md) How to Send and Receive Messages
* [Advanced Usage:](/doc/ADVANCEDUSAGE.md) AsyncTask Factory and Callbacks

##Can I have an example of how easy is Teleport to use?

There you go :-) 

Here's a *Mobile and a Wear Activity* already configured to Sync Data. 

* The MobileActivity synchronizes a string "Hello, World!".
* The WearActivity shows a Toast with the synchronized string.

###MobileActivity.java   
```java
    
    public class MobileActivity extends Activity {

    TeleportClient mTeleportClient;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mobile);

             mTeleportClient = new TeleportClient(this);         
         }

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
    
  
        public void syncDataItem(View v) {                   
           //Let's sync a String!
           mTeleportClient.syncString("hello", "Hello, World!");   
        }
        
    }
```
    
###WearActivity.java
    
```java

    public class WearActivity extends Activity {
    
        TeleportClient mTeleportClient;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_wear);
    
             mTeleportClient = new TeleportClient(this);
             
             mTeleportClient.setOnSyncDataItemTask(new ShowToastHelloWorldTask());
             
        }
    
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
    
        public class ShowToastHelloWorldTask extends TeleportClient.OnSyncDataItemTask {
        
                @Override
                protected void onPostExecute(DataMap dataMap) {
        
                    String hello = dataMap.getString("hello");   
        
                    Toast.makeText(getApplicationContext(),hello,Toast.LENGTH_SHORT).show();
                }
        }
            
    }
```
    
Jump to [Library Set Up](/doc/SETUP.md) !!!

##Follow me on
Author: Mario Viviani
<a href="https://plus.google.com/+MarioViviani/posts">
  <img alt="Follow me on Google+"
       src="https://github.com/Mariuxtheone/Teleport/raw/master/doc/images/googleplus64.png" />
</a>
<a href="https://it.linkedin.com/pub/mario-viviani/45/b96/a59/">
  <img alt="Follow me on LinkedIn"
       src="https://github.com/Mariuxtheone/Teleport/raw/master/doc/images/linkedin64.png" />
</a>

##Thanks to:
Damien Cavaillès - https://github.com/thedamfr


##License

Teleport is released under the **Apache License 2.0**

    Copyright 2014-2015 Mario Viviani
    
        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at
    
           http://www.apache.org/licenses/LICENSE-2.0
    
        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.



