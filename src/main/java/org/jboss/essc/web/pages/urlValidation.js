function getHTTPObject(){ 
    // Create a boolean variable to check for a valid Internet Explorer instance.
    var xmlhttp = false;
    // Check if we are using IE.
    try {
        // If the Javascript version is greater than 5.
        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
        // If not, then use the older active x object.
        try {
            // If we are using Internet Explorer.
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (E) {
            // Else we must be using a non-IE browser.
            xmlhttp = false;
        }
    }
    // If we are using a non-IE browser, create a javascript instance of the object.
    if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
        xmlhttp = new XMLHttpRequest();
    }
    return xmlhttp;
}

function isValidURL( url ){
    var pattern = /^(ftp|http|https):\/\//;
    return pattern.test(url);
}

function isWorkingURL( url ){
    xmlhttp = getHTTPObject();
    xmlhttp.open("HEAD", url,true);
    xmlhttp.onreadystatechange = function() {
        if( true || xmlhttp.readyState == 4 ) {
            if (xmlhttp.status == 200) alert("URL Exists!")
            else if (xmlhttp.status == 404) alert("URL doesn't exist!")
            else alert("Status of "+url+" is " + xmlhttp.status)
        }
    }
    xmlhttp.send(null)
}
