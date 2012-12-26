package lv.k2611a.domain;

public class Player {
    private long money = 10000;
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
}
