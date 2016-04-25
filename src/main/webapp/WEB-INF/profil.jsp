<%-- 
    Document   : profil
    Created on : 4 avr. 2016, 21:28:07
    Author     : ben
--%>

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
                            + "</tr>"); } %>
            </table><br>
            <% if (utilisateur == utilisateurConnecte) out.println("<a href='./controleur?action=ModifProfil'>Modifier votre profil</a>"); %>
        </section>
    </body>
</html>
