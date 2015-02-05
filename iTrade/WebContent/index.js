var brokerId=0;
var brokerName="";
var selectedFund="";
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
	var query="rest/broker/all/has";
	$.ajax({
		type: "GET",
		url: query,
		data: "",
		cache: false,
		success: function(html) {
		status.innerHTML=html;
		query="rest/broker/all";
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
		});
		//document.getElementById("status").setAttribute("style", "background-color:red");
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
	"<input type=\"submit\" style=\"width:100px; margin-left:10px\"value=\"Back\" onClick=\"brokers();\"></div>"+
	"<input type=\"text\" id=\"balance\"><input type=\"submit\" value=\"Create Account\" id=\"sbalance\" onclick=\"addBroker();\">";
	
	var status = document.getElementById("brokers");
	// Returns successful data submission message when the entered information is stored in database.
	var valid=true;
	//status.innerHTML="";
	if(valid.valueOf()==true){
		//document.getElementById("status").setAttribute("style", "background-color:red");
		var query="rest/policy/all/exclusive/"+id;
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
	selectedFund=fund;
	var status = document.getElementById("brokers");
	var query="rest/fund/query/"+fund;
	$.ajax({
		type: "GET",
		url: query,
		data: "",
		cache: false,
		success: function(html) {
		status.innerHTML = html;
		query="rest/broker/all/has/dropdown"
		$.ajax({
			type: "GET",
			url: query,
			data: "",
			cache: false,
			success: function(html) {
			$("#bselect").html(html);
			}
			});
		}
		});
}

function submitOffer(){
	var broker = $("#bselect").val();
	var type = $("#btype").val();
	var price = $("#sprice").text();
	var shares = $("#shares").val();
	var query = "rest/offer/"+price+"/"+type+"/"+shares+"/"+broker+"/"+selectedFund;
	$.ajax({
		type: "GET",
		url: query,
		data: "",
		cache: false,
		success: function(html) {
		location.reload();
		}
		});
}

function addBroker(){
	var status = document.getElementById("brokers");
	var query="rest/broker/add/"+brokerId+"/"+$("#balance").val();
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