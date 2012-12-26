function MoneyTab() {
    this.money = 0;
    this.electricity = 0;
}

MoneyTab.prototype.setMoney = function (money,electricity) {
    this.money = money;
    this.electricity = electricity;
    this.redraw();
};

MoneyTab.prototype.redraw = function () {
    var electricityText = this.electricity;
    if (electricityText > 0) {
        electricityText = "<font color=\"green\"><b> +" + electricityText + "</b></font>";
    } else if (electricityText < 0){
        electricityText = "<font color=\"red\"><b> " + electricityText + "</b></font>";
    } else {
        electricityText = "<font color=\"orange\"><b> " + electricityText + "</b></font>";
    }
    $('#moneyTab').html("Credits : " + this.money + " Power : " + electricityText);
};