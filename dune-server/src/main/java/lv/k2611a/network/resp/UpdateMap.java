package lv.k2611a.network.resp;

import lv.k2611a.network.MapDTO;
import lv.k2611a.util.ByteUtils;

public class UpdateMap implements Response, CustomSerializationHeader {
    private MapDTO map;
    private long tickCount;

    public MapDTO getMap() {
        return map;
    }

    public void setMap(MapDTO map) {
        this.map = map;
    }

    public long getTickCount() {
        return tickCount;
    }

    public void setTickCount(long tickCount) {
        this.tickCount = tickCount;
    }

    @Override
    public byte serializerId() {
        return 3;
    }

    @Override
    public byte[] toBytes() {
        byte[] payload = new byte[getSize()];
        byte[] mapDTOBytes = map.toBytes();
        int position = 0;

        System.arraycopy(mapDTOBytes,0,payload,0,mapDTOBytes.length);
        position += mapDTOBytes.length;

        byte[] tickCountBytes = ByteUtils.longToBytes(tickCount);
        payload[position] = tickCountBytes[0];
        payload[position + 1] = tickCountBytes[1];
        payload[position + 2] = tickCountBytes[2];
        payload[position + 3] = tickCountBytes[3];
        payload[position + 4] = tickCountBytes[4];
        payload[position + 5] = tickCountBytes[5];
        payload[position + 6] = tickCountBytes[6];
        payload[position + 7] = tickCountBytes[7];

        position+=8;


        return payload;
    }


    @Override
    public int getSize() {
        return map.getSize() + 8;
    }
}
