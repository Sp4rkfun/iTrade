<!DOCTYPE html>
<%@page import="iTrade.User"%>
<html>
<% String username = (String)request.getSession().getAttribute("user"); %>
<head>
<script type="text/javascript" src="index.js"></script>
<link rel="stylesheet" href="index.css">
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
</head>
<body onload="myFunction();">
<% if(username==null){%>
	<div>Username<input class="create" id="user" type="text">Password:<input class="create" id="pass" type="password"><input class="create" type="submit" value="Sign In" onclick="login();"></div>
	<% 
}
else{%>
	<div>Hello, <%= username %>
	<input class="create" type="submit" value="Sign Out" onclick="logout();">
	</div>
	<div>Balance: <%= User.getBalance(request) %></div>
<%}
%>

<a href="broker.html">Create A Broker</a>
<a href="policy.html">Create A Policy</a>
<a href="register.html">Sign up!</a><br>

<div style="position: relative; height: 90%; width: 90%; margin: auto;background: green; border-radius:10px;">
<div style="width: 100%; position: relative; display: inline-block; background: cadetblue;">
<div class="tabs" onclick="myFunction();">Funds</div>
<div class="tabs" onclick="brokers();">Brokers</div>
<div class="tabs" onclick="funds();">Equity</div>
<div class="tabs" onclick="userData();">Transactions</div>
</div>
<div id="brokers"></div></div>
</body>

</html>