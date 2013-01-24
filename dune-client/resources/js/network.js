function NetworkConnection() {
    this.username = null;
    this.playerId = -1;
    this.connEstablished = false;
    this.ws = null;
}

NetworkConnection.prototype.start = function (name, host) {
    this._username = name;
    this._connEstablished = false;
    this._ws = new WebSocket(host, "chat");
    this._ws.binaryType = "arraybuffer";
    this._ws.onopen = this.onopen;
    this._ws.onmessage = this.onmessage;
    this._ws.onclose = this.onclose;
    console.log("Username set to " + this._username);
    console.log("connecting.. ");
};

NetworkConnection.prototype.logout = function (name) {
    this._ws.close();
};

NetworkConnection.prototype.onopen = function () {
    connection._connEstablished = true;
    console.log("connected");
    connection.sendJoin(connection._username);
};

NetworkConnection.prototype.sendJoin = function (username) {
    var joinRequest = {};
    joinRequest.nickname = username;
    this.sendNetworkRequest("Join", joinRequest);
};

NetworkConnection.prototype.sendChatMessage = function (message) {
    var chatMessage = {};
    chatMessage.message = message;
    this.sendNetworkRequest("ChatMessage", chatMessage);
};

NetworkConnection.prototype.sendUnitAction = function (ids, x, y) {
    var unitAction = {};
    unitAction.ids = ids;
    unitAction.x = x;
    unitAction.y = y;
    this.sendNetworkRequest("UnitAction", unitAction);
};


NetworkConnection.prototype.sendAttackMove = function (ids, x, y) {
    var unitAttackMove = {};
    unitAttackMove.ids = ids;
    unitAttackMove.x = x;
    unitAttackMove.y = y;
    this.sendNetworkRequest("UnitAttackMove", unitAttackMove);
};

NetworkConnection.prototype.sendUnitStop = function (ids) {
    var unitStop = {};
    unitStop.ids = ids;
    this.sendNetworkRequest("UnitStop", unitStop);
};

NetworkConnection.prototype.sendCreateGame = function () {
    var createNewGame = {};
    createNewGame.height = 64;
    createNewGame.width = 64;
    this.sendNetworkRequest("CreateNewGame", createNewGame);
};

NetworkConnection.prototype.sendStartGame = function () {
    var startGame = {};
    this.sendNetworkRequest("StartGame", startGame);
};

NetworkConnection.prototype.sendJoinGame = function (id) {
    var joinGame = {};
    joinGame.id = id;
    this.sendNetworkRequest("JoinGame", joinGame);
};

NetworkConnection.prototype.sendLeaveGame = function () {
    var leaveGame = {};
    this.sendNetworkRequest("LeaveGame", leaveGame);
};

NetworkConnection.prototype.moveToPlayers = function (username) {
    var moveToPlayers = {};
    moveToPlayers.username = username;
    this.sendNetworkRequest("MoveToPlayers", moveToPlayers);
};

NetworkConnection.prototype.moveToObservers = function (username) {
    var moveToObservers = {};
    moveToObservers.username = username;
    this.sendNetworkRequest("MoveToObservers", moveToObservers);
};

NetworkConnection.prototype.sendBuildingSelection = function (id) {
    var selectBuilding = {};
    selectBuilding.selectedId = id;
    this.sendNetworkRequest("SelectBuilding", selectBuilding);
};

NetworkConnection.prototype.sendCancelConstruction = function (builderId) {
    var cancelConstruction = {};
    cancelConstruction.builderId = builderId;
    this.sendNetworkRequest("CancelConstruction", cancelConstruction);
};

NetworkConnection.prototype.sendBuildingPlacement = function (x, y, builderId) {
    var placeBuilding = {};
    placeBuilding.x = x;
    placeBuilding.y = y;
    placeBuilding.builderId = builderId;
    this.sendNetworkRequest("PlaceBuilding", placeBuilding);
};

NetworkConnection.prototype.sendStartConstruction = function (builderId, entityToBuildId) {
    var startConstruction = {};
    startConstruction.builderId = builderId;
    startConstruction.entityToBuildId = entityToBuildId;
    this.sendNetworkRequest("StartConstruction", startConstruction);
};

NetworkConnection.prototype.sendNetworkRequest = function (messageName, messageObj) {
    if (!connection._ws) {
        console.log("Trying to send message, while disconnected");
        Utils.showError("Disconnected");
        return;
    }
    var networkPacket = {};
    networkPacket.messageName = messageName;
    networkPacket.payload = $.toJSON(messageObj);
    if (this._ws) {
        this._ws.send($.toJSON(networkPacket));
    } else {
        console.log("Trying to send message, while ws = null");
    }
};

NetworkConnection.prototype.onmessage = function (m) {
    var that = this;
    if (m.data) {
        if (typeof m.data === "string") {
            var jsonPayload = m.data.substring(0);
            obj = $.parseJSON(jsonPayload);
            func = handler["handle" + obj.messageName];
            func.call(handler, $.parseJSON(obj.payload));
        } else {
            var byteArray = new Uint8Array(m.data);
            var serializerId = byteArray[0];
            var data = Serializers[serializerId](byteArray.subarray(1));
            func = handler["handle" + data[0]];
            func.call(handler, data[1]);
        }
    }
};

NetworkConnection.prototype.onclose = function (m) {
    if (!connection._connEstablished) {
        console.log("Cannot connect to socket");
        Utils.showError("Cannot connect to socket");
    }
    connection._ws = null;
    windows.openLogin();
    console.log("connection closed");
};

NetworkConnection.prototype.init = function () {
    if (!window.WebSocket) {
        window.WebSocket = window.MozWebSocket;
        if (!window.WebSocket) {
            alert("WebSocket not supported by this browser");
        }
    }
    console.log("NetworkConnection initialized");
};

connection = new NetworkConnection();
