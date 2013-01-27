var gameLog;
var engine;
var minimapEngine;
var map;
var chat;
var networking;
var handler;
var rightmenu;
var moneyTab;
var sprites;

var urlArgs = "bust=" + Math.floor(Math.random()*10000);
require.config({
    urlArgs: urlArgs
});

console.log("Adding url args to js files " + urlArgs);

require(
        [
            "jquery",
            "jquery.json-2.4.min",
            "jquery.mousewheel-3.0.6",
            "utils",
            "enums",
            "log",
            "windows",
            "sprites",
            "lobby",
            "engine",
            "minimapengine",
            "map",
            "chat",
            "serializers",
            "network",
            "handler",
            "rightmenu",
            "moneytab",
            "main"
        ], function ($) {
            var thing = { plugin: 'jquery-json', version: 2.4 };

            console.log("Initialized");

            $("#host").val("ws://" + window.location.hostname + ":" + window.location.port + "/chat");

            gameLog = new GameLog();
            sprites = new Sprites();
            engine = new GameEngine();
            minimapEngine = new MinimapGameEngine();
            map = new GameMap();
            networking = new NetworkConnection();
            lobby = new Lobby();
            chat = new Chat();
            rightmenu = new RightMenu();
            moneyTab = new MoneyTab();
            handler = new Handler(map, gameLog, rightmenu, engine, moneyTab, lobby);

            var canvas = $("#canvas").get(0);
            engine.setCanvas(canvas);
            engine.setMap(map);

            var minimapCanvas = $("#minimap").get(0);
            minimapEngine.setCanvas(minimapCanvas);
            minimapEngine.setMap(map);
            minimapEngine.setEngine(engine);

            map.setSprites(sprites);

            rightmenu.setEngine(engine);
            rightmenu.setMainSprite(Utils.getImageElement("images/buys.jpg"));

            sprites.setMainSprite(Utils.getImageElement("images/main_sprite.png"));
            sprites.setUnitSprite(Utils.getImageElement("images/units.png"));
            sprites.setBulletSprite(Utils.getImageElement("images/bullet.png"));
            sprites.setBuildingsSprite(Utils.getImageElement("images/buildings.png"));
            sprites.setBlastSprite(Utils.getImageByDataUrl("data:image/png;base64,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"));
            sprites.setBuildingMarkerSprite(Utils.getImageByDataUrl("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAAzUlEQVQoU42SOw7CMAyGHVUobDkOIyvQSrBzEk7AdUBCiCtwnIyo6iN25OC4qRQvdRx//t3YBgr2AJh0+AJgZCw7IHB3Lt3fvCf/8IqhTwfABRK4c26hIkHbhutxhGfTEEyghhBAFXscQsoIv/cGbBv8KfrbrgCyCvdLBU49AWzYspFqGsrgoI5tslWBmHwe+gD+VatBbHl/jS+OnVWDCPCoCNSvuvafcr5f79fHIbdEL0WaY2mWpVXEGKrhN1u50vbIAgwtQE7SBSTAOTNb5V56txg1rgAAAABJRU5ErkJggg=="));

            sprites.setOkButton(Utils.getImageByDataUrl("data:image/png;base64,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"));

            Utils.afterImagesLoaded(function () {

                sprites.init();

                minimapEngine.bindEvents();

                rightmenu.bindEvents();

                gameLog.redraw();

                networking.init();

                lobby.setNetwork(connection);
                lobby.init();

                chat.setEngine(engine);
                chat.init();

                rightmenu.setOptions([]);
            });

        });
