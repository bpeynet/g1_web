<%--TODO--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>BDError</title>
    </head>
    <body>
        <h1>Erreur dans la base de données</h1>
        <p><% out.println(request.getAttribute("erreur")); %></p>
        <p><% out.println(request.getAttribute("erreur_message")); %></p>
    </body>
</html>
