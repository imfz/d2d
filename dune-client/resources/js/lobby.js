function Lobby() {

}

Lobby.prototype.setNetwork = function (network) {
    this.network = network;
};

Lobby.prototype.init = function () {
    var that = this;
    $("#creategamebutton").click(function () {
        that.createGame()
    });
    console.log("Lobby initialized");
};

Lobby.prototype.createGame = function () {
    this.network.sendCreateGame();
};

Lobby.prototype.updateGameList = function (games) {
    $("#gamelisttable > tbody").empty();
    for (var i = 0; i < games.length; i++) {
        var game = games[i];
        this.showGameInfo(game);
    }
};


Lobby.prototype.showGameInfo = function (game) {
    var joinGameButton = $('<button id="creategamebutton" class="btn btn-primary">Join game</button>');
    joinGameButton.click(function () {
        connection.sendJoinGame(game.id)
    });

    var joinGameButtonTd = $('<td></td>');
    joinGameButtonTd.append(joinGameButton);


    var gameIdTd = $('<td></td>');
    gameIdTd.append(document.createTextNode(game.id));

    var gameHtml = $('<tr></tr>');
    gameHtml.append(joinGameButtonTd);
    gameHtml.append(gameIdTd);

    $("#gamelisttable > tbody:last").append(gameHtml);

};