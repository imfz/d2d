package lv.k2611a.network;

import lv.k2611a.domain.Tile;

public class TileWithCoordinatesDTO {
    private int tileType;
    private int x;
    private int y;

    public int getTileType() {
        return tileType;
    }

    public void setTileType(int tileType) {
        this.tileType = tileType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static TileWithCoordinatesDTO fromTile(Tile tile) {
        TileWithCoordinatesDTO tileDTO = new TileWithCoordinatesDTO();
        tileDTO.setTileType(tile.getTileType().getIdOnJS());
        tileDTO.setX(tile.getX());
        tileDTO.setY(tile.getY());
        return tileDTO;
    }

    @Override
    public String toString() {
        return "TileWithCoordinatesDTO{" +
                "tileType=" + tileType +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
