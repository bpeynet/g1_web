<%-- 
    Document   : ficheTache
    Created on : 10 avr. 2016, 15:24:01
    Author     : ben
--%>

<%@page import="java.util.HashSet"%>
<%@page import="modeles.outils.Competences"%>
<%@page import="modeles.TacheAtom"%>
<%@page import="modeles.Tache"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><% out.print(request.getAttribute("tache")!=null ? ((Tache)request.getAttribute("tache")).getTitreTache() : "Tache non spécifiée"); %></title>
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
                HashSet candidatures = (HashSet) request.getAttribute("candidatures");
                out.println("<table id='ficheTacheTableau'>");
                out.println("<tr id='ficheTacheTableauHaut'>\n<td>");
                out.println("Titre</td>\n<td>");
                out.println("Description</td>\n<td>");
                out.println("Réalisation au plus tôt le</td>\n<td>");
                out.println("Réalisation au plus tard le</td>\n<td>");
                out.println("Récompense</td>\n<td>");
                out.println("Compétences nécessaires</td>\n<td hidden>");
                out.println("Postuler</td>\n</tr>");
            for (TacheAtom ta : ((Tache) request.getAttribute("tache")).getTaches()) {
                out.println("<tr>\n<td>");
                out.println(ta.getTitreTacheAtom() + "</td>\n<td>");
                out.println(ta.getDescription() + "</td>\n<td>");
                out.println(ta.getDateTot() + "</td>\n<td>");
                out.println(ta.getDatetard()+ "</td>\n<td>");
                out.println(ta.getPrix()+ "&euro;</td>\n<td>");
                if (ta.getCompetences().size()>0) {
                    for (Competences c : ta.getCompetences()) {
                        out.println(c.getNomCompetence() + "<br>");
                    }
                } else {
                    out.println("Pas de compétence particulière attendue");
                }
                out.print("</td><td><a href='./controleur?action=");
                if (candidatures.contains(ta.getIdTacheAtom())) {
                    out.println("Depostuler&idTacheAtom=" +
                            ta.getIdTacheAtom() + "'>Dépostuler</a></td></tr>");
                } else {
                    out.println("Postuler&idTacheAtom=" + 
                            ta.getIdTacheAtom() + "'>Postuler</a></td></tr>");
                }
            }
            out.println("</table>");
          }
        %>
    </section>
