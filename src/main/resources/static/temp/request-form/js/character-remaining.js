var area = document.getElementById("describtion");
var message = document.getElementById("message");
var char = document.getElementById("characters-remaining")
var maxLength = 10;
var checkLength = function() {
    if(area.value.length <= maxLength) {
        message.innerHTML = (maxLength-area.value.length) + " " +  char.innerText;
    }
}
setInterval(checkLength, 300);