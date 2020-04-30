<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@  taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div>Hweilà, bienvenido administrador!</div>
	<br>

	<a href="Logout"><button>Logout</button></a>

	<a href=""><button>Modifica dati administrador</button></a>

	<br>
	<hr>
	<br>
	<p>${messaggio}</p>
<hr>

	<h2>Catalogo</h2>
	<hr>
	<br>
	<ul>
		<c:forEach var="i" begin="1" end="${categorie.size()}">

			<li>${categorie.get(i-1).descrizione}
				<ul>
					<c:forEach var="j" begin="1" end="${corsi.get(i-1).size()}">
						<li>${corsi.get(i-1).get(j-1).titolo}
								<a href='Calendario?corso=${corsi.get(i-1).get(j-1).codice}'><button type='button'>Visualizza edizioni corso</button></a> 
								<a href='ModificaCorso?set=first&corsoCodice=${corsi.get(i-1).get(j-1).codice}'><button type='button'>Modifica corso</button></a>
						</li>
					</c:forEach>
				</ul>
			</li>

		</c:forEach>
	</ul>
	<hr>
	<br>
	<a href='AccessoPagina?url=jsp/creaCategoria.jsp'><button
			type='button'>Crea nuova categoria</button></a>
	<br>
	<a href='AccessoPagina?url=jsp/creaCorso.jsp'><button type='button'>Crea
			nuovo Corso</button></a>



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