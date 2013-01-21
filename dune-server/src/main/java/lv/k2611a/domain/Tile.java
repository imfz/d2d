package lv.k2611a.domain;

import lv.k2611a.util.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tile {

    public static final int TICKS_IN_SPICE_TILE = 100;
    private static final Logger log = LoggerFactory.getLogger(Tile.class);

    private int x;
    private int y;
    private int usedBy;
    private TileType tileType;
    private int spiceRemainingTicks = TICKS_IN_SPICE_TILE;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isUnoccupied() {
        if (usedBy == -1) {
            return true;
        }
        return false;
    }

    public boolean isUnoccupied(long myId) {
        if (usedBy == -1) {
            return true;
        }
        return myId == usedBy;
    }

    public boolean isPassable() {
        if (usedBy <= -Map.ID_OFFSET) {
            return false;
        }
        return true;
    }

    public boolean isUsedByUnit() {
        return usedBy >= Map.ID_OFFSET;
    }

    public int getUsedBy() {
        return usedBy;
    }

    public void setUsed(int used) {
        this.usedBy = used;
    }

    public Point getPoint() {
        return new Point(x,y);
    }

    public int getSpiceRemainingTicks() {
        return spiceRemainingTicks;
    }

    public void setSpiceRemainingTicks(int spiceRemainingTicks) {
        this.spiceRemainingTicks = spiceRemainingTicks;
    }
}
