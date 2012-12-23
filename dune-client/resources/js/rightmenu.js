
var BUY_OPTION_HEIGHT = 110;
var BUY_OPTION_WIDTH = 182;

function RightMenu() {

}

RightMenu.prototype.setCanvas = function (canvas) {
    this.canvas = canvas;
};

RightMenu.prototype.setMainSprite = function (mainSprite) {
    this.mainSprite = mainSprite;
};

RightMenu.prototype.getBuyOptionConfig = function(type) {
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
        y =  0 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_HARVESTER) {
        y =  1 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_RADAR) {
        y =  2 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_CONCRETE) {
        y =  5 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_JEEP) {
        y =  6 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_DEVIATOR) {
        y =  7 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_REFINERY) {
        y =  9 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_FACTORY) {
        y =  10 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_ROCKET_TURRET) {
        y =  11 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_SILO) {
        y =  12 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_REPAIR_DEPO) {
        y =  13 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_AIRBASE) {
        y =  14 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_WALL) {
        y =  15 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_MCV) {
        y =  16 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_TURRET) {
        y =  17 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_SONIC_TANK) {
        y =  18 * BUY_OPTION_HEIGHT;
    }
    if (type == BUY_OPTION_TRIKE) {
        y =  20 * BUY_OPTION_HEIGHT;
    }

    var result = new Object();
    result.x = x;
    result.y = y;
    return result;

};

RightMenu.prototype.bindEvents = function () {
    var that = this;
    $(this.canvas).bind("contextmenu",function(e){
        return false;
    });
    $(this.canvas).click(function(e){
        var y = Math.floor((e.pageY-$(that.canvas).offset().top));
        var i = Math.floor(y / BUY_OPTION_HEIGHT);
        if (i < that.options.length) {
            var option = that.options[i];
            var func = option.onclick;
            func.call(that);
        }

    });
};

RightMenu.prototype.setOptions = function (builderId, options) {
    if (!options) {
        options = new Array();
    }
    if (!this.options) {
        this.options = new Array();
    }
    if (options.length == this.options.length) {
        var foundDiff = false;
        for (var i = 0; i < options.length; i++) {
            if (options[i].type != this.options[i].type) {
                foundDiff = true;
            }
        }
        if (!foundDiff) {
            return;
        }
    }
    this.options = options;
    var context = this.canvas.getContext("2d");
    context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    this.canvas.height = options.length * BUY_OPTION_HEIGHT;
    for (var i = 0; i < options.length; i++) {
        var option = options[i];
        option.onclick = sendStartConnectionClosure(builderId, option.entityToBuildId);
        var buyOptionConfig = this.getBuyOptionConfig(option.type);
        context.drawImage(this.mainSprite, buyOptionConfig.x, buyOptionConfig.y, BUY_OPTION_WIDTH, BUY_OPTION_HEIGHT, 0, i * BUY_OPTION_HEIGHT, BUY_OPTION_WIDTH, BUY_OPTION_HEIGHT);

    }
};

function sendStartConnectionClosure(builderId, entityToBuildId) {
    var thatBuilderId = builderId;
    var thatEntityToBuildId = entityToBuildId;
    return function() {
        connection.sendStartConstruction(thatBuilderId, thatEntityToBuildId);
    }

}