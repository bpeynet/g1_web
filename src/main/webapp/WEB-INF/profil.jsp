<%-- 
    Document   : profil
    Created on : 4 avr. 2016, 21:28:07
    Author     : ben
--%>

<%@page import="modeles.Utilisateurs"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Profil de <% out.println(((Utilisateurs) request.getSession().getAttribute("utilisateur")).getNomPrenom());%></title>
        <script language="javascript">
            function confirmSupp(){
                if(confirm("Supprimer votre compte ?\nC'est irréversible.")===true) {
                    return true;
                } else {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <jsp:include page="banniere.jsp"/>
        <section id="banner">
            <h2>Profil de <% out.println(((Utilisateurs) request.getSession().getAttribute("utilisateur")).getNomPrenom());%></h2>
            <jsp:include page="form.jsp"/>
            <a href="./controleur?action=SupprimerCompte&email=<% out.println(((Utilisateurs) request.getSession().getAttribute("utilisateur")).getEmail());%>" onclick="return confirmSupp(this);">Supprimer votre compte</a>
        </section>
    </body>
</html>
