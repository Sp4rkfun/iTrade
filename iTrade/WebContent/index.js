var brokerId = 0;
var brokerName = "";
var selectedFund = "";
function myFunction() {
	var status = document.getElementById("brokers");
	status.innerHTML = "";
	var query = "rest/fund/all";
	ajax(query, function(html) {
		status.innerHTML = html;
	});
}

function brokers() {
	var status = document.getElementById("brokers");
	var valid = true;
	status.innerHTML = "";
	var query = "rest/broker/all/has";
	ajax(query, function(html) {
		status.innerHTML = html;
		query = "rest/broker/all";
		ajax(query, function(html) {
			status.innerHTML += html;
		});
	});
}

function login() {
	var query = "rest/user/" + $("#user").val() + "/" + $("#pass").val();
	ajax(query, function(html) {
		location.reload();
	});
}

function logout() {
	var query = "rest/user/logout";
	ajax(query, function(html) {
		location.reload()
	});
}

function policies(id, name) {
	brokerId = id;
	brokerName = name;
	$("#label1").text("Type");
	$("#label2").text("Frequency");
	$("#label3").text("Condition");
	document.getElementById("brokers").innerHTML = "<div>"
			+ brokerName
			+ "<input type=\"submit\" style=\"width:100px; margin-left:10px\"value=\"Back\" onClick=\"brokers();\"></div>"
			+ "<input type=\"text\" id=\"balance\"><input type=\"submit\" value=\"Create Account\" id=\"sbalance\" onclick=\"addBroker();\">";
	var query = "rest/policy/all/exclusive/" + id;
	ajax(query, function(html) {
		var status = document.getElementById("brokers");
		status.innerHTML += html;
	});
}

function selectFund(fund) {
	selectedFund = fund;
	var status = document.getElementById("brokers");
	var query = "rest/fund/query/" + fund;
	ajax(query, function(html) {
		status.innerHTML = html;
		query = "rest/broker/all/has/dropdown"
		ajax(query, function(html) {
			$("#bselect").html(html);
		})
	});
}

function submitOffer() {
	var broker = $("#bselect").val();
	var type = $("#btype").val();
	var price = $("#sprice").text();
	var shares = $("#shares").val();
	var query = "rest/offer/" + price + "/" + type + "/" + shares + "/"
			+ broker + "/" + selectedFund;
	ajax(query, function(html) {
		location.reload();
	});
}

function addBroker() {
	var query = "rest/broker/add/" + brokerId + "/" + $("#balance").val();
	ajax(query, function(html) {
		location.reload();
	});
}



function adjustEstimate(){
	
}

function userData(){
	var status = document.getElementById("brokers");
	status.innerHTML = "";
	var query = "rest/fund/user";
	ajax(query, function(html) {
		status.innerHTML = html;
	});
}
function ajax(query, callback) {
	$.ajax({
		type : "GET",
		url : query,
		data : "",
		cache : false,
		success : function(html) {
			callback(html);
		}
	});
}