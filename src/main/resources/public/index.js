function displayJson(json, div) {
    for (const key of Object.keys(json)) {
        if (key == "Ratings") {
            div.innerHTML += "Ratings: "
            for (const ratingKey of Object.keys(json[key])) {
                div.innerHTML += json[key][ratingKey]["Source"] + ": " + json[key][ratingKey]["Value"] + ", ";
            }
            div.innerHTML += "<br/>"
        } else {
            div.innerHTML += key + ": " + json[key] + "<br/>";
        }
    }
}

function loadPostMsg(name){
    let movie;
    let url = "/form?name=" + name.value;
    fetch (url, {method: 'POST'})
        .then(response => response.json())
        .then(y => {
            let msg = document.getElementById("postrespmsg");
            msg.innerHTML = "";
            console.log(y);
            displayJson(y, msg);
    }
}