package com.croconaut.wifonmini;

import android.app.Application;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.croconaut.cpt.data.NearbyUser;
import com.croconaut.cpt.ui.CptController;

import java.util.List;

public class WifonMiniApplication extends Application implements NearbyListener, NearbyProvider {
    private static final String TAG = "WifonMiniApplication";

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

        CptController cptController = new CptController(this);
        cptController.setInternetEnabled(false);    // disable internet server usage
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
    public void onNearbyPeers(List<NearbyUser> nearbyPeers) {
        latestNearbyUsersTimestamp = System.currentTimeMillis();
        latestNearbyUsers = nearbyPeers;
    }

    @Override
    public long getLatestNearbyUsersTimestamp() {
        return latestNearbyUsersTimestamp;
    }

    @Override
    public List<NearbyUser> getLatestNearbyUsers() {
        return latestNearbyUsers;
    }
}