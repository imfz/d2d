package lv.k2611a.network.resp;

import java.util.Arrays;

import lv.k2611a.util.ByteUtils;

public class UpdateMoney implements Response, CustomSerialization {
    private long money;
    private long electricity;

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getElectricity() {
        return electricity;
    }

    public void setElectricity(long electricity) {
        this.electricity = electricity;
    }


    @Override
    public byte[] getData() {
        byte[] payload = new byte[9];
        payload[0] = 1;

        byte[] moneyBytes = ByteUtils.intToBytes(money);
        byte[] electricityBytes = ByteUtils.intToBytes(electricity);

        payload[1] = moneyBytes[0];
        payload[2] = moneyBytes[1];
        payload[3] = moneyBytes[2];
        payload[4] = moneyBytes[3];

        payload[5] = electricityBytes[0];
        payload[6] = electricityBytes[1];
        payload[7] = electricityBytes[2];
        payload[8] = electricityBytes[3];

        return payload;
    }
}
