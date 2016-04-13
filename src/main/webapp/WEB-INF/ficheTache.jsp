<%-- 
    Document   : ficheTache
    Created on : 10 avr. 2016, 15:24:01
    Author     : ben
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="modeles.Utilisateurs"%>
<%@page import="dao.UtilisateurDAO"%>
<%@page import="java.util.HashSet"%>
<%@page import="modeles.outils.Competences"%>
<%@page import="modeles.TacheAtom"%>
<%@page import="modeles.Tache"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% Tache t = request.getAttribute("tache") != null ? (Tache) request.getAttribute("tache") : null; %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><% out.print(t != null ? t.getTitreTache() : "Tache non spécifiée"); %></title>
        <script language="javascript">
            function confirmSupp(){
                if(confirm("Supprimer la tâche ?")===true) {
                    return true;
                } else {
                    return false;
                }
            }
        </script>   
    </head>
    <body>
        <jsp:include page="banniere.jsp" />
        <section id="banner">
            <% if (t != null) {
                    out.println("<h2>" + t.getTitreTache() + "</h2>");
                    out.println("<span id='ficheTacheCommanditaire'>proposé par <i>" + t.getEmailCommanditaire() + "</i></span>");
                    out.println("<a href='./controleur?action=SupprimerTache&idTache=" + t.getIdTache() + "' onclick='return confirmSupp(this);'>Supprimer</a>");
                }
            %>
        </section>
        <section class="container">
            <% if (t != null) {
                    out.println("<table id='ficheTacheTableau'>");
                    out.println("<tr id='ficheTacheTableauHaut'>\n<td>");
                    out.println("Titre</td>\n<td>");
                    out.println("Description</td>\n<td>");
                    out.println("Réalisation au plus tôt le</td>\n<td>");
                    out.println("Réalisation au plus tard le</td>\n<td>");
                    out.println("Récompense</td>\n<td>");
                    out.println("Compétences nécessaires</td>\n<td>");
                    if (t.getEmailCommanditaire().equals(((Utilisateurs) request.getSession(false).getAttribute("utilisateur")).getEmail())) {
                        out.println("Exécutant");
                    }
                    out.println("</td>\n</tr>");
                    for (TacheAtom ta : t.getTaches()) {
                        out.println("<tr>\n<td>");
                        out.println(ta.getTitreTacheAtom() + "</td>\n<td>");
                        out.println(ta.getDescription() + "</td>\n<td>");
                        out.println(ta.getDateTot() + "</td>\n<td>");
                        out.println(ta.getDatetard() + "</td>\n<td>");
                        out.println(ta.getPrix() + "&euro;</td>\n<td>");
                        if (ta.getCompetences().size() > 0) {
                            for (Competences c : ta.getCompetences()) {
                                out.println(c.getNomCompetence() + "<br>");
                            }
                        } else {
                            out.println("Pas de compétence particulière attendue</td>");
                        }
                        if (ta.getEmailCommanditaire().equals(((Utilisateurs) request.getSession(false).getAttribute("utilisateur")).getEmail())) {
                            if (ta.getEmailExecutant() == null) {
                                HashMap<Integer, Integer> candidatures = (HashMap<Integer, Integer>) request.getAttribute("candidatures");
                                out.println(candidatures != null ?
                                        (candidatures.get(ta.getIdTacheAtom()) != null ?
                                                "<td><a href='./controleur?action=voirCandidaturesTache&idTacheAtom=" + ta.getIdTacheAtom() + "'>"
                                                        + candidatures.get(ta.getIdTacheAtom()) + " candidature"
                                        + (candidatures.get(ta.getIdTacheAtom()) > 1 ? "s" : "") + "</a></td>"
                                        : "<td>0 candidature</td>")
                                        : "<td>0 candidature</td>");
                            } else {
                                out.println("<td>" + ta.getEmailExecutant() + "</td>");
                            }
                            out.println("<td><a href='./controleur?action=SupprimerTacheAtom&idTacheAtom=" + ta.getIdTacheAtom() + "' onclick='return confirmSupp(this);'>Supprimer</a></td></tr>");
                        } else {
                            HashSet candidatures = (HashSet) request.getAttribute("candidatures");
                            if (ta.getEmailExecutant() == null) {
                                out.print("<td><a href='./controleur?action=");
                                if (candidatures.contains(ta.getIdTacheAtom())) {
                                    out.println("Depostuler&idTacheAtom="
                                            + ta.getIdTacheAtom() + "'>Retirer ma candidature</a></td></tr>");
                                } else {
                                    out.println("Postuler&idTacheAtom="
                                            + ta.getIdTacheAtom() + "'>Postuler</a></td></tr>");
                                }
                            } else {
                                out.println("</tr>");
                            }
                        }
                    }
                    out.println("</table>");
                }
            %>
        </section>
    </body>
</html>