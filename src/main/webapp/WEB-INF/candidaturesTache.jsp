<%-- 
    Document   : candidaturesTache
    Created on : 12 avr. 2016, 13:59:08
    Author     : peynetb
--%>

<%@page import="modeles.TacheAtom"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%  TacheAtom ta = request.getAttribute("tacheAtom") != null ? ((TacheAtom) request.getAttribute("tacheAtom")) : null; %>
<%  ArrayList<String> listeCandidatures = request.getAttribute("candidatures") != null ? (ArrayList<String>) request.getAttribute("candidatures") : null;%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Candidatures à <% out.print(ta != null ? ta.getTitreTacheAtom() : ""); %></title>
    </head>
    <body>
        <jsp:include page="banniere.jsp" />
        <section id="banner">
            <% if (ta != null) {
                    out.println("<h2>Candidatures</h2><br><h2><i>" + ta.getTitreTacheAtom() + "</i></h2>");
                }
            %>
        </section>
        <section class="container">
        <%
            if (listeCandidatures != null && ta != null && listeCandidatures.get(ta.getIdTacheAtom()) != null) {
                out.println("<table id='ficheTacheTableau'>");
                out.println("<tr id='ficheTacheTableauHaut'>");
                out.println("<td>Candidats</td>");
                out.println("<td></td>");
                out.println("</tr");
                for (String s : listeCandidatures) {
                    out.println("<tr>");
                    out.println("<td>" + s + "</td>");
                    out.println("<td><a href='./controleur?action=AccepterCandidature&idTacheAtom=" + ta.getIdTacheAtom()
                            + "&idCandidat=" + s + "'>Accepter</a></td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }
        %>
        </section>
    </body>
</html>
