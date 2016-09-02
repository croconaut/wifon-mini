package com.croconaut.wifonmini;

import com.croconaut.cpt.data.NearbyUser;

import java.util.List;

public interface NearbyListener {
    void onNearbyPeers(List<NearbyUser> nearbyPeers);
}
