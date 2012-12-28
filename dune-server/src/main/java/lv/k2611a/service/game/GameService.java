package lv.k2611a.service.game;

import lv.k2611a.network.resp.UpdateMap;
import lv.k2611a.util.Point;

public interface GameService {
    UpdateMap getFullMapUpdate();

    long getTickCount();

    void setTickCount(long tickCount);

    boolean isOwner(int buildingId, int playerId);

    boolean playerExist(int playerId);

    void registerChangedTile(Point point);
}
