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
                    out.println("<div  class='card'><table>");
                    int largeur = 0;
                    for (Tache t : rs) {
                        if (largeur == 0) {
                            out.println("<tr>");
                        }
                        out.println("<td>");
                        //out.print("<a class='button fit special'>");
                        out.print("<strong>" + t.getTitreTache()+"</strong><br>");
                        //out.println("</a>");
                        //out.print("<a class='button fit'>");
                        out.print("<i>" + t.getEmail() + "</i>");
                        //out.println("</a>");
                        if (t.getTaches().size()>1) {
                            out.print("<hr>");
                            for (TacheAtom ta : t.getTaches()) {
                                out.println(/*"<a class='button fit special'>" +*/ ta.getTitreTacheAtom() /*+ "</a>"*/ + "<br>");
                            }
                        }
                        out.println("</td>");
                        if (largeur == 3) {
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