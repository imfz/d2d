package lv.k2611a.network;

import lv.k2611a.domain.Tile;
import lv.k2611a.network.resp.CustomSerialization;

public class TileDTO implements CustomSerialization {
    private byte tileType;

    public byte getTileType() {
        return tileType;
    }

    public void setTileType(byte tileType) {
        this.tileType = tileType;
    }

    public static TileDTO fromTile(Tile tile) {
        TileDTO tileDTO = new TileDTO();
        tileDTO.setTileType((byte) tile.getTileType().getIdOnJS());
        return tileDTO;
    }

    @Override
    public byte[] toBytes() {
        byte[] payload = new byte[1];
        payload[0] = tileType;
        return payload;
    }

    @Override
    public int getSize() {
        return 1;
    }
}
