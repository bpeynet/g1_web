<%-- 
    Document   : ficheTache
    Created on : 10 avr. 2016, 15:24:01
    Author     : ben
--%>

<%@page import="modeles.outils.Competences"%>
<%@page import="modeles.TacheAtom"%>
<%@page import="modeles.Tache"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <jsp:include page="banniere.jsp" />
    <section id="banner">
        <% if (request.getAttribute("tache")!=null) {
            out.println("<h2>" + ((Tache)request.getAttribute("tache")).getTitreTache() + "</h2>");
            out.println("<span id='ficheTacheCommanditaire'>proposé par <i>" + ((Tache)request.getAttribute("tache")).getEmail() + "</i></span>");
          }
        %>
    </section>
    <section class="container">
        <% if (request.getAttribute("tache")!=null) {
            out.println("<table id='ficheTacheTableau'>");
            for (TacheAtom ta : ((Tache) request.getAttribute("tache")).getTaches()) {
                out.println("<tr>\n<td>");
                out.println(ta.getTitreTacheAtom() + "</td><td>");
                out.println(ta.getDescription() + "</td><td>");
                out.println(ta.getDateTot() + "</td><td>");
                out.println(ta.getDatetard()+ "</td><td>");
                out.println(ta.getPrix()+ "&euro;</td><td>");
                if (ta.getCompetences().size()>0) {
                    for (Competences c : ta.getCompetences()) {
                        out.println(c.getNomCompetence() + "<br>");
                    }
                } else {
                    out.println("Pas de compétence particulière attendue");
                }
                out.println("<td><td><a href='./controleur?action=Postuler&idTacheAtom=" +
                        ta.getIdTacheAtom() + "'>Postuler</a></td></tr>\n</td>");
            }
          }
        %>
    </section>
