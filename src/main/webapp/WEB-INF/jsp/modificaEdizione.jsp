<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Calendario lezioni</title>
<%@  taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
</head>
<body>
<div>Modifica Edizione</div>
<br>

<a href="Logout"><button>Logout</button></a>

<a href=""><button>Modifica dati administrador</button></a>

<br>
<hr>
<br>
						<form action='/modificaEdizione' method='get'> <!-- PATH CORRETTO -->
							<div>
								<label for='usrn'> ID Corso </label> <input id='usrn'
									value='${userID}' name='userID' required>
							</div>				
							<div>
								<label for='birthdate'> Data di inizio </label> <input
									id='dataDiInizio' type="date" name='dataDiInizio'
									value='${dataDiInizio}' required>
							</div>
							<div>
								<label for='usrn'> Durata </label> <input id='usrn'
									value='${durata}' name='durata' required>
							</div>
							<div>
								<label for='usrn'> Aula </label> <input id='usrn'
									value='${aula}' name='aula' required>
							</div>
							<div>
								<label for='usrn'> Docente </label> <input id='usrn'
									value='${docente}' name='docente' required>
							</div>
								<button type="submit" class="button">Modifica Edizione</button>
						</form>
<br><br>

<br>
<br>
<hr>
<br>
<br>
<br>
<a href='AccessoPagina?url=jsp/index.jsp'><button type='button'>Back to Index</button></a>
</body>
</html>