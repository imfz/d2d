package lv.k2611a.network.resp;

public class UpdateMoney implements Response {
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
}
