package lv.k2611a.network;

import lv.k2611a.domain.Building;
import lv.k2611a.network.resp.CustomSerialization;
import lv.k2611a.util.ByteUtils;

public class BuildingDTO implements CustomSerialization {
    private int id;
    private short x;
    private short y;
    private byte type;
    private short hp;
    private short maxHp;
    private byte width;
    private byte height;
    private boolean constructionComplete;
    private byte entityBuiltId;
    private byte ownerId;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public short getHp() {
        return hp;
    }

    public void setHp(short hp) {
        this.hp = hp;
    }

    public short getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(short maxHp) {
        this.maxHp = maxHp;
    }

    public byte getWidth() {
        return width;
    }

    public void setWidth(byte width) {
        this.width = width;
    }

    public byte getHeight() {
        return height;
    }

    public void setHeight(byte height) {
        this.height = height;
    }

    public boolean isConstructionComplete() {
        return constructionComplete;
    }

    public void setConstructionComplete(boolean constructionComplete) {
        this.constructionComplete = constructionComplete;
    }

    public int getEntityBuiltId() {
        return entityBuiltId;
    }

    public void setEntityBuiltId(byte entityBuiltId) {
        this.entityBuiltId = entityBuiltId;
    }

    public byte getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(byte ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public int getSize() {
        return 18;
    }

    @Override
    public byte[] toBytes() {
        byte[] payload = new byte[getSize()];

        byte[] idBytes = ByteUtils.intToBytes(id);
        payload[0] = idBytes[0];
        payload[1] = idBytes[1];
        payload[2] = idBytes[2];
        payload[3] = idBytes[3];

        byte[] xBytes = ByteUtils.shortToBytes(x);
        byte[] yBytes = ByteUtils.shortToBytes(y);

        payload[4] = xBytes[0];
        payload[5] = xBytes[1];
        payload[6] = yBytes[0];
        payload[7] = yBytes[1];

        byte[] hpBytes = ByteUtils.shortToBytes(hp);
        byte[] maxHpBytes = ByteUtils.shortToBytes(maxHp);

        payload[8] = hpBytes[0];
        payload[9] = hpBytes[1];

        payload[10] = maxHpBytes[0];
        payload[11] = maxHpBytes[1];

        payload[12] = type;

        payload[13] = width;
        payload[14] = height;

        payload[15] = ByteUtils.booleanToByte(constructionComplete);

        payload[16] = entityBuiltId;

        payload[17] = ownerId;

        return payload;
    }

    public static BuildingDTO fromBuilding(Building building) {
        BuildingDTO dto = new BuildingDTO();
        dto.setType((byte) building.getType().getIdOnJS());
        dto.setHp((short) building.getHp());
        dto.setMaxHp((short) building.getType().getHp());
        dto.setId(building.getId());
        dto.setX((short) building.getX());
        dto.setY((short) building.getY());
        dto.setWidth((byte) building.getType().getWidth());
        dto.setHeight((byte) building.getType().getHeight());
        dto.setConstructionComplete(building.isAwaitingClick());
        if (building.getBuildingTypeBuilt() != null) {
            dto.setEntityBuiltId((byte) building.getBuildingTypeBuilt().getIdOnJS());
        }
        dto.setOwnerId((byte) building.getOwnerId());
        return dto;
    }
}
