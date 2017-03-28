function showProcessingDialog() {
    return bootbox.dialog({
        message: '<div class="text-center"><i class="fa fa-spin fa-spinner"></i> Processing...</div>',
        onEscape: false,
        closeButton: false
    });
}

function disableButton(requestId) {
    var cancelButton = $("#cancel_" + requestId);
    cancelButton.removeClass("btn-warning").addClass("btn-disable");
    cancelButton.attr("id", "");
    cancelButton.css("pointer-events", "none");
    cancelButton.css("cursor", "not-allowed");
}

function onProcessSuccess(requestId,dialog) {
    dialog.modal("hide");
    bootbox.confirm("Are you sure?", function(result) {
        if(result)
            disableButton(requestId);
    });

}

function onProcessFail(status, dialog) {
    showErrorMessage(status, dialog);
}

function changeRequestStatusAsync(requestId) {
    var dialog = showProcessingDialog();
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var data = {requestId: requestId};
    $.ajax({
        url: "cancel",
        async: true,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(data),
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (result, status, xhr) {
            onProcessSuccess(requestId,dialog);
        },
        error: function (xhr, status, error) {
            onProcessFail(status, dialog);
        }
    });
}

function cancel(requestId) {
    changeRequestStatusAsync(requestId);
}

$("a[id^='cancel_']").click(function () {
    var id = this.id + "";
    if (id != "") {
        var requestId = id.substr("cancel_".length);
        cancel(requestId);
    }
});


