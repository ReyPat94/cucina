<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>bienvenido amigo!</title>
</head>
<body>
<div>Hweil�, bienvenido amigo!</div>

<a href="">Welcome, ${myUser.userName}!</a>
<a href="/Logout"><button>Logout</button></a>
<br><br>
<a href=""><button>Le mie Edizioni</button></a>
<a href=""><button>Catalogo</button></a>
<a href=""><button>Modifica dati utente</button></a>
</body>
</html>