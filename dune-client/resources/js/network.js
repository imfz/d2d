var connection = {
    start : function(name) {
        this._username = name;
        var location = "ws://localhost:8080/chat";
        this._ws = new WebSocket(location, "chat");
        this._ws.onopen = this.onopen;
        this._ws.onmessage = this.onmessage;
        this._ws.onclose = this.onclose;
    },

    onopen : function() {
        var that = connection;
        document.getElementById('join').className = 'hidden';
        document.getElementById('joined').className = '';
        document.getElementById('phrase').focus();
        that.sendJoin(that._username);
    },

    sendJoin: function(username) {
        var joinRequest = new Object();
        joinRequest.nickname = username;
        this.sendNetworkRequest("Join",joinRequest);
    },

    sendChatMessage : function(message) {
        var chatMessage = new Object();
        chatMessage.message = message;
        this.sendNetworkRequest("ChatMessage", chatMessage);
    },

    sendUnitAction : function(ids,x,y) {
        var unitAction = new Object();
        unitAction.ids = ids;
        unitAction.x = x;
        unitAction.y = y;
        this.sendNetworkRequest("UnitAction", unitAction);
    },

    sendNetworkRequest:function (messageName ,messageObj) {
        var networkPacket = new Object();
        networkPacket.messageName = messageName;
        networkPacket.payload = $.toJSON(messageObj);
        if (this._ws)
            this._ws.send($.toJSON(networkPacket));
    },

    onmessage : function(m) {
        var that = connection;
        if (m.data) {
            obj = $.parseJSON(m.data);
            func = handler["handle" + obj.messageName];
            func.call(handler, $.parseJSON(obj.payload));
        }
    },

    onclose : function(m) {
        this._ws = null;
        document.getElementById('join').className = '';
        document.getElementById('joined').className = 'hidden';
        document.getElementById('username').focus();
        document.getElementById('chat').innerHTML = '';
    }
};

function Networking() {
    console.log("Networking created");
}

Networking.prototype.init = function() {
    if (!window.WebSocket) {
        window.WebSocket=window.MozWebSocket;
        if (!window.WebSocket)
            alert("WebSocket not supported by this browser");
    }
    console.log("Networking initialized");
}
