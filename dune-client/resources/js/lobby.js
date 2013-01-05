function Lobby() {

}

Lobby.prototype.setNetwork = function (network) {
    this.network = network;
};

Lobby.prototype.init = function () {
    var that = this;
    $("#signinbutton").click(function () {
        that.signIn();
        return false;
    });
    $("#creategamebutton").click(function () {
        that.createGame()
    });
    $("#logoutbutton").click(function () {
        that.logout()
    });
    console.log("Lobby initialized");
};

Lobby.prototype.createGame = function () {
    this.network.sendCreateGame();
};

Lobby.prototype.logout = function () {
    this.network.logout();
};

Lobby.prototype.signIn = function () {
    var username = $("#username").val();
    if (username.length == 0) {
        Utils.showError("Username empty");
    } else {
        this.network.start(username);
    }
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

    var playersTd = $('<td></td>');
    playersTd.append(document.createTextNode(game.usedSlotCount + " / " + game.totalSlotCount));

    var sizeTd = $('<td></td>');
    sizeTd.append(document.createTextNode(game.width + " x "+ game.height));

    var creatorTd = $('<td></td>');
    creatorTd.append(document.createTextNode(game.creator));



    var gameHtml = $('<tr></tr>');
    gameHtml.append(joinGameButtonTd);
    gameHtml.append(gameIdTd);
    gameHtml.append(playersTd);
    gameHtml.append(sizeTd);
    gameHtml.append(creatorTd);

    $("#gamelisttable > tbody:last").append(gameHtml);

};