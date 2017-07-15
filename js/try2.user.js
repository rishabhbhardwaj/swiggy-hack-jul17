


// ==UserScript==
// @name        Checkout popup
// @include     https://example.com/
// @require     http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js
// @grant       GM_addStyle
// ==/UserScript==
/*- The @grant directive is needed to work around a design change
    introduced in GM 1.0.   It restores the sandbox.
*/
//--- Use jQuery to add the form in a "popup" dialog.

$("body").append (`                                                         
    <div id="myNav" class="overlay">
		<div class="overlay-content">
			<select id="input_PUT" onChange="OnSelectedIndexChange()></select>
			<button id="check" type="button" >Check</button>	
		</div>
	</div>                                                                   
`);

$("#check").click ( function () {
    console.log(document.getElementById("input_PUT").value);
} );

function OnSelectedIndexChange(){
	
}
function populateSelectOptions(){
	var select = document.getElementById('input_PUT');
	var pickUpTime = parseInt(getPickUpTimeValue());
	var max_time = '1350';
	var size = (parseInt(max_time) - pickUpTime)/15;
	for(var i = 0 ; i <=size ; i++){
			var time = pickUpTime + (i*15);
			var time_string = Math.floor(time/60) + ":" + ((time%60 == 0) ? '00' : time%60);
			var opt = document.createElement('option');
			opt.value = time_string;
			opt.innerHTML = time_string;
			select.appendChild(opt);
			//select.options[i] = new Option(time.toString(), time.toString()); 
	}
}

function getPickUpTimeValue(){
	
	//var xmlHttp = new XMLHttpRequest();
	var url = 'http://172.16.120.154:8082/pickup/?pickUpLat=18.22&pickUpLng=19.22&restaurantId=22';
    //xmlHttp.open( "GET", url, false ); // false for synchronous request
    //xmlHttp.send( null );
	//console.log(xmlHttp.responseText);
    //console.log(xmlHttp.responseText.bestPickUpTime);
	//return xmlHttp.responseText.bestPickUpTime;
	
	$.get( url, function( data ) {
	console.log(data);
	  //alert( "Data Loaded: " + data );
	});
	return 1050;
}

function openNav() {
	document.getElementById("myNav").style.width = "100%";
	populateSelectOptions();
}

function closeNav() {
    document.getElementById("myNav").style.width = "0%";
}

openNav();
//--- CSS styles make it work...
GM_addStyle ( `
	.body{
		margin: 0;
		font-family: 'Lato', sans-serif;
	}
	
    .overlay {                                                  
    height: 100%;
    width: 0;
    position: fixed;
    z-index: 1;
    top: 0;
    left: 0;
    background-color: rgb(0,0,0);
    background-color: rgba(0,0,0, 0.9);
    overflow-x: hidden;
    transition: 0.5s;
	}

	.overlay-content {
		position: relative;
		top: 25%;
		width: 100%;
		text-align: center;
		margin-top: 30px;
	}

	.overlay a {
		padding: 8px;
		text-decoration: none;
		font-size: 36px;
		color: #818181;
		display: block;
		transition: 0.3s;
	}

	.overlay a:hover, .overlay a:focus {
		color: #f1f1f1;
	}

	.overlay .closebtn {
		position: absolute;
		top: 20px;
		right: 45px;
		font-size: 60px;
	}

	@media screen and (max-height: 450px) {
	  .overlay a {font-size: 20px}
	  .overlay .closebtn {
		font-size: 40px;
		top: 15px;
		right: 35px;
	  }
	}                                                      
` );
