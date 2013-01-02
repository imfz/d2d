function getKeyCode(ev) {
    if (window.event) {
        return window.event.keyCode;
    }
    return ev.keyCode;
}

function Chat() {
    console.log("Chat created");
}

Chat.prototype.sendMessage = function (message) {
    if (message != null && message.length > 0) {
        connection.sendChatMessage(message);
    }
};

Chat.prototype.setEngine = function (engine) {
    this.engine = engine;
};

Chat.prototype.init = function () {
    var that = this;

    var disableHotkeys = function(e) {
        that.engine.hotkeysEnabled = false;
    };

    var enableHotkeys = function(e) {
        that.engine.hotkeysEnabled = true;
    };

    $('#username').focus(disableHotkeys).blur(enableHotkeys);
    $('#phrase').focus(disableHotkeys).blur(enableHotkeys);

    $('#joinB').click(function (event) {
        var $username = $('#username');
        var name = $username.val();
        if (name == '') {
            Utils.showError("Type your name!");
            $username.focus();
            return false;
        }
        connection.start(name, $('#playerId').val());
        return false;
    });

    $('#phrase')
        .attr({autocomplete: 'off'})
        .keyup(function (ev) {
            var keyc = getKeyCode(ev);
            if (keyc == 13 || keyc == 10) {
                var $phrase = $('#phrase');
                that.sendMessage($phrase.val());
                $phrase.val('');
                return false;
            }
            return true;
        });
    $('#sendB').click(function (event) {
        var $phrase = $('#phrase');
        that.sendMessage($phrase.val());
        $phrase.val('');
        return false;
    });
    console.log("Chat initialized");
};
