package lv.k2611a.network;

import lv.k2611a.domain.ConstructionOption;
import lv.k2611a.network.resp.CustomSerialization;
import lv.k2611a.util.ByteUtils;

public class OptionDTO implements CustomSerialization {
    private byte type;
    private byte entityToBuildType;
    private short cost;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getEntityToBuildType() {
        return entityToBuildType;
    }

    public void setEntityToBuildType(byte entityToBuildType) {
        this.entityToBuildType = entityToBuildType;
    }

    public short getCost() {
        return cost;
    }

    public void setCost(short cost) {
        this.cost = cost;
    }

    @Override
    public byte[] toBytes() {
        byte[] payload = new byte[getSize()];
        byte[] costBytes = ByteUtils.shortToBytes(cost);

        payload[0] = costBytes[0];
        payload[1] = costBytes[1];
        payload[2] = type;
        payload[3] = entityToBuildType;
        return payload;
    }

    @Override
    public int getSize() {
        return 4;
    }

    public static OptionDTO fromConstructionOption(ConstructionOption constructionOption) {
        OptionDTO dto = new OptionDTO();
        dto.setType((byte) constructionOption.getIdOnJS());
        dto.setEntityToBuildType((byte) constructionOption.getEntityToBuildIdOnJs());
        dto.setCost((short) constructionOption.getCost());
        return dto;
    }
}
