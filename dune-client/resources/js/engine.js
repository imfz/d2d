var TILE_HEIGHT = 63;
var TILE_WIDTH = 63;
var OK_BUTTON_ENABLED_TICKS = 30;
var OK_BUTTON_DISABLED_TICKS = 30;

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
    this.shownBuildings = new Array();
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

GameEngine.prototype.setBuildingsSprite = function (buildingsSprite) {
    this.buildingsSprite = buildingsSprite;
};

GameEngine.prototype.setOkButton = function (okButtonSprite) {
    this.okButtonSprite = okButtonSprite;
};

GameEngine.prototype.setBuildBgGreen = function (bgGreen) {
    this.bgGreenSprite = bgGreen;
};

GameEngine.prototype.setBuildBgRed = function (bgRed) {
    this.bgRedSprite = bgRed;
};

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
            if (e.ctrlKey) {
                that.setCoordinates(that.x - that.widthInTiles, that.y);
            } else {
                that.setCoordinates(that.x - 1, that.y);
            }
            break;
        case arrow.up:
            if (e.ctrlKey) {
                that.setCoordinates(that.x, that.y - that.heightInTiles);
            } else {
                that.setCoordinates(that.x, that.y - 1);
            }
            break;
        case arrow.right:
            if (e.ctrlKey) {
                that.setCoordinates(that.x + that.widthInTiles, that.y);
            } else {
                that.setCoordinates(that.x + 1, that.y);
            }
            break;
        case arrow.down:
            if (e.ctrlKey) {
                that.setCoordinates(that.x, that.y + that.heightInTiles);
            } else {
                that.setCoordinates(that.x, that.y + 1);
            }
            break;
        case 27:
            // handle ESC button
            if (that.placementEnabled) {
                that.placementEnabled = false;
                return;
            }
            that.selectedUnitId = [];
            break;
        case 72:
            // handle H button
            that.centerOnMain();
            break;
        }

    });
    $(this.canvas).mouseup(function (e) {
        switch (event.which) {
        case 1:
            var x = Math.floor((e.pageX - $(that.canvas).offset().left));
            var y = Math.floor((e.pageY - $(that.canvas).offset().top));
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
            if (!that.placementEnabled) {
                that.selectedUnitId = [];
                for (var i = 0; i < that.shownUnits.length; i++) {
                    var showUnitInfo = that.shownUnits[i];
                    if ((x2 > showUnitInfo.x) && (x < showUnitInfo.x + TILE_WIDTH)) {
                        if ((y2 > showUnitInfo.y) && (y < showUnitInfo.y + TILE_HEIGHT)) {
                            that.selectedUnitId.push(showUnitInfo.id);
                        }
                    }
                }
                // if no units selected, search for buildings
                if (that.selectedUnitId.length == 0) {
                    for (var i = 0; i < that.shownBuildings.length; i++) {
                        var shownBuildingInfo = that.shownBuildings[i];
                        if ((x2 > shownBuildingInfo.x) && (x < shownBuildingInfo.width + shownBuildingInfo.x)) {
                            if ((y2 > shownBuildingInfo.y) && (y < shownBuildingInfo.height + shownBuildingInfo.y)) {
                                // ok text is blinking, go on with placement
                                if (shownBuildingInfo.placementEnabled) {
                                    that.placementEnabled = true;
                                    that.builderId = shownBuildingInfo.id;
                                    var buildingConfig = that.getBuildingPlacementConfig(shownBuildingInfo.buildingTypeBuilt);
                                    that.placementWidth = buildingConfig.width;
                                    that.placementHeight = buildingConfig.height;
                                }
                                that.selectedBuilding = shownBuildingInfo.id;
                                connection.sendBuildingSelection(that.selectedBuilding);
                            }
                        }
                    }
                }
            } else {
                var mapX = Math.floor((x / TILE_WIDTH) + that.x);
                var mapY = Math.floor((y / TILE_HEIGHT) + that.y);
                connection.sendBuildingPlacement(mapX, mapY, that.builderId);
            }
            that.xMouseDown = null;
            that.yMouseDown = null;
        }
        return false;
    });
    $(this.canvas).mousemove(function (e) {
        var x = Math.floor((e.pageX - $(that.canvas).offset().left));
        var y = Math.floor((e.pageY - $(that.canvas).offset().top));
        that.xCurrentMouse = x;
        that.yCurrentMouse = y;
    });
    $(this.canvas).mouseleave(function (e) {
        var x = Math.floor((e.pageX - $(that.canvas).offset().left));
        var y = Math.floor((e.pageY - $(that.canvas).offset().top));
        that.xMouseDown = null;
        that.yMouseDown = null;
    });
    $(this.canvas).mousedown(function (e) {
        var x = Math.floor((e.pageX - $(that.canvas).offset().left));
        var y = Math.floor((e.pageY - $(that.canvas).offset().top));
        switch (event.which) {
        case 1:
            // remember mousedown for rectangle selection
            that.xMouseDown = x;
            that.yMouseDown = y;
            break;
        case 3:
            if (that.placementEnabled) {
                that.placementEnabled = false;
                return;
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
}

GameEngine.prototype.centerOnCoordinates = function (x, y) {
    // because x,y represent topright corner, and the desired coordinates represent the middle of the screen
    var mapX = x;
    var mapY = y;
    mapX = Math.round(mapX - this.widthInTiles / 2);
    mapY = Math.round(mapY - this.heightInTiles / 2);
    this.setCoordinates(mapX, mapY);
};

GameEngine.prototype.shiftMovingUnit = function (x, y, travelled, viewDirection) {
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
    var units = this.map.getUnits(this.x - 1, this.y - 1, this.x + this.widthInTiles + 1, this.y + this.heightInTiles + 1);

    this.renderTiles(units, buildings);
    this.renderBuildings(buildings);
    this.renderUnits(units);
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
    var currentMouseMapX = Math.floor(this.xCurrentMouse / TILE_WIDTH + this.x);
    var currentMouseMapY = Math.floor(this.yCurrentMouse / TILE_HEIGHT + this.y);
    for (var x = this.x; x < this.x + this.widthInTiles; x++) {
        for (var y = this.y; y < this.y + this.heightInTiles; y++) {
            //console.log("Rendering tile x: " + x + " y: " + y);
            var tileConfig = sprites.getTileConfig(x, y, this.map);
            if (tileConfig) {
                context.drawImage(this.mainSprite, tileConfig.x, tileConfig.y, TILE_WIDTH, TILE_HEIGHT, (x - this.x) * TILE_WIDTH, (y - this.y) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
            }
            if (this.placementEnabled) {
                if (x >= currentMouseMapX && x < currentMouseMapX + this.placementWidth) {
                    if (y >= currentMouseMapY && y < currentMouseMapY + this.placementHeight) {
                        if (this.map.isTileOkForBuilding(x, y, units, buildings)) {
                            context.drawImage(this.bgGreenSprite,
                                    0, 0, TILE_WIDTH, TILE_HEIGHT,
                                    (x - this.x) * TILE_WIDTH, (y - this.y) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT
                            );
                        } else {
                            context.drawImage(this.bgRedSprite,
                                    0, 0, TILE_WIDTH, TILE_HEIGHT,
                                    (x - this.x) * TILE_WIDTH, (y - this.y) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT
                            )
                        }
                    }
                }
            }

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
        context.drawImage(this.buildingsSprite, buildingConfig.x, buildingConfig.y, buildingConfig.width, buildingConfig.height, xToDrawTo, yToDrawTo, buildingConfig.width, buildingConfig.height);
        // construction complete image
        if (building.constructionComplete && okButtonEnabled) {
            context.drawImage(this.okButtonSprite,
                    0,
                    0,
                    this.okButtonSprite.width, this.okButtonSprite.height,
                    xToDrawTo + TILE_WIDTH - this.okButtonSprite.width / 2,
                    yToDrawTo + TILE_HEIGHT - this.okButtonSprite.height / 2,
                    this.okButtonSprite.width, this.okButtonSprite.height
            );
        }
        if (building.ownerId == connection._playerId) {
            var shownBuildingInfo = new Object();
            shownBuildingInfo.id = building.id;
            shownBuildingInfo.x = xToDrawTo;
            shownBuildingInfo.y = yToDrawTo;
            shownBuildingInfo.width = buildingConfig.width;
            shownBuildingInfo.height = buildingConfig.height;
            shownBuildingInfo.placementEnabled = building.constructionComplete;
            shownBuildingInfo.buildingTypeBuilt = building.entityBuiltId;
            this.shownBuildings.push(shownBuildingInfo);
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
            var movingCoord = this.shiftMovingUnit(xToDrawTo, yToDrawTo, unit.travelled, unit.viewDirection);
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

            if (unit.ownerId == connection._playerId) {
                var shownUnitInfo = new Object();
                shownUnitInfo.id = unit.id;
                shownUnitInfo.x = movingCoord.x;
                shownUnitInfo.y = movingCoord.y;
                this.shownUnits.push(shownUnitInfo);
            }
        }
    }
};

GameEngine.prototype.renderRectangle = function () {
    var context = this.canvas.getContext("2d");
    if (!this.placementEnabled) {
        // green selection rectangle
        if (this.xMouseDown) {
            if (this.yMouseDown) {
                context.beginPath();
                context.strokeStyle = '#00cc00';
                context.lineWidth = 2;
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




