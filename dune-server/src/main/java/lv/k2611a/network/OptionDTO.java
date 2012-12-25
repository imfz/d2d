package lv.k2611a.network;

public class OptionDTO {
    private int type;
    private int entityToBuildId;
    private String name;
    private int cost;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getEntityToBuildId() {
        return entityToBuildId;
    }

    public void setEntityToBuildId(int entityToBuildId) {
        this.entityToBuildId = entityToBuildId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
