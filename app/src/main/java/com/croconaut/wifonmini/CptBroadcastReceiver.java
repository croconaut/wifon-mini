package com.croconaut.wifonmini;

import android.content.Context;

import com.croconaut.cpt.data.IncomingMessage;
import com.croconaut.cpt.data.MessageAttachment;
import com.croconaut.cpt.data.NearbyUser;
import com.croconaut.cpt.network.NetworkHop;
import com.croconaut.cpt.ui.CptReceiver;

import java.util.ArrayList;
import java.util.Date;

public class CptBroadcastReceiver extends CptReceiver {
    // Message related listeners

    @Override
    protected void onNewMessage(Context context, long messageId, Date receivedTime, IncomingMessage incomingMessage) {

    }
    @Override
    protected void onMessageSentToRecipient(Context context, long messageId, Date sentTime) {

    }
    @Override
    protected void onMessageSentToAppServer(Context context, long messageId, Date sentTime) {

    }
    @Override
    protected void onMessageSentToOtherDevice(Context context, long messageId, Date sentTime) {

    }
    @Override
    protected void onMessageAcked(Context context, long messageId, Date deliveredTime, ArrayList<NetworkHop> hops) {

    }
    @Override
    protected void onMessageDeleted(Context context, long messageId) {

    }

    // Download related listeners

    @Override
    protected void onMessageAttachmentDownloadConfirmed(Context context, long messageId, String sourceUri, String storageDirectory, String from) {

    }
    @Override
    protected void onMessageAttachmentDownloadCancelled(Context context, long messageId, String sourceUri, String storageDirectory, String from) {

    }
    @Override
    protected void onMessageAttachmentDownloading(Context context, long messageId, String sourceUri, String storageDirectory, String from, MessageAttachment messageAttachment) {

    }
    @Override
    protected void onMessageAttachmentDownloaded(Context context, long messageId, String sourceUri, String storageDirectory, String from, MessageAttachment messageAttachment, Date downloadedTime, int downloadedBytesPerSecond) {

    }
    @Override
    protected void onMessageAttachmentDownloadExpired(Context context, long messageId, String sourceUri, String storageDirectory, String from) {

    }

    // Upload related listeners

    @Override
    protected void onMessageAttachmentUploadConfirmed(Context context, long messageId, String sourceUri, String storageDirectory) {

    }
    @Override
    protected void onMessageAttachmentUploadCancelled(Context context, long messageId, String sourceUri, String storageDirectory) {

    }
    @Override
    protected void onMessageAttachmentUploadingToRecipient(Context context, long messageId, String sourceUri, String storageDirectory) {

    }
    @Override
    protected void onMessageAttachmentUploadingToAppServer(Context context, long messageId, String sourceUri, String storageDirectory) {

    }
    @Override
    protected void onMessageAttachmentUploadedToRecipient(Context context, long messageId, String sourceUri, String storageDirectory, Date uploadedTime, int uploadedBytesPerSecond) {

    }
    @Override
    protected void onMessageAttachmentUploadedToAppServer(Context context, long messageId, String sourceUri, String storageDirectory, Date uploadedTime, int uploadedBytesPerSecond) {

    }
    @Override
    protected void onMessageAttachmentDelivered(Context context, long messageId, String sourceUri, String storageDirectory, Date deliveredTime) {

    }

    // Other listeners

    @Override
    protected void onNearbyPeers(Context context, ArrayList<NearbyUser> nearbyUsers) {
        NearbyListener nearbyListener = (NearbyListener) context.getApplicationContext();
        nearbyListener.onNearbyPeers(nearbyUsers);
    }
    @Override
    protected void onCptNotificationTapped(Context context) {

    }
    @Override
    protected void onDownloadNotificationTapped(Context context, long messageId, String sourceUri, String storageDirectory, String from) {

    }
    @Override
    protected void onUploadNotificationTapped(Context context, long messageId, String sourceUri, String storageDirectory) {

    }
}
