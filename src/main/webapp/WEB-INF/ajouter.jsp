<%-- 
    Document   : ajouter
    Created on : 4 avr. 2016, 14:41:58
    Author     : hargec
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
        <jsp:include page="banniere.jsp" />
        <script>
            var j = 1,i;
            var tableCompetences = [];
            <c:forEach items="${competences}" var ="element">tableCompetences.push("${element.nomCompetence}");</c:forEach>
            var nbCompetences = tableCompetences.length;
            var l = 1; // permet d'afficher 2 taches atomiques au minimum pour un projet
            var numeroDeTache = 1;
            var barres;
            var bloc_tache;
            function change() {
                if (j===1) {
                    document.getElementById("projet").style.display="";
                    document.getElementById("projetName").disabled=false;
                    document.getElementById("boutonAjoutTache").style.display="";
                    document.getElementsByClassName("titre")[0].placeholder="Titre de la première tâche";
                    barres = document.getElementsByClassName("hr");
                    for (i=0; i<barres.length; i++) {
                        barres[i].style.display="";
                    }
                    bloc_tache = document.getElementsByClassName("tache");
                    for (i=1; i<bloc_tache.length; i++){
                        bloc_tache[i].style.display="";
                    }
                    if (l===1) {
                        ajoutTache();
                    }
                    l=0;
                    j=0;
                } else {
                    document.getElementById("projet").style.display='none';
                    document.getElementById("projetName").disabled=true;
                    document.getElementById("boutonAjoutTache").style.display='none';
                    document.getElementsByClassName("titre")[0].placeholder="Titre de la tâche";
                    barres = document.getElementsByClassName("hr");
                    for (i=0; i<barres.length; i++) {
                        barres[i].style.display="";
                    }
                    bloc_tache = document.getElementsByClassName("tache");
                    for (i=1; i<bloc_tache.length; i++){
                        bloc_tache[i].style.display="none";
                    }
                    j=1;
                }
                return true;
            }
            function ajoutTache() {
                numeroDeTache++;
                var x = document.getElementsByClassName("tache")[0];
                var clonedElement = x.cloneNode(true);
                clonedElement.childNodes[1].value="";
                clonedElement.childNodes[2].value="";
                clonedElement.childNodes[3].value="";
                clonedElement.childNodes[4].value="";
                clonedElement.childNodes[5].value="";
                clonedElement.childNodes[6].value="";
                clonedElement.childNodes[7].value="";
                clonedElement.childNodes[11].value="";
                if (numeroDeTache>2) {
                    var b = document.createElement("button");
                    b.appendChild(document.createTextNode("Supprimer cette tâche"));
                    b.setAttribute("type","button");
                    b.setAttribute("onclick","this.parentElement.remove();numeroDeTache--;correction();");
                    clonedElement.appendChild(b);
                }
                clonedElement.insertBefore(document.createElement("hr"),clonedElement.childNodes[0]);
                document.getElementById("Taches").appendChild(clonedElement);
                document.getElementsByClassName("titre")[numeroDeTache-1].setAttribute("placeholder", "Titre de la " + numeroDeTache +"ème tâche");
                for(var n = 0; n<nbCompetences; n++) {
                    document.getElementsByClassName("competence")[(numeroDeTache-1)*nbCompetences+n].setAttribute("id", "id"+n+"-"+numeroDeTache);
                    document.getElementsByClassName("competence")[(numeroDeTache-1)*nbCompetences+n].setAttribute("name", tableCompetences[n]+"-"+numeroDeTache);
                    document.getElementsByClassName("labelCompetence")[(numeroDeTache-1)*nbCompetences+n].setAttribute("for", "id"+n+"-"+numeroDeTache);
                }
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
                    for (var c=0; c<nbCompetences; c++) {
                        document.getElementsByClassName("competence")[i*nbCompetences+c].setAttribute("name", tableCompetences[c]+"-"+j);
                        document.getElementsByClassName("competence")[i*nbCompetences+c].setAttribute("id", "id"+c+"-"+j);
                        document.getElementsByClassName("labelCompetence")[i*nbCompetences+c].setAttribute("for", "id"+c+"-"+j);
                    }
                }
            }
        </script>
        <section id="banner">
            <h2>Ajouter ..?</h2>
            <% out.print(request.getAttribute("erreurMessage")==null ? "" : "<p>" + request.getAttribute("erreurMessage") +"</p>");%>
            <div class="container 50%">
                <form id="formAjoutTache" action="controleur" method="post" accept-charset="UTF-8">
                    <div id="Taches">
                        <input type="radio" name="typeTache" value="TUnique" id="TUnique" checked onchange="change()">
                        <label for="TUnique"><h3 style="color:whitesmoke">une tâche</h3></label>
                        <input type="radio" name="typeTache" value="TComplexe" id="TComplexe" onchange="change()">
                        <label for="TComplexe"><h3 style="color:whitesmoke">un projet</h3></label>
                        <div id="projet" style="display:none">
                            <input type="text" id="projetName" name="projetName" placeholder="Nom du projet" required disabled/><br>
                        </div>
                        <div class="tache">
                            <input type="text" class="titre" name="titre1" placeholder="Titre de la tâche" required>
                            <input type="date" class="SoonestDate" placeholder="Date d'exécution au plus tôt" name="SoonestDate1" required>
                            <input type="date" class="LatestDate" placeholder="Date d'exécution au plus tard" name="LatestDate1" required>
                            <input type="number" placeholder="Récompense" class="prix" name="prix1" min="0" step="0.01" required><span style="color:white">&euro;</span>
                            <input type="text" placeholder="Description" class="description" name="description1" class="description" required>
                            <br>
                            <span style="color:whitesmoke">Compétences requises pour cette tâche : </span>
                            <br><% int a = 0; %><c:forEach items ="${competences}" var = "element">
                            <input id="id<%=a%>-1" name="${element.nomCompetence}-1" class="competence" type="checkbox" >
                            <label for="id<%=a%>-1" class="labelCompetence">${element.nomCompetence}</label><% a = a + 1 ;%></c:forEach>
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
