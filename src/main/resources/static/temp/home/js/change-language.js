$(document).ready(function () {
    $("#locale").change(function () {
        var selectedOption = $('#locale').val();
        if (selectedOption != '') {
            window.location.replace('?lang=' + selectedOption);
        }
    });
});