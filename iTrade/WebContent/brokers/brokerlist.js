var brokerId=0;
function brokers() {
var status = document.getElementById("brokers");
// Returns successful data submission message when the entered information is stored in database.
var valid=true;
status.innerHTML="";
if(valid.valueOf()==true){
	//document.getElementById("status").setAttribute("style", "background-color:red");
	var query="../rest/broker/all";
	$.ajax({
		type: "GET",
		url: query,
		data: "",
		cache: false,
		success: function(html) {
		status.innerHTML=html;
		}
		});
}
return false;
}
function policies(id) {
	brokerId=id;
	var status = document.getElementById("brokers");
	// Returns successful data submission message when the entered information is stored in database.
	var valid=true;
	status.innerHTML="";
	if(valid.valueOf()==true){
		//document.getElementById("status").setAttribute("style", "background-color:red");
		var query="../rest/policy/all/"+id;
		$.ajax({
			type: "GET",
			url: query,
			data: "",
			cache: false,
			success: function(html) {
			status.innerHTML=html;
			}
			});
	}
	return false;
	}
function addToBroker(id) {
	var status = document.getElementById("brokers");
	// Returns successful data submission message when the entered information is stored in database.
	var valid=true;
	status.innerHTML="";
	if(valid.valueOf()==true){
		//document.getElementById("status").setAttribute("style", "background-color:red");
		var query="../rest/policy/"+brokerId+"/"+id;
		$.ajax({
			type: "GET",
			url: query,
			data: "",
			cache: false,
			success: function(html) {
			status.innerHTML=html;
			}
			});
	}
	return false;
	}
function removeFromBroker(id) {
	var status = document.getElementById("brokers");
	// Returns successful data submission message when the entered information is stored in database.
	var valid=true;
	status.innerHTML="";
	if(valid.valueOf()==true){
		//document.getElementById("status").setAttribute("style", "background-color:red");
		var query="../rest/policy/remove/"+brokerId+"/"+id;
		$.ajax({
			type: "GET",
			url: query,
			data: "",
			cache: false,
			success: function(html) {
			status.innerHTML=html;
			}
			});
	}
	return false;
	}

function createBroker() {
	var name = document.getElementById("name").value;
	var limit = document.getElementById("limit").value;
	var time = document.getElementById("time").value;
	// Returns successful data submission message when the entered information is stored in database.
	var valid=true;
	if(name==''){
		valid=false;
		status.innerHTML+="Enter a Name!<br\>"
	}
	if(limit==''||limit<10){
		limit=10;
	}
	if(time==''||time<10){
		time=10;
	}
	if(valid.valueOf()==true){
		//document.getElementById("status").setAttribute("style", "background-color:red");
		if(brokerId==0)
		var query="../rest/broker/"+name+"/"+limit+"/"+time;
		else{
			query="../rest/policy/"+name+"/"+limit+"/"+time;
		}
		$.ajax({
			type: "GET",
			url: query,
			data: "",
			cache: false,
			success: function(html) {
				if(brokerId==0)
				brokers();
				else
					policies(brokerId);
			}
			});
	}
	return false;
	}