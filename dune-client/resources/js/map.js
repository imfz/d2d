var CELL_BAD = 1;
var CELL_OK = 2;
var CELL_OK_FAR = 3;

function GameMap() {
    this.tiles = [];
    this.units = [];
    this.buildings = [];
    this.bullets = [];
    this.explosions = [];
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
        this.tiles[x] = [];
    }
    this.tiles[x][y] = tile;
};

GameMap.prototype.getTile = function (x, y) {
    if (!this.tiles[x]) {
        this.tiles[x] = [];
    }
    return this.tiles[x][y];
};

GameMap.prototype.setBuildings = function (buildings) {
    this.buildings = buildings;
};

GameMap.prototype.setUnits = function (units) {
    this.units = units;
};

GameMap.prototype.setBullets = function (bullets) {
    this.bullets = bullets;
};

GameMap.prototype.setSprites = function (sprites) {
    this.sprites = sprites;
};

GameMap.prototype.addExplosions = function (explosions) {
    var currentTime = new Date().getTime();
    for (var i = 0; i < explosions.length; i++)  {
        var explosion = explosions[i];
        explosion.timeStarted = currentTime;
        explosion.timeToExpire = currentTime + this.sprites.getExplosionTTL(explosion.type);

    }
    this.explosions = this.explosions.concat(explosions);
};

GameMap.prototype.expireExplosions = function() {
   var currentTime = new Date().getTime();
   var explosionsSurvived = [];
   for (var i = 0; i < this.explosions.length; i++) {
       var explosion = this.explosions[i];
       if (explosion.timeToExpire >= currentTime) {
           explosionsSurvived.push(explosion);
       }
   }
    this.explosions = explosionsSurvived;
};

GameMap.prototype.getUnits = function (x, y, x2, y2) {
    var result = [];
    for (var i = 0; i < this.units.length; i++) {
        var unit = this.units[i];
        if ((unit.x >= x) && (unit.x <= x2) && (unit.y >= y) && (unit.y <= y2)) {
            result.push(unit);
        }
    }
    return result;
};

GameMap.prototype.getExplosions = function (x, y, x2, y2) {
    var result = [];
    for (var i = 0; i < this.explosions.length; i++) {
        var explosion = this.explosions[i];
        //if ((explosion.x >= x) && (explosion.x <= x2) && (explosion.y >= y) && (explosion.y <= y2)) {
            result.push(explosion);
        //}
    }
    return result;
};

GameMap.prototype.isTileOkForBuilding = function (x, y, units, buildings) {

    function getSurroundedCells(centerCell, cellStore) {
        cellStore.push({x: centerCell.x - 1, y: centerCell.y - 1});
        cellStore.push({x: centerCell.x - 1, y: centerCell.y});
        cellStore.push({x: centerCell.x - 1, y: centerCell.y + 1});
        cellStore.push({x: centerCell.x, y: centerCell.y - 1});
        cellStore.push({x: centerCell.x, y: centerCell.y});
        cellStore.push({x: centerCell.x, y: centerCell.y + 1});
        cellStore.push({x: centerCell.x + 1, y: centerCell.y - 1});
        cellStore.push({x: centerCell.x + 1, y: centerCell.y});
        cellStore.push({x: centerCell.x + 1, y: centerCell.y + 1});
    }

    var allocatedCellsByBuildings = [];
    var usedCellsByBuildings = [];
    for (var i = 0; i < buildings.length; i++) {
        var building = buildings[i];
        if (building.ownerId != connection._playerId) {
            continue;
        }
        for (var j = 0; j < building.width; j++) {
            for (var k = 0; k < building.height; k++) {
                var centerCell = {x: building.x + j, y: building.y + k};
                getSurroundedCells(centerCell, allocatedCellsByBuildings);
                usedCellsByBuildings.push(centerCell);
            }
        }
    }

    var tileType = this.getTileType(x, y);
    if (tileType != TILE_TYPE_ROCK) {
        return CELL_BAD;
    }
    for (var i = 0; i < units.length; i++) {
        var unit = units[i];
        if ((unit.x == x) && (unit.y == y)) {
            return CELL_BAD;
        }
        if (unit.travelledPercents > 0) {
            if ((unit.x == x - 1 ) && (unit.y == y) && (unit.viewDirection == VIEW_DIRECTION_RIGHT)) {
                return CELL_BAD;
            }
            if ((unit.x == x - 1 ) && (unit.y == y - 1) && (unit.viewDirection == VIEW_DIRECTION_BOTTOMRIGHT)) {
                return CELL_BAD;
            }
            if ((unit.x == x - 1 ) && (unit.y == y + 1) && (unit.viewDirection == VIEW_DIRECTION_TOPRIGHT)) {
                return CELL_BAD;
            }
            if ((unit.x == x + 1) && (unit.y == y) && (unit.viewDirection == VIEW_DIRECTION_LEFT)) {
                return CELL_BAD;
            }
            if ((unit.x == x + 1) && (unit.y == y - 1) && (unit.viewDirection == VIEW_DIRECTION_BOTTOMLEFT)) {
                return CELL_BAD;
            }
            if ((unit.x == x + 1) && (unit.y == y + 1) && (unit.viewDirection == VIEW_DIRECTION_TOPLEFT)) {
                return CELL_BAD;
            }

            if ((unit.x == x) && (unit.y == y - 1) && (unit.viewDirection == VIEW_DIRECTION_BOTTOM)) {
                return CELL_BAD;
            }

            if ((unit.x == x) && (unit.y == y + 1) && (unit.viewDirection == VIEW_DIRECTION_TOP)) {
                return CELL_BAD;
            }
        }
    }
    for (var i = 0; i < usedCellsByBuildings.length; i++) {
        var usedCell = usedCellsByBuildings[i];
        if (usedCell.x == x && usedCell.y == y) {
            return CELL_BAD;
        }
    }
    for (var i = 0; i < allocatedCellsByBuildings.length; i++) {
        var allocatedCell = allocatedCellsByBuildings[i];
        if (allocatedCell.x == x && allocatedCell.y == y) {
            return CELL_OK;
        }
    }
    return CELL_OK_FAR;
};

GameMap.prototype.getBuildings = function (x, y, x2, y2) {
    //TODO: optimization
    return this.buildings;
};

GameMap.prototype.getBuildingById = function (id) {
    for (var i = 0; i < this.buildings.length; i++) {
        var building = this.buildings[i];
        if (building.id == id) {
            return building;
        }
    }
    return null;
};

GameMap.prototype.getBullets = function (x, y, x2, y2) {
    //TODO: optimization
    return this.bullets;
};


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




