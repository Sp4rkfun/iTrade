var brokerId=0;
var brokerName="";
function myFunction() {
var status = document.getElementById("brokers");
// Returns successful data submission message when the entered information is stored in database.
var valid=true;
status.innerHTML="";
if(valid.valueOf()==true){
	//document.getElementById("status").setAttribute("style", "background-color:red");
	var query="rest/fund/all";
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

function brokers() {
	var status = document.getElementById("brokers");
	// Returns successful data submission message when the entered information is stored in database.
	var valid=true;
	status.innerHTML="";
	if(valid.valueOf()==true){
		//document.getElementById("status").setAttribute("style", "background-color:red");
		var query="rest/broker/all";
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

function login(){
	var query="rest/user/"+$("#user").val()+"/"+$("#pass").val();
	$.ajax({
		type: "GET",
		url: query,
		data: "",
		cache: false,
		success: function(html) {
		location.reload()
		}
		});
}

function logout(){
	var query="rest/user/logout";
	$.ajax({
		type: "GET",
		url: query,
		data: "",
		cache: false,
		success: function(html) {
		location.reload()
		}
		});
}

function policies(id, name) {
	brokerId=id;
	brokerName=name;
	$("#label1").text("Type");
	$("#label2").text("Frequency");
	$("#label3").text("Condition");
	document.getElementById("brokers").innerHTML="<div>"+brokerName+
	"<input type=\"submit\" style=\"width:100px; margin-left:10px\"value=\"Back\" onClick=\"brokers();\"></div>";
	var status = document.getElementById("brokers");
	// Returns successful data submission message when the entered information is stored in database.
	var valid=true;
	//status.innerHTML="";
	if(valid.valueOf()==true){
		//document.getElementById("status").setAttribute("style", "background-color:red");
		var query="rest/policy/all/"+id;
		$.ajax({
			type: "GET",
			url: query,
			data: "",
			cache: false,
			success: function(html) {
			status.innerHTML+=html;
			}
			});
	}
	return false;
	}

function selectFund(fund){
	var status = document.getElementById("brokers");
	var query="rest/fund/query/"+fund;
	$.ajax({
		type: "GET",
		url: query,
		data: "",
		cache: false,
		success: function(html) {
		status.innerHTML = html;
		}
		});
}