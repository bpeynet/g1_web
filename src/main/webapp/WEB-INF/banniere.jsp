<%-- 
    Document   : banni�re
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
                <% String user = ((Utilisateurs)request.getAttribute("utilisateur")).getNomPrenom();
                out.println(user);
                %>
            </h1>
            <nav id="nav">
                <ul>
                    <li><a href="#one">Accueil</a></li>
                    <li><a href="generic.html">T�ches</a></li> <%--TODO--%>
                    <li><a href="#one">Proposer une t�che</a></li> <%--TODO--%>
                    <li><a href="#one">Mon Profil</a></li> <%--TODO--%>
                    <li><a href="login_accueil.jsp">Se d�connecter</a></li>
                </ul>
            </nav>
        </header>
        <section id="banner">
            <h2>CrowdHelping</h2>
            
    </section>