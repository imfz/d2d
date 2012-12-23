function MoneyTab() {
}

MoneyTab.prototype.setMoney = function (money) {
    this.money = money;
    this.redraw();
};

MoneyTab.prototype.redraw = function () {
    $('#moneyTab').html("Credits : " + this.money);
};