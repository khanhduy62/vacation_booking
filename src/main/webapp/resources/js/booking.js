$(function () {
    $('#from').datetimepicker({
        format: 'DD/MM/YYYY'
    });

    $('#to').datetimepicker({
        format: 'DD/MM/YYYY',
        useCurrent: false //Important! See issue #1075
    });

    $("#from").on("dp.change", function (e) {
        $('#to').data("DateTimePicker").minDate(e.date);
        getWorkingDays();
    });

    $("#to").on("dp.change", function (e) {
        $('#from').data("DateTimePicker").maxDate(e.date);
        getWorkingDays();
    });
});

function getWorkingDays(){
    var from = $('#from').data("DateTimePicker").date();
    var to = $('#to').data("DateTimePicker").date();

    var fromDate = new Date(from);
    var endDate = new Date(to);

    if (to) {
        $.ajax({
            url: "working-days",
            dataType: "text",
            data: {
                fromDate: fromDate,
                endDate: endDate,
            },
            method: "GET",
            async: true,
            success: function (result) {
                $('#days').html(result);
            },
            error: function (xhr, status, error) {
            }
        });
    }
}
