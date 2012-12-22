package lv.k2611a;

public class NetworkPacket {
    private String messageName;
    private String payload;

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "NetworkPacket{" +
                "messageName='" + messageName + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
