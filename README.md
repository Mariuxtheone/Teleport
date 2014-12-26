# Teleport (Android Wear sync lib) + EventBus

![Screen](/doc/images/teleport_256.png)

**This is a modified version of Teleport, it uses EventBus to propagate Messages or DataItems across Services/Activities instead of AsyncTasks**

I really encourage you to check both original lib and this to see which one fits your needs best.

So use is the same as Teleport but receiving is done a bit differently.

Firstly you need a Service that extends `WearableListenerService` to receive changes, if you want some custom/background handling of events, override methods `onDataChanged()` or `onMessageReceived()` - remember to call in those `EventBus.getDefault().post(content);` as otherwise bus will be **not** notified.

In your Wear Activity it is dead easy if you work with EventBus any time before that - even if not it is still easy.

Just registered `onStart()` and unregister in `onStop()` with default bus:
'EventBus.getDefault().register(this);'

and to receive messages implement method:
`public void onEvent(final String event) {//do your magic here}`

to receive DataItems just use:
`public void onEvent(DataMap dataMap) {//get object etc. }`

Simple as that!

Michał Tajchert
If you have any idea what to change just poke me:
[Google+](https://plus.google.com/+MichalTajchert/)

I don't thing it is stuff to pull request, so (for now) it is quite separate and we will see what to do next - **merge or not to merge?**

Original author of Teleport:

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


