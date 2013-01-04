package lv.k2611a.service.lobby;

import java.util.List;

import lv.k2611a.domain.lobby.Game;

public interface LobbyService {
    List<Game> getGames();
    void addGame(Game game);
}
