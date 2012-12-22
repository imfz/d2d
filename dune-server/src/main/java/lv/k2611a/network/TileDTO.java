package lv.k2611a.network;

import lv.k2611a.domain.Tile;

public class TileDTO {
    private int tileType;

    public int getTileType() {
        return tileType;
    }

    public void setTileType(int tileType) {
        this.tileType = tileType;
    }

    public static TileDTO fromTile(Tile tile) {
        TileDTO tileDTO = new TileDTO();
        tileDTO.setTileType(tile.getTileType().getIdOnJS());
        return tileDTO;
    }
}
