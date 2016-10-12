# wifon-mini
A minimalistic app to share files between devices using an ACTION_SEND / ACTON_SEND_MULTIPLE via our Wi-Fi / Wi-Fi Direct based peer to peer communication framework for Android. Checkout the [CPT wiki](https://github.com/croconaut/cpt/wiki) for details.

The app itself doesn't do much, it allows you to send file to other device (you can change the username in [WifonMiniApplication.java](../master/app/src/main/java/com/croconaut/wifonmini/WifonMiniApplication.java)) either as "Share as..." or directly from the main activity.

Naturally, both devices must have *CPT* installed and running. The app doesn't use our application server by default (to demonstrate its P2P nature).
