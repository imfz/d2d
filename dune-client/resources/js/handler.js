function Handler(map, gameLog, rightMenu, engine, moneyTab) {
    this.map = map;
    this.gameLog = gameLog;
    this.rightMenu = rightMenu;
    this.engine = engine;
    this.moneyTab = moneyTab;
    this.centeredOnMain = false;
}

Handler.prototype.addMessageToChat = function(from, text) {
    var chat = document.getElementById('chat');
    var spanFrom = document.createElement('span');
    spanFrom.className = 'from';
    spanFrom.innerHTML = from + ':&nbsp;';
    var spanText = document.createElement('span');
    spanText.className = 'text';
    spanText.innerHTML = text;
    var lineBreak = document.createElement('br');
    chat.appendChild(spanFrom);
    chat.appendChild(spanText);
    chat.appendChild(lineBreak);
    chat.scrollTop = chat.scrollHeight - chat.clientHeight;
};

Handler.prototype.handleIncomingChatMessage = function(data) {
    var message = data.message;
    var from = data.from
            .replace('<', '&lt;')
            .replace('>', '&gt;');
    var text = message
            .replace('<', '&lt;')
            .replace('>', '&gt;');
    this.addMessageToChat(from, text);
};

Handler.prototype.handleUpdateMap = function(data) {

    if (typeof data.map.units === "undefined") {
        data.map.units = new Array();
    }
    if (typeof data.map.buildings === "undefined") {
        data.map.buildings = new Array();
    }
    if (typeof data.map.tiles === "undefined") {
        data.map.tiles = new Array();
    }

    this.map.setHeight(data.map.height);
    this.map.setWidth(data.map.width);
    this.map.setTickCount(data.tickCount);
    var x = 0;
    var y = 0;
    for (var i = 0; i < data.map.tiles.length; i++) {
        var tile = data.map.tiles[i];
        this.map.setTile(x,y,tile);
        y++;
        if (y >= data.map.height) {
            y = 0;
            x++;
        }
    }


    this.map.setUnits(data.map.units);
    this.map.setBuildings(data.map.buildings);
    console.log("Map loaded. Processing saved updates");
    this.processSavedUpdates();
    if (!this.centeredOnMain) {
        this.centeredOnMain = true;
        this.engine.centerOnMain();
    }

};

Handler.prototype.processSavedUpdates = function() {
    if (this.updates) {
        for (var i = 0; i < this.updates.length; i++) {
            var update = this.updates[i];
            this.processUpdate(update);
        }
        this.updates = new Array();
    }
};

Handler.prototype.processUpdate = function(update) {

    if (typeof update.changedTiles === "undefined") {
        update.changedTiles = new Array();
    }

    if (typeof update.buildings === "undefined") {
        update.buildings = new Array();
    }

    if (typeof update.units === "undefined") {
        update.units = new Array();
    }

    if (update.tickCount == this.map.tickCount + 1) {
        this.map.tickCount++;
        this.gameLog.setMapVersion(this.map.tickCount);
    } else {
        console.log("Processing map update to version " + update.tickCount + " but internal map was in version " + this.map.tickCount + " . Ignoring");
    }
    this.map.setUnits(update.units);
    this.map.setBuildings(update.buildings);
    for (var i = 0; i < update.changedTiles.length; i++) {
        var tile = update.changedTiles[i];
        this.map.setTile(tile.x, tile.y, tile);
    }
};

Handler.prototype.storeUpdateForFutureUse = function(data) {
    if (!this.updates) {
        this.updates = new Array();
    }
    this.updates.push(data);
};

Handler.prototype.handleUpdateMapIncremental = function(data) {
    if (data.tickCount == this.map.tickCount + 1) {
        this.processUpdate(data);
    } else {
        console.log("Received map update to version " + data.tickCount + " but internal map was in version " + this.map.tickCount + " . Saved");
        this.storeUpdateForFutureUse(data);
    }
};

Handler.prototype.handleJoined = function(data) {
    this.addMessageToChat("SYSTEM", data.nickname + " has joined the game");
};

Handler.prototype.handleLeft = function(data) {
    this.addMessageToChat("SYSTEM", data.nickname + " has left the game");
};

Handler.prototype.handleUpdateMoney = function(data) {
    this.moneyTab.setMoney(data.money, data.electricity);
};

Handler.prototype.handleUpdateConstructionOptions = function(data) {

    if (typeof data.options === "undefined") {
        data.options = new Array();
    }

    // reset building mouse cursor here, after building
    if (engine.placementEnabled) {
        if (engine.builderId == data.builderId) {
            if (data.readyToBuild) {
                this.engine.placementEnabled = false;
            }
        }
    }

    this.rightMenu.setOptions(data.builderId, data.options, data.percentsDone, data.currentlyBuildingId, data.currentlyBuildingOptionId);
};
