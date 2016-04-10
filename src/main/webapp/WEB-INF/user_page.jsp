<%-- 
    Document   : user_page
    Created on : 3 avr. 2016, 14:24:47
    Author     : ralambom
--%>

<%@page import="modeles.TacheAtom"%>
<%@page import="modeles.Tache"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<jsp:include page="banniere.jsp"/>

<section id="banner">
    <% out.print(request.getAttribute("succesMessage") == null ? ""
                : "<p>" + request.getAttribute("succesMessage") + "</p>"); %>
</section>
<div class="container">
    <section>
        <%
            if (request.getAttribute("taches") != null) {
                ArrayList<Tache> rs = (ArrayList<Tache>) request.getAttribute("taches");
                if (rs.size() > 0) {
                    out.println("<br><h2>Quelques tâches</h2>\n\t<div  class='cards'>\n\t\t<table id='cardtable'>");
                    int largeur = 0;
                    for (Tache t : rs) {
                        if (largeur == 0) {
                            out.println("<tr>");
                        }
                        out.println("<td><a href='./controleur?action=voirTache&idTache=" + t.getIdTache() + "'>");
                        out.print("<span class='titreTache'>" + t.getTitreTache()+"</span><br>");
                        out.print("proposé par <i>" + t.getEmail() + "</i>");
                        if (t.getTaches().size()>1) {
                            out.print("<hr><table id='card'>");
                            for (TacheAtom ta : t.getTaches()) {
                                out.println("<tr><td>" + ta.getTitreTacheAtom() + "</td><td>");
                                //out.println(ta.getDescription() + "<br>");
                                out.println(ta.getPrix() + "&euro;</td></tr>");
                            }
                            out.print("</table>");
                        } else {
                            out.println("<br>" + t.getTaches().get(0).getPrix() + "&euro;<br>");
                        }
                        out.println("</a></td>");
                        if (largeur == 2) {
                            out.println("</tr>");
                            largeur = 0;
                        }
                        largeur++;
                    }
                    out.println("</tr></table></div>");
                }
            }
        %>
    </section>
</div>
</body>
</html>