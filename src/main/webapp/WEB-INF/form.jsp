<%-- 
    Document   : form
    Created on : 3 avr. 2016, 14:38:05
    Author     : ralambom
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<p><% out.print(request.getAttribute("erreurMessage")==null ? "" : request.getAttribute("erreurMessage")); %></p>
<form method="post" accept-charset="utf-8" action="./controleur">
                <div class="container 50%">
                    <input name="nom" id="nom"
                           value="<% out.print(request.getAttribute("nom")==null ? "" : request.getAttribute("nom"));%>" placeholder="Nom" type="text" autofocus required>
                    <input name="prenom" id="prenom"
                           value="<% out.print( request.getAttribute("prenom")==null ? "" : request.getAttribute("prenom"));%>" placeholder="Prenom" type="text" required>
                    <input type="radio" id="genre2" name="genre" value="2" required <% out.print( request.getAttribute("genre")!=null ? ( (int) request.getAttribute("genre")==2 ? "checked" : "") : "" ); %>><label for="genre2">Femme</label>
                    <input type="radio" id="genre1" name="genre" value="1" required <% out.print( request.getAttribute("genre")!=null ? ( (int) request.getAttribute("genre")==1 ? "checked" : "") : "" ); %>><label for="genre1">Homme</label>
                    <input name="adresse" id="adresse"
                           value="<% out.print( request.getAttribute("adresse")==null ? "" : request.getAttribute("adresse"));%>" placeholder="Adresse postale" type="text" required>
                    <input name="date" id="date"
                           value="<% out.print( request.getAttribute("date")==null ? "" : request.getAttribute("date"));%>" placeholder="Date de naissance" type="date" required>
                    <label style="color:whitesmoke"> La distance maximale des offres qui me sont proposées : </label>
                    <select name ="rayon" style="background-color:grey;" >
                        <option value="10">  10 km </option>
                        <option value="100"> 100 km </option>
                        <option value="1000"> 1000 km </option>
                        <option value="-1" selected> non spécifiée </option>
                    </select>
                    <label style="color:whitesmoke"> Quelles sont vos compétences ? </label>
                     
                    <% int a = 0; %>
                    <c:forEach items ="${usrCompetences}" var = "element">
                        <input id="id<%=a%>" name="${element.nomCompetence}" type="checkbox" checked="checked"> <label for="id<%=a%>">${element.nomCompetence}</label>
                        <% a = a + 1 ;%>
                    </c:forEach>
                    
                    <% a = 0; %>
                    <c:forEach items ="${competences}" var = "element">
                        <input id="id<%=a%>" name="${element.nomCompetence}" type="checkbox" > <label for="id<%=a%>">${element.nomCompetence}</label>
                        <% a = a + 1 ;%>
                    </c:forEach>
                    
                    <input name="email" id="email"
                           value="<% out.print( request.getAttribute("email")==null ? "" : request.getAttribute("email"));%>" placeholder="Email" type="email" required>
                    <input name="mdp" id="mdp" value="" placeholder="Mot de passe" type="password" required>
                    <input name="mdpconfirm" id="mdpconfirm" value="" placeholder="Confirmer le mot de passe" type="password" required>
                    <input id="conditions" name="conditions" type="checkbox" required>
                    <label for="conditions">J'ai lu et j'accepte les <a href="./CU.html" target="_blank">conditions générales d'utilisation</a> du service CrowdHelping.</label>
                    <ul class="actions">
                        <li><input value="Validation" class="special" type="submit" name="action"></li>
                    </ul>
                </div>
            </form>
