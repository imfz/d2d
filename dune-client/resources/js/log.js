function GameLog() {
}

GameLog.prototype.setMapVersion = function (mapVersion) {
    this.mapVersion = mapVersion;
    this.redraw();
};

GameLog.prototype.redraw = function () {
    $('#gameLog').html("map version : " + this.mapVersion);
};