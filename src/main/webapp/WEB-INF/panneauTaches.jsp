<%-- 
    Document   : panneauTaches
    Created on : 6 avr. 2016, 21:43:57
    Author     : ben
--%>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modeles.TacheAtom"%>
<%@page import="modeles.Tache"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tâches</title>
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
    <jsp:include page="banniere.jsp"/>
    <section id="banner" class="wrapper" style=" color: rgb(202, 202, 202);">
        <h2>Mes tâches</h2>
        <% out.print(request.getAttribute("Message") == null ? ""
                : "<p>" + request.getAttribute("Message") + "</p>"); %>
    </section>
    <section class="container">
        <% ArrayList<Tache> listeTaches = (ArrayList<Tache>) request.getAttribute("taches");
            if (listeTaches != null) {
                if (listeTaches.size() > 0) {
                    HashMap<Integer, Integer> candidatures = (HashMap<Integer, Integer>) request.getAttribute("candidatures");
                    out.println("<table id='tableauMesTaches'>");
                    for (Tache t : listeTaches) {
                        out.print("<tr>");
                        out.print("<td><a href='./controleur?action=voirTache&idTache=" + t.getIdTache() + "'>" + t.getTitreTache() + "</a></td>");
                        if (t.getTaches() != null) {
                            if (t.getTaches().size() > 1) {
                                out.print("<td><table id='tableauMesTachesAtom'>");
                                for (TacheAtom ta : t.getTaches()) {
                                    out.println("<tr><td>" + ta.getTitreTacheAtom() + "</td>");
                                    if (ta.getIndicateurFin()==1) {
                                        out.println("<td> Tâche finie </td><td><a href='./controleur?action=Facture&idTacheAtom=" + ta.getIdTacheAtom() + "' target='_blank'>Facture</a></td>");
                                    } else {
                                        if(ta.getEmailExecutant() != null) {
                                            out.println("<td><a href='./controleur?action=FinDeTache&idTacheAtom=" + ta.getIdTacheAtom() + "&idCandidat=" + ta.getEmailExecutant() + "' onclick='return confirmFinTache(this);'>Tâche finie ?</a></td><td></td>");
                                        } else {
                                            out.println(candidatures != null ?
                                                    (candidatures.get(ta.getIdTacheAtom())!= null ?
                                                        "<td><a href='./controleur?action=voirCandidaturesTache&idTacheAtom=" + ta.getIdTacheAtom() + "'>" + candidatures.get(ta.getIdTacheAtom()) + " candidature"
                                                                + (candidatures.get(ta.getIdTacheAtom())>1 ? "s" : "") + "</a></td>"
                                                            : "<td></td>")
                                                    : "<td></td>");
                                            out.println("<td><a href='./controleur?action=SupprimerTacheAtom&idTacheAtom=" + ta.getIdTacheAtom() + "' onclick='return confirmSupp(this);'>Supprimer</a></td>");
                                        }
                                    }
                                }
                                out.println("</table></td>");
                            } else {
                                out.println("<td><table id='tableauMesTachesAtom'><tr>");
                                out.println("<td></td>");
                                if (t.isOver()) {
                                    out.println("<td> Tâche finie </td><td><a href='./controleur?action=Facture&idTacheAtom=" + t.getTaches().get(0).getIdTacheAtom() + "' target='_blank'>Facture</a></td>");
                                } else {
                                    out.println(candidatures != null ?
                                            (candidatures.get(t.getTaches().get(0).getIdTacheAtom())!= null ?
                                                "<td><a href='./controleur?action=voirCandidaturesTache&idTacheAtom=" + t.getTaches().get(0).getIdTacheAtom() + "'>" + candidatures.get(t.getTaches().get(0).getIdTacheAtom()) + " candidature"
                                                        + (candidatures.get(t.getTaches().get(0).getIdTacheAtom())>1 ? "s" : "") + "</a></td>"
                                                    : "<td></td>")
                                            : "<td></td>");
                                    if(t.getTaches().get(0).getEmailExecutant() != null) {
                                        out.println("<td><a href='./controleur?action=FinDeTache&idTacheAtom="
                                                + t.getTaches().get(0).getIdTacheAtom() + "&idCandidat="
                                                + t.getTaches().get(0).getEmailExecutant()
                                                + "' onclick='return confirmFinTache(this);'>Tâche finie</a></td>");
                                    } else {
                                        out.println("<td><a href='./controleur?action=SupprimerTacheAtom&idTacheAtom="
                                                + t.getTaches().get(0).getIdTacheAtom()
                                                + "' onclick='return confirmSupp(this);'>Supprimer</a></td>");
                                    }
                                }
                                out.println("</tr></table></td>");
                            }
                        }
                        if (t.isOver() || t.estEntamee()) {
                            out.println("<td></td>");
                        } else {
                            out.println("<td><a class ='button' href='./controleur?action=SupprimerTache&idTache="
                                    + t.getIdTache() + "' onclick='return confirmSupp(this);'>Supprimer</a></td>");
                        }
                        out.println("</tr>");
                    }
                    out.println("</table>");
                } else {
                    out.println("<p>Aucune tâche proposée.<p>");
                }
            } else {
                out.println("<p>Aucune tâche proposée.<p>");
            }
        %>
    </section>
</body>
</html>
