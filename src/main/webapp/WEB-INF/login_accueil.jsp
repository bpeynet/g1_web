<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CrowdHelping</title>
        <script src="js/jquery.min.js"></script>
        <script src="js/skel.min.js"></script>
        <script src="js/skel-layers.min.js"></script>
        <script src="js/init.js"></script>
    </head>
    <body class="landing">
        <header id="header" class="alt">
            <h1><strong><a href=".">CrowdHelping</a></strong> by M.C.A.B.</h1>
            <nav id="nav">
                <ul>
                    <li><a href="#one">A propos</a></li>
                </ul>
            </nav>
        </header>
        <section id="banner">
            <h2>CrowdHelping</h2>
            <p>Des rencontres, des services, de l'humain.</p>
            <form method="post" action="./controleur">
                <div class="container 50%">
                    <input name="email" id="email" value="" placeholder="Email" type="email" autofocus required>
                    <input name="mdp" id="mdp" value="" placeholder="Mot de passe" type="password" required>
                    
                    <ul class="actions">
                        <li><input value="Connexion" class="special" type="submit" name="action"></li>
                        <li><a href="./controleur?action=Inscription"><input type="button" value="Inscription"></a></li>
                    </ul>                   
                    
                    <% out.print(request.getAttribute("erreur")==null ? "" : "<p>" + request.getAttribute("erreur") +"</p>");%>
                    <% out.print(request.getAttribute("message")==null ? "" : "<p>" + request.getAttribute("message") +"</p>");%>
                
                </div>
            </form>
        </section>
        <section id="one" class="wrapper style1" style="padding-bottom: 0em">
            <div class="container 75%">
                <div class="row 200%">
                    <div class="6u 12u$(medium)">
                        <header class="major">
                            <h2>Proposez des t??ches</h2>
                            <p>contre r??mun??ration</p>
                        </header>
                    </div>
                    <div class="6u$ 12u$(medium)">
                        <p>Si vous avez besoin d'aide pour bricoler dans votre maison, ou pour cr??er des oeuvres d'arts, signalez-le !</p>
                        <p>Indiquez le type de comp??tences que demande votre t??che et quelqu'un se manifestera vite pour r??pondre ?? votre demande.</p>
                    </div>
                </div>
                <div class="row 200%">
                    <div class="6u$ 12u$(medium)">
                        <p>A la recherche d'une activit?? r??mun??r??e ? Mettez vos talents manuels ?? profit !</p>
                        <p>Indiquez les comp??tences que vous poss??dez et choisissez une t??che pour laquelle vous ??tes qualifi?? !</p>
                    </div>
                    <div class="6u 12u$(medium)" style="clear:none">
                        <header class="major">
                            <h2>Effectuez des t??ches</h2>
                            <p>contre r??mun??ration</p>
                        </header>
                    </div>
                </div>
            </div>
        </section>
    </body>
</html>