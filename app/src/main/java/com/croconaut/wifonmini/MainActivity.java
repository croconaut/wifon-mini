package com.croconaut.wifonmini;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.croconaut.cpt.data.Communication;
import com.croconaut.cpt.data.LocalAttachment;
import com.croconaut.cpt.data.OutgoingMessage;
import com.croconaut.cpt.data.OutgoingPayload;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private NearbyProvider nearbyProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        nearbyProvider = (NearbyProvider) getApplication();

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendFile(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleFiles(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            Snackbar.make(findViewById(R.id.content), getString(R.string.snack_only_files), Snackbar.LENGTH_LONG).show();
        } else {
            handleSendFile(intent);
        }
    }

    private void handleSendFile(Intent intent) {
        Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (fileUri != null) {
            try {
                OutgoingPayload outgoingPayload = new OutgoingPayload("");
                outgoingPayload.addAttachment(new LocalAttachment(this, fileUri, Environment.DIRECTORY_DOWNLOADS));

                OutgoingMessage outgoingMessage = new OutgoingMessage("to");    // TODO
                outgoingMessage.setPayload(outgoingPayload);

                Communication.newMessage(this, outgoingMessage);
            } catch (IOException e) {
                Log.e(TAG, "handleSendFile()", e);
            }
        } else {
            Snackbar.make(findViewById(R.id.content), getString(R.string.snack_missing_uri), Snackbar.LENGTH_LONG).show();
        }
    }

    private void handleSendMultipleFiles(Intent intent) {
        ArrayList<Uri> fileUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (fileUris != null) {
            try {
                OutgoingPayload outgoingPayload = new OutgoingPayload("");
                for (Uri fileUri : fileUris) {
                    outgoingPayload.addAttachment(new LocalAttachment(this, fileUri, Environment.DIRECTORY_DOWNLOADS));
                }

                OutgoingMessage outgoingMessage = new OutgoingMessage("to");    // TODO
                outgoingMessage.setPayload(outgoingPayload);

                Communication.newMessage(this, outgoingMessage);
            } catch (IOException e) {
                Log.e(TAG, "handleSendMultipleFiles()", e);
            }
        } else {
            Snackbar.make(findViewById(R.id.content), getString(R.string.snack_missing_uri), Snackbar.LENGTH_LONG).show();
        }
    }
}
