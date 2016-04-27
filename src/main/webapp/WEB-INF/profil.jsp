<%-- 
    Document   : profil
    Created on : 4 avr. 2016, 21:28:07
    Author     : ben
--%>

<%@page import="modeles.Evaluation"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="modeles.Utilisateurs"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% Utilisateurs utilisateurConnecte = (Utilisateurs) request.getSession().getAttribute("utilisateur");
   Utilisateurs utilisateur = (Utilisateurs) request.getAttribute("utilisateurConsulte"); %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Profil de <%= utilisateur.getNomPrenom() %></title>
    </head>
    <body>
        <jsp:include page="banniere.jsp"/>
        <section id="banner">
            <h2>Profil de <% out.println(utilisateur.getNomPrenom());%></h2>
        </section>
        <section class="profil">
            <p><% if (utilisateur.getGenre()==1) {
                        out.print("M. ");
                    } else {
                        out.print("Mme. ");
            }%><%= utilisateur.getNomPrenom() %></p>
            <p><% Date today = new Date();
            long age = today.getTime() - utilisateur.getDate().getTime();
            out.println(age/1000/60/60/24/365 + " ans"); %></p>
            <table>
                <tr>
                    <td>Adresse</td>
                    <td><%= utilisateur.getAdresse() %></td>
                </tr><% if (utilisateur.getEvaluation()>=0) {
                    out.println("<tr>"
                            + "<td>Evaluation</td>"
                            + "<td>" + utilisateur.getEvaluation() + "/10 </td>"
                            + "</tr>"); }
                if (utilisateur == utilisateurConnecte) {
                    String rayon = Integer.toString(utilisateur.getRayon());
                    rayon = rayon.equals("-1") ?
                            "Aucune" : rayon.substring(0, rayon.length()-3) + " km";
                    out.println("<tr>"
                            + "<td>Distance max des tâches proposées</td>"
                            + "<td>"
                            + rayon
                            + "</td>"
                            + "</tr>");
                } %>
            </table><br>
            <% if (utilisateur == utilisateurConnecte) out.println("<a href='./controleur?action=ModifProfil'>Modifier mon profil et mes préférences</a>"); %>
        </section>
        <% if (request.getAttribute("commentaires") != null) {
            ArrayList<Evaluation> listCommentaire = (ArrayList<Evaluation>) request.getAttribute("commentaires");
            if (listCommentaire.size()>0) {
                out.println("<section class='commentairesProfil'>");
                out.println("<table>");
                out.println("<tr>");
                out.println("<td>Date</td>");
                out.println("<td>Commentaire</td>");
                out.println("<td>Note</td>");
                out.println("</tr>");

                for (Evaluation e : listCommentaire) {
                    out.println("<tr>");
                    out.println("<td>" + e.getDate() + "</td>");
                    out.println("<td>" + (e.getCommentaire()==null ? "-" : e.getCommentaire()) + "</td>");
                    out.println("<td>" + e.getEvaluation() + "/10</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                out.println("</section> ");
            }
        }
        %>
    </body>
</html>
