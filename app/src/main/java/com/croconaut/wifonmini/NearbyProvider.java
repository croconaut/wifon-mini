package com.croconaut.wifonmini;

import com.croconaut.cpt.data.NearbyUser;

import java.util.List;

public interface NearbyProvider {
    long getLatestNearbyUsersTimestamp();
    List<NearbyUser> getLatestNearbyUsers();
}
