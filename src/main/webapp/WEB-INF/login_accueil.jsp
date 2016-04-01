<%-- TODO--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="js/jquery.min.js"></script>
        <script src="js/skel.min.js"></script>
        <script src="js/skel-layers.min.js"></script>
        <script src="js/init.js"></script>
    </head>
    <body class="landing">
        <header id="header" class="alt">
            <h1><strong><a href="index.html">CrowdHelping</a></strong> by M.C.A.B.</h1>
            <nav id="nav">
                <ul>
                    <li><a href="index.html">A propos</a></li>
                    <li><a href="generic.html">Exemples</a></li>
                </ul>
            </nav>
        </header>
        <section id="banner">
            <h2>CrowdHelping</h2>
            <p>Des rencontres, des services, de l'humain.</p>
            <form method="post" action="#">
                <div class="container 50%">
                    <input name="email" id="email" value="" placeholder="Email" type="email" autofocus>
                    <input name="mdp" id="mdp" value="" placeholder="Mot de passe" type="password" required>
                    <input id="conditions" name="conditions" type="checkbox" required>
                    <label for="conditions">J'ai lu et j'accepte les conditions générales d'utilisation du service CrowdHelping.</label>
                    <ul class="actions">
                        <li><input value="Connexion" class="special" type="submit"></li>
                        <li><input value="Effacer" type="reset"></li>
                    </ul>
                </div>
            </form>
    </section>
</body>
</html>