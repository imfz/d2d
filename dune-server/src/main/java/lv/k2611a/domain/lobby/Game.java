package lv.k2611a.domain.lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private int id;
    private int ticksWithoutPlayers;
    private int width;
    private int height;
    private String creator;
    private boolean started;

    private List<String> players = Collections.synchronizedList(new ArrayList<String>());
    private List<String> observers = Collections.synchronizedList(new ArrayList<String>());

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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getPlayers() {
        return players;
    }

    public List<String> getObservers() {
        return observers;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
