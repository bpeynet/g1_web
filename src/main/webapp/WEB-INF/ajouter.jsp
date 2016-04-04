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
    </head>
    <body>
         <form action="controleur" method="post" accept-charset="UTF-8">
            <label>Titre de la tâche :</label><input type="text" name="titre" /> <br/>
            <label>Cette tâche fait-elle partie d'un projet plus grand ? </label>
                <input type="checkbox" name ="tacheatom" value='0'> Oui
                <input type="checkbox" name="inproject" value='1'> Non
            <!-- Annuler est un simple lien car il ne soumet pas le formulaire -->
            <input name="confirm" type="submit" value="Annuler" />
            <input name="confirm" type="submit" value="Valider" />
            <!-- Pour indiquer au contrôleur quelle action faire, on utilise un champ caché -->
            <input type="hidden" name="action" value="ajouter" />
        </form>
    </body>
</html>
