#Teleport Library Setup

**Teleport requires API 15+**

You have 2 options to import the project. The easiest one is to import the maven project from jcenter().
Or, you can clone the repo from Github, but you'll need to manually update it.

#Import the project from Maven (suggested)

You need to have jcenter() repository in your project root build.gradle, but since it is the default artifact repository in Android Studio, you should be already good to go!

##1) Add dependency in your modules build.gradle file

In **both your mobile and wear modules**, add Teleport under *dependencies*:

    compile 'Teleport:teleportlib:0.1.4'
        
Be sure to add the latest version of the lib (use lint check on Android Studio).

     
##2) Add the wearable dependency to your build.gradle dependencies:

Be sure to have downloaded all the Android Wear components from SDK Manager, then add the wearable dependency under *dependencies*
    
    compile 'com.google.android.gms:play-services-wearable:+'
    
##3) Add Google Play Services meta tag in your modules AndroidManifest.xml

In **both your mobile and wear modules**, add the Google Play meta tag under the *application* tag:

    <meta-data 
     android:name="com.google.android.gms.version" 
     android:value="@integer/google_play_services_version" />

##4) If it's not there, in your **mobile module** add your wear module to dependencies

    wearApp project(':wear')


#Clone the repository from Github

##1) Clone the repository from Github

Clone the repository from 
        
        https://github.com/Mariuxtheone/Teleport
        
##2) Import the teleportlib Module

The complete Teleport repository contains **3 modules**

* **teleportlib**  - The **Teleport library** (the module you need to include Teleport inside your project)*

There are Two additional modules containing a **Demo app**.

* **mobile** - A Mobile App you can use to test Teleport main features
* **wear** - A companion Wear App which is controlled by the Mobile App

Add the **teleportlib** module to your build.gradle dependencies

    compile project(':teleportlib')

##3) Be sure to have downloaded all the Android Wear components from SDK Manager, then add teleport to your build.gradle dependencies:
    
    compile 'com.google.android.gms:play-services-wearable:+'
    compile project(':teleportlib')
    
##4) If it's not there, in your mobile module add your wear module to dependencies

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

