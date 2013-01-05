package lv.k2611a.service.lobby;

import java.util.List;

import lv.k2611a.domain.lobby.Game;

public interface LobbyService {
    List<Game> getGames();
    void addGame(Game game);
    Game getCurrentGame();

    boolean isCurrentGameStarted();
    void setCurrentGameStarted();

    void addUserToCurrentGame(String username);
    void removeUserFromCurrentGame(String username);
    void movePlayerToObservers(String username);
    void movePlayerToPlayers(String username);


    void destroyIfOrphan(Game currentGame);
}
