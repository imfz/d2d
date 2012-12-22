var TILE_TYPE_SAND = 1;
var TILE_TYPE_ROCK = 2;
var TILE_TYPE_SPICE = 3;
var TILE_TYPE_RICH_SPICE = 4;

var UNIT_TYPE_BATTLE_TANK = 1;
var UNIT_TYPE_SIEGE_TANK = 2;
var UNIT_TYPE_LAUNCHER = 3;
var UNIT_TYPE_DEVASTATOR = 4;

var BUILDING_TYPE_SILO = 1;
var BUILDING_TYPE_POWERPLANT = 2;
var BUILDING_TYPE_REPAIRSHOP = 3;
var BUILDING_TYPE_BARRACKS = 4;
var BUILDING_TYPE_RADAR = 5;
var BUILDING_TYPE_LIGHTFACTORY = 6;
var BUILDING_TYPE_CONSTRUCTION_YARD = 7;
var BUILDING_TYPE_FACTORY = 8;
var BUILDING_TYPE_AIRBASE = 9;

var VIEW_DIRECTION_TOPLEFT = 1;
var VIEW_DIRECTION_TOP = 2;
var VIEW_DIRECTION_TOPRIGHT = 3;
var VIEW_DIRECTION_RIGHT = 4;
var VIEW_DIRECTION_BOTTOMRIGHT = 5;
var VIEW_DIRECTION_BOTTOM = 6;
var VIEW_DIRECTION_BOTTOMLEFT = 7;
var VIEW_DIRECTION_LEFT = 8;

var TILE_HEIGHT = 63;
var TILE_WIDTH = 63;

var HP_BAR_LENGTH = 33;
var HP_BAR_HEIGHT = 5;
var HP_BAR_X_OFFSET = 15;
var HP_BAR_Y_OFFSET = 3;

var TARGET_FPS = 60;

function GameEngine() {
    this.x = 0;
    this.y = 0;
    this.frameCount = 0;
    this.startTime = 0;
    this.shownUnits = new Array();
    this.selectedUnitId = new Array();
    console.log("Created game engine");
}

GameEngine.prototype.setCanvas = function (canvas) {
    this.canvas = canvas;
    this.widthInTiles = Math.ceil(canvas.width / TILE_WIDTH);
    this.heightInTiles = Math.ceil(canvas.height / TILE_HEIGHT);
    console.log("Width in tiles : " + this.widthInTiles);
    console.log("Height in tiles : " + this.heightInTiles);
};

GameEngine.prototype.setMainSprite = function (mainSprite) {
    this.mainSprite = mainSprite;
};

GameEngine.prototype.setBuildingsSprite = function(buildingsSprite) {
    this.buildingsSprite = buildingsSprite;
}

GameEngine.prototype.setUnitSprite = function (unitSprite) {
    this.unitSprite = unitSprite;
};

GameEngine.prototype.setMap = function (map) {
    this.map = map;
};



GameEngine.prototype.bindEvents = function () {
    var that = this;
    $(window).keydown(function (e) {
        var keyCode = e.keyCode || e.which,
                arrow = {left:37, up:38, right:39, down:40 };
        switch (keyCode) {
        case arrow.left:
            if (that.x > 0) {
                that.x -= 1;
            }
            break;
        case arrow.up:
            if (that.y > 0) {
                that.y -= 1;
            }
            break;
        case arrow.right:
            if (that.x + that.widthInTiles < that.map.getWidth()) {
                that.x += 1;
            }
            break;
        case arrow.down:
            if (that.y + that.heightInTiles < that.map.getHeight()) {
                that.y += 1;
            }
            break;
        }

    });
    $(this.canvas).mouseup(function(e){
        switch (event.which) {
        case 1:
            var x = Math.floor((e.pageX-$(that.canvas).offset().left));
            var y = Math.floor((e.pageY-$(that.canvas).offset().top));
            var x2 = that.xMouseDown;
            var y2 = that.yMouseDown;
            var tmp;
            if (x > x2) {
                tmp = x;
                x = x2;
                x2 = tmp;
            }
            if (y > y2) {
                tmp = y;
                y = y2;
                y2 = tmp;
            }
            that.selectedUnitId = [];
            for (var i = 0; i < that.shownUnits.length; i++) {
                var showUnitInfo =  that.shownUnits[i];
                if ((x2 > showUnitInfo.x) && (x < showUnitInfo.x + TILE_WIDTH)) {
                    if ((y2 > showUnitInfo.y) && (y < showUnitInfo.y + TILE_HEIGHT)) {
                        that.selectedUnitId.push(showUnitInfo.id);
                    }
                }
            }
            that.xMouseDown = null;
            that.yMouseDown = null;
        }
        return false ;
    });
    $(this.canvas).mousemove(function(e){
        var x = Math.floor((e.pageX-$(that.canvas).offset().left));
        var y = Math.floor((e.pageY-$(that.canvas).offset().top));
        that.xCurrentMouse = x;
        that.yCurrentMouse = y;
    });
    $(this.canvas).mouseleave(function(e){
        var x = Math.floor((e.pageX-$(that.canvas).offset().left));
        var y = Math.floor((e.pageY-$(that.canvas).offset().top));
        that.xMouseDown = null;
        that.yMouseDown = null;
    });
    $(this.canvas).mousedown(function(e){
        switch (event.which) {
        case 1:
            var x = Math.floor((e.pageX-$(that.canvas).offset().left));
            var y = Math.floor((e.pageY-$(that.canvas).offset().top));
            that.xMouseDown = x;
            that.yMouseDown = y;
            break;
        case 3:
            var x = Math.floor((e.pageX-$(that.canvas).offset().left));
            var y = Math.floor((e.pageY-$(that.canvas).offset().top));
            var mapX = (x / TILE_WIDTH) + that.x;
            var mapY = (y / TILE_WIDTH) + that.y;
            connection.sendUnitAction(that.selectedUnitId,Math.floor(mapX),Math.floor(mapY));
            break;
        }
        return false ;
    });
    $(this.canvas).bind("contextmenu",function(e){
        return false;
    });
};

GameEngine.prototype.getTileConfig = function (targetTileX, targetTileY) {
    if (targetTileX >= this.map.getWidth()) {
        return null;
    }
    if (targetTileY >= this.map.getHeight()) {
        return null;
    }

    var tileType = this.map.getTileType(targetTileX, targetTileY);
    var tileTypeUp = this.map.getTileType(targetTileX, targetTileY - 1);
    var tileTypeDown = this.map.getTileType(targetTileX, targetTileY + 1);
    var tileTypeLeft = this.map.getTileType(targetTileX - 1, targetTileY);
    var tileTypeRight = this.map.getTileType(targetTileX + 1, targetTileY);
    var tileTypeUpLeft = this.map.getTileType(targetTileX - 1, targetTileY - 1);
    var tileTypeUpRight = this.map.getTileType(targetTileX + 1, targetTileY - 1);
    var tileTypeDownLeft = this.map.getTileType(targetTileX - 1, targetTileY + 1);
    var tileTypeDownRight = this.map.getTileType(targetTileX + 1, targetTileY + 1);

    var x;
    var y;

    if (tileType == TILE_TYPE_SAND) {
        x = 0;
        y = 0;


        if ((tileTypeUp == TILE_TYPE_ROCK) && (tileTypeLeft == TILE_TYPE_ROCK)) {
            x = 4;
            y = 3;
        }


        else if ((tileTypeDown == TILE_TYPE_ROCK) && (tileTypeRight == TILE_TYPE_ROCK)) {
            x = 3;
            y = 2;
        }
        else if ((tileTypeDown == TILE_TYPE_ROCK) && (tileTypeLeft == TILE_TYPE_ROCK)) {
            x = 4;
            y = 2;
        }
        else if ((tileTypeUp == TILE_TYPE_ROCK) && (tileTypeRight == TILE_TYPE_ROCK)) {
            x = 3;
            y = 3;
        }
        else if ((tileTypeUp == TILE_TYPE_ROCK) && (tileTypeLeft == TILE_TYPE_ROCK)) {
            x = 4;
            y = 3;
        }
        else if (tileTypeUp == TILE_TYPE_ROCK) {
            x = 5;
            y = 3;
        }

        else if (tileTypeDown == TILE_TYPE_ROCK) {
            x = 5;
            y = 2;
        }

        else if (tileTypeLeft == TILE_TYPE_ROCK) {
            x = 4;
            y = 1
        }
        else if (tileTypeRight == TILE_TYPE_ROCK) {
            x = 3;
            y = 1;
        }

        if ((tileTypeUp == TILE_TYPE_SPICE) && (tileTypeLeft == TILE_TYPE_SPICE)) {
            x = 25;
            y = 3;
        }


        else if ((tileTypeDown == TILE_TYPE_SPICE) && (tileTypeRight == TILE_TYPE_SPICE)) {
            x = 24;
            y = 2;
        }
        else if ((tileTypeDown == TILE_TYPE_SPICE) && (tileTypeLeft == TILE_TYPE_SPICE)) {
            x = 25;
            y = 2;
        }
        else if ((tileTypeUp == TILE_TYPE_SPICE) && (tileTypeRight == TILE_TYPE_SPICE)) {
            x = 24;
            y = 3;
        }
        else if ((tileTypeUp == TILE_TYPE_SPICE) && (tileTypeLeft == TILE_TYPE_SPICE)) {
            x = 25;
            y = 3;
        }
        else if (tileTypeUp == TILE_TYPE_SPICE) {
            x = 26;
            y = 3;
        }

        else if (tileTypeDown == TILE_TYPE_SPICE) {
            x = 26;
            y = 2;
        }

        else if (tileTypeLeft == TILE_TYPE_SPICE) {
            x = 25;
            y = 1
        }
        else if (tileTypeRight == TILE_TYPE_SPICE) {
            x = 24;
            y = 1;
        }

        else if ((tileTypeDown == TILE_TYPE_RICH_SPICE) && (tileTypeRight == TILE_TYPE_RICH_SPICE)) {
            x = 24;
            y = 2;
        }
        else if ((tileTypeDown == TILE_TYPE_RICH_SPICE) && (tileTypeLeft == TILE_TYPE_RICH_SPICE)) {
            x = 25;
            y = 2;
        }
        else if ((tileTypeUp == TILE_TYPE_RICH_SPICE) && (tileTypeRight == TILE_TYPE_RICH_SPICE)) {
            x = 24;
            y = 3;
        }
        else if ((tileTypeUp == TILE_TYPE_RICH_SPICE) && (tileTypeLeft == TILE_TYPE_RICH_SPICE)) {
            x = 25;
            y = 3;
        }
        else if (tileTypeUp == TILE_TYPE_RICH_SPICE) {
            x = 26;
            y = 3;
        }

        else if (tileTypeDown == TILE_TYPE_RICH_SPICE) {
            x = 26;
            y = 2;
        }

        else if (tileTypeLeft == TILE_TYPE_RICH_SPICE) {
            x = 25;
            y = 1
        }
        else if (tileTypeRight == TILE_TYPE_RICH_SPICE) {
            x = 24;
            y = 1;
        }
    }

    if (tileType == TILE_TYPE_SPICE) {
        x = 30;
        y = 0;
        if ((tileTypeUp == TILE_TYPE_RICH_SPICE) && (tileTypeLeft == TILE_TYPE_RICH_SPICE)) {
            x = 25;
            y = 3;
        }


        else if ((tileTypeDown == TILE_TYPE_RICH_SPICE) && (tileTypeRight == TILE_TYPE_RICH_SPICE)) {
            x = 24;
            y = 2;
        }
        else if ((tileTypeDown == TILE_TYPE_RICH_SPICE) && (tileTypeLeft == TILE_TYPE_RICH_SPICE)) {
            x = 25;
            y = 2;
        }
        else if ((tileTypeUp == TILE_TYPE_RICH_SPICE) && (tileTypeRight == TILE_TYPE_SPICE)) {
            x = 24;
            y = 3;
        }
        else if ((tileTypeUp == TILE_TYPE_RICH_SPICE) && (tileTypeLeft == TILE_TYPE_RICH_SPICE)) {
            x = 25;
            y = 3;
        }
        else if (tileTypeUp == TILE_TYPE_RICH_SPICE) {
            x = 26;
            y = 3;
        }

        else if (tileTypeDown == TILE_TYPE_RICH_SPICE) {
            x = 26;
            y = 2;
        }

        else if (tileTypeLeft == TILE_TYPE_RICH_SPICE) {
            x = 25;
            y = 1
        }
        else if (tileTypeRight == TILE_TYPE_RICH_SPICE) {
            x = 24;
            y = 1;
        }
    }

    if (tileType == TILE_TYPE_RICH_SPICE) {
        x = 40;
        y = 0;
    }

    if (tileType == TILE_TYPE_ROCK) {
        x = 7;
        y = 2;
    }


    x *= TILE_WIDTH;
    y *= TILE_HEIGHT;
    var result = new Object();
    result.x = x;
    result.y = y;
    return result;
};

GameEngine.prototype.shiftMovingUnit = function(x,y,travelled,viewDirection) {
    var newX = x;
    var newY = y;

    if (viewDirection == VIEW_DIRECTION_TOP) {
        newY -= TILE_HEIGHT * travelled;
    }
    if (viewDirection == VIEW_DIRECTION_TOPLEFT) {
        newY -= TILE_HEIGHT * travelled;
        newX -= TILE_WIDTH * travelled;
    }
    if (viewDirection == VIEW_DIRECTION_TOPRIGHT) {
        newY -= TILE_HEIGHT * travelled;
        newX += TILE_WIDTH * travelled;
    }
    if (viewDirection == VIEW_DIRECTION_LEFT) {
        newX -= TILE_WIDTH * travelled;
    }
    if (viewDirection == VIEW_DIRECTION_RIGHT) {
        newX += TILE_WIDTH * travelled;
    }
    if (viewDirection == VIEW_DIRECTION_BOTTOMLEFT) {
        newY += TILE_HEIGHT * travelled;
        newX -= TILE_WIDTH * travelled;
    }
    if (viewDirection == VIEW_DIRECTION_BOTTOMRIGHT) {
        newY += TILE_HEIGHT * travelled;
        newX += TILE_WIDTH * travelled;
    }
    if (viewDirection == VIEW_DIRECTION_BOTTOM) {
        newY += TILE_HEIGHT * travelled;
    }

    var result = new Object();
    result.x = newX;
    result.y = newY;
    return result;
};

GameEngine.prototype.getBuildingConfig = function (building) {
    var x = 0;
    var width = 2;
    var height = 2;
    if (building.type == BUILDING_TYPE_SILO) {
        x = 1;
    }
    if (building.type == BUILDING_TYPE_POWERPLANT) {
        x = 2;
    }
    if (building.type == BUILDING_TYPE_REPAIRSHOP) {
        x = 3;
        width = 3;
    }
    if (building.type == BUILDING_TYPE_BARRACKS) {
        x = 4;
    }
    if (building.type == BUILDING_TYPE_RADAR) {
        x = 5;
    }
    if (building.type == BUILDING_TYPE_LIGHTFACTORY) {
        x = 6;
    }
    if (building.type == BUILDING_TYPE_CONSTRUCTION_YARD) {
        x = 7;
    }
    if (building.type == BUILDING_TYPE_FACTORY) {
        x = 8;
        width = 3;
    }
    if (building.type == BUILDING_TYPE_AIRBASE) {
        x = 9;
    }

    var result = new Object();
    result = new Object();
    result.x = x * 63;
    result.y = 0;
    result.width = width * 63;
    result.height = height * 63;
    return result;

};

GameEngine.prototype.getUnitConfig = function (unit) {
    var x = 0;
    if (unit.viewDirection == VIEW_DIRECTION_TOP) {
        x = 0;
    }
    if (unit.viewDirection == VIEW_DIRECTION_TOPLEFT) {
        x = 1;
    }
    if (unit.viewDirection == VIEW_DIRECTION_TOPRIGHT) {
        x = 7;
    }
    if (unit.viewDirection == VIEW_DIRECTION_LEFT) {
        x = 2;
    }
    if (unit.viewDirection == VIEW_DIRECTION_RIGHT) {
        x = 6;
    }
    if (unit.viewDirection == VIEW_DIRECTION_BOTTOMLEFT) {
        x = 3;
    }
    if (unit.viewDirection == VIEW_DIRECTION_BOTTOMRIGHT) {
        x = 5;
    }
    if (unit.viewDirection == VIEW_DIRECTION_BOTTOM) {
        x = 4;
    }

    var y = 0;
    if (unit.unitType == UNIT_TYPE_BATTLE_TANK) {
        y = 16;
    }
    if (unit.unitType == UNIT_TYPE_SIEGE_TANK) {
        y = 1;
    }
    if (unit.unitType == UNIT_TYPE_DEVASTATOR) {
        y = 10;
    }
    if (unit.unitType == UNIT_TYPE_LAUNCHER) {
        y = 21;
    }

    result = new Object();
    result.x = x * 63;
    result.y = y * 63;
    return result;
};

GameEngine.prototype.render = function () {
    if (this.startTime == 0) {
        this.startTime = new Date().getTime();
    }
    var currentTime = new Date().getTime();
    this.frameCount++;
    var fps = this.frameCount / (currentTime - this.startTime) * 1000;
    //console.log("Rendering frame " + this.frameCount + " with fps " + fps + " x : " + this.x + " y : " + this.y );
    var context = canvas.getContext("2d");
    context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    for (var x = this.x; x < this.x + this.widthInTiles; x++) {
        for (var y = this.y; y < this.y + this.heightInTiles; y++) {
            //console.log("Rendering tile x: " + x + " y: " + y);
            var tileConfig = this.getTileConfig(x, y);
            if (tileConfig) {
                context.drawImage(this.mainSprite, tileConfig.x, tileConfig.y, TILE_WIDTH, TILE_HEIGHT, (x - this.x) * TILE_WIDTH, (y - this.y) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
            }

        }
    }

    var buildings = this.map.getBuildings(this.x-1, this.y-1, this.x + this.widthInTiles+1, this.y + this.heightInTiles+1);

    for (var i = 0; i < buildings.length; i++) {
        var building = buildings[i];
        var buildingConfig = this.getBuildingConfig(building);
        var xToDrawTo = (building.x - this.x) * TILE_WIDTH;
        var yToDrawTo = (building.y - this.y) * TILE_HEIGHT;
        context.drawImage(this.buildingsSprite, buildingConfig.x, buildingConfig.y, buildingConfig.width, buildingConfig.height, xToDrawTo, yToDrawTo, buildingConfig.width, buildingConfig.height);
    }

    // +1 to handle units moving from/into screen
    this.shownUnits = new Array();
    var units = this.map.getUnits(this.x-1, this.y-1, this.x + this.widthInTiles+1, this.y + this.heightInTiles+1);
    for (var i = 0; i < units.length; i++) {
        var unit = units[i];
        var unitConfig = this.getUnitConfig(unit);
        if (unitConfig) {
            var xToDrawTo = (unit.x - this.x) * TILE_WIDTH;
            var yToDrawTo = (unit.y - this.y) * TILE_HEIGHT;
            var movingCoord = this.shiftMovingUnit(xToDrawTo,yToDrawTo, unit.travelled, unit.viewDirection);
            context.drawImage(this.unitSprite, unitConfig.x, unitConfig.y, TILE_WIDTH, TILE_HEIGHT, movingCoord.x, movingCoord.y, TILE_WIDTH, TILE_HEIGHT);

            if ($.inArray(unit.id, this.selectedUnitId) >= 0) {
                // draw green border near selected unit
                context.beginPath();
                context.strokeStyle = '#00cc00';
                context.lineWidth = 3;
                context.moveTo(movingCoord.x, movingCoord.y);
                context.lineTo(movingCoord.x, movingCoord.y + TILE_HEIGHT);
                context.lineTo(movingCoord.x + TILE_WIDTH, movingCoord.y + TILE_HEIGHT);
                context.lineTo(movingCoord.x + TILE_WIDTH, movingCoord.y);
                context.closePath();
                context.stroke();
            }

            // draw hp bar near selected unit
            context.beginPath();
            context.fillStyle = '#00cc00';
            var hpBarLength = unit.hp / unit.maxHp * HP_BAR_LENGTH;
            context.moveTo(movingCoord.x + HP_BAR_X_OFFSET, movingCoord.y + HP_BAR_Y_OFFSET);
            context.lineTo(movingCoord.x + HP_BAR_X_OFFSET, movingCoord.y + HP_BAR_Y_OFFSET + HP_BAR_HEIGHT);
            context.lineTo(movingCoord.x + HP_BAR_X_OFFSET + hpBarLength, movingCoord.y + HP_BAR_Y_OFFSET + HP_BAR_HEIGHT);
            context.lineTo(movingCoord.x + HP_BAR_X_OFFSET + hpBarLength, movingCoord.y + HP_BAR_Y_OFFSET);
            context.closePath();
            context.fill();

            var shownUnitInfo = new Object();
            shownUnitInfo.id = unit.id;
            shownUnitInfo.x = movingCoord.x;
            shownUnitInfo.y = movingCoord.y;
            this.shownUnits.push(shownUnitInfo);
        }
    }

    // green selection rectangle
    if (this.xMouseDown) {
        if (this.yMouseDown) {
            context.beginPath();
            context.strokeStyle = '#00cc00';
            context.lineWidth = 2;
            context.moveTo(this.xCurrentMouse,this.yCurrentMouse);
            context.lineTo(this.xMouseDown, this.yCurrentMouse);
            context.lineTo(this.xMouseDown, this.yMouseDown);
            context.lineTo(this .xCurrentMouse, this.yMouseDown);
            context.closePath();
            context.stroke();
        }
    }

    var that = this;
    setTimeout(function () {
        that.render()
    }, 1000 / TARGET_FPS);
}




