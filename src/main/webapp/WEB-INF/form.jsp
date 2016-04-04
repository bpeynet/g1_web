<%-- 
    Document   : form
    Created on : 3 avr. 2016, 14:38:05
    Author     : ralambom
--%>

<form method="get" action="/controleur">
                <div class="container 50%">
                    <input name="nom" id="nom" value="<% out.println(request.getAttribute("nom")==null ? "" : request.getAttribute("nom"));%>" placeholder="Nom" type="text" autofocus required>
                    <input name="prenom" id="prenom" value="<% out.println( request.getAttribute("prenom")==null ? "" : request.getAttribute("prenom"));%>" placeholder="Prenom" type="text" required>
                    <input name="adresse" id="adresse" value="<% out.println( request.getAttribute("adresse")==null ? "" : request.getAttribute("adresse"));%>" placeholder="Adresse postale" type="text" required>
                    <input name="date" id="date" value="<% out.println( request.getAttribute("date")==null ? "" : request.getAttribute("date"));%>" placeholder="Date de naissance" type="date" required>
                    <input name="email" id="email" value="<% out.println( request.getAttribute("email")==null ? "" : request.getAttribute("email"));%>" placeholder="Email" type="email" required>
                    <input name="mdp" id="mdp" value="" placeholder="Mot de passe" type="password" required>
                    <input name="mdp" value="" placeholder="Confirmer le mot de passe" type="password" required>
                    <input id="conditions" name="conditions" type="checkbox" required>
                    <label for="conditions">J'ai lu et j'accepte les conditions générales d'utilisation du service CrowdHelping.</label>
                    <ul class="actions">
                        <li><input value="Validation" class="special" type="submit" name="action"></li>
                    </ul>
                </div>
            </form>
