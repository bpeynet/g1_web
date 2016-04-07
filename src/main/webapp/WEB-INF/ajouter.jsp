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
        function remplir(){
            document.getElementById('projet').value=document.getElementById('titre1').value;
            return true;
        }
        function desactiver(){
            document.getElementById('projet').display="true";
            //document.form.projet.disabled=true;
            return true;
        }
        function activer(){
        document.getElementById('projet').display="none";    
        //document.form.projet.disabled=false;
            return true;
        }
        </script>
        <section id="banner">
            <h2>Nouvelle tâche</h2>
            <div class="container 50%">
                <form action="controleur" method="post" accept-charset="UTF-8">
                    <input type="text" id="titre1" name="titre" placeholder="Titre de la tâche" onkeyup="remplir()"/><br/>
                    
                    Cette tâche fait-elle partie d'un projet plus grand ?
                    <input type="radio" name="composition" id="tacheatom"  onclick="activer()">
                    <label for="tacheatom">Oui</label>
                    <input type="radio" name="composition" id="inproject" checked onclick="desactiver()">
                    <label for="inproject">Non</label>
                    
                    <input type="text" id="projet" name="projet" placeholder="Nom du projet"/>
                    
                    <input type="date" placeholder="Date d'exécution au plus tôt" id="SoonestDate">
                    <input type="date" placeholder="Date d'exécution au plus tard" id="LatestDate">
                    <input type="number" placeholder="Récompense" id="prix" min="0" step="0.01">
                    <input type="text" placeholder="Description" id="description">
                    <!-- Annuler est un simple lien car il ne soumet pas le formulaire -->
                    <ul class="actions">
                        <li><a href="."><input name="annuler" type="button" value="Annuler"></a></li>
                        <li><input name="confirm" class="special" type="submit" value="Valider" /></li>
                        <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
                        <li><input type="hidden" name="action" value="ajouter" /></li>
                    </ul>
                </form>
            </div>
        </section>
    </body>
</html>
