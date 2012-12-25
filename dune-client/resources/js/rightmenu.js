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
        if (this.currentlyBuildingOptionId >= 0) {
            var currentlyBuildingCanvas = $('<canvas></canvas>').attr({width:BUY_OPTION_WIDTH, height:BUY_OPTION_HEIGHT});
            $("#currentlyBuildingCanvas").append(currentlyBuildingCanvas);
            var currentlyBuildingCanvasEl = currentlyBuildingCanvas[0];
            currentlyBuildingCanvasEl.height = BUY_OPTION_HEIGHT;
            var ctx = currentlyBuildingCanvasEl.getContext("2d");
            var buildingOptionConfig = this.getBuyOptionConfig(this.currentlyBuildingOptionId);
            ctx.drawImage(this.mainSprite, buildingOptionConfig.x, buildingOptionConfig.y, BUY_OPTION_WIDTH, BUY_OPTION_HEIGHT, 0, 0, BUY_OPTION_WIDTH, BUY_OPTION_HEIGHT);
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
            context.fillStyle = 'green';
            context.fillText(option.name, 5, 15);
            context.fillText(option.cost, 140, 15);

            canvas.click(sendStartConnectionClosure(this.builderId, option.entityToBuildId));
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