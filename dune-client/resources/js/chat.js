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

Chat.prototype.init = function () {
    var that = this;
    $('#joinB').click(function (event) {
        var $username = $('#username');
        var name = $username.val();
        if (name == '') {
            Utils.showError("Type your name!");
            $username.focus();
            return false;
        }
        connection.start(name, $('#playerId').val());
    });

    $('#phrase')
        .attr({autocomplete: 'OFF'})
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
