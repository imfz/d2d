package lv.k2611a.network;

import lv.k2611a.domain.Unit;
import lv.k2611a.domain.UnitType;
import lv.k2611a.domain.unitgoals.Harvest;
import lv.k2611a.domain.unitgoals.UnitGoal;
import lv.k2611a.network.resp.CustomSerialization;
import lv.k2611a.util.ByteUtils;

public class UnitDTO implements CustomSerialization {
    private int id;
    private short x;
    private short y;
    private byte unitType;
    private byte viewDirection;
    private byte travelledPercents;
    private short hp;
    private short maxHp;
    private byte spicePercents;
    private byte ownerId;
    private byte harvesting;

    public int getUnitType() {
        return unitType;
    }

    public void setUnitType(byte unitType) {
        this.unitType = unitType;
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

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getViewDirection() {
        return viewDirection;
    }

    public void setViewDirection(byte viewDirection) {
        this.viewDirection = viewDirection;
    }

    public byte getTravelledPercents() {
        return travelledPercents;
    }

    public void setTravelledPercents(byte travelledPercents) {
        this.travelledPercents = travelledPercents;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(short hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(short maxHp) {
        this.maxHp = maxHp;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(byte ownerId) {
        this.ownerId = ownerId;
    }

    public int getSpicePercents() {
        return spicePercents;
    }

    public void setSpicePercents(byte spicePercents) {
        this.spicePercents = spicePercents;
    }

    public int getHarvesting() {
        return harvesting;
    }

    public void setHarvesting(byte harvesting) {
        this.harvesting = harvesting;
    }

    @Override
    public int getSize() {
        return 18;
    }

    @Override
    public byte[] toBytes() {
        byte[] payload = new byte[getSize()];
        byte[] idBytes = ByteUtils.intToBytes(id);
        byte[] xBytes = ByteUtils.shortToBytes(x);
        byte[] yBytes = ByteUtils.shortToBytes(y);
        byte[] hpBytes = ByteUtils.shortToBytes(hp);
        byte[] maxHpBytes = ByteUtils.shortToBytes(maxHp);

        payload[0] = idBytes[0];
        payload[1] = idBytes[1];
        payload[2] = idBytes[2];
        payload[3] = idBytes[3];

        payload[4] = xBytes[0];
        payload[5] = xBytes[1];

        payload[6] = yBytes[0];
        payload[7] = yBytes[1];

        payload[8] = hpBytes[0];
        payload[9] = hpBytes[1];

        payload[10] = maxHpBytes[0];
        payload[11] = maxHpBytes[1];

        payload[12] = unitType;
        payload[13] = viewDirection;
        payload[14] = travelledPercents;

        payload[15] = spicePercents;
        payload[16] = ownerId;
        payload[17] = harvesting;
        return payload;
    }

    public static UnitDTO fromUnit(Unit unit) {
        UnitDTO dto = new UnitDTO();
        dto.setUnitType(unit.getUnitType().getIdOnJS());
        dto.setX((short) unit.getX());
        dto.setY((short) unit.getY());
        dto.setHp((short) unit.getHp());
        dto.setMaxHp((short) unit.getUnitType().getHp());
        dto.setId(unit.getId());
        dto.setViewDirection((byte) unit.getViewDirection().getIdOnJS());
        dto.setOwnerId((byte) unit.getOwnerId());

        if (unit.getCurrentGoal() != null) {
            unit.getCurrentGoal().saveAdditionalInfoIntoDTO(unit, dto);
        }
        return dto;
    }
}
