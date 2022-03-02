window.onload = function(){
    window.onload = document.getElementById('input_text_area').select();
    let out = document.getElementById("output_text_area");
    let clear = document.getElementById("clear-div");

    if (out !== null && out.value !== "") {
        clear.innerHTML = "<form id=\"clear-form\" action=\"/clear\" method=\"post\">\n" +
                          "     <button id=\"clear-button\" name=\"action\" value=\"clear\" type=\"submit\" class=\"btn btn-primary\" style=\"border: none; background-color: rgba(230,48,27,0.8) !important; width: 100% !important; float: right\">Zurücksetzen</button>\n" +
                          "</form>";
    } else {
        clear.innerHTML = "";
    }

};

function loadGitHub() {
    window.open('https://github.com/HWR-Berlin-SWE-II-Gruppe-2-Team-3-2022', '_blank').focus();
}