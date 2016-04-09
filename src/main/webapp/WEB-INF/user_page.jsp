<%-- 
    Document   : user_page
    Created on : 3 avr. 2016, 14:24:47
    Author     : ralambom
--%>

<%@page import="modeles.Tache"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<jsp:include page="banniere.jsp"/>

<section id="banner">
    <% out.print(request.getAttribute("succesMessage")==null ? "" :
            "<p>" + request.getAttribute("succesMessage") + "</p>"); %>
</section>
<section id="one">
    <%
        if (request.getAttribute("taches") != null) {
            ArrayList<Tache> rs = (ArrayList<Tache>) request.getAttribute("taches");
            if (rs.size() > 0) {
                out.print("<table>");
                int largeur = 3;
                for (Tache t : rs) {
                    if(largeur == 3) {
                        out.print("<tr>");
                    }
                    out.print("<td>");
                    out.print("<a class='button'>");
                    out.print(t.getTitreTache());
                    out.print("</a>");
                    out.print("</td>");
                    if (largeur == 3) {
                        out.print("</tr>");
                        largeur = 0;
                    }
                    largeur++;
                }
                out.print("</table>");
            }
        }
    %>
</section>
</body>
</html>