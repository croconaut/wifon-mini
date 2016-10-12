package com.croconaut.wifonmini;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.croconaut.cpt.data.Communication;
import com.croconaut.cpt.data.LocalAttachment;
import com.croconaut.cpt.data.NearbyUser;
import com.croconaut.cpt.data.OutgoingMessage;
import com.croconaut.cpt.data.OutgoingPayload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class NearbyListFragment extends ListFragment implements NearbyListener {
    private static final String TAG = "NearbyListFragment";

    private OutgoingMessage outgoingMessage;
    private NearbyManager nearbyManager;
    private Uri uri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nearbyManager = (NearbyManager) getContext().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearby_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView textView = (TextView)getActivity().findViewById(R.id.textView);
        textView.setText(getResources().getString(R.string.label_files_summary, 0, Formatter.formatFileSize(getContext(), 0)));

        Intent intent = getActivity().getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            handleFile(intent);
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            handleMultipleFiles(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        nearbyManager.addNearbyListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        nearbyManager.removeNearbyListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        @SuppressWarnings("unchecked") HashMap<String, String> map = (HashMap<String, String>) l.getAdapter().getItem(position);
        String username = map.get("username");
        String crocoId = map.get("crocoId");

        // Get intent, action and MIME type
        Intent intent = getActivity().getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            handleSendFile(intent, crocoId);
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            handleSendMultipleFiles(intent, crocoId);
        } else if (uri != null) {
            // TODO: refactor...
            try {
                OutgoingPayload outgoingPayload = new OutgoingPayload("");
                outgoingPayload.addAttachment(new LocalAttachment(getContext(), uri, Environment.DIRECTORY_DOWNLOADS));

                outgoingMessage = new OutgoingMessage(crocoId);
                outgoingMessage.setPayload(outgoingPayload);
            } catch (IOException e) {
                Log.e(TAG, "handleUri()", e);
            }
            uri = null;
        } else {
            Snackbar.make(v, getString(R.string.snack_no_message), Snackbar.LENGTH_SHORT).show();
            return;
        }

        Communication.newMessage(getContext(), outgoingMessage);
        Toast.makeText(getContext(), getString(R.string.snack_message_in_progress), Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void onNearbyPeers(List<NearbyUser> nearbyPeers) {
        List<Map<String, String>> list = new ArrayList<>();
        for (NearbyUser nearbyUser : nearbyPeers) {
            if (nearbyUser.username != null) {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", nearbyUser.username);
                map.put("crocoId", nearbyUser.crocoId);
                list.add(map);
            }
        }
        String[] from = { "username", "crocoId" };
        int[] to = { android.R.id.text1, android.R.id.text2 };
        SimpleAdapter adapter = new SimpleAdapter(getContext(), list, android.R.layout.simple_list_item_2, from, to);
        // this is a terrible way how to update items
        setListAdapter(adapter);
    }

    public void onUri(Uri uri) {
        TextView textView = (TextView)getActivity().findViewById(R.id.textView);
        textView.setText(getResources().getString(R.string.label_files_summary, 1, Formatter.formatShortFileSize(getContext(), getFileSize(uri))));

        this.uri = uri;
    }

    private void handleFile(Intent intent) {
        Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (fileUri != null) {
            TextView textView = (TextView)getActivity().findViewById(R.id.textView);
            textView.setText(getResources().getString(R.string.label_files_summary, 1, Formatter.formatShortFileSize(getContext(), getFileSize(fileUri))));
        } else {
            Snackbar.make(getView(), getString(R.string.snack_missing_uri), Snackbar.LENGTH_LONG).show();
        }
    }

    private void handleMultipleFiles(Intent intent) {
        ArrayList<Uri> fileUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (fileUris != null) {
            long totalSize = 0;
            for (Uri fileUri : fileUris) {
                totalSize += getFileSize(fileUri);
            }
            TextView textView = (TextView)getActivity().findViewById(R.id.textView);
            textView.setText(getResources().getString(R.string.label_files_summary, fileUris.size(), Formatter.formatShortFileSize(getContext(), totalSize)));
        } else {
            Snackbar.make(getView(), getString(R.string.snack_missing_uri), Snackbar.LENGTH_LONG).show();
        }
    }

    private long getFileSize(Uri uri) {
        long size = -1;

        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                int rowsCount = cursor.getCount();
                if (rowsCount > 0 && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

                    return cursor.getLong(sizeIndex);
                }
            } finally {
                cursor.close();
            }
        }

        return size;
    }

    private void handleSendFile(Intent intent, String to) {
        Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (fileUri != null) {
            try {
                OutgoingPayload outgoingPayload = new OutgoingPayload("");
                outgoingPayload.addAttachment(new LocalAttachment(getContext(), fileUri, Environment.DIRECTORY_DOWNLOADS));

                outgoingMessage = new OutgoingMessage(to);
                outgoingMessage.setPayload(outgoingPayload);
            } catch (IOException e) {
                Log.e(TAG, "handleSendFile()", e);
            }
        } else {
            Snackbar.make(getView(), getString(R.string.snack_missing_uri), Snackbar.LENGTH_LONG).show();
        }
    }

    private void handleSendMultipleFiles(Intent intent, String to) {
        ArrayList<Uri> fileUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (fileUris != null) {
            try {
                OutgoingPayload outgoingPayload = new OutgoingPayload("");
                for (Uri fileUri : fileUris) {
                    outgoingPayload.addAttachment(new LocalAttachment(getContext(), fileUri, Environment.DIRECTORY_DOWNLOADS));
                }

                outgoingMessage = new OutgoingMessage(to);
                outgoingMessage.setPayload(outgoingPayload);
            } catch (IOException e) {
                Log.e(TAG, "handleSendMultipleFiles()", e);
            }
        } else {
            Snackbar.make(getView(), getString(R.string.snack_missing_uri), Snackbar.LENGTH_LONG).show();
        }
    }
}
