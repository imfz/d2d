package lv.k2611a.domain;

public class Tile {
    private int x;
    private int y;
    private long usedBy;
    private TileType tileType;

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

    public boolean isPassable(long myId) {
        if (usedBy == -1) {
            return true;
        }
        return myId == usedBy;
    }

    public void setUsed(long used) {
        this.usedBy = used;
    }
}
