package lv.k2611a.domain.lobby;

public class Game {
    private int id;
    private int ticksWithoutPlayers;
    private int width;
    private int height;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTicksWithoutPlayers() {
        return ticksWithoutPlayers;
    }

    public void setTicksWithoutPlayers(int ticksWithoutPlayers) {
        this.ticksWithoutPlayers = ticksWithoutPlayers;
    }
}
