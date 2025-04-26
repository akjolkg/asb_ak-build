var timer33 = null;
function updateTime33(){
	if(timeDiff <= 0) {
		document.location.reload(true);
		clearTimeout(timer33);
		return;
	} 
}
$(document).ready(function (){
	timer33 = setInterval(updateTime33, 1000);
});
