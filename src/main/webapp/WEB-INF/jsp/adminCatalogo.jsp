<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Crea categoria</title>
</head>
<body>

<p>Inserisci il nome della nuova categoria

<form action='CreaCategoria'>
  <label for="descr">Nome nuova categoria:</label><br>
  <input type="text" id="descr" name="descr"><br>
   <input type="submit" value="Inserisci">
</form>

<br>
<br>
<br>
<br>
<hr>
<br>
<br>
<br>
<a href='AccessoPagina?url=jsp/index.jsp'><button type='button'>Back to Index</button></a>

</body>
</html>