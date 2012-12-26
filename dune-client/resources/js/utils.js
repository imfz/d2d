function Utils() {}

Utils.showError = function (msg) {
    $('#error_container').append('' +
        '<div class="alert alert-error" style="display: none;" onclick="$(this).remove();" >' +
        '  <button type="button" class="close" data-dismiss="alert" onclick="$(this).parent().remove();">&times;</button>' +
        '  ' + msg +
        '</div>'
    );

    $('#error_container div:last').fadeIn('slow');
    setTimeout(function () {
        $('#error_container div:visible:not(:animated):last').fadeOut('fast', function () {$(this).remove();});
    }, 5000);
    return false;
};

Utils.getImageElement = function (imgName) {
    var image = new Image();
    image.src = imgName;
    return image;
};
