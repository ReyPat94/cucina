<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@  taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Gestione corso</title>
</head>
<body>

<p>${messaggio}</p>
<hr>

	<p>Visualizza corso ${corso.codice}</p>

	<form action='ModificaCorso'>
		<table>
			<tr style="display:none">
			<td><input readonly="text" id="corsoCodice" name="corsoCodice" value="${corso.codice}" required></td>
			</tr>
			<tr>
				<td><label for="nome">Nome corso:</label></td>
				<td><input type="text" id="nome" name="nome" value="${corso.titolo}" required></td>
			</tr>
			<tr>
				<td><label for="cat">Categoria:</label></td>
				<td width= 60%>
				<c:forEach items = "${categorie}" var= "categoria">
				<c:choose>
				<c:when test="${corso.idCategoria == categoria.idCategoria}">
				<input checked required type="radio" id="${categoria.idCategoria}" name="idCategoria" value="${categoria.idCategoria}">
				<label>${categoria.descrizione}</label>
				</c:when>
				<c:otherwise>
				<input required type="radio" id="${categoria.idCategoria}" name="idCategoria" value="${categoria.idCategoria}">
				<label>${categoria.descrizione}</label>
				</c:otherwise>
				</c:choose>
				</c:forEach>
				</td>
			</tr>
			<tr>
				<td><label for="maxP">Numero massimo partecipanti:</label></td>
				<td><input type="number" id="maxP" name="maxP" min="1" value="${corso.maxPartecipanti}" required></td>
			</tr>
			<tr>
				<td><label for="costo">Costo:</label></td>
				<td><input type="number" id="costo" name="costo" min="1" value="${corso.costo}" required></td>
			</tr>
			<tr>
				<td><label for="descr">Descrizione:</label></td>
				<td><textarea id="descr" name="descr" rows="10" cols=80% required>${corso.descrizione}</textarea></td>
			</tr>
			<tr>
				<th><input type="submit" value="Modifica"></th>
			</tr>
		</table>
	</form>
	
		<br>
	<br>
	<hr>
	<br>
	<a href='AccessoPagina?url=jsp/adminpage.jsp'><button type='button'>Back
			to Admin Page</button></a>
	<br>
	<br>
	<a href='AccessoPagina?url=jsp/index.jsp'><button type='button'>Back
			to Index</button></a>

</body>
</html>