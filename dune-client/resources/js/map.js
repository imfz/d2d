function GameMap() {
    this.tiles = new Array();
    this.units = new Array();
    this.buildings = new Array();
    console.log("Map created");
    this.width = 0;
    this.height = 0;
}

GameMap.prototype.getWidth = function () {
    return this.width;
};

GameMap.prototype.getHeight = function () {
    return this.height;
};

GameMap.prototype.setHeight = function (height) {
    this.height = height;
};

GameMap.prototype.setWidth = function (width) {
    this.width = width;
};

GameMap.prototype.setTickCount = function (tickCount) {
    this.tickCount = tickCount;
};

GameMap.prototype.setTile = function (x, y, tile) {
    if (!this.tiles[x]) {
        this.tiles[x] = new Array();
    }
    this.tiles[x][y] = tile;
};

GameMap.prototype.getTile = function (x, y) {
    if (!this.tiles[x]) {
        this.tiles[x] = new Array();
    }
    return this.tiles[x][y];
};

GameMap.prototype.setBuildings = function (buildings) {
    this.buildings = buildings;
};

GameMap.prototype.setUnits = function (units) {
    this.units = units;
};

GameMap.prototype.getUnits = function (x, y, x2, y2) {
    var result = new Array();
    for (var i = 0; i < this.units.length; i++) {
        var unit = this.units[i];
        if ((unit.x >= x) && (unit.x <= x2) && (unit.y >= y) && (unit.y <= y2)) {
            result.push(unit);
        }
    }
    return result;
};

GameMap.prototype.isTileOkForBuilding = function (x, y, units, buildings) {
    var tileType = this.getTileType(x, y);
    if (tileType != TILE_TYPE_ROCK) {
        return false;
    }
    for (var i = 0; i < units.length; i++) {
        var unit = units[i];
        if ((unit.x == x) && (unit.y == y)) {
            return false;
        }
        if (unit.travelled > 0) {
            if ((unit.x == x - 1 ) && (unit.y == y) && (unit.viewDirection == VIEW_DIRECTION_RIGHT)) {
                return false;
            }
            if ((unit.x == x - 1 ) && (unit.y == y-1) && (unit.viewDirection == VIEW_DIRECTION_BOTTOMRIGHT)) {
                return false;
            }
            if ((unit.x == x - 1 ) && (unit.y == y+1) && (unit.viewDirection == VIEW_DIRECTION_TOPRIGHT)) {
                return false;
            }
            if ((unit.x == x + 1) && (unit.y == y) && (unit.viewDirection == VIEW_DIRECTION_LEFT)) {
                return false;
            }
            if ((unit.x == x + 1) && (unit.y == y-1) && (unit.viewDirection == VIEW_DIRECTION_BOTTOMLEFT)) {
                return false;
            }
            if ((unit.x == x + 1) && (unit.y == y+1) && (unit.viewDirection == VIEW_DIRECTION_TOPLEFT)) {
                return false;
            }

            if ((unit.x == x) && (unit.y == y - 1) && (unit.viewDirection == VIEW_DIRECTION_BOTTOM)) {
                return false;
            }

            if ((unit.x == x) && (unit.y == y + 1) && (unit.viewDirection == VIEW_DIRECTION_TOP)) {
                return false;
            }
        }
    }

    return true;
};

GameMap.prototype.getBuildings = function (x, y, x2, y2) {
    //TODO: optimization
    return this.buildings;
}


GameMap.prototype.getTileType = function (x, y) {
    if (x < 0) {
        return null;
    }
    if (y < 0) {
        return null;
    }
    if (x >= this.getWidth()) {
        return null;
    }
    if (y >= this.getHeight()) {
        return null;
    }
    var tile = this.getTile(x, y);
    if (!(typeof tile === "undefined")) {
        return tile.tileType;
    }
    return null;
};




