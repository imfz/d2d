package lv.k2611a.network;

import lv.k2611a.domain.Explosion;
import lv.k2611a.network.resp.CustomSerialization;
import lv.k2611a.util.ByteUtils;

public class ExplosionDTO implements CustomSerialization {

    private short x;
    private short y;
    private byte explosionType;

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public int getExplosionType() {
        return explosionType;
    }

    public void setExplosionType(byte explosionType) {
        this.explosionType = explosionType;
    }

    @Override
    public byte[] toBytes() {
        byte[] payload = new byte[getSize()];

        byte[] xBytes = ByteUtils.shortToBytes(x);
        byte[] yBytes = ByteUtils.shortToBytes(y);

        payload[0] = xBytes[0];
        payload[1] = xBytes[1];

        payload[2] = yBytes[0];
        payload[3] = yBytes[1];

        payload[4] = explosionType;

        return payload;
    }

    @Override
    public int getSize() {
        return 5;
    }

    public static ExplosionDTO fromExplosion(Explosion explosion) {
        ExplosionDTO dto = new ExplosionDTO();
        dto.setX((short) explosion.getX());
        dto.setY((short) explosion.getY());
        dto.setExplosionType(explosion.getExplosionType().getIdOnJS());
        return dto;
    }

    @Override
    public String toString() {
        return "ExplosionDTO{" +
                "x=" + x +
                ", y=" + y +
                ", explosionType=" + explosionType +
                '}';
    }
}
