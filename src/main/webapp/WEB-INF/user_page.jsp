<%-- 
    Document   : user_page
    Created on : 3 avr. 2016, 14:24:47
    Author     : ralambom
--%>

<%@page import="java.util.Collection"%>
<%@page import="java.util.HashMap"%>
<%@page import="modeles.TacheAtom"%>
<%@page import="modeles.Tache"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<jsp:include page="banniere.jsp"/>

<section id="banner">
    <% out.print(request.getAttribute("succesMessage") == null ? ""
                : "<p>" + request.getAttribute("succesMessage") + "</p>"); %>
<div class="container">
        
        <%if (request.getAttribute("tachesCommanditaire") != null) {
                ArrayList<Tache> rs = (ArrayList<Tache>) request.getAttribute("tachesCommanditaire");
                if (rs.size() > 0) {
                    out.println("<br><h2>Mes offres</h2>\n\t<div  class='cards'>\n\t\t<table id='cardtable'>");
                    int largeur = 0;
                    for (Tache t : rs) {
                        if (largeur == 0) {
                            out.println("<tr>");
                        }
                        out.println("<td><a href='./controleur?action=voirTache&idTache=" + t.getIdTache() + "'>");
                        out.print("<span class='titreTache'>" + t.getTitreTache()+"</span><br>");
                        out.print("proposé par <i>" + t.getEmailCommanditaire()+ "</i>");
                        if (t.getTaches().size()>1) {
                            out.print("<hr><table id='card'>");
                            for (TacheAtom ta : t.getTaches()) {
                                out.println("<tr><td>" + ta.getTitreTacheAtom() + "</td><td>");
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
        <%if (request.getAttribute("tachesEnCours") != null) {
                ArrayList<TacheAtom> rs = (ArrayList<TacheAtom>) request.getAttribute("tachesEnCours");
                if (rs != null) {
                    out.println("<br><h2>Les tâches que j'effectue</h2>\n\t<div  class='cards'>\n\t\t<table id='cardtable'>");
                    int largeur = 0;
                    for (TacheAtom ta : rs) {
                        if (largeur == 0) {
                            out.println("<tr>");
                        }
                        out.println("<td><a href='./controleur?action=voirTache&idTache=" + ta.getIdTacheMere() + "'>");
                        out.print("<span class='titreTache'>" + ta.getTitreTacheAtom()+"</span><br>");
                        out.print("proposé par <i>" + ta.getEmailCommanditaire()+ "</i>");
                        out.println("<br>" + ta.getPrix() + "&euro;<br>");
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
</div>
</section>
<section>
    <div class="container">
         <%if (request.getAttribute("tachesExecutant") != null) {
                HashMap<Integer,TacheAtom> rs = (HashMap<Integer,TacheAtom>) request.getAttribute("tachesExecutant");
                
                if (rs.size() > 0) {
                    out.println("<br><h2> Candidatez ! </h2>\n\t<div  class='cards'>\n\t\t<table id='cardtable'>");
                    int largeur = 0;
                    Collection<TacheAtom> result = rs.values();
                    for (TacheAtom t : result) {
                        if (largeur == 0) {
                            out.println("<tr>");
                        }
                        out.println("<td><a href='./controleur?action=voirTache&idTache=" + t.getIdTacheAtom() + "'>");
                        out.print("<span class='titreTache'>" + t.getTitreTacheAtom()+"</span><br>");
                        out.print("proposé par <i>" + t.getEmailCommanditaire() + "</i>");
                        out.println("<br>" + t.getPrix() + "&euro;<br>");
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
        
        
    </div> 
    </section>
</body>
</html>