<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<%--JavaScript --%>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!--     <script defer> 
   $('#options').change(function allYears(){
        $.getJSON("/JSONuserpageAnni", function(anniList){
            var ul = $("<div>").appendTo($("#anno"));
            $.each(anniList, function(index, item) {
            $("<button type='button' name = 'choice' value = " + item + " >").text(item).appendTo(ul);
            })
        });
    });
     </script> -->

<title>bienvenido amigo!</title>

</head>
<body>
<div>Weilà, bienvenido amigo!</div>


<a href=""><button>Logout</button></a> <br>
<a href=""><button>I corsi ai quali sei iscritta/o</button></a><br>

Visualizza i corsi per:
<select id="options" onchange="allYears()">
  <option > Seleziona </option>
  <option value="anno" onclick="allYears();">Anno</option>
  <option value="cat">Categoria</option>
  <option value="costo">Costo</option>
  <option value="altrox">Altro</option>
</select>
<br>

<div id="anno"></div>						  	<%-- collega a "options" --%> 

<hr>
<p>Scheda corso selezionato con ajax</p> 
<a href=""><button>Iscriviti</button></a>
<hr>
<hr>
<p>Feedback corso selezionato con ajax</p> 
<a href=""><button>Iscriviti</button></a>
<hr>
<hr>
<p>Calendario</p> 
<a href=""><button>Iscriviti</button></a>
<hr>

<a href=""><button>Modifica dati utente</button></a>




<!-- 6.	Visualizza catalogo corsi (per diverse modalità: nell anno in corso, per categoria ecc) -->
<!-- 7.	Visualizza scheda singolo corso -->
<!-- 8.	Visualizza feedback di un singolo corso -->
<!-- 9.	Visualizza info corso (docente, numero partecipanti ecc..) UGUALE ALLA 7-->   
<!-- 10. Visualizza lista dei corsi a calendario per categoria -->
<!-- 11. Visualizza lista dei corsi a calendario per range data -->
<!-- 12. Iscrizione ad un corso -->
<!-- 13. Annullamento iscrizione ad un corso -->
<!-- 14. Inserimento feedback corso (solo se si è iscritti al corso ed il corso è terminato) -->
<!-- 15. Modifica/cancella feedback -->


      <script>
   function allYears(){
        $.getJSON("JSONuserpageAnni", function(anniList){
            $.each(anniList, function(index, item) {
            $("<button type='button' name = 'choice' value = " + item + " >").text(item).appendTo("#anno");
            })
        });
    }
    </script>




</body>
</html>