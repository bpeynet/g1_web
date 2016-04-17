<%-- 
    Document   : evaluations
    Created on : 14 avr. 2016, 08:56:14
    Author     : ralambom
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Evaluation</title>
    </head>
    
    <jsp:include page="banniere.jsp"/>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
    <section id="banner">
    <% out.print(request.getAttribute("erreurMessage") == null ? ""
                : "<p>" + request.getAttribute("erreurMessage") + "</p>"); %>
    <%-- TODO : un message d'erreur quand on évalue sans note --%>
         
        <form method="post" accept-charset="utf-8" action="./controleur">
        <div class="container 50%">
            <p> Evaluation sur 10</p>
            <% Integer i;
                for(i = 0; i<11; i++) {
                    out.println("<input type=\"radio\" id=\"note" + i.toString() +"\" name=\"note\" value=\"" + i.toString() +"\" required><label for=\"note" + i.toString() +"\">"+ i.toString() +"</label>");
                }
            %>
            <input id="commentaire" name="commentaire" value="" placeholder="Commentaire" type="text"><br>
            
            <c:set scope="session" var="idTacheAtom" value="${tache.idTacheAtom}" />
            
            
            <ul class="actions">
                        <li><input value="Evaluer" class="special" type="submit" name="action"></li>
                        <li><input value="Facture" class="special" type="submit" name="action"></li>
                    </ul>
        </div>
        </form>
    </section>
</body>
</html>