package lv.k2611a.network.req;

import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.network.resp.UpdateMap;
import lv.k2611a.service.connection.ConnectionState;
import lv.k2611a.service.game.GameService;

public class FullSync implements Request {

    @Autowired
    private GameService mapService;

    @Autowired
    private ConnectionState connectionState;

    @Override
    public void process() {
        UpdateMap updateMap = mapService.getFullMapUpdate();
        connectionState.getConnection().sendMessage(updateMap);
    }
}
