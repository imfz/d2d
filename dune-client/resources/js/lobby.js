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
    $("#startgamebutton").click(function () {
        that.startGame()
    });
    $("#leavegamebutton").click(function () {
        that.leaveGame()
    });
    console.log("Lobby initialized");
};

Lobby.prototype.createGame = function () {
    this.network.sendCreateGame();
};


Lobby.prototype.startGame = function () {
    this.network.sendStartGame();
};

Lobby.prototype.leaveGame = function () {
    this.network.sendLeaveGame();
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
    playersTd.append(document.createTextNode(game.usedSlotCount));

    var sizeTd = $('<td></td>');
    sizeTd.append(document.createTextNode(game.width + " x " + game.height));

    var creatorTd = $('<td></td>');
    creatorTd.append(document.createTextNode(game.creator));

    var observersTd = $('<td></td>');
    observersTd.append(document.createTextNode(game.observersCount));

    var startedTd = $('<td></td>');
    if (game.started) {
        startedTd.append(document.createTextNode("Yes"));
    } else {
        startedTd.append(document.createTextNode("No"));
    }



    var gameHtml = $('<tr></tr>');
    gameHtml.append(joinGameButtonTd);
    gameHtml.append(gameIdTd);
    gameHtml.append(playersTd);
    gameHtml.append(observersTd);
    gameHtml.append(sizeTd);
    gameHtml.append(creatorTd);
    gameHtml.append(startedTd);

    $("#gamelisttable > tbody:last").append(gameHtml);

};

Lobby.prototype.showOrUpdateInGameLobby = function (data) {
    windows.openInGameLobby();

    var game = data.gameDTO;

    var youAreOwner = game.creator == this.network._username;
    if (youAreOwner) {
        $("#startgamebutton").css({display: "block"});
    } else {
        $("#startgamebutton").css({display: "none"});
    }

    $("#playersInGameList > tbody").empty();
    $("#observersInGameList > tbody").empty();

    for (var i = 0; i < game.players.length; i++) {
        var player = game.players[i];

        var nicknameTd = $('<td></td>');
        nicknameTd.append(document.createTextNode(player));

        var moveBtn = $('<td></td>');
        if (youAreOwner) {
            moveBtn.append(this.createMovePlayerDown(player));
        }

        var playerHtml = $('<tr></tr>');
        playerHtml.append(nicknameTd);
        playerHtml.append(moveBtn);

        if (player == this.network._username) {
            playerHtml.addClass("info");
        } else {
            if (player == game.creator) {
                playerHtml.addClass("success");
            }
        }



        $("#playersInGameList > tbody").append(playerHtml);

    }

    $("#playersInGameList").find('caption').text("Players ( " + game.players.length  +" )");

    for (var i = 0; i < game.observers.length; i++) {
        var observer = game.observers[i];

        var nicknameTd = $('<td></td>');
        nicknameTd.append(document.createTextNode(observer));

        var moveBtn = $('<td></td>');

        if (youAreOwner) {
            moveBtn.append(this.createMovePlayerUp(observer));
        }

        var observerHtml = $('<tr></tr>');
        observerHtml.append(nicknameTd);
        observerHtml.append(moveBtn);

        if (observer == this.network._username) {
            observerHtml.addClass("info");
        } else {
            if (observer == game.creator) {
                observerHtml.addClass("success");
            }
        }


        $("#observersInGameList > tbody").append(observerHtml);

    }

    $("#observersInGameList").find('caption').text("Observers ( " + game.observers.length  +" )");

};

Lobby.prototype.createMovePlayerUp = function(playerName) {
    var movePlayerBtn = $('<button class="btn">Move to players</button>');
    movePlayerBtn.click(function () {
        connection.moveToPlayers(playerName)
    });
    return movePlayerBtn;
};

Lobby.prototype.createMovePlayerDown = function(playerName) {
    var movePlayerBtn = $('<button class="btn">Move to observers</button>');
    movePlayerBtn.click(function () {
        connection.moveToObservers(playerName)
    });
    return movePlayerBtn;
};