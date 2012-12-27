function NetworkConnection() {
    this.username = null;
    this.playerId = -1;
    this.connEstablished = false;
    this.location = "ws://localhost:8080/chat";
    this.ws = null;
}


NetworkConnection.prototype.start = function (name, playerId) {
    this._username = name;
    this._playerId = playerId;
    this._connEstablished = false;
    var location = "ws://localhost:8080/chat";
    this._ws = new WebSocket(location, "chat");
    this._ws.onopen = this.onopen;
    this._ws.onmessage = this.onmessage;
    this._ws.onclose = this.onclose;
};

NetworkConnection.prototype.onopen = function () {
    connection._connEstablished = true;
    $("#joinform").hide();
    $("#joinedform").show();
    $("#phrase").focus();
    connection.sendJoin(connection._username, connection._playerId);
};

NetworkConnection.prototype.sendJoin = function (username, playerId) {
    var joinRequest = new Object();
    joinRequest.nickname = username;
    joinRequest.playerId = playerId;
    this.sendNetworkRequest("Join", joinRequest);
};

NetworkConnection.prototype.sendChatMessage = function (message) {
    var chatMessage = new Object();
    chatMessage.message = message;
    this.sendNetworkRequest("ChatMessage", chatMessage);
};

NetworkConnection.prototype.sendUnitAction = function (ids, x, y) {
    var unitAction = new Object();
    unitAction.ids = ids;
    unitAction.x = x;
    unitAction.y = y;
    this.sendNetworkRequest("UnitAction", unitAction);
};

NetworkConnection.prototype.sendUnitStop = function (ids) {
    var unitStop = new Object();
    unitStop.ids = ids;
    this.sendNetworkRequest("UnitStop", unitStop);
};

NetworkConnection.prototype.sendBuildingSelection = function (id) {
    var selectBuilding = new Object();
    selectBuilding.selectedId = id;
    this.sendNetworkRequest("SelectBuilding", selectBuilding);
};

NetworkConnection.prototype.sendCancelConstruction = function (builderId) {
    var cancelConstruction = new Object();
    cancelConstruction.builderId = builderId;
    this.sendNetworkRequest("CancelConstruction", cancelConstruction);
};

NetworkConnection.prototype.sendBuildingPlacement = function (x, y, builderId) {
    var placeBuilding = new Object();
    placeBuilding.x = x;
    placeBuilding.y = y;
    placeBuilding.builderId = builderId;
    this.sendNetworkRequest("PlaceBuilding", placeBuilding);
};

NetworkConnection.prototype.sendStartConstruction = function (builderId, entityToBuildId) {
    var startConstruction = new Object();
    startConstruction.builderId = builderId;
    startConstruction.entityToBuildId = entityToBuildId;
    this.sendNetworkRequest("StartConstruction", startConstruction);
};

NetworkConnection.prototype.sendNetworkRequest = function (messageName, messageObj) {
    var networkPacket = new Object();
    networkPacket.messageName = messageName;
    networkPacket.payload = $.toJSON(messageObj);
    if (this._ws) {
        this._ws.send($.toJSON(networkPacket));
    }
};

NetworkConnection.prototype.onmessage = function (m) {
    var that = this;
    if (m.data) {
        obj = $.parseJSON(m.data);
        func = handler["handle" + obj.messageName];
        func.call(handler, $.parseJSON(obj.payload));
    }
};

NetworkConnection.prototype.onclose = function (m) {
    if (!connection._connEstablished) {
        Utils.showError("Cannot connect to socket");
    }
    connection._ws = null;
    $("#joinform").show();
    $("#joinedform").hide();
    $("#chat").html('');
    $("#username").focus();
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
