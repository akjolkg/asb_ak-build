function onPrinterClick() {
	var cols =document.getElementsByClassName('not-show');
  	for(i=0; i<cols.length; i++) {
  		cols[i].style.display ='none';
  	}
}

function onPrinterComplete() {
	var cols =document.getElementsByClassName('not-show');
	for(i=0; i<cols.length; i++) {
		cols[i].style.display ='block';
	}
}