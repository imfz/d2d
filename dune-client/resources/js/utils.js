function Utils() {

}

var globalImageCounter = 0;
var imageLoadedCallback;
var allImagesLoaded = false;

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
    if (allImagesLoaded) {
        console.log("Loading the images after preloader has done his job is a bad idea");
        return;
    }
    globalImageCounter++;
    var image = new Image();
    image.src = imgName;
    image.onload = function () {
        globalImageCounter--;
        if (globalImageCounter <= 0) {
            imageLoadedCallback();
        }
    };
    image.onerror = function (error) {
        Utils.showError('<b>' + error.target.attributes.src.nodeValue + '</b> not found');
    };
    return image;
};

Utils.getImageByDataUrl = function(dataUrl) {
    var newImg = document.createElement("img");
    newImg.src = dataUrl;
    return newImg;
};

Utils.afterImagesLoaded = function (callback) {
    imageLoadedCallback = callback;
    allImagesLoaded = true;
};

