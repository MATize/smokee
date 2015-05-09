<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c" %>
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Smoker Client</title>
		<script id="jquery" src=".${servletContext}/static/js/jquery-2.1.3.min.js" ></script>
		<link rel="stylesheet" type="text/css" href=".${servletContext}/static/css/smoker.css" />
		 <script>
			$( document ).ready(function() {
				$.ajaxSetup( { async: false } );
				$("#login").click(
					function(event) {
						event.preventDefault();
						$.ajax(
							{
								cache: false,
								crossDomain: false,
								headers: {
									"${apiKeyHeaderName}": "${apikey}"
								},
								dataType: "json",
								url: "${smokerUrl}/login",
								type: "POST",
								data: {
									"user":$("#user").val(),
									"pass":$("#pass").val()
								},
								success: function(login_result, login_status, login_xhr) {
									$("#loginform").hide();
									$("#resultlist").show();
									$.ajax(
										{
											cache: false,
											crossDomain: false,
											headers: {
												"${apiKeyHeaderName}": "${apikey}",
												"${authTokenHeaderName}": login_result.auth_token
											},
											dataType: "json",
											url: "${smokerUrl}/tests",
											type: "GET",
											success: function(test_result, test_status, test_xhr) {
												$("#results").html(JSON.stringify(test_result));
											}
										}
									);
								}
							}
						);
							
					}
				);
			});
		</script>
	</head>
	<body>
		<div id="loginform">
			<h1>Smoker Login</h1>
			<form action="${smokerUrl}/login" method="post">
				<fieldset class="hidden">
					<legend>API</legend>
					<label for="apikey">Key</label> <input type="text" name="apikey"
						id="apikey" value="${apikey}" class="form input apikey" /><br />
				</fieldset>
				<fieldset>
					<legend>Credentials</legend>
	
					<label for="user">User</label> <input type="text" name="user"
						id="user" value="" class="form input" /><br /> <label for="pass">Password</label>
					<input type="password" name="pass" id="pass" value=""
						class="form input" /><br /> <label for="login">&#160;</label> <input
						type="button" value="Login" id="login" name="login"
						class="form input button" />
				</fieldset>
			</form>
		</div>
		<div id="resultlist">
			<h1>Test results</h1>
			<div id="results"></div>
		</div>
	</body>
</html>