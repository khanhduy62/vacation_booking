function showProcessingDialog() {
    return bootbox.dialog({
        message: '<div class="text-center"><i class="fa fa-spin fa-spinner"></i> Processing...</div>',
        onEscape: false,
        closeButton: false
    });
}

function disableButton(button) {
    button.removeClass("btn-success").removeClass("btn-danger").addClass("btn-disable");
    button.attr("id", "");
    button.css("pointer-events", "none");
    button.css("cursor", "not-allowed");
}

function disableButtons(requestId) {
    var approveButton = $("#approve_" + requestId);
    disableButton(approveButton);
    var rejectButton = $("#reject_" + requestId);
    disableButton(rejectButton);
}

function showErrorMessage(message, dialog) {
    dialog.find(".bootbox-body").html('<div class="alert alert-warning"><strong>Unsuccessful!</strong> '.concat(message).concat('</div>'));
    var modelContent = dialog.find(".modal-content");
    modelContent.html(modelContent.html() + '<div class="modal-footer"><button data-bb-handler="ok" type="button" class="btn btn-primary">OK</button></div>');
}

function updateApprovals(requestId, approved) {
    var spanClass = approved ? "glyphicon-ok green" : "glyphicon-remove red";
    $("#span" + requestId).removeClass("glyphicon-minus").removeClass("glyphicon-remove").removeClass("glyphicon-ok").addClass(spanClass);
}

function onProcessSuccess(result, requestId, approved, dialog) {
    var json =  $.parseJSON(result);
    if (json["errorCode"] > 0) {
        var message = json["message"];
        showErrorMessage(message, dialog);
    } else {
        dialog.modal("hide");
        disableButtons(requestId);
        updateApprovals(requestId, approved);
    }
}

function onProcessFail(status, dialog) {
    showErrorMessage(status, dialog);
}

function changeRequestStatusAsync(requestId, approved) {
    var dialog = showProcessingDialog();
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        url: "status",
        async: true,
        type: "POST",
        contextType: "application/x-www-form-urlencoded",
        dataType: "text",
        data: {
            requestId: requestId,
            approved: approved
        },
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (result, status, xhr) {
            onProcessSuccess(result, requestId, approved, dialog);
        },
        error: function (xhr, status, error) {
            onProcessFail(status, dialog);
        }
    });
}

function approve(requestId) {
    changeRequestStatusAsync(requestId, true);
}

function reject(requestId) {
    changeRequestStatusAsync(requestId, false);
}

$("a[id^='approve_']").click(function () {
    var id = this.id + "";
    if (id != "") {
        var requestId = id.substr("approve_".length);
        approve(requestId);
    }
});

$("a[id^='reject_']").click(function () {
    var id = this.id + "";
    if (id != "") {
        var requestId = id.substr("reject_".length);
        reject(requestId);
    }
});

function changePageSize() {
    var selectedValue = $('#dl-page-size').val();
    var searchKeyword = $('#hd-search-keyword').val();
    if (searchKeyword.length == 0) {
        $('#hd-search-keyword').remove();
    }
    if (selectedValue == 'all') {
        $('#hd-page-current-index').remove();
    }
    $('#f-change-page-size').submit();
}

$('#task-table-filter').keyup(function (event) {
    if (event.keyCode == 13) {
        var searchKeyword = $('#task-table-filter').val().trim();
        if (searchKeyword.length > 0) {
            $('#form-search').submit();
        }
    }
});

$(function () {
    var searchKeyword = $('#task-table-filter').val().trim();
    if (searchKeyword.length > 0) {
        $('.panel-body').slideToggle();
    }
});

$(function(){
    $('.container').on('click', '.panel-heading span.filter', function(e){
        var $this = $(this),
            $panel = $this.parents('.panel');

        $panel.find('.panel-body').slideToggle();
        if($this.css('display') != 'none') {
            $panel.find('.panel-body input').focus();
        }
    });
    $('[data-toggle="tooltip"]').tooltip();
});