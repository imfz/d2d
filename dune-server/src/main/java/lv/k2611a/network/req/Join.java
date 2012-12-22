package lv.k2611a.network.req;

import org.springframework.beans.factory.annotation.Autowired;

import lv.k2611a.ClientConnection;
import lv.k2611a.network.resp.Joined;
import lv.k2611a.network.resp.UpdateMap;
import lv.k2611a.service.GameService;
import lv.k2611a.service.SessionsService;

public class Join implements Request {
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Autowired
    private SessionsService sessionsService;

    @Autowired
    private GameService mapService;


    @Override
    public void process() {
        ClientConnection.getCurrentConnection().setUsername(nickname);
        UpdateMap updateMap = mapService.getFullMapUpdate();
        ClientConnection.getCurrentConnection().sendMessage(updateMap);

        Joined joined = new Joined();
        joined.setNickname(nickname);

        sessionsService.sendUpdate(joined);
    }
}
