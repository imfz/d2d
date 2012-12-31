package lv.k2611a.network.resp;

import lv.k2611a.network.BuildingDTO;
import lv.k2611a.network.BulletDTO;
import lv.k2611a.network.TileWithCoordinatesDTO;
import lv.k2611a.network.UnitDTO;
import lv.k2611a.util.ByteUtils;

public class UpdateMapIncremental implements Response, CustomSerializationHeader {
    private UnitDTO[] units;
    private BuildingDTO[] buildings;
    private BulletDTO[] bullets;
    private TileWithCoordinatesDTO[] changedTiles;
    private long tickCount;

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

    public TileWithCoordinatesDTO[] getChangedTiles() {
        return changedTiles;
    }

    public void setChangedTiles(TileWithCoordinatesDTO[] changedTiles) {
        this.changedTiles = changedTiles;
    }

    public long getTickCount() {
        return tickCount;
    }

    public void setTickCount(long tickCount) {
        this.tickCount = tickCount;
    }

    public BulletDTO[] getBullets() {
        return bullets;
    }

    public void setBullets(BulletDTO[] bullets) {
        this.bullets = bullets;
    }

    @Override
    public byte serializerId() {
        return 2;
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



        sizeBytes = ByteUtils.intToBytes(bullets.length);
        System.arraycopy(sizeBytes,0,payload,position,sizeBytes.length);
        position+=4;

        for (BulletDTO bullet : bullets) {
            byte[] bytes = bullet.toBytes();
            System.arraycopy(bytes,0,payload,position,bytes.length);
            position += bytes.length;
        }




        sizeBytes = ByteUtils.intToBytes(changedTiles.length);
        System.arraycopy(sizeBytes,0,payload,position,sizeBytes.length);
        position+=4;

        for (TileWithCoordinatesDTO changedTile : changedTiles) {
            byte[] bytes = changedTile.toBytes();
            System.arraycopy(bytes,0,payload,position,bytes.length);
            position += bytes.length;
        }

        byte[] tickBytes = ByteUtils.longToBytes(tickCount);
        payload[position] = tickBytes[0];
        payload[position+1] = tickBytes[1];
        payload[position+2] = tickBytes[2];
        payload[position+3] = tickBytes[3];
        payload[position+4] = tickBytes[4];
        payload[position+5] = tickBytes[5];
        payload[position+6] = tickBytes[6];
        payload[position+7] = tickBytes[7];

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
        for (BulletDTO bullet : bullets) {
            totalSize += bullet.getSize();
        }

        totalSize += 4;
        for (TileWithCoordinatesDTO changedTile : changedTiles) {
            totalSize += changedTile.getSize();
        }

        totalSize += 8;

        return totalSize;
    }
}
