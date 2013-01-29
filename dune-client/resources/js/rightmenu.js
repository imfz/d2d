var BUY_OPTION_HEIGHT = 110;
var BUY_OPTION_WIDTH = 182;

function RightMenu() {
    this.builderid = -1;
    this.currentlyBuildingOptionType = -1;
    var that = this;
    $("#rightmenucancelbutton").click(function () {
        connection.sendCancelConstruction(that.builderId);
    });
}

RightMenu.prototype.setMainSprite = function (mainSprite) {
    this.mainSprite = mainSprite;
};

RightMenu.prototype.setEngine = function (engine) {
    this.engine = engine;
};

RightMenu.prototype.getBuyOptionConfig = function (type) {
    var result = new Object();
    result.x = 0;
    switch (type) {
    case BUY_OPTION_POWERPLANT:
        result.y = 0 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_HARVESTER:
        result.y = 1 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_RADAR:
        result.y = 2 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_SIEGE_TANK:
        result.y = 3 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_DEVASTATOR:
        result.y = 4 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_CONCRETE:
        result.y = 5 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_QUAD:
        result.y = 6 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_DEVIATOR:
        result.y = 7 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_BATTLE_TANK:
        result.y = 8 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_REFINERY:
        result.y = 9 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_LIGHT_FACTORY:
        result.y = 10 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_FACTORY:
        result.y = 10 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_ROCKET_TURRET:
        result.y = 11 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_SILO:
        result.y = 12 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_REPAIR_DEPO:
        result.y = 13 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_AIRBASE:
        result.y = 14 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_WALL:
        result.y = 15 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_MCV:
        result.y = 16 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_TURRET:
        result.y = 17 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_SONIC_TANK:
        result.y = 18 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_LAUNCHER:
        result.y = 19 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_TRIKE:
        result.y = 20 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_TRIKE_2:
        result.y = 20 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_BARRACKS:
        result.y = 15 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_INFANTRIES:
        result.y = 15 * BUY_OPTION_HEIGHT;
        return result;
    case BUY_OPTION_ROCKET_TROOPERS:
        result.y = 15 * BUY_OPTION_HEIGHT;
        return result;
    }
    console.log("Unknown build option type: " + type);
    result.y = 0;
    return result;

};

RightMenu.prototype.bindEvents = function () {
    var that = this;
    $("#rightmenu").bind("contextmenu", function (e) {
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

RightMenu.prototype.setOptions = function (builderId, options, percentsDone, currentlyBuildingId, currentlyBuildingOptionType) {
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
        if (this.currentlyBuildingOptionType != currentlyBuildingOptionType) {
            foundDiff = true;
        }
    } else {
        foundDiff = true;
    }
    this.currentlyBuildingId = currentlyBuildingId;
    this.currentlyBuildingOptionType = currentlyBuildingOptionType;
    this.percentsDone = percentsDone;
    this.builderId = builderId;

    if (foundDiff) {
        this.options = options;
        this.redraw();
    }
};

RightMenu.prototype.redraw = function () {
    var that = this;
    $("#rightmenulistul").empty();
    $("#rightmenuprogress").html("");
    $("#rightmenucancel").css("display", "none");
    $("#currentlyBuildingCanvas").html("");
    if (this.currentlyBuildingId > 0) {
        $("#rightmenuprogress").append(
                $('<div class="progress progress-success"></div>')
                        .css({width:182})
                        .append(
                        $('<div class="bar"></div>')
                                .css({width:String(this.percentsDone) + "%"})
                )

        );
        $("#rightmenucancel").css("display", "block");
        if (this.currentlyBuildingOptionType >= 0) {
            var currentlyBuildingCanvas = $('<canvas></canvas>').attr({width:BUY_OPTION_WIDTH, height:BUY_OPTION_HEIGHT});
            $("#currentlyBuildingCanvas").append(currentlyBuildingCanvas);
            var currentlyBuildingCanvasEl = currentlyBuildingCanvas[0];
            currentlyBuildingCanvasEl.height = BUY_OPTION_HEIGHT;
            var ctx = currentlyBuildingCanvasEl.getContext("2d");
            var buildingOptionConfig = this.getBuyOptionConfig(this.currentlyBuildingOptionType);
            ctx.drawImage(this.mainSprite, buildingOptionConfig.x, buildingOptionConfig.y, BUY_OPTION_WIDTH, BUY_OPTION_HEIGHT, 0, 0, BUY_OPTION_WIDTH, BUY_OPTION_HEIGHT);
            $("#currentlyBuildingCanvas").click(function(){that.engine.enablePlacement();});

        }
    } else {
        for (var i = 0; i < this.options.length; i++) {
            var option = this.options[i];
            var canvas = $('<canvas height="' + BUY_OPTION_HEIGHT + '" width=" ' + BUY_OPTION_WIDTH + ' ">');
            var descr = $('<canvas height="' + 15 + '" width=" ' + BUY_OPTION_WIDTH + ' ">');
            $("#rightmenulistul").
                    append(
                    $('<li class="span2"></li>')
                            .css({ height:BUY_OPTION_HEIGHT + 10 })
                            .css({ width:BUY_OPTION_WIDTH + 10 })
                            .append(
                            $('<a href="#" class="thumbnail"></a>')
                                    .append(canvas)
                                    .append(descr)
                    )
            );

            canvasEl = canvas[0];
            var context = canvasEl.getContext("2d");
            context.clearRect(0, 0, canvasEl.width, canvasEl.height);

            var buyOptionConfig = this.getBuyOptionConfig(option.type);
            context.drawImage(this.mainSprite, buyOptionConfig.x, buyOptionConfig.y, BUY_OPTION_WIDTH, BUY_OPTION_HEIGHT, 0, 0, BUY_OPTION_WIDTH, BUY_OPTION_HEIGHT);

            context = descr[0].getContext("2d");
            context.font = '15px Arial Bold';
            context.fillStyle = 'black';
            context.fillText(getConstructionOptionName(option), 5, 10);
            context.fillText(option.cost, 140, 10);

            canvas.click(sendStartConnectionClosure(this.builderId, option.entityToBuildType));
        }
    }
};

function getConstructionOptionName(option) {
    switch (option.type) {
        case BUY_OPTION_TRIKE:
            return "Trike"
        case BUY_OPTION_TRIKE_2:
            return "Ordos trike"
        case BUY_OPTION_QUAD:
            return "Quad"
        case BUY_OPTION_INFANTRIES:
            return "Infantries"
        case BUY_OPTION_ROCKET_TROOPERS:
            return "Rocket troopers"
        case BUY_OPTION_HARVESTER:
            return "Harvester"
        case BUY_OPTION_BATTLE_TANK:
            return "Battle tank"
        case BUY_OPTION_MCV:
            return "MCV"
        case BUY_OPTION_LAUNCHER:
            return "Launcher"
        case BUY_OPTION_SIEGE_TANK:
            return "Siege tank"
        case BUY_OPTION_DEVASTATOR:
            return "Devastator"
        case BUY_OPTION_DEVIATOR:
            return "Deviator"
        case BUY_OPTION_SONIC_TANK:
            return "Sonic tank"
        case BUY_OPTION_POWERPLANT:
            return "Powerplant"
        case BUY_OPTION_REFINERY:
            return "Refinery"
        case BUY_OPTION_SILO:
            return "Silo"
        case BUY_OPTION_RADAR:
            return "Radar"
        case BUY_OPTION_BARRACKS:
            return "Barracks"
        case BUY_OPTION_LIGHT_FACTORY:
            return "Light factory"
        case BUY_OPTION_FACTORY:
            return "Heavy factory"
        case BUY_OPTION_REPAIR_DEPO:
            return "Repair depot"
        case BUY_OPTION_AIRBASE:
            return "Airbase"
        case BUY_OPTION_CONCRETE:
            return "Concrete"
        case BUY_OPTION_WALL:
            return "Wall"
        case BUY_OPTION_TURRET:
            return "Turret"
        case BUY_OPTION_ROCKET_TURRET:
            return "Rocket turret"
    }
    console.log("Unknown consctuction type: " + option.type);
    return "Something";
}

function sendStartConnectionClosure(builderId, entityToBuildType) {
    var thatBuilderId = builderId;
    var thatEntityToBuildId = entityToBuildType;
    return function () {
        connection.sendStartConstruction(thatBuilderId, thatEntityToBuildId);
    }

}