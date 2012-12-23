package lv.k2611a.network.resp;

import lv.k2611a.network.OptionDTO;

public class UpdateConstructionOptions implements Response {
    private int builderId;
    private boolean readyToBuild;
    private OptionDTO[] options;
    private int percentsDone;
    private int currentlyBuildingId;

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

    public int getPercentsDone() {
        return percentsDone;
    }

    public void setPercentsDone(int percentsDone) {
        this.percentsDone = percentsDone;
    }

    public int getCurrentlyBuildingId() {
        return currentlyBuildingId;
    }

    public void setCurrentlyBuildingId(int currentlyBuildingId) {
        this.currentlyBuildingId = currentlyBuildingId;
    }
}
