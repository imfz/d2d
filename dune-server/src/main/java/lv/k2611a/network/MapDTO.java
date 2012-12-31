package lv.k2611a.network;

import lv.k2611a.network.resp.CustomSerialization;
import lv.k2611a.util.ByteUtils;

public class MapDTO implements CustomSerialization {
    private short height;
    private short width;
    private TileDTO[] tiles;
    private UnitDTO[] units;
    private BuildingDTO[] buildings;
    private BulletDTO[] bullets;

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public short getWidth() {
        return width;
    }

    public void setWidth(short width) {
        this.width = width;
    }

    public TileDTO[] getTiles() {
        return tiles;
    }

    public void setTiles(TileDTO[] tiles) {
        this.tiles = tiles;
    }

    public UnitDTO[] getUnits() {
        return units;
    }

    public void setUnits(UnitDTO[] units) {
        this.units = units;
    }

    public BuildingDTO[] getBuildings() {
        return buildings;
    }

    public void setBuildings(BuildingDTO[] buildings) {
        this.buildings = buildings;
    }

    public BulletDTO[] getBullets() {
        return bullets;
    }

    public void setBullets(BulletDTO[] bullets) {
        this.bullets = bullets;
    }

    @Override
    public byte[] toBytes() {
        byte[] payload = new byte[getSize()];

        int position = 0;

        byte[] sizeBytes = ByteUtils.intToBytes(units.length);
        System.arraycopy(sizeBytes,0,payload,position,sizeBytes.length);
        position+=4;

        for (UnitDTO unit : units) {
            byte[] bytes = unit.toBytes();
            System.arraycopy(bytes,0,payload,position,bytes.length);
            position += bytes.length;
        }





        sizeBytes = ByteUtils.intToBytes(buildings.length);
        System.arraycopy(sizeBytes,0,payload,position,sizeBytes.length);
        position+=4;

        for (BuildingDTO building : buildings) {
            byte[] bytes = building.toBytes();
            System.arraycopy(bytes,0,payload,position,bytes.length);
            position += bytes.length;
        }





        sizeBytes = ByteUtils.intToBytes(tiles.length);
        System.arraycopy(sizeBytes,0,payload,position,sizeBytes.length);
        position+=4;

        for (TileDTO tileDTO : tiles) {
            byte[] bytes = tileDTO.toBytes();
            System.arraycopy(bytes,0,payload,position,bytes.length);
            position += bytes.length;
        }





        sizeBytes = ByteUtils.intToBytes(bullets.length);
        System.arraycopy(sizeBytes,0,payload,position,sizeBytes.length);
        position+=4;

        for (BulletDTO bulletDTO : bullets) {
            byte[] bytes = bulletDTO.toBytes();
            System.arraycopy(bytes,0,payload,position,bytes.length);
            position += bytes.length;
        }




        byte[] widthBytes = ByteUtils.shortToBytes(width);
        payload[position] = widthBytes[0];
        payload[position+1] = widthBytes[1];
        position+=2;

        byte[] heightBytes = ByteUtils.shortToBytes(height);
        payload[position] = heightBytes[0];
        payload[position+1] = heightBytes[1];
        position+=2;

        return payload;

    }

    @Override
    public int getSize() {
        int totalSize = 0;
        totalSize += 4;
        for (UnitDTO unit : units) {
            totalSize += unit.getSize();
        }

        totalSize += 4;
        for (BuildingDTO building : buildings) {
            totalSize += building.getSize();
        }

        totalSize += 4;
        for (TileDTO tile : tiles) {
            totalSize += tile.getSize();
        }

        totalSize += 4;
        for (BulletDTO bullet : bullets) {
            totalSize += bullet.getSize();
        }

        totalSize += 4;

        return totalSize;
    }
}
