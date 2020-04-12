var pageLoaded = false;
window.addEventListener('beforeunload', function (e) { 
	getInitialPageValues(false);
});

window.onload  = function(){
}

function changeValueOfPit(id, pieceCount) {
	document.getElementById("pit"+id).innerText  = pieceCount;
}

function changeValueOfAllPits(pitList, nextPlayer) {
	pitList.forEach(pit => {
		changeVisibilityOfPit(pit.id, nextPlayer);
		changeValueOfPit(pit.id, pit.pieceCount);
		if(pit.pieceCount === 0){
			document.getElementById("pit"+pit.id).disabled = true;
		}
	})
}

function changeVisibilityOfPit(id, nextPlayer) {
	if(nextPlayer === "Player 1" && id<6){
		buttonVisibilityChange("pit"+id, false);
	} else if (nextPlayer === "Player 2" && id<6){
		buttonVisibilityChange("pit"+id, true);
	} else if (nextPlayer === "Player 1" && id>6 && id<13){
		buttonVisibilityChange("pit"+id, true);
	} else if (nextPlayer === "Player 2" && id>6 && id<13){
		buttonVisibilityChange("pit"+id, false);
	}
}

function buttonVisibilityChange(id, isVisible) {
	document.getElementById(id).disabled = isVisible;
}

function doPageAdjustments(data){
	
	changeValueOfAllPits(data.pitList, data.nextPlayer);
	isGameOver(data);
}

function isGameOver(data){
	// buttonVisibilityChange("startButtonId",!(data.gameOver));
	if(data.gameOver){
		for(var i=0; i<14; i++){
			buttonVisibilityChange("pit"+i, true);
			
		}
		var message = "";
		if(data.pitList[6].pieceCount === data.pitList[13].pieceCount){
			message = "Game is Over...It is a tie game!!!";
		} else if (data.pitList[6].pieceCount > data.pitList[13].pieceCount){
			message = "Game is Over...Player 1 wins!!! Congratulations to Player 1";
		} else {
			message = "Game is Over...Player 2 wins!!! Congratulations to Player 2";
		}
		
		alert(message);
	}
	else {
		document.getElementById("currentPlayerMessage").innerHTML = data.nextPlayer + " 's turn..." ;
	}
}
function myFunction() {
	    return confirm("Do you want to restart the game?");
}

function getURLtoLoadPitsFirstTime(){
	if(pageLoaded){
		if (myFunction()){
			return 'http://localhost:8080/replayGame';
		}
		else{
			return '';
		}
	}else{
		return 'http://localhost:8080/new';
	}
}

function newGame() {
	getInitialPageValues(true);
}

function getInitialPageValues(loadFromNewGameButton){
	var request = new XMLHttpRequest()

	var URL = '';
	if(loadFromNewGameButton){
		URL = getURLtoLoadPitsFirstTime()
	} else{
		URL = 'http://localhost:8080/replayGame';
	}
	 
	if(URL ===''){
		return;
	}
	// Open a new connection, using the GET request on the URL endpoint
	request.open('GET', URL, true)

	request.onload = function() {
	  // Begin accessing JSON data here
	  var data = JSON.parse(this.response)
	
	  if (request.status >= 200 && request.status < 400) {
	     doPageAdjustments(data);
	  }
	   else {
	    console.log('error')
	  }
	}
	
	request.send()
	
	pageLoaded = true;
}

function withdrawSelectedPit(id){
	var request = new XMLHttpRequest()

	// Open a new connection, using the GET request on the URL endpoint
	request.open('GET', 'http://localhost:8080/withdraw/'+id, true)
	
	request.onload = function() {
	  // Begin accessing JSON data here
	  var data = JSON.parse(this.response)
	
	  if (request.status >= 200 && request.status < 400) {
	     doPageAdjustments(data);
	  }
	   else {
	    console.log('error')
	  }
	}

	request.send()
	
}

function button_0_ClickAction(){
	withdrawSelectedPit(0)
}
function button_1_ClickAction(){
	withdrawSelectedPit(1)
}
function button_2_ClickAction(){
	withdrawSelectedPit(2)
}
function button_3_ClickAction(){
	withdrawSelectedPit(3)
}
function button_4_ClickAction(){
	withdrawSelectedPit(4)
}
function button_5_ClickAction(){
	withdrawSelectedPit(5)
}
function button_7_ClickAction(){
	withdrawSelectedPit(7)
}
function button_8_ClickAction(){
	withdrawSelectedPit(8)
}
function button_9_ClickAction(){
	withdrawSelectedPit(9)
}
function button_10_ClickAction(){
	withdrawSelectedPit(10)
}
function button_11_ClickAction(){
	withdrawSelectedPit(11)
}
function button_12_ClickAction(){
	withdrawSelectedPit(12)
}
