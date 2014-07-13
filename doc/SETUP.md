#Teleport Library Setup

**Teleport requires API 15+**

##1) Clone the repository from Github

Clone the repository from 
        
        https://github.com/Mariuxtheone/Teleport
        
##2) Import the teleportlib Module

The complete Teleport repository contains **3 modules**

* **teleportlib**  - The Teleport library (the module you need to include Teleport inside your project)*

Two additional modules containing a Demo app.

* **mobile** - A Mobile App you can use to test Teleport main features
* **wear** - A companion Wear App which is controlled by the Mobile App

##3) Be sure to have downloaded all the Android Wear components from SDK Manager, then add teleport to your build.gradle dependencies:
    
    compile 'com.google.android.gms:play-services-wearable:+'
    compile project(':teleportlib')
    
##4) If it's not there, add your wear module to dependencies

    wearApp project(':wear')
     
##5) Do a gradle sync

Just in case...

##6) You're set!! :-D

Teleport is imported and you can start using it.

------------------------

#DEMO APP:

In order to have the Wear app to auto-deploy to your Wear device when the Mobile App is deployed, you need to sign both the Mobile and Wear app with your keystore. 

I provided a script inside gradle to automatically sign with a keystore when  Release variant is selected.

You just need to

1) Create a **signing.properties** file with this informations (basically the same info you add when signing an app):

        STORE_FILE= path to your keystore file (like C:/Users/MyUser/Documents/android-studio/AndroidStudioProjects/mykey.keystore)
        STORE_PASSWORD= your store password
        KEY_ALIAS= your keystore alias
        KEY_PASSWORD= your keystore password

2) Put this file in your Android Studio projects directory

3) In Android Studio, select Build Variant "release" both for wear and mobile modules.

4) Do a gradle build

5) Launch the mobile app

6) The app will install on your phone. If you have a Wear device connected, after about 30 seconds it will automatically sync the wear app too.

##If you don't want to sign your app with a keystore, you can still run both the Mobile and Wear apps in Debug Mode. 

1) You'll have to turn on ADB Debugging (And eventually Bluetooth Debugging) in your Wear Device.

2) You need to install the **mobile** app on your phone and the **wear** app on the Wear device


To start incorporating Teleport inside your app proceed to [TeleportClient](/doc/TELEPORTCLIENT.md)

