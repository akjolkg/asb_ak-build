/**
 * authour: Akzholbek Omorov
 */

function handleFileSelect(event) {
	var reader = new FileReader();
	var files = event.target.files;
	reader.onload = (function(theFile) {
		return function(e) {
			alert(e.target.result);
        };
    })(files[0]);
	
	reader.readAsText(files[0], "UTF-8");
}