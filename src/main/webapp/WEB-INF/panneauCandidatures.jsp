<%-- 
    Document   : panneauTaches
    Created on : 6 avr. 2016, 21:43:57
    Author     : ben
--%>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modeles.TacheAtom"%>
<%@page import="modeles.Tache"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tâches</title>
    </head>
    <jsp:include page="banniere.jsp"/>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <section id="banner" class="wrapper" style=" color: rgb(202, 202, 202);">
        <h2>Vos Candidatures</h2>
    </section>
    <section class="container">
        <c:if test="${candidature.size() > 0}">
            <table id='tableauMesCandidatures'>   
            <c:forEach items="candidatures" var="element">
                <tr><td> ${element.titreTacheAtom} de ${element.emailCommanditaire} </td>
                    <td><a class ='button' href='./controleur?action=Depostuler&idTacheAtom=${element.idTacheAtom}'>Retirer sa candidature</a></td>
                </c:forEach>
        </c:if>
        <c:if test="${candidatures.size() == 0}">
            <p>Aucune candidature soumise.<p>
        </c:if>
            <h2> Mes candidatures validées </h2>
            
            <%--TODO : les candidatures validées, à chercher dans TacheAtom --%>
            
            <h2>Vos services rendus</h2>
        <c:if test="${services.size() > 0}">
            <table id='tableauMesCandidatures'>   
            <c:forEach items="services" var="element">
                <tr><td> ${element.titreTacheAtom} de ${element.emailCommanditaire} </td>
                    <td><a class ='button' href='./controleur?action=Depostuler&idTacheAtom=${element.idTacheAtom}'>Retirer sa candidature</a></td>
                </c:forEach>
        </c:if>
        <c:if test="${services.size() == 0}">
            <p>Aucun service rendu.<p>
        </c:if>  
    </section>
</body>
</html>
