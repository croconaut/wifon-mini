package com.croconaut.wifonmini;

import android.app.Application;

import com.croconaut.cpt.data.NearbyUser;

import java.util.List;

public class WifonMiniApplication extends Application implements NearbyListener, NearbyProvider {
    private long latestNearbyUsersTimestamp = -1;
    private List<NearbyUser> latestNearbyUsers;

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
