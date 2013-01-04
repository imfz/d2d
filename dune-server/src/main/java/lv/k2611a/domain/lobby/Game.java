package lv.k2611a.domain.lobby;

public class Game {
    private int id;
    private int ticksWithoutPlayers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTicksWithoutPlayers() {
        return ticksWithoutPlayers;
    }

    public void setTicksWithoutPlayers(int ticksWithoutPlayers) {
        this.ticksWithoutPlayers = ticksWithoutPlayers;
    }
}
