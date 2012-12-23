package lv.k2611a.network.resp;

public class UpdateMoney implements Response {
    private long money;

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}
