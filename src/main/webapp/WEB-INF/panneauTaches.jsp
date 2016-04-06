<%-- 
    Document   : panneauTaches
    Created on : 6 avr. 2016, 21:43:57
    Author     : ben
--%>

<%@page import="modeles.Tache"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tâches</title>
    </head>
    <body>
        <jsp:include page="banniere.jsp"/>
        <section id="banner">
            <table>
                <% Tache tache = (Tache) request.getAttribute("taches");
                if(tache!=null) {
                out.print("<tr>");
                out.print("<td>" + tache.getTitreTache() + "</td>");
                out.print("</tr>");
                }
                %>
            </table>
        </section>
    </body>
</html>
