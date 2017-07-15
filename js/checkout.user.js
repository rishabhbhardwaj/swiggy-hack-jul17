// ==UserScript==
// @name        Checkout popup
// @include     https://stackoverflow.com/tags
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
		First name: <input id="input_PUT" type="text" value="Enter Pick up Time">
		<button id="check" type="button" >Check</button>	
	</div>
	</div>                                                                   
`);

$("#check").click ( function () {
    console.log(document.getElementById("input_PUT").value);
} );

function openNav() {
	console.log("in here");
	document.getElementById("myNav").style.width = "100%";
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
