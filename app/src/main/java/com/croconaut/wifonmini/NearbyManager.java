package com.croconaut.wifonmini;

import com.croconaut.cpt.data.NearbyUser;

import java.util.List;

public interface NearbyManager {
    void addNearbyListener(NearbyListener nearbyListener);
    void removeNearbyListener(NearbyListener nearbyListener);

    void updateNearbyPeers(List<NearbyUser> nearbyPeers);
}
