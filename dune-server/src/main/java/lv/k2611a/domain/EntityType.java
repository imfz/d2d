package lv.k2611a.domain;

public interface EntityType {
    byte getIdOnJS();

    int getCost();

    String getName();

    BuildingType[] getPrerequisites();
}
