package lv.k2611a.network.resp;

import lv.k2611a.network.OptionDTO;
import lv.k2611a.util.ByteUtils;

public class UpdateConstructionOptions implements Response, CustomSerializationHeader {
    private OptionDTO[] options;
    private int builderId;
    private int currentlyBuildingId;
    private boolean readyToBuild;
    private byte percentsDone;
    private byte currentlyBuildingOptionType;

    public OptionDTO[] getOptions() {
        return options;
    }

    public void setOptions(OptionDTO[] options) {
        this.options = options;
    }

    public int getBuilderId() {
        return builderId;
    }

    public void setBuilderId(int builderId) {
        this.builderId = builderId;
    }

    public boolean isReadyToBuild() {
        return readyToBuild;
    }

    public void setReadyToBuild(boolean readyToBuild) {
        this.readyToBuild = readyToBuild;
    }

    public byte getPercentsDone() {
        return percentsDone;
    }

    public void setPercentsDone(byte percentsDone) {
        this.percentsDone = percentsDone;
    }

    public int getCurrentlyBuildingId() {
        return currentlyBuildingId;
    }

    public void setCurrentlyBuildingId(int currentlyBuildingId) {
        this.currentlyBuildingId = currentlyBuildingId;
    }

    public byte getCurrentlyBuildingOptionType() {
        return currentlyBuildingOptionType;
    }

    public void setCurrentlyBuildingOptionType(byte currentlyBuildingOptionType) {
        this.currentlyBuildingOptionType = currentlyBuildingOptionType;
    }

    @Override
    public byte serializerId() {
        return 4;
    }

    @Override
    public byte[] toBytes() {
        byte[] payload = new byte[getSize()];

        int position = 0;


        if (options != null) {
            byte[] sizeBytes = ByteUtils.intToBytes(options.length);
            System.arraycopy(sizeBytes, 0, payload, position, sizeBytes.length);
            position += 4;
            for (OptionDTO optionDTO : options) {
                byte[] bytes = optionDTO.toBytes();
                System.arraycopy(bytes, 0, payload, position, bytes.length);
                position += bytes.length;
            }
        } else {
            byte[] sizeBytes = ByteUtils.intToBytes(0);
            System.arraycopy(sizeBytes, 0, payload, position, sizeBytes.length);
            position += 4;
        }

        byte[] builderIdBytes = ByteUtils.intToBytes(builderId);
        payload[position] = builderIdBytes[0];
        payload[position + 1] = builderIdBytes[1];
        payload[position + 2] = builderIdBytes[2];
        payload[position + 3] = builderIdBytes[3];

        position += 4;

        byte[] currentlyBuildingIdBytes = ByteUtils.intToBytes(currentlyBuildingId);
        payload[position] = currentlyBuildingIdBytes[0];
        payload[position + 1] = currentlyBuildingIdBytes[1];
        payload[position + 2] = currentlyBuildingIdBytes[2];
        payload[position + 3] = currentlyBuildingIdBytes[3];

        position += 4;

        payload[position] = ByteUtils.booleanToByte(this.readyToBuild);
        position++;

        payload[position] = percentsDone;
        position++;

        payload[position] = currentlyBuildingOptionType;
        position++;


        return payload;
    }

    @Override
    public int getSize() {
        int totalSize = 0;
        totalSize += 4;
        if (options != null) {
            for (OptionDTO option : options) {
                totalSize += option.getSize();
            }
        }
        totalSize += 11;
        return totalSize;
    }
}
