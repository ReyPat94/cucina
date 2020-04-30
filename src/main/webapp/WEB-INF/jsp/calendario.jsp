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
<div>Calendario lezioni</div>
<br>

<a href="Logout"><button>Logout</button></a>

<a href=""><button>Modifica dati administrador</button></a>

<br>
<hr>
<br>

<ul>
<c:forEach items = "${calendario}" var= "edizioneDTO"> <!-- SISTEMA CON ATTR NOTO -->
<li>
${edizioneDTO.edizione.codice}, ${edizioneDTO.edizione.idCorso},  ${edizioneDTO.edizione.dataInizio},  ${edizioneDTO.edizione.idCorso},
 ${edizioneDTO.edizione.durata}, ${edizioneDTO.edizione.aula}, ${edizioneDTO.edizione.docente}
</li>
<a href='EliminaEdizione?edizione=${edizioneDTO.edizione.codice}'><button type='button'>Elimina edizione</button></a>
<br><br>
<a href='ModificaEdizione?edizione=${edizioneDTO.edizione.codice}'><button type='button'>Modifica edizione</button></a>
<br><br>
</c:forEach>
</ul>
<br><br>

<a href='AccessoPagina?url=jsp/aggiungiEdizione.jsp'><button type='button'>Aggiungi nuova edizione</button></a>
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