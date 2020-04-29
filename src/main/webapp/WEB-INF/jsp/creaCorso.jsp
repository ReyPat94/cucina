<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@  taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Crea corso</title>
</head>
<body>

	<p>Crea un nuovo Corso</p>

	<form action='CreaCorso'>
		<table>
			<tr>
				<td><label for="nome">Nome corso:</label></td>
				<td><input type="text" id="nome" name="nome"></td>
			</tr>
			<tr>
				<td><label for="cat">Categoria:</label></td>
				<td>
				<c:forEach items = "${categorie}" var= "categoria">
				<input type="radio" id="${categoria.idCategoria }" name="idCategoria" value="${categoria.idCategoria}">
				<label>${categoria.descrizione}</label>
				</c:forEach>
				</td>
			</tr>
			<tr>
				<th><input type="submit" value="Inserisci"></th>
			</tr>
		</table>
	</form>

</body>
</html>