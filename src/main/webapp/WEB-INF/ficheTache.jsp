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
<% Utilisateurs utilisateur = request.getSession(false).getAttribute("utilisateur") != null ? (Utilisateurs) request.getSession(false).getAttribute("utilisateur") : null; %>
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
            function confirmFinTache(){
                if(confirm("Marquer la tâche comme finie ?")===true) {
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
            <% if (t != null && utilisateur != null) {
                    out.println("<h2>" + t.getTitreTache() + "</h2>");
                    out.println("<span id='ficheTacheCommanditaire'>proposée par "
                            + "<a href='./controleur?action=Profil&utilisateurConsulte="
                            + t.getEmailCommanditaire() + "'><i>" + t.getEmailCommanditaire()
                            + "</i></a></span>");
                    if (t.getEmailCommanditaire().equals(utilisateur.getEmail())
                            && !t.estEntamee()) out.println("<a href='./controleur?action=SupprimerTache&idTache=" + t.getIdTache() + "' onclick='return confirmSupp(this);'>Supprimer</a>");
                }
            %>
        </section>
        <section class="container 125%">
            <% if (t != null && utilisateur != null) {
                    boolean tUnique = false;
                    out.println("<table id='ficheTacheTableau'>");
                    out.println("<tr id='ficheTacheTableauHaut'>\n<td>");
                    out.println("Titre</td>\n<td>");
                    out.println("Description</td>\n<td>");
                    out.println("Lieu</td>\n<td>");
                    out.println("Réalisation au plus tôt le</td>\n<td>");
                    out.println("Réalisation au plus tard le</td>\n<td>");
                    out.println("Récompense</td>\n<td>");
                    out.println("Compétences nécessaires</td>\n");
                    if (t.getEmailCommanditaire().equals(utilisateur.getEmail())) {
                        out.println("<td>Exécutant</td>");
                    }
                    out.println("\n</tr>");
                    if (t.getTaches().size()==1) tUnique=true;
                    for (TacheAtom ta : t.getTaches()) {
                        out.println("<tr>\n<td>");
                        out.println(ta.getTitreTacheAtom() + "</td>\n<td>");
                        out.println(ta.getDescription() + "</td>\n<td>");
                        out.println(ta.getAdresse() + "</td>\n<td>");
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
                        if (ta.getEmailCommanditaire().equals(utilisateur.getEmail())) {
                            if (ta.getEmailExecutant() == null) {
                                HashMap<Integer, Integer> candidatures = (HashMap<Integer, Integer>) request.getAttribute("candidatures");
                                out.println(candidatures != null ?
                                        (candidatures.get(ta.getIdTacheAtom()) != null ?
                                                "<td><a href='./controleur?action=voirCandidaturesTache&idTacheAtom=" + ta.getIdTacheAtom() + "'>"
                                                        + candidatures.get(ta.getIdTacheAtom()) + " candidature"
                                        + (candidatures.get(ta.getIdTacheAtom()) > 1 ? "s" : "") + "</a></td>"
                                        : "<td>0 candidature</td>")
                                        : "<td>0 candidature</td>");
                                if (!tUnique) out.println("<td><a href='./controleur?action=SupprimerTacheAtom&idTacheAtom=" + ta.getIdTacheAtom() + "' onclick='return confirmSupp(this);'>Supprimer</a></td></tr>");
                            } else {
                                out.println("<td><a href='./controleur?action=Profil&utilisateurConsulte="
                                        + ta.getEmailExecutant() + "'>" + ta.getEmailExecutant() + "</a></td>");
                                if (ta.getIndicateurFin()==1) {
                                    out.println("<td> Tâche finie </td><td><a href='./controleur?action=Facture&idTacheAtom=" + ta.getIdTacheAtom() + "' target='_blank'>Facture</a></td>");
                                } else {
                                    out.println("<td><a href='./controleur?action=FinDeTache&idTacheAtom="
                                        + ta.getIdTacheAtom() + "&idCandidat=" + ta.getEmailExecutant() + "' onclick='return confirmFinTache(this);'>Tâche finie ?</a></td></tr>");
                                }
                            }
                        } else {
                            if (ta.getEmailExecutant() == null) {
                                HashSet candidatures = (HashSet) request.getAttribute("candidatures");
                                out.print("<td><a href='./controleur?action=");
                                if (candidatures.contains(ta.getIdTacheAtom())) {
                                    out.println("Depostuler&idTacheAtom="
                                            + ta.getIdTacheAtom() + "'>Retirer ma candidature</a></td></tr>");
                                } else {
                                    out.println("Postuler&idTacheAtom="
                                            + ta.getIdTacheAtom() + "'>Postuler</a></td></tr>");
                                }
                            } else if (ta.getIndicateurFin()==1
                                    && (ta.getEmailCommanditaire().equals(utilisateur.getEmail())
                                        || ta.getEmailExecutant().equals(utilisateur.getEmail()))) {
                                out.println("<td> Tâche finie </td><td><a href='./controleur?action=Facture&idTacheAtom=" + ta.getIdTacheAtom() + "' target='_blank'>Facture</a></td>");
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