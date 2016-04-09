<%-- 
    Document   : ajouter
    Created on : 4 avr. 2016, 14:41:58
    Author     : hargec
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nouvelle tâche</title>
        <script src="js/jquery.min.js"></script>
        <script src="js/skel.min.js"></script>
        <script src="js/skel-layers.min.js"></script>
        <script src="js/init.js"></script>
    </head>
    <body class="landing">
        <%-- TODO : ajouter required pour chaque champ --%>
        <script language=javascript>
            var j = 1,i;
            var l = 1; // permet d'afficher 2 taches atomiques au minimum pour un projet
            var numeroDeTache = 1;
            var barres;
            var bloc_tache;
            function change() {
                if (j==1) {
                    document.getElementById("projet").style.display="";
                    document.getElementById("projetName").disabled=false;
                    document.getElementById("boutonAjoutTache").style.display="";
                    document.getElementsByClassName("titre")[0].placeholder="Titre de la première tâche";
                    barres = document.getElementsByClassName("hr")
                    for (i=0; i<barres.length; i++) {
                        barres[i].style.display="";
                    }
                    bloc_tache = document.getElementsByClassName("tache");
                    for (i=1; i<bloc_tache.length; i++){
                        bloc_tache[i].style.display="";
                    }
                    if (l==1) {
                        ajoutTache();
                    }
                    l=0;
                    j=0;
                } else {
                    document.getElementById("projet").style.display='none';
                    document.getElementById("projetName").disabled=true;
                    document.getElementById("boutonAjoutTache").style.display='none';
                    document.getElementsByClassName("titre")[0].placeholder="Titre de la tâche";
                    barres = document.getElementsByClassName("hr")
                    for (i=0; i<barres.length; i++) {
                        barres[i].style.display="";
                    }
                    bloc_tache = document.getElementsByClassName("tache");
                    for (i=1; i<bloc_tache.length; i++){
                        bloc_tache[i].style.display="none"
                    }
                    j=1;
                }
                return true;
            }
            function ajoutTache() {
                numeroDeTache++;
                var x = document.getElementsByClassName("tache")[0];
                var clonedElement = x.cloneNode(true);
                var b = document.createElement("button");
                b.appendChild(document.createTextNode("Supprimer cette tâche"));
                b.setAttribute("type","button");
                b.setAttribute("onclick","this.parentElement.remove();correction()");
                clonedElement.appendChild(b);
                clonedElement.insertBefore(document.createElement("hr"),clonedElement.childNodes[0]);
                document.getElementById("Taches").appendChild(clonedElement);
                document.getElementsByClassName("titre")[numeroDeTache-1].setAttribute("placeholder", "Titre de la " + numeroDeTache +"ème tâche");
                
            }
            Element.prototype.remove = function() {
                this.parentElement.removeChild(this);
            }
            NodeList.prototype.remove = HTMLCollection.prototype.remove = function() {
                for(var i = this.length - 1; i >= 0; i--) {
                    if(this[i] && this[i].parentElement) {
                        this[i].parentElement.removeChild(this[i]);
                    }
                }
            }
            function correction() {
                var blocsTache = document.getElementsByClassName("tache");
                var j;
                for (var i = 1; i<blocsTache.length; i++){
                    j=i+1;
                    document.getElementsByClassName("titre")[i].setAttribute("placeholder", "Titre de la " + j +"ème tâche");
                    document.getElementsByClassName("titre")[i].setAttribute("name", "titre" + j);
                    document.getElementsByClassName("SoonestDate")[i].setAttribute("name", "SoonestDate" + j);
                    document.getElementsByClassName("LatestDate")[i].setAttribute("name", "LatestDate" + j);
                    document.getElementsByClassName("prix")[i].setAttribute("name", "prix" + j);
                    document.getElementsByClassName("description")[i].setAttribute("name", "description" + j);
                }
            }
        </script>
        <section id="banner">
            <h2>Ajouter ..?</h2>
            <div class="container 50%">
                <form id="formAjoutTache" action="controleur" method="post" accept-charset="UTF-8">
                    <div id="Taches">
                        <input type="radio" name="typeTache" value="TUnique" id="TUnique" checked onchange="change()">
                        <label for="TUnique"><h3 style="color:whitesmoke">une tâche</h3></label>
                        <input type="radio" name="typeTache" value="TComplexe" id="TComplexe" onchange="change()">
                        <label for="TComplexe"><h3 style="color:whitesmoke">un projet</h3></label>
                        <div id="projet" style="display:none">
                            <input type="text" id="projetName" name="projetName" placeholder="Nom du projet" disabled/><br>
                        </div>
                        <div class="tache">
                            <input type="text" class="titre" name="titre1" placeholder="Titre de la tâche"/>
                            <input type="date" class="SoonestDate" placeholder="Date d'exécution au plus tôt" name="SoonestDate1">
                            <input type="date" class="LatestDate" placeholder="Date d'exécution au plus tard" name="LatestDate1">
                            <input type="number" placeholder="Récompense" class="prix" name="prix1" min="0" step="0.01"><span style="color:white">&euro;</span>
                            <input type="text" placeholder="Description" class="description" name="description1" class="description">
                            
                            <br>
                            <label style="color:whitesmoke">Compétences requises pour cette tâche : </label>
                    
                            <% int a = 0; %>
                            <c:forEach items ="${competences}" var = "element">
                                <input id="id<%=a%>" name="${element.nomCompetence}" type="checkbox" > <label for="id<%=a%>">${element.nomCompetence}</label>
                                <% a = a + 1 ;%>
                            </c:forEach>
                                
                            <br>
                        </div>
                    </div>
                    <button type="button" id="boutonAjoutTache" onclick="ajoutTache()" style="display:none">Ajouter une tâche</button>
                    <br><br>
                    <ul class="actions">
                        <li><a href="."><input name="annuler" type="button" value="Annuler"></a></li>
                        <li><input class="special" type="submit" value="Valider" onclick="correction()"/></li>
                    </ul>
                    <input type="text" value="ValidationAjoutTache" name="action" style="display:none" />
                </form>
            </div>
        </section>
    </body>
</html>
