<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>

<p>
${errore}</p>

	<h1>Welcome to Login</h1>
	<form action="Login" method="post">
		<label for="usrn">Username</label> <input id="usrn" name="usrn">
		<br>
		<br>
		<br> <label for="Password">Password</label> <input id="pswd" name="pswd" type="password"> 
			<br><input type="radio" id="user" name="accesstype" value="user" checked><label for="admin">User</label><br>
		<input type="radio" id="admin" name="accesstype" value="admin">
		<label for="fadmin">Admin</label><br>
		
	<button type='submit'>Invia</button>
	</form>
	<br>
	<br>
	<br>
	<br>
	<hr>
	<br>
	<br>
	<br>
	<a href='AccessoPagina?url=jsp/index.jsp'><button type='button'>Back
			to Index</button></a>

</body>
</html>