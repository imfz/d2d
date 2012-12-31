package lv.k2611a.network;

import lv.k2611a.domain.Bullet;
import lv.k2611a.network.resp.CustomSerialization;
import lv.k2611a.util.ByteUtils;

public class BulletDTO implements CustomSerialization {

    private byte type;
    private short startX;
    private short startY;
    private short goalX;
    private short goalY;
    private byte progress;

    public short getStartX() {
        return startX;
    }

    public void setStartX(short startX) {
        this.startX = startX;
    }

    public short getStartY() {
        return startY;
    }

    public void setStartY(short startY) {
        this.startY = startY;
    }

    public short getGoalX() {
        return goalX;
    }

    public void setGoalX(short goalX) {
        this.goalX = goalX;
    }

    public short getGoalY() {
        return goalY;
    }

    public void setGoalY(short goalY) {
        this.goalY = goalY;
    }

    public byte getProgress() {
        return progress;
    }

    public void setProgress(byte progress) {
        this.progress = progress;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public static BulletDTO fromBullet(Bullet bullet) {
        BulletDTO dto = new BulletDTO();
        dto.setType(bullet.getBulletType().getIdOnJS());
        dto.setGoalX((short) bullet.getGoalX());
        dto.setGoalY((short) bullet.getGoalY());
        dto.setStartX((short) bullet.getStartX());
        dto.setStartY((short) bullet.getStartY());
        int progress = (int) ((double)bullet.getTicksToMove() / bullet.getTicksToMoveTotal() * 100);
        progress = 100 - progress;
        dto.setProgress((byte) progress);
        return dto;
    }

    @Override
    public byte[] toBytes() {
        byte[] payload = new byte[getSize()];
        byte[] xBytes = ByteUtils.shortToBytes(startX);
        byte[] yBytes = ByteUtils.shortToBytes(startY);
        byte[] xGoalBytes = ByteUtils.shortToBytes(goalX);
        byte[] yGoalBytes = ByteUtils.shortToBytes(goalY);
        payload[0] = xBytes[0];
        payload[1] = xBytes[1];
        payload[2] = yBytes[0];
        payload[3] = yBytes[1];
        payload[4] = xGoalBytes[0];
        payload[5] = xGoalBytes[1];
        payload[6] = yGoalBytes[0];
        payload[7] = yGoalBytes[1];
        payload[8] = progress;
        payload[9] = type;
        return payload;
    }

    @Override
    public int getSize() {
        return 10;
    }
}
