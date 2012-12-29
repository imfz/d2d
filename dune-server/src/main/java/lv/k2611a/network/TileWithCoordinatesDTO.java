package lv.k2611a.network;

import lv.k2611a.domain.Tile;
import lv.k2611a.network.resp.CustomSerialization;
import lv.k2611a.util.ByteUtils;

public class TileWithCoordinatesDTO implements CustomSerialization {
    private byte tileType;
    private short x;
    private short y;

    public int getTileType() {
        return tileType;
    }

    public void setTileType(byte tileType) {
        this.tileType = tileType;
    }

    public int getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public static TileWithCoordinatesDTO fromTile(Tile tile) {
        TileWithCoordinatesDTO tileDTO = new TileWithCoordinatesDTO();
        tileDTO.setTileType((byte) tile.getTileType().getIdOnJS());
        tileDTO.setX((short) tile.getX());
        tileDTO.setY((short) tile.getY());
        return tileDTO;
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public byte[] toBytes() {
        byte[] payload = new byte[getSize()];

        byte[] xBytes = ByteUtils.shortToBytes(x);
        byte[] yBytes = ByteUtils.shortToBytes(y);

        payload[0] = this.tileType;
        payload[1] = xBytes[0];
        payload[2] = xBytes[1];
        payload[3] = yBytes[0];
        payload[4] = yBytes[1];

        return payload;
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
