<%-- 
    Document   : panneauTaches
    Created on : 6 avr. 2016, 21:43:57
    Author     : ben
--%>

<%@page import="modeles.TacheAtom"%>
<%@page import="modeles.Tache"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tâches</title>
    </head>
        <jsp:include page="banniere.jsp"/>
        <section id="banner" class="wrapper" style=" color: rgb(202, 202, 202);">
            <h2>Vos tâches</h2>
            <div class="container">
            <div class=""table-wrapper">
            <table class="alt">
                <% Tache tache = (Tache) request.getAttribute("taches");
                if(tache!=null) {
                    for(TacheAtom t : tache.getTaches()) {
                        out.print("<tr>");
                        out.print("<td>" + t.getTitreTacheAtom()+ "</td>");
                        out.print("<td>" + t.getDescription()+ "</td>");
                        out.print("<td>" + t.getPrix()+ "€ </td>");
                        out.print("<td>" + t.getDateTot()+ "</td>");
                        out.print("<td>" + t.getDatetard()+ "</td>");
                        out.println("</tr>");
                    }
                }
                %>
            </table>
        </div>
            </div>
        </section>
    </body>
</html>
