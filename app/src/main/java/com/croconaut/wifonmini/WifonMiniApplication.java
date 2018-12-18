package com.croconaut.wifonmini;

import android.app.Application;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.croconaut.cpt.data.Communication;
import com.croconaut.cpt.data.NearbyUser;
import com.croconaut.cpt.ui.CptController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WifonMiniApplication extends Application implements NearbyManager {
    private static final String TAG = "WifonMiniApplication";

    private final ArrayList<NearbyListener> nearbyListeners = new ArrayList<>();
    private long latestNearbyUsersTimestamp = -1;
    private List<NearbyUser> latestNearbyUsers;

    @Override
    public void onCreate() {
        super.onCreate();

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        List<WifiConfiguration> networks = wm.getConfiguredNetworks();
        if (networks != null) {
            // disable configured networks so we minimize the risk of something going wrong
            for (WifiConfiguration wifiConfiguration : networks) {
                wm.disableNetwork(wifiConfiguration.networkId);
            }
        }

        // first thing before any CPT related enquiry
        Communication.register(this, "User " + UUID.randomUUID().toString().substring(0, 20), CptBroadcastReceiver.class);

        CptController cptController = new CptController(this);
        if (cptController.isBatteryOptimizationModifiable()) {
            Log.i(TAG, "Disabling batter optimization");
            cptController.setBatteryOptimizationEnabled(false);
        }
        if (cptController.isWifiSleepPolicyModifiable()) {
            Log.i(TAG, "Setting WiFi sleep policy to 'never'");
            cptController.setWifiSleepPolicy(CptController.SleepPolicy.WIFI_SLEEP_POLICY_NEVER);
        }
        if (cptController.isDimScreenWorkaroundRecommended()) {
            Log.i(TAG, "Applying dim screen workaround");
            cptController.setDimScreenWorkaroundEnabled(true);
        }
    }

    @Override
    public void addNearbyListener(NearbyListener nearbyListener) {
        nearbyListeners.add(nearbyListener);
        if (latestNearbyUsersTimestamp != -1) {
            nearbyListener.onNearbyPeers(latestNearbyUsers);
        }
    }

    @Override
    public void removeNearbyListener(NearbyListener nearbyListener) {
        nearbyListeners.remove(nearbyListener);
    }

    @Override
    public void updateNearbyPeers(List<NearbyUser> nearbyPeers) {
        latestNearbyUsersTimestamp = System.currentTimeMillis();
        latestNearbyUsers = nearbyPeers;

        for (NearbyListener nearbyListener : nearbyListeners) {
            nearbyListener.onNearbyPeers(nearbyPeers);
        }
    }
}
