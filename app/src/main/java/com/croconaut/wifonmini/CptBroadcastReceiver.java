package com.croconaut.wifonmini;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.croconaut.cpt.data.Communication;
import com.croconaut.cpt.data.IncomingMessage;
import com.croconaut.cpt.data.MessageAttachment;
import com.croconaut.cpt.data.NearbyUser;
import com.croconaut.cpt.network.NetworkHop;
import com.croconaut.cpt.ui.CptController;
import com.croconaut.cpt.ui.CptReceiver;
import com.croconaut.cpt.ui.LinkLayerMode;

import java.util.ArrayList;
import java.util.Date;

public class CptBroadcastReceiver extends CptReceiver {
    private static final String TAG = "CptBroadcastReceiver";

    // Message related listeners

    @Override
    protected void onNewMessage(Context context, long messageId, Date receivedTime, IncomingMessage incomingMessage) {
        Log.d(TAG, "onNewMessage: " + incomingMessage.getPayload().getAttachments());
        // we don't use the trust system so we must ask for them manually
        for (MessageAttachment messageAttachment : incomingMessage.getPayload().getAttachments()) {
            Communication.requestPublicDownload(context, messageId, incomingMessage.getFrom(), messageAttachment.getSourceUri(), messageAttachment.getStorageDirectory());
        }
    }
    @Override
    protected void onMessageSentToRecipient(Context context, long messageId, Date sentTime) {
        Log.d(TAG, "onMessageSentToRecipient: " + messageId);
    }
    @Override
    protected void onMessageSentToAppServer(Context context, long messageId, Date sentTime) {
        Log.d(TAG, "onMessageSentToAppServer: " + messageId);
    }
    @Override
    protected void onMessageSentToOtherDevice(Context context, long messageId, Date sentTime) {
        Log.d(TAG, "onMessageSentToOtherDevice: " + messageId);
    }
    @Override
    protected void onMessageAcked(Context context, long messageId, Date deliveredTime, ArrayList<NetworkHop> hops) {
        Log.d(TAG, "onMessageAcked: " + messageId);
    }
    @Override
    protected void onMessageDeleted(Context context, long messageId) {
        Log.d(TAG, "onMessageDeleted: " + messageId);
    }

    // Download related listeners

    @Override
    protected void onMessageAttachmentDownloadConfirmed(Context context, long messageId, String sourceUri, String storageDirectory, String from) {
        Log.d(TAG, "onMessageAttachmentDownloadConfirmed: " + sourceUri);
    }
    @Override
    protected void onMessageAttachmentDownloadCancelled(Context context, long messageId, String sourceUri, String storageDirectory, String from) {
        Log.d(TAG, "onMessageAttachmentDownloadCancelled: " + sourceUri);
    }
    @Override
    protected void onMessageAttachmentDownloading(Context context, long messageId, String sourceUri, String storageDirectory, String from, MessageAttachment messageAttachment) {
        Log.d(TAG, "onMessageAttachmentDownloading: " + sourceUri);
    }
    @Override
    protected void onMessageAttachmentDownloaded(Context context, long messageId, String sourceUri, String storageDirectory, String from, MessageAttachment messageAttachment, Date downloadedTime, int downloadedBytesPerSecond) {
        Log.d(TAG, "onMessageAttachmentDownloaded: " + sourceUri);
        Toast.makeText(context, context.getString(R.string.snack_download_completed, messageAttachment.getName(context)), Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onMessageAttachmentDownloadExpired(Context context, long messageId, String sourceUri, String storageDirectory, String from) {
        Log.d(TAG, "onMessageAttachmentDownloadExpired: " + sourceUri);
    }

    // Upload related listeners

    @Override
    protected void onMessageAttachmentUploadConfirmed(Context context, long messageId, String sourceUri, String storageDirectory, String to) {
        Log.d(TAG, "onMessageAttachmentUploadConfirmed: " + sourceUri);
    }
    @Override
    protected void onMessageAttachmentUploadCancelled(Context context, long messageId, String sourceUri, String storageDirectory, String to) {
        Log.d(TAG, "onMessageAttachmentUploadCancelled: " + sourceUri);
    }
    @Override
    protected void onMessageAttachmentUploadingToRecipient(Context context, long messageId, String sourceUri, String storageDirectory, String to) {
        Log.d(TAG, "onMessageAttachmentUploadingToRecipient: " + sourceUri);
    }
    @Override
    protected void onMessageAttachmentUploadingToAppServer(Context context, long messageId, String sourceUri, String storageDirectory, String to) {
        Log.d(TAG, "onMessageAttachmentUploadingToAppServer: " + sourceUri);
    }
    @Override
    protected void onMessageAttachmentUploadedToRecipient(Context context, long messageId, String sourceUri, String storageDirectory, String to, Date uploadedTime, int uploadedBytesPerSecond) {
        Log.d(TAG, "onMessageAttachmentUploadedToRecipient: " + sourceUri);
    }
    @Override
    protected void onMessageAttachmentUploadedToAppServer(Context context, long messageId, String sourceUri, String storageDirectory, String to, Date uploadedTime, int uploadedBytesPerSecond) {
        Log.d(TAG, "onMessageAttachmentUploadedToAppServer: " + sourceUri);
    }
    @Override
    protected void onMessageAttachmentDelivered(Context context, long messageId, String sourceUri, String storageDirectory, String to, Date deliveredTime) {
        Toast.makeText(context, context.getString(R.string.snack_upload_completed, sourceUri), Toast.LENGTH_LONG).show();
    }

    // Other listeners

    @Override
    protected void onNearbyPeers(Context context, ArrayList<NearbyUser> nearbyUsers) {
        NearbyListener nearbyListener = (NearbyListener) context.getApplicationContext();
        nearbyListener.onNearbyPeers(nearbyUsers);
    }
    @Override
    protected void onCptNotificationTapped(Context context) {
        CptController cptController = new CptController(context);
        cptController.setMode(LinkLayerMode.OFF);
        Toast.makeText(context, context.getString(R.string.snack_cpt_off, context.getApplicationInfo().name), Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDownloadNotificationTapped(Context context, long messageId, String sourceUri, String storageDirectory, String from) {
        Communication.cancelPublicDownload(context, messageId, from, sourceUri, storageDirectory);
        Toast.makeText(context, context.getString(R.string.snack_download_cancelled, sourceUri), Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onUploadNotificationTapped(Context context, long messageId, String sourceUri, String storageDirectory, String to) {
        Communication.cancelPublicUpload(context, messageId, to, sourceUri, storageDirectory);
        Toast.makeText(context, context.getString(R.string.snack_upload_cancelled, sourceUri), Toast.LENGTH_LONG).show();
    }
}
