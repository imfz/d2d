var BUY_OPTION_HEIGHT = 110;
var BUY_OPTION_WIDTH = 182;

function RightMenu() {
    this.builderid = -1;
    this.currentlyBuildingOptionId = -1;
    var that = this;
    $("#rightmenucancelbutton").click(function () {
        connection.sendCancelConstruction(that.builderId);
    });
}

RightMenu.prototype.setCanvas = function (canvas) {
    this.canvas = canvas;
};

RightMenu.prototype.setCurrentlyBuildingCanvas = function (currentlyBuildingCanvas) {
    this.currentlyBuildingCanvas = currentlyBuildingCanvas;
};

RightMenu.prototype.setMainSprite = function (mainSprite) {
    this.mainSprite = mainSprite;
};

RightMenu.prototype.getBuyOptionConfig = function (type) {
    var x = 0;
    var y = 0;
    if (type == BUY_OPTION_SIEGE_TANK) {
        y = 3 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_LAUNCHER) {
        y = 19 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_TANK) {
        y = 8 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_DEVASTATOR) {
        y = 4 * BUY_OPTION_HEIGHT;
    }

    if (type == BUY_OPTION_POWERPLANT) {
        y = 0 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_HARVESTER) {
        y = 1 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_RADAR) {
        y = 2 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_CONCRETE) {
        y = 5 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_JEEP) {
        y = 6 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_DEVIATOR) {
        y = 7 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_REFINERY) {
        y = 9 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_FACTORY) {
        y = 10 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_ROCKET_TURRET) {
        y = 11 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_SILO) {
        y = 12 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_REPAIR_DEPO) {
        y = 13 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_AIRBASE) {
        y = 14 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_WALL) {
        y = 15 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_MCV) {
        y = 16 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_TURRET) {
        y = 17 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_SONIC_TANK) {
        y = 18 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_TRIKE) {
        y = 20 * BUY_OPTION_HEIGHT;
    }

    var result = new Object();
    result.x = x;
    result.y = y;
    return result;

};

RightMenu.prototype.bindEvents = function () {
    var that = this;
    $(this.canvas).bind("contextmenu", function (e) {
        return false;
    });
    $(this.canvas).click(function (e) {
        var y = Math.floor((e.pageY - $(that.canvas).offset().top));
        var i = Math.floor(y / BUY_OPTION_HEIGHT);
        if (i < that.options.length) {
            var option = that.options[i];
            var func = option.onclick;
            func.call(that);
        }

    });
};

RightMenu.prototype.setOptions = function (builderId, options, percentsDone, currentlyBuildingId, currentlyBuildingOptionId) {
    if (!options) {
        options = new Array();
    }
    if (!this.options) {
        this.options = new Array();
    }
    var foundDiff = false;
    if (options.length == this.options.length) {
        for (var i = 0; i < options.length; i++) {
            if (options[i].type != this.options[i].type) {
                foundDiff = true;
            }
        }
        if (this.percentsDone != percentsDone) {
            foundDiff = true;
        }
        if (this.currentlyBuildingId != currentlyBuildingId) {
            foundDiff = true;
        }
        if (this.currentlyBuildingOptionId != currentlyBuildingOptionId) {
            foundDiff = true;
        }
    } else {
        foundDiff = true;
    }
    this.currentlyBuildingId = currentlyBuildingId;
    this.currentlyBuildingOptionId = currentlyBuildingOptionId;
    this.percentsDone = percentsDone;
    this.builderId = builderId;

    if (foundDiff) {
        this.options = options;
        this.redraw();
    }
};

RightMenu.prototype.redraw = function () {
    var context = this.canvas.getContext("2d");
    context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    $("#rightmenuprogress").html("");
    $("#rightmenucancel").css("display", "none");
    this.currentlyBuildingCanvas.height = 0;
    this.canvas.height = 0;
    if (this.currentlyBuildingId > 0) {
        $("#rightmenuprogress").html('<div class="progress progress-success"><div class="bar" style="width: ' + this.percentsDone + '%"></div></div>');
        $("#rightmenucancel").css("display", "block");
        if (this.currentlyBuildingOptionId >= 0) {
            this.currentlyBuildingCanvas.height = BUY_OPTION_HEIGHT;
            var ctx = this.currentlyBuildingCanvas.getContext("2d");
            var buildingOptionConfig = this.getBuyOptionConfig(this.currentlyBuildingOptionId);
            ctx.drawImage(this.mainSprite, buildingOptionConfig.x, buildingOptionConfig.y, BUY_OPTION_WIDTH, BUY_OPTION_HEIGHT, 0, 0, BUY_OPTION_WIDTH, BUY_OPTION_HEIGHT);
        }
    } else {
        this.canvas.height = this.options.length * BUY_OPTION_HEIGHT;
        for (var i = 0; i < this.options.length; i++) {
            var option = this.options[i];
            option.onclick = sendStartConnectionClosure(this.builderId, option.entityToBuildId);
            var buyOptionConfig = this.getBuyOptionConfig(option.type);
            context.drawImage(this.mainSprite, buyOptionConfig.x, buyOptionConfig.y, BUY_OPTION_WIDTH, BUY_OPTION_HEIGHT, 0, i * BUY_OPTION_HEIGHT, BUY_OPTION_WIDTH, BUY_OPTION_HEIGHT);
        }
    }
};

function sendStartConnectionClosure(builderId, entityToBuildId) {
    var thatBuilderId = builderId;
    var thatEntityToBuildId = entityToBuildId;
    return function () {
        connection.sendStartConstruction(thatBuilderId, thatEntityToBuildId);
    }

}