package lv.k2611a.service;

import lv.k2611a.network.resp.UpdateMap;

public interface GameService {
    UpdateMap getFullMapUpdate();

    long getTickCount();

    void setTickCount(long tickCount);

    boolean isOwner(int buildingId, int playerId);
}
