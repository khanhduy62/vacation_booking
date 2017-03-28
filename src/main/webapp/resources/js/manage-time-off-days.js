$(document).ready(function () {
    token = $("meta[name='_csrf']").attr("content");
    header = $("meta[name='_csrf_header']").attr("content");

    $('#select-year').on('change', chooseYear);
    $('#copy-data-link').on('click', copyData);

    $('.quota-value-column').editable(function (value) {
        if (isValueChanged(value, this.revert)) {
            $(this).find('input').addClass('loadinggif');
            if (isValidateQuotaValue(value)) {
                if (value == NO_LIMIT_STRING)
                    value = "";
                return updateQuotaValue($(this).attr('id'), value, this.revert);
            } else {
                return this.revert;
            }
        } else {
            return value == "" ? NO_LIMIT_STRING : value;
        }
    }, {
        width: "90%",
        placeholder: '',
        onblur: 'submit',
        data: function(value, settings) {
            return value == NO_LIMIT_STRING ? "" : value;
        }
    });

    $("select").each(function () {
        $(this).val($(this).find('option[selected]').val());
    });
});

function isValueChanged(value, oldValue) {
    if (oldValue == NO_LIMIT_STRING && value == "") return false;
    return value != oldValue;
}

function getSelectedYear() {
    return $("#select-year").find(":selected").text();
}

function chooseYear() {
    window.location = "manage_time_off_days?year=" + getSelectedYear();
}

function copyData() {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "manage_time_off_days?selectedYear=" + getSelectedYear(),
        dataType: 'json',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            bootbox.alert({
                message: '<span class="text-success">' + data.message + '</span>',
                callback: function () {
                    window.location = "manage_time_off_days?year=" + getSelectedYear();
                }
            });
        },
        error: function (e) {
            alert('error: ' + e);
        }
    });
}

function updateQuotaValue(yearlyQuotaId, newValue, oldValue) {
    var result;
    $.ajax({
        async: false,
        type: "POST",
        contentType: "application/json",
        url: "updateQuotaValue?yearlyQuotaId=" + yearlyQuotaId + "&newValue=" + newValue,
        dataType: 'json',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            if (data.errorCode == 0) {
                result = newValue;
            }
        },
        error: function (e) {
            alert('error: ' + e);
            result = oldValue;
        }
    });
    if (result == null || result == "")
        result = NO_LIMIT_STRING;
    return result;
}

function isValidateQuotaValue(value) {
    if (value == "") return true;
    if (value == NO_LIMIT_STRING) return true;
    if (/^\d+$/.test(value) && value >= 0) {
        if (value > 365)
            bootbox.alert('Please enter a value less than or equals 365.');
        else
            return true;
    } else {
        bootbox.alert('Please enter a empty string or a positive decimal number.');
    }
    return false;
}
