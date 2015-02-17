var brokerId = 0;
var brokerName = "";
var selectedFund = "";
var style ="";
function ifunds(){
	style = document.getElementById("container").getAttribute("style");
	funds();
}
function funds() {
	setFolderColor("#F1AB00");
	var status = document.getElementById("brokers");
	var query = "rest/fund/all";
	ajax(query, function(html) {
		status.innerHTML = html;
	});
}

function brokers() {
	var status = document.getElementById("brokers");
	var valid = true;
	var text;
	setFolderColor("#CD1E10");
	var query = "rest/broker/all/has";
	ajax(query, function(html) {
		text = html;
		query = "rest/broker/all";
		ajax(query, function(html) {
			status.innerHTML = text+html;
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
	document.getElementById("brokers").innerHTML = "<div id=\"brokername\">"
			+ brokerName
			+ "</br><input type=\"submit\" style=\"width:100px;\"value=\"Back\" onClick=\"brokers();\"></div></br>"
			+ "<div style=\"text-align: center;\"><div id=\"curbal\"></div><input type=\"text\" id=\"balance\">" +
					"<input type=\"submit\" value=\"Create Account\" style=\"width:120px;\" id=\"sbalance\" onclick=\"addBroker();\"></div>";
	var query = "rest/policy/all/exclusive/" + id;
	$("#curbal").text("Balance:" +$("#balance").text());
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
	$("#eprice").text("Estimated price: "+$("#shares").val()*$("#sprice").text());
}

function userData(){
	var status = document.getElementById("brokers");
	setFolderColor("#FADF00");
	status.innerHTML = "";
	var query = "rest/fund/user";
	ajax(query, function(html) {
		status.innerHTML = html;
	});
}
function equity(){
	setFolderColor("#007E3A");
	var status = document.getElementById("brokers");
	var query = "rest/fund/user/funds";
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

function setFolderColor(color){
	document.getElementById("container").setAttribute("style",style+ "background-color:"+color);
}

function brokerSelect(value){
	alert($("#b"+value).val()+value)
}