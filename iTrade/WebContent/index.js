function myFunction() {
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