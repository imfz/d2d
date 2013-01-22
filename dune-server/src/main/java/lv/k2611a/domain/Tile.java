package lv.k2611a.domain;

import lv.k2611a.util.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tile {

    public static final int TICKS_IN_SPICE_TILE = 100;
    private static final Logger log = LoggerFactory.getLogger(Tile.class);
    public static final int ID_OFFSET = 2;

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

    // Check if the tile is completely free
    public boolean isUnoccupied() {
        if (usedBy == -1) {
            return true;
        }
        return false;
    }

    // Check if the tile is not occupied by someone other than current unit
    public boolean isUnoccupied(long myId) {
        if (usedBy == -1) {
            return true;
        }
        return myId == (usedBy - ID_OFFSET);
    }

    // Check if the tile contains no buildings, which means it can be passed,
    // while it might currently be occupied by another unit.
    public boolean isPassable() {
        if (usedBy <= -ID_OFFSET) {
            return false;
        }
        return true;
    }

    public boolean isUsedByUnit() {
        return usedBy >= ID_OFFSET;
    }

    public boolean isUsedByBuilding() {
        return usedBy <= -ID_OFFSET;
    }

    public int getUsedByUnit() {
        return usedBy - ID_OFFSET;
    }

    public int getUsedByBuilding() {
        return -usedBy + ID_OFFSET;
    }

    public void setUsedClear() {
        this.usedBy = -1;
    }


    public void setUsedByUnit(int id) {
        this.usedBy = id + ID_OFFSET;
    }

    public void setUsedByBuilding(int id) {
        this.usedBy = -id - ID_OFFSET;
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
