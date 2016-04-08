<%-- 
    Document   : ajouter
    Created on : 4 avr. 2016, 14:41:58
    Author     : hargec
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            var numeroDeTache = 1;
            var barres;
            function change() {
                if (j==1) {
                    document.getElementById("projet").style.display="";
                    document.getElementById("boutonAjoutTache").style.display="";
                    document.getElementsByClassName("titre")[0].placeholder="Titre de la première tâche";
                    barres = document.getElementsByClassName("hr")
                    for (i=0; i<barres.length; i++) {
                        barres[i].style.display="";
                    }
                    j=0;
                } else {
                    document.getElementById("projet").style.display='none';
                    document.getElementById("boutonAjoutTache").style.display='none';
                    document.getElementsByClassName("titre")[0].placeholder="Titre de la tâche";
                    barres = document.getElementsByClassName("hr")
                    for (i=0; i<barres.length; i++) {
                        barres[i].style.display="";
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
                b.setAttribute("onclick","this.parentElement.remove()");
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
        </script>
        <section id="banner">
            <h2>Ajouter ..?</h2>
            <div class="container 50%">
                <form id="formAjoutTache" action="controleur" method="get" accept-charset="UTF-8">
                    <div id="Taches">
                        <input type="radio" name="typeTache" value="TUnique" id="TUnique" checked onchange="change()">
                        <label for="TUnique"><h3 style="color:whitesmoke">une tâche</h3></label>
                        <input type="radio" name="typeTache" value="TComplexe" id="TComplexe" onchange="change()">
                        <label for="TComplexe"><h3 style="color:whitesmoke">un groupe de tâches liées</h3></label>
                        <div id="projet" style="display:none">
                            <input type="text" name="projet" placeholder="Nom du projet"/><br>
                        </div>
                        <div class="tache">
                            <input type="text" class="titre" name="titre" placeholder="Titre de la tâche"/>
                            <input type="date" placeholder="Date d'exécution au plus tôt" name="SoonestDate">
                            <input type="date" placeholder="Date d'exécution au plus tard" name="LatestDate">
                            <input type="number" placeholder="Récompense" name="prix" min="0" step="0.01"><span style="color:white">&euro;</span>
                            <input type="text" placeholder="Description" class="description">
                        </div>
                    </div>
                    <button type="button" id="boutonAjoutTache" onclick="ajoutTache()" style="display:none">Ajouter une tâche</button>
                    <!-- Annuler est un simple lien car il ne soumet pas le formulaire -->
                    <ul class="actions">
                        <li><a href="."><input name="annuler" type="button" value="Annuler"></a></li>
                        <li><input name="action" class="special" type="submit" value="ValidationTache" /></li>
                    </ul>
                </form>
            </div>
        </section>
    </body>
</html>
