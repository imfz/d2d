package lv.k2611a.network.resp;

import lv.k2611a.network.GameDTO;

public class GameLobbyUpdate implements Response {
    private GameDTO gameDTO;

    public GameDTO getGameDTO() {
        return gameDTO;
    }

    public void setGameDTO(GameDTO gameDTO) {
        this.gameDTO = gameDTO;
    }
}
