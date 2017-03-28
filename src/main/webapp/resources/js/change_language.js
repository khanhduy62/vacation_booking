$(function(){
    var returnUrl = window.location.href;
    returnUrl = escape(returnUrl);
    $("#changeLanguageEN").attr("href", $("#changeLanguageEN").attr('href') + '&returnUrl=' + returnUrl);
    $("#changeLanguageVI").attr("href", $("#changeLanguageVI").attr('href') + '&returnUrl=' + returnUrl);
})
