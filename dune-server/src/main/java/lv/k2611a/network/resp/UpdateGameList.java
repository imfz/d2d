package lv.k2611a.network.resp;

import lv.k2611a.network.GameDTO;

public class UpdateGameList implements Response {
    private GameDTO[] games;

    public GameDTO[] getGames() {
        return games;
    }

    public void setGames(GameDTO[] games) {
        this.games = games;
    }
}
