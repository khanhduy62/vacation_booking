function Approve(requestId){
    swal({
            title: "Are you sure?",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: 'Yes, approve it!',
            closeOnConfirm: false
        },
        function(){
            swal("Approval!", "You have approved!", "success");
            add(requestId);
            $('#reject'+requestId).remove();
        });
}

function Reject(requestId){
    swal({
            title: "Are you sure?",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: 'Yes, reject it!',
            closeOnConfirm: false
        },
        function(){
            swal("Reject!", "You have rejected!", "success");
            remove(requestId);
        });

}
function remove(requestId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var data = {
        approved : false,
        requestId : requestId
    };
    $.ajax({
        type : 'POST',
        url : 'updateApprove',
        contentType: "application/json",
        data: JSON.stringify(data),
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success : function(response) {
            console.log("SUCCESS: ", response);
            $("#result").load("listPendingRequest");
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}


function add(requestId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var data = {
        approved : true,
        requestId : requestId
    };
    $.ajax({
        type : 'POST',
        url : 'updateApprove',
        contentType: "application/json",
        data: JSON.stringify(data),
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success : function(response) {
            console.log("SUCCESS: ", response);
            $("#result").load("listPendingRequest");
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}