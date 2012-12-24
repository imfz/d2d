package lv.k2611a.network.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.Joined;
import lv.k2611a.network.resp.UpdateMap;
import lv.k2611a.service.GameService;
import lv.k2611a.service.SessionsService;

public class Join implements Request {

    private static final Logger log = LoggerFactory.getLogger(Join.class);

    private String nickname;
    private int playerId;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Autowired
    private SessionsService sessionsService;

    @Autowired
    private GameService mapService;


    @Override
    public void process() {
        if (nickname == null) {
            throw new IllegalArgumentException("Nickname cannot be null");
        }
        if (!mapService.playerExist(playerId)) {
            throw new IllegalArgumentException("Illegal player id " + playerId);
        }
        ClientConnection.getCurrentConnection().setUsername(nickname);
        ClientConnection.getCurrentConnection().setPlayerId(playerId);
        UpdateMap updateMap = mapService.getFullMapUpdate();
        ClientConnection.getCurrentConnection().sendMessage(updateMap);

        Joined joined = new Joined();
        joined.setNickname(nickname);

        sessionsService.sendUpdate(joined);

        log.info("Joined player with nickname " + nickname + " and id " + playerId);
    }
}
