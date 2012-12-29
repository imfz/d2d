package lv.k2611a.network.resp;

import lv.k2611a.util.ByteUtils;

public class UpdateMoney implements Response, CustomSerialization {
    private int money;
    private int electricity;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getElectricity() {
        return electricity;
    }

    public void setElectricity(int electricity) {
        this.electricity = electricity;
    }


    @Override
    public byte serializerId() {
        return 1;
    }

    @Override
    public int getSize() {
        return 8;
    }

    @Override
    public byte[] toBytes() {
        byte[] payload = new byte[getSize()];

        byte[] moneyBytes = ByteUtils.intToBytes(money);
        byte[] electricityBytes = ByteUtils.intToBytes(electricity);

        payload[0] = moneyBytes[0];
        payload[1] = moneyBytes[1];
        payload[2] = moneyBytes[2];
        payload[3] = moneyBytes[3];

        payload[4] = electricityBytes[0];
        payload[5] = electricityBytes[1];
        payload[6] = electricityBytes[2];
        payload[7] = electricityBytes[3];

        return payload;
    }


}
