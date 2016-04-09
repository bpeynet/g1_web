<%-- 
    Document   : user_page
    Created on : 3 avr. 2016, 14:24:47
    Author     : ralambom
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<jsp:include page="banniere.jsp"/>

<section id="banner">
    <% out.print(request.getAttribute("succesMessage")==null ? "" :
            "<p>" + request.getAttribute("succesMessage") + "</p>"); %>
</section>
</body>
</html>