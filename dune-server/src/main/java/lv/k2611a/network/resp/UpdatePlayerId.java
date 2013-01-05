package lv.k2611a.network.resp;

public class UpdatePlayerId implements Response {
    private int playerId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
