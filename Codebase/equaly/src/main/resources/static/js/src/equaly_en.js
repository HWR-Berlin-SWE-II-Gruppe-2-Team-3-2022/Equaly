window.onload = function(){
    window.onload = document.getElementById('input_text_area').select();
    let out = document.getElementById("output_text_area");
    let clear = document.getElementById("clear-div");

    if (out !== null && out.value !== "") {
        clear.innerHTML = "<form id=\"clear-form\" action=\"/clear\" method=\"post\">\n" +
                          "     <button id=\"clear-button\" name=\"action\" value=\"clear\" type=\"submit\" class=\"btn btn-primary\" style=\"border: none; background-color: rgba(255, 58, 33, 1) !important; width: 100% !important; float: right\">Reset</button>\n" +
                          "</form>";
    } else {
        clear.innerHTML = "";
    }

};

function loadGitHub() {
    window.open('https://github.com/HWR-Berlin-SWE-II-Gruppe-2-Team-3-2022', '_blank').focus();
}