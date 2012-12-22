package lv.k2611a.network.resp;

import lv.k2611a.network.MapDTO;

public class UpdateMap implements Response {
    private MapDTO map;
    private long tickCount;

    public MapDTO getMap() {
        return map;
    }

    public void setMap(MapDTO map) {
        this.map = map;
    }

    public long getTickCount() {
        return tickCount;
    }

    public void setTickCount(long tickCount) {
        this.tickCount = tickCount;
    }
}
