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

            rightmenu.setEngine(engine);

            rightmenu.setMainSprite(Utils.getImageElement("images/buys.jpg"));

            sprites.setMainSprite(Utils.getImageElement("images/main_sprite.png"));
            sprites.setUnitSprite(Utils.getImageElement("images/units.png"));
            sprites.setBulletSprite(Utils.getImageElement("images/bullet.png"));
            sprites.setBuildingsSprite(Utils.getImageElement("images/buildings.png"));
            sprites.setBuildingMarkerSprite(Utils.getImageByDataUrl("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAAzUlEQVQoU42SOw7CMAyGHVUobDkOIyvQSrBzEk7AdUBCiCtwnIyo6iN25OC4qRQvdRx//t3YBgr2AJh0+AJgZCw7IHB3Lt3fvCf/8IqhTwfABRK4c26hIkHbhutxhGfTEEyghhBAFXscQsoIv/cGbBv8KfrbrgCyCvdLBU49AWzYspFqGsrgoI5tslWBmHwe+gD+VatBbHl/jS+OnVWDCPCoCNSvuvafcr5f79fHIbdEL0WaY2mWpVXEGKrhN1u50vbIAgwtQE7SBSTAOTNb5V56txg1rgAAAABJRU5ErkJggg=="));

            sprites.setOkButton(Utils.getImageElement("images/okbutton.png"));

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
