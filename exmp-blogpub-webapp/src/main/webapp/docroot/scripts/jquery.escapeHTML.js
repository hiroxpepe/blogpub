;(function($){
    $.escapeHTML = function(val) {
        return $("<div/>").text(val).html();
    };
    $.erasureHTML = function(val) {
        return $("<div/>").html(val).text();
    };
})(jQuery);
