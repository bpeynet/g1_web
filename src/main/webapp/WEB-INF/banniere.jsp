<%-- 
    Document   : bannière
    Created on : 4 avr. 2016, 15:53:54
    Author     : ralambom
--%>

<%@page import="modeles.Utilisateurs"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Accueil</title>
    <script src="js/jquery.min.js"></script>
    <script src="js/skel.min.js"></script>
    <script src="js/skel-layers.min.js"></script>
    <script src="js/init.js"></script>
</head>
<body class="landing">
    <header id="header" class="alt">
        <h1>Bienvenue, 
            <% String user = ((Utilisateurs) request.getSession().getAttribute("utilisateur")).getNomPrenom();
                out.println(user);
            %>
        </h1>
        <nav id="nav">
            <ul>
                <li><a href="#">Accueil</a></li>
                <li><a href="generic.html">Tâches</a></li> <%--TODO--%>
                <li><a href="./controleur?action=AjoutTache">Proposer une tâche</a></li> <%--TODO--%>
                <li><a href="#one">Mon Profil</a></li> <%--TODO--%>
                <li><a href="./controleur?action=Deconnexion">Se déconnecter</a></li>
            </ul>
        </nav>
    </header>
    <section id="banner">
        <h2>CrowdHelping</h2>

    </section>
