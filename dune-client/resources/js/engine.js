var TILE_HEIGHT = 32;
var TILE_WIDTH = 32;
var OK_BUTTON_ENABLED_TICKS = 30;
var OK_BUTTON_DISABLED_TICKS = 30;

var HP_BAR_LENGTH = 16;
var HP_BAR_HEIGHT = 2;
var HP_BAR_X_OFFSET = 7;
var HP_BAR_Y_OFFSET = 2;

var SPICE_BAR_LENGTH = 16;
var SPICE_BAR_HEIGHT = 2;
var SPICE_BAR_X_OFFSET = 7;
var SPICE_BAR_Y_OFFSET = 7;

var BUILDING_HP_BAR_HEIGHT = 2;
var BUILDING_HP_BAR_X_OFFSET = 7;
var BUILDING_HP_BAR_Y_OFFSET = 2;


var ZOOM_IN_FACTOR = 0.5;
var ZOOM_OUT_FACTOR = 2;
var MAX_ZOOM = 4;
var MIN_ZOOM = 0.2;


function GameEngine() {
    this.x = 0;
    this.y = 0;
    this.scale = 1.0;
    this.frameCount = 0;
    this.startTime = 0;
    this.shownUnits = [];
    this.shownBuildings = [];
    this.selectedUnitId = [];
    this.groups = [];
    this.hotkeysEnabled = true;
    console.log("Created game engine");
}

GameEngine.prototype.setCanvas = function (canvas) {
    this.canvas = canvas;
    this.setFieldSizeInTiles();
    console.log("Width in tiles : " + this.widthInTiles);
    console.log("Height in tiles : " + this.heightInTiles);
};


GameEngine.prototype.setFieldSizeInTiles = function () {
    this.widthInTiles = Math.ceil(this.canvas.width / (TILE_WIDTH * (1 / engine.scale)));
    this.heightInTiles = Math.ceil(this.canvas.height / (TILE_HEIGHT * (1 / engine.scale)));

};

GameEngine.prototype.setMap = function (map) {
    this.map = map;
};


GameEngine.prototype.bindEvents = function () {
    var that = this;
    var onKeyDown = function (e) {
        if (!that.hotkeysEnabled) {
            return true;
        }
        var keyCode = e.keyCode || e.which,
                arrow = {left: 37, up: 38, right: 39, down: 40 };
        switch (keyCode) {
        case arrow.left:
            if (e.ctrlKey) {
                that.setCoordinates(that.x - that.widthInTiles, that.y);
            } else {
                that.setCoordinates(that.x - 1, that.y);
            }
            return false;
        case arrow.up:
            if (e.ctrlKey) {
                that.setCoordinates(that.x, that.y - that.heightInTiles);
            } else {
                that.setCoordinates(that.x, that.y - 1);
            }
            return false;
        case arrow.right:
            if (e.ctrlKey) {
                that.setCoordinates(that.x + that.widthInTiles, that.y);
            } else {
                that.setCoordinates(that.x + 1, that.y);
            }
            return false;
        case arrow.down:
            if (e.ctrlKey) {
                that.setCoordinates(that.x, that.y + that.heightInTiles);
            } else {
                that.setCoordinates(that.x, that.y + 1);
            }
            return false;
        case 27:
            // handle ESC button
            if (that.placementEnabled) {
                that.placementEnabled = false;
                return;
            }
            that.selectedUnitId = [];
            return false;
        case 72:
            // handle H button
            that.centerOnMain();
            return false;
        case 83:
            // handle S button
            connection.sendUnitStop(that.selectedUnitId);
            return false;
        case 80:
            // handle P button
            that.enablePlacement();
            return false;
        case 109:
        case 173:
            that.setScale(ZOOM_OUT_FACTOR);
            return false;
        case 107:
            that.setScale(ZOOM_IN_FACTOR);
            return false;
        case 61:
            if (e.shiftKey) {
                that.setScale(ZOOM_IN_FACTOR);
            }
            return false;
        case 48:
        case 49:
        case 50:
        case 51:
        case 52:
        case 53:
        case 54:
        case 55:
        case 56:
        case 57:

            if (e.ctrlKey) {
                that.saveCurrentSelection(keyCode);
            } else {
                that.restoreCurrentSelection(keyCode);
            }
            return false;
            break;
        }


    };
    $(window).keydown(onKeyDown);

    $(this.canvas).dblclick(function (event) {
        var x = Math.floor((event.pageX - $(that.canvas).offset().left) * engine.scale);
        var y = Math.floor((event.pageY - $(that.canvas).offset().top) * engine.scale);
        selectUnitsByType(x, y, event.shiftKey);
    });

    $(this.canvas).mousewheel(function (event, delta, deltaX, deltaY) {
        if (delta > 0) {
            that.setScale(ZOOM_OUT_FACTOR);
        } else {
            that.setScale(ZOOM_IN_FACTOR);
        }
    });

    $(this.canvas).mouseup(function (event) {
        switch (event.which) {
        case 1:
            var x = Math.floor((event.pageX - $(that.canvas).offset().left) * engine.scale);
            var y = Math.floor((event.pageY - $(that.canvas).offset().top) * engine.scale);
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
            if (that.placementEnabled) {
                var mapX = Math.floor((x / TILE_WIDTH) + that.x);
                var mapY = Math.floor((y / TILE_HEIGHT) + that.y);
                connection.sendBuildingPlacement(mapX, mapY, that.builderId);
            } else {
                selectUnits(x2, x, y2, y, event.shiftKey);
                var buildingSelected = false;
                if (that.selectedUnitId.length == 0) {
                    // if no units selected, search for buildings
                    buildingSelected = selectBuildings(x2, x, y2, y);
                }

                // try to unselect building
                if (!buildingSelected && that.selectedBuilding != -1) {
                    that.selectedBuilding = -1;
                    connection.sendBuildingSelection(that.selectedBuilding);
                }
            }
            that.xMouseDown = null;
            that.yMouseDown = null;
        }
        return false;
    });

    $(this.canvas).mousemove(function (event) {
        var x = Math.floor((event.pageX - $(that.canvas).offset().left) * engine.scale);
        var y = Math.floor((event.pageY - $(that.canvas).offset().top) * engine.scale);
        that.xCurrentMouse = x;
        that.yCurrentMouse = y;
    });

    $(this.canvas).mouseleave(function (event) {
        var x = Math.floor((event.pageX - $(that.canvas).offset().left) * engine.scale);
        var y = Math.floor((event.pageY - $(that.canvas).offset().top) * engine.scale);
        that.xMouseDown = null;
        that.yMouseDown = null;
    });

    $(this.canvas).mousedown(function (event) {
        var x = Math.floor((event.pageX - $(that.canvas).offset().left) * engine.scale);
        var y = Math.floor((event.pageY - $(that.canvas).offset().top) * engine.scale);
        switch (event.which) {
        case 1:
            // remember mousedown for rectangle selection
            that.xMouseDown = x;
            that.yMouseDown = y;
            break;
        case 3:
            if (that.placementEnabled) {
                that.placementEnabled = false;
                return false;
            }
            var mapX = Math.floor((x / TILE_WIDTH) + that.x);
            var mapY = Math.floor((y / TILE_HEIGHT) + that.y);
            connection.sendUnitAction(that.selectedUnitId, mapX, mapY);
            break;
        }
        return false;
    });

    $(this.canvas).bind("contextmenu", function (e) {
        return false;
    });

    function selectUnits(x2, x, y2, y, shiftKey) {
        var newSelectedUnits = [];
        for (var i = 0; i < that.shownUnits.length; i++) {
            var showUnitInfo = that.shownUnits[i];
            if ((x2 > showUnitInfo.x) && (x < showUnitInfo.x + TILE_WIDTH)) {
                if ((y2 > showUnitInfo.y) && (y < showUnitInfo.y + TILE_HEIGHT)) {
                    newSelectedUnits.push(showUnitInfo.id);
                }
            }
        }

        if (!shiftKey) {
            if (newSelectedUnits.length > 0) {
                that.selectedUnitId = [];
            }
        }

        if (shiftKey) {
            addUnitToSelection(newSelectedUnits);
        } else {
            setSelectedUnit(newSelectedUnits);
        }
    }

    function selectUnitsByType(x, y, shiftKey) {
        var newSelectedUnits = [];
        for (var i = 0; i < that.shownUnits.length; i++) {
            var showUnitInfo = that.shownUnits[i];
            if ((x >= showUnitInfo.x) && (x < showUnitInfo.x + TILE_WIDTH)) {
                if ((y >= showUnitInfo.y) && (y < showUnitInfo.y + TILE_HEIGHT)) {
                    newSelectedUnits.push(showUnitInfo);
                }
            }
        }


        // select by type only if we double clicked on one unit and no rectangle select was in progress.
        if (newSelectedUnits.length == 1) {
            var newSelectedUnitsOfType = [];
            for (i = 0; i < that.shownUnits.length; i++) {
                showUnitInfo = that.shownUnits[i];
                if (showUnitInfo.unitType == newSelectedUnits[0].unitType) {
                    newSelectedUnitsOfType.push(showUnitInfo.id);
                }
            }
            if (shiftKey) {
                addUnitToSelection(newSelectedUnitsOfType);
            } else {
                setSelectedUnit(newSelectedUnitsOfType);
            }
        }

    }

    function addUnitToSelection(unitId) {
        for (var i = 0; i < unitId.length; i++) {
            var found = false;
            var unit = unitId[i];
            for (var j = 0; j < that.selectedUnitId.length; j++) {
                if (that.selectedUnitId == unit) {
                    found = true;
                }
            }
            if (!found) {
                that.selectedUnitId.push(unit);
            }
        }
    }

    function setSelectedUnit(unitId) {
        that.selectedUnitId = [];
        for (var i = 0; i < unitId.length; i++) {
            that.selectedUnitId.push(unitId[i]);
        }
    }

    function selectBuildings(x2, x, y2, y) {
        var buildingFound = false;
        for (var i = 0; i < that.shownBuildings.length; i++) {
            var shownBuildingInfo = that.shownBuildings[i];
            if ((x2 > shownBuildingInfo.x) && (x < shownBuildingInfo.width + shownBuildingInfo.x)) {
                if ((y2 > shownBuildingInfo.y) && (y < shownBuildingInfo.height + shownBuildingInfo.y)) {
                    // ok text is blinking, go on with placement
                    that.selectedBuilding = shownBuildingInfo.id;
                    if (shownBuildingInfo.placementEnabled) {
                        that.enablePlacement();
                    }
                    buildingFound = true;
                    connection.sendBuildingSelection(that.selectedBuilding);
                }
            }
        }
        return buildingFound;
    }
};

GameEngine.prototype.enablePlacement = function() {
    var building = this.map.getBuildingById(this.selectedBuilding);
    if (!building) {
        return;
    }
	if (building.type != BUILDING_TYPE_CONSTRUCTION_YARD) {
		return;
	}
    var buildingConfig = this.getBuildingPlacementConfig(building.entityBuiltId);
    if (!buildingConfig) {
        return;
    }
    this.placementEnabled = true;
    this.builderId = building.id;
    this.placementWidth = buildingConfig.width;
    this.placementHeight = buildingConfig.height;
};

GameEngine.prototype.saveCurrentSelection = function (keyCode) {
    this.groups[keyCode] = [this.selectedUnitId, this.selectedBuilding];
};

GameEngine.prototype.restoreCurrentSelection = function (keyCode) {
    var selectedBuildingWas = this.selectedBuilding;
    var group = this.groups[keyCode];

    if (group) {
        var currentTime = new Date().getTime();
        if (this.lastTimePressed) {
            if (this.lastGroupPressed == keyCode) {
                if (currentTime - this.lastTimePressed < 500) {
                    this.centerOnGroup(group);
                }
            }
        }
        this.lastGroupPressed = keyCode;
        this.lastTimePressed = currentTime;
        this.selectedUnitId = group[0];
        this.selectedBuilding = group[1];
        if (selectedBuildingWas != this.selectedBuilding) {
            connection.sendBuildingSelection(this.selectedBuilding);
        }
    }
};

GameEngine.prototype.centerOnGroup = function (group) {
    if (group) {
        if (group.length == 2) {
            if (group[1] > 0) {
                this.centerOnBuilding(group[1]);
            } else {
                this.centerOnUnits(group[0]);
            }
        }
    }
};

GameEngine.prototype.centerOnBuilding = function (buildingId) {
    for (var i = 0; i < this.map.buildings.length; i++) {
        var building = this.map.buildings[i];
        if (building.id == buildingId) {
            this.centerOnCoordinates(building.x, building.y);
            return;
        }
    }
};

GameEngine.prototype.centerOnUnits = function (unitIds) {
    var unitCount = 0;
    var totalX = 0;
    var totalY = 0;
    for (var i = 0; i < this.map.units.length; i++) {
        var unit = this.map.units[i];
        for (var j = 0; j < unitIds.length; j++) {
            if (unitIds[j] == unit.id) {
                totalX += unit.x;
                totalY += unit.y;
                unitCount++;
            }
        }
    }
    if (unitCount > 0) {
        this.centerOnCoordinates(Math.round(totalX / unitCount), Math.round(totalY / unitCount));
    }
};

GameEngine.prototype.centerOnMain = function () {
    for (var i = 0; i < this.map.buildings.length; i++) {
        var building = this.map.buildings[i];
        if (building.type == BUILDING_TYPE_CONSTRUCTION_YARD) {
            if (building.ownerId == connection._playerId) {
                this.centerOnCoordinates(building.x, building.y);
                return;
            }
        }
    }
};

GameEngine.prototype.setCoordinates = function (x, y) {
    var mapX = x;
    var mapY = y;
    if (mapX < 0) {
        mapX = 0;
    }
    if (mapY < 0) {
        mapY = 0;
    }
    if (mapX >= this.map.width) {
        mapX = this.map.width - 1;
    }
    if (mapY >= this.map.height) {
        mapY = this.map.height - 1;
    }
    if (mapX >= this.map.width - this.widthInTiles) {
        mapX = this.map.width - this.widthInTiles;
    }
    if (mapY >= this.map.height - this.heightInTiles) {
        mapY = this.map.height - this.heightInTiles;
    }
    this.x = mapX;
    this.y = mapY;
};

GameEngine.prototype.centerOnCoordinates = function (x, y) {
    // because x,y represent top-right corner, and the desired coordinates represent the middle of the screen
    var mapX = x;
    var mapY = y;
    mapX = Math.round(mapX - this.widthInTiles + (this.widthInTiles / 2));
    mapY = Math.round(mapY - this.heightInTiles + (this.heightInTiles / 2));
    this.setCoordinates(mapX, mapY);
};

GameEngine.prototype.shiftMovingUnit = function (x, y, travelledPercents, viewDirection) {
    var newX = x;
    var newY = y;

    if (viewDirection == VIEW_DIRECTION_TOP) {
        newY -= TILE_HEIGHT * travelledPercents / 100;
    }
    if (viewDirection == VIEW_DIRECTION_TOPLEFT) {
        newY -= TILE_HEIGHT * travelledPercents / 100;
        newX -= TILE_WIDTH * travelledPercents / 100;
    }
    if (viewDirection == VIEW_DIRECTION_TOPRIGHT) {
        newY -= TILE_HEIGHT * travelledPercents / 100;
        newX += TILE_WIDTH * travelledPercents / 100;
    }
    if (viewDirection == VIEW_DIRECTION_LEFT) {
        newX -= TILE_WIDTH * travelledPercents / 100;
    }
    if (viewDirection == VIEW_DIRECTION_RIGHT) {
        newX += TILE_WIDTH * travelledPercents / 100;
    }
    if (viewDirection == VIEW_DIRECTION_BOTTOMLEFT) {
        newY += TILE_HEIGHT * travelledPercents / 100;
        newX -= TILE_WIDTH * travelledPercents / 100;
    }
    if (viewDirection == VIEW_DIRECTION_BOTTOMRIGHT) {
        newY += TILE_HEIGHT * travelledPercents / 100;
        newX += TILE_WIDTH * travelledPercents / 100;
    }
    if (viewDirection == VIEW_DIRECTION_BOTTOM) {
        newY += TILE_HEIGHT * travelledPercents / 100;
    }

    var result = new Object();
    result.x = newX;
    result.y = newY;
    return result;
};

GameEngine.prototype.getBuildingPlacementConfig = function (buildingTypeBuilt) {
    var width = 2;
    var height = 2;
    if (buildingTypeBuilt == BUILDING_TYPE_FACTORY) {
        width = 3;
    }
    if (buildingTypeBuilt == BUILDING_TYPE_REPAIRSHOP) {
        width = 3;
    }
    if (buildingTypeBuilt == BUILDING_TYPE_TURRET) {
        width = 1;
        height = 1;
    }
    if (buildingTypeBuilt == BUILDING_TYPE_ROCKET_TURRET) {
        width = 1;
        height = 1;
    }
    if (buildingTypeBuilt == BUILDING_TYPE_REFINERY) {
        width = 3;
        height = 2;
    }
    var result = new Object();
    result.width = width;
    result.height = height;
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
    var context = this.canvas.getContext("2d");
    context.clearRect(0, 0, this.canvas.width, this.canvas.height);

    var buildings = this.map.getBuildings(this.x - 1, this.y - 1, this.x + this.widthInTiles + 1, this.y + this.heightInTiles + 1);
    var bullets = this.map.getBullets(this.x - 1, this.y - 1, this.x + this.widthInTiles + 1, this.y + this.heightInTiles + 1);
    var units = this.map.getUnits(this.x - 1, this.y - 1, this.x + this.widthInTiles, this.y + this.heightInTiles);

    this.renderTiles(units, buildings);
    this.renderBuildings(buildings);
    this.renderBuildingPlacement(units, buildings);
    this.renderUnits(units);
    this.renderBullets(bullets);
    this.renderRectangle();

    var that = this;

    requestAnimFrame(function () {
        that.render()
    });

};

requestAnimFrame = (function () {
    return window.requestAnimationFrame ||
        window.webkitRequestAnimationFrame ||
        window.mozRequestAnimationFrame ||
        window.oRequestAnimationFrame ||
        window.msRequestAnimationFrame ||
        function (/* function FrameRequestCallback */ callback, /* DOMElement Element */ element) {
            window.setTimeout(callback, 1000 / 60);
        };
})();

GameEngine.prototype.renderTiles = function (units, buildings) {
    var context = this.canvas.getContext("2d");

    for (var x = this.x; x < this.x + this.widthInTiles; x++) {
        for (var y = this.y; y < this.y + this.heightInTiles; y++) {
            var tileConfig = sprites.getTileConfig(x, y, this.map);
            if (tileConfig) {
                context.drawImage(tileConfig.sprite, tileConfig.x, tileConfig.y, TILE_WIDTH, TILE_HEIGHT, (x - this.x) * TILE_WIDTH, (y - this.y) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
            }

        }
    }

};


GameEngine.prototype.renderBuildingPlacement = function (units, buildings) {
    if (!this.placementEnabled) {
        return;
    }
    var context = this.canvas.getContext("2d");

    function drawPlacementTile(x, y, color) {
        context.drawImage(color, 0, 0, TILE_WIDTH, TILE_HEIGHT, x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
    }

    var currentMouseMapX = Math.floor(this.xCurrentMouse / TILE_WIDTH + this.x);
    var currentMouseMapY = Math.floor(this.yCurrentMouse / TILE_HEIGHT + this.y);

    var tileColors = [];
    var foundBadTile = false;
    var foundGoodTile = false;
    var foundGoodFarTile = false;
    for (var x = this.x; x < this.x + this.widthInTiles; x++) {
        for (var y = this.y; y < this.y + this.heightInTiles; y++) {
            if (x >= currentMouseMapX && x < currentMouseMapX + this.placementWidth) {
                if (y >= currentMouseMapY && y < currentMouseMapY + this.placementHeight) {
                    var tileStateForBuilding = this.map.isTileOkForBuilding(x, y, units, buildings);
                    switch (tileStateForBuilding) {
                    case CELL_OK:
                        foundGoodTile = true;
                        tileColors.push({x:x - this.x, y:y - this.y, type:CELL_OK});
                        break;
                    case CELL_OK_FAR:
                        foundGoodFarTile = true;
                        tileColors.push({x:x - this.x, y:y - this.y, type:CELL_OK_FAR});
                        break;
                    case CELL_BAD:
                        foundBadTile = true;
                        tileColors.push({x:x - this.x, y:y - this.y, type:CELL_BAD});
                        break;
                    }
                }
            }

        }
    }

    for (var i = 0; i < tileColors.length; i++) {
        var tile = tileColors[i];
        if (tile.type == CELL_OK) {
            drawPlacementTile(tile.x, tile.y, sprites.bgGreenSprite);
        }
        if (tile.type == CELL_OK_FAR) {
            if (foundGoodTile) {
                // one of building tiles is near another building, so we can build
                drawPlacementTile(tile.x, tile.y, sprites.bgGreenSprite);
            } else {
                drawPlacementTile(tile.x, tile.y, sprites.bgYellowSprite);
            }
        }
        if (tile.type == CELL_BAD) {
            drawPlacementTile(tile.x, tile.y, sprites.bgRedSprite);
        }
    }
};

GameEngine.prototype.renderBuildings = function (buildings) {
    var okButtonEnabled = false;
    if (this.frameCount % (OK_BUTTON_ENABLED_TICKS + OK_BUTTON_DISABLED_TICKS) > OK_BUTTON_DISABLED_TICKS) {
        okButtonEnabled = true;
    }
    var context = this.canvas.getContext("2d");

    this.shownBuildings = new Array();
    for (var i = 0; i < buildings.length; i++) {
        var building = buildings[i];
        var buildingConfig = sprites.getBuildingConfig(building);
        var xToDrawTo = (building.x - this.x) * TILE_WIDTH;
        var yToDrawTo = (building.y - this.y) * TILE_HEIGHT;
        context.drawImage(buildingConfig.sprite, buildingConfig.x, buildingConfig.y, buildingConfig.width, buildingConfig.height, xToDrawTo, yToDrawTo, buildingConfig.width, buildingConfig.height);
        // construction complete image
        if (building.constructionComplete && okButtonEnabled) {
            context.drawImage(sprites.okButtonSprite,
                0,
                0,
                sprites.okButtonSprite.width, sprites.okButtonSprite.height,
                xToDrawTo + TILE_WIDTH - sprites.okButtonSprite.width / 2,
                yToDrawTo + TILE_HEIGHT - sprites.okButtonSprite.height / 2,
                sprites.okButtonSprite.width, sprites.okButtonSprite.height
            );
        }
        if (building.ownerId == connection._playerId) {
            var shownBuildingInfo = new Object();
            shownBuildingInfo.id = building.id;
            shownBuildingInfo.x = xToDrawTo;
            shownBuildingInfo.y = yToDrawTo;
            shownBuildingInfo.width = buildingConfig.width;
            shownBuildingInfo.height = buildingConfig.height;
            shownBuildingInfo.placementEnabled = (building.constructionComplete) && (building.type == BUILDING_TYPE_CONSTRUCTION_YARD);
            this.shownBuildings.push(shownBuildingInfo);
        }

    }

    for (i = 0; i < buildings.length; i++) {
        building = buildings[i];
        xToDrawTo = (building.x - this.x) * TILE_WIDTH;
        yToDrawTo = (building.y - this.y) * TILE_HEIGHT;

        if (building.id == this.selectedBuilding) {
            context.beginPath();
            context.strokeStyle = '#00cc00';
            context.lineWidth = 3;
            context.moveTo(xToDrawTo, yToDrawTo);
            context.lineTo(xToDrawTo, yToDrawTo + TILE_HEIGHT * building.height);
            context.lineTo(xToDrawTo + TILE_WIDTH * building.width, yToDrawTo + TILE_HEIGHT * building.height);
            context.lineTo(xToDrawTo + TILE_WIDTH * building.width, yToDrawTo);
            context.closePath();
            context.stroke();

            var maxBarLength = Math.floor(TILE_WIDTH * building.width * 0.8);
            this.drawBar(context, xToDrawTo, yToDrawTo, BUILDING_HP_BAR_X_OFFSET, BUILDING_HP_BAR_Y_OFFSET, BUILDING_HP_BAR_HEIGHT, maxBarLength, '#00cc00', building.hp / building.maxHp);
        }
    }
    return building;
};

GameEngine.prototype.renderUnits = function (units) {
    var context = this.canvas.getContext("2d");
    this.shownUnits = new Array();
    // +1 to handle units moving from/into screen
    for (var i = 0; i < units.length; i++) {
        var unit = units[i];
        var unitConfig = sprites.getUnitConfig(unit);
        if (unitConfig) {
            var xToDrawTo = (unit.x - this.x) * TILE_WIDTH;
            var yToDrawTo = (unit.y - this.y) * TILE_HEIGHT;
            var movingCoord = this.shiftMovingUnit(xToDrawTo, yToDrawTo, unit.travelledPercents, unit.viewDirection);
            context.drawImage(unitConfig.sprite, unitConfig.x, unitConfig.y, unitConfig.width, unitConfig.height,
                movingCoord.x + unitConfig.xOffset, movingCoord.y + unitConfig.yOffset, unitConfig.width, unitConfig.height);

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
            this.drawBar(context, movingCoord.x, movingCoord.y, HP_BAR_X_OFFSET, HP_BAR_Y_OFFSET, HP_BAR_HEIGHT, HP_BAR_LENGTH, '#00cc00', unit.hp / unit.maxHp);

            if (unit.unitType == UNIT_TYPE_HARVESTER) {
                // draw spice bar near selected unit
                this.drawBar(context, movingCoord.x, movingCoord.y, SPICE_BAR_X_OFFSET, SPICE_BAR_Y_OFFSET, SPICE_BAR_HEIGHT, SPICE_BAR_LENGTH, '#e8A65d', unit.spicePercents / 100);
            }

            if (unit.ownerId == connection._playerId) {
                var shownUnitInfo = new Object();
                shownUnitInfo.id = unit.id;
                shownUnitInfo.x = movingCoord.x;
                shownUnitInfo.y = movingCoord.y;
                shownUnitInfo.unitType = unit.unitType;
                this.shownUnits.push(shownUnitInfo);
            }
        }
    }
};

GameEngine.prototype.renderBullets = function (bullets) {
    var context = this.canvas.getContext("2d");
    for (var i = 0; i < bullets.length; i++) {
        var bullet = bullets[i];
        var bulletConfig = sprites.getBulletConfig(bullet);
        if (bulletConfig) {

            var xToDrawTo = bullet.x + ((bullet.goalX - bullet.x) / 100 * bullet.progress);
            xToDrawTo -= this.x;
            xToDrawTo = xToDrawTo * TILE_WIDTH;
            var yToDrawTo = bullet.y + ((bullet.goalY - bullet.y) / 100 * bullet.progress);
            yToDrawTo -= this.y;
            yToDrawTo = yToDrawTo * TILE_HEIGHT;

            xToDrawTo += (TILE_WIDTH - bulletConfig.width) / 2;
            yToDrawTo += (TILE_HEIGHT - bulletConfig.height) / 2;

            context.drawImage(bulletConfig.sprite, bulletConfig.x, bulletConfig.y, bulletConfig.width, bulletConfig.height,
                    xToDrawTo, yToDrawTo, bulletConfig.width, bulletConfig.height);


        }
    }
};

GameEngine.prototype.drawBar = function (context, x, y, offsetX, offsetY, barHeight, fullBarLength, barColor, percent) {
    context.beginPath();
    context.strokeStyle = '#000000';
    context.lineWidth = 1;
    context.moveTo(x + offsetX - 1, y + offsetY - 1);
    context.lineTo(x + offsetX - 1, y + offsetY + barHeight + 1);
    context.lineTo(x + offsetX + fullBarLength + 1, y + offsetY + barHeight + 1);
    context.lineTo(x + offsetX + fullBarLength + 1, y + offsetY - 1);
    context.closePath();
    context.stroke();

    var filledBarLength = percent * fullBarLength;
    context.beginPath();
    context.fillStyle = barColor;
    context.moveTo(x + offsetX, y + offsetY);
    context.lineTo(x + offsetX, y + offsetY + HP_BAR_HEIGHT);
    context.lineTo(x + offsetX + filledBarLength, y + offsetY + HP_BAR_HEIGHT);
    context.lineTo(x + offsetX + filledBarLength, y + offsetY);
    context.closePath();
    context.fill();
};

GameEngine.prototype.renderRectangle = function () {
    var context = this.canvas.getContext("2d");
    if (!this.placementEnabled) {
        // green selection rectangle
        if (this.xMouseDown) {
            if (this.yMouseDown) {
                context.beginPath();
                context.strokeStyle = '#00cc00';
                context.lineWidth = 2 * engine.scale;
                context.moveTo(this.xCurrentMouse, this.yCurrentMouse);
                context.lineTo(this.xMouseDown, this.yCurrentMouse);
                context.lineTo(this.xMouseDown, this.yMouseDown);
                context.lineTo(this.xCurrentMouse, this.yMouseDown);
                context.closePath();
                context.stroke();
            }
        }
    }
};

GameEngine.prototype.setScale = function (factor) {
    if (this.scale / factor > MAX_ZOOM || this.scale / factor < MIN_ZOOM) {
        return;
    }

    var currCenterX = Math.ceil(engine.x + engine.widthInTiles / 2);
    var currCenterY = Math.ceil(engine.y + engine.heightInTiles / 2);

    this.scale /= factor;
    this.canvas.getContext('2d').scale(factor, factor);
    this.setFieldSizeInTiles();

    var newX = Math.floor(currCenterX - engine.widthInTiles / 2);
    var newY = Math.floor(currCenterY - engine.heightInTiles / 2);
    this.setCoordinates(newX, newY);
};
