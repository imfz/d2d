package lv.k2611a.domain;

public interface EntityType {
    int getIdOnJS();

    int getCost();

    String getName();

    BuildingType[] getPrerequisites();
}
