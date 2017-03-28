var selectedEmployeeId = -1;
var approvers;
var count = document.getElementsByTagName('select').length;

$(function () {
    var lastAjaxRequest = null;
    $("#employee").autocomplete({
        delay: 400,
        source: function (request, response) {
            var currentAjaxRequest = $.ajax({
                url: "search_employees",
                dataType: "json",
                beforeSend: function (request) {
                    if (lastAjaxRequest != null)
                        lastAjaxRequest.abort();
                    lastAjaxRequest = currentAjaxRequest;
                },
                data: {
                    fullname: request.term
                },
                success: function (data) {
                    response(data);
                    for (var i = 0; i < data.length; i++) {
                        console.log(data[i].value);
                    }
                },
                complete: function () {
                    lastAjaxRequest = null;
                }
            });
        },
        minLength: 1,
        select: function (event, ui) {
            getApprovers(ui.item.id);
            selectedEmployeeId = ui.item.id;
            $("#employeeId").text("");
        }
    });
});

function getApprovers(employeeId) {
    $.ajax({
        url: "approvers_of_employee",
        async: true,
        type: "GET",
        dataType: "json",
        data: {employeeId: employeeId},
        success: function (result, status, xhr) {

            for (var i = 1; i <= count; i++)
                updateApproverList(approvers, 'approvers'+i);

            if (result.length < 1) {
                for (var i = 1; i <= count; i++)
                    $('#approvers' + i).val("");
            }

            for (var i = 0; i <result.length; i++){
                console.log('length: '+result.length);
                $('#approvers' + (i + 1)).val(result[i].id);
                for(var j = i; j<result.length; j++){
                    hide(result[i].id, 'approvers'+(j+2));
                    if(j>=1){
                        for(var k = j; k>=0; k--){
                            hide(result[i].id, 'approvers'+(k));
                        }
                    }
                }
            }
        },
        error: function (xhr, status, error) {
        }
    });
}


function updateApproverList(newApprovers, selectHtmlId) {
    var selectHtml = $('#' + selectHtmlId);
    selectHtml.find('option').remove().end();
    $.each(newApprovers, function (key, value) {
        selectHtml.append($("<option></option>").attr("value", value.id).text(value.label));
    });
}

function hide(approverId, selectHtmlId) {
    var selectHtml = $('#' + selectHtmlId);
    var lastSelectedId = selectHtml.val();
    for (var i = 0; i < approvers.length; i++) {
        var approver = approvers[i];
        if (approver.id == approverId) {
            var newApprovers = JSON.parse(JSON.stringify(approvers));
            newApprovers.splice(i, 1);
            updateApproverList(newApprovers, selectHtmlId);
            break;
        }
    }
    if (lastSelectedId != approverId) {
        selectHtml.val(lastSelectedId);
    }
}

$(function () {
    $.ajax({
        url: "admin_list",
        async: true,
        type: "GET",
        dataType: "json",
        success: function (result, status, xhr) {
            approvers = result;
            for (var i = 1; i <= count; i++)
                updateApproverList(approvers, 'approvers'+i);
            for (var i = 1; i <= count; i++)
                $('#approvers'+i).val("");
        },
        error: function (xhr, status, error) {
        }
    });
});

function onSummitRequestUpdateApprovers() {
    var employeeId = selectedEmployeeId;
    updateApprovers(employeeId);
}

function showProcessingDialog() {
    return bootbox.dialog({
        message: '<div class="text-center"><i class="fa fa-spin fa-spinner"></i> Processing...</div>',
        onEscape: false,
        closeButton: false
    });
}

function clearErrorMessage() {
    $("#employeeId").text("");
    for (var i = 1; i <= count; i++) {
        $("#approver"+i).text("");
        $("#approver"+i).text("");
    }
}

function showErrorMessage(errorDescriptions) {
    clearErrorMessage();
    for (var i = 0; i < errorDescriptions.length; i++) {
        var errorDescription = errorDescriptions[i];
        $("#" + errorDescription["fieldName"]).text(errorDescription["message"]);
    }
}

function onProcessSuccess(result) {
    var json = result;
    if (json["errorCode"] > 0) {
        showErrorMessage(json["errorDescriptions"]);
    } else {
        clearErrorMessage();
        bootbox.alert({
            message: json["errorDescriptions"][0]["message"],
            className: 'bb-alternate-modal'
        });
    }
}

function updateApprovers(employeeId) {
    var dialog = showProcessingDialog();
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var approverList = [];
    for (var i= 1;i<=count;i++){
        var approver = $('#approvers'+i).val();
        approverList.push(approver);
    }
    var cdata = {employeeId:employeeId, approverList:approverList};
    var sdata = JSON.stringify(cdata);
    $.ajax({
        url: "update_approvers",
        async: true,
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        data: sdata,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (result, status, xhr) {
            dialog.modal("hide");
            onProcessSuccess(result, dialog);
        },
        error: function (xhr, status, error) {
            showErrorMessage(status);
            dialog.modal("hide");
        }
    });
}

function showSelectionBox(result) {
    var employeeOptions = [];
    for (var i = 0; i < result.length; i++) {
        var employee = result[i];
        employeeOptions.push({
            "text"  : employee["label"],
            "value" : employee["id"]
        });
    }
    bootbox.prompt({
        title: "Conflict employee name!",
        inputType: 'select',
        inputOptions: employeeOptions,
        callback: function (result) {
            if (result == null) {
                $("#employee").val("");
            } else {
                selectedEmployeeId = result;
                getApprovers(selectedEmployeeId);
            }
        }
    });
}

function displayValidationResult(result) {
    clearErrorMessage();
    if (result["employeeList"].length == 0) {
        showErrorMessage(JSON.parse('[{"fieldName":"employeeId", "message":"'+result["errorMessage"]+'"}]'));

    } else if (result["employeeList"].length == 1) {
        selectedEmployeeId = result["employeeList"][0]["id"];
        getApprovers(selectedEmployeeId);
    } else {
        showSelectionBox(result["employeeList"]);
    }
}

function validateEmployeeName(employeeName) {
    $.ajax({
        url: "valid_employee",
        async: true,
        type: "GET",
        dataType: "json",
        data: {
            fullName: employeeName
        },
        success: function (result, status, xhr) {
            displayValidationResult(result);
        },
        error: function (xhr, status, error) {

        },
        complete: function () {

        }
    });
}

function onEmployeeNameLostFocus() {
    if (selectedEmployeeId == null) {
        var employeeElement = $("#employee");
        var employeeName = employeeElement.val();
        if (employeeName != null) {
            employeeName = employeeName.trim();
            employeeElement.val(employeeName);
            if (employeeName.length > 0)
                validateEmployeeName(employeeName);
        }
    }
}

function onEmployeeNameChange() {
    selectedEmployeeId = null;
}