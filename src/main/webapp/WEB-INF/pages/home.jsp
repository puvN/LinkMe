<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="resourcesConnect.jsp"/>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<meta http-equiv="CONTENT-TYPE" content="text/html" charset="UTF-8"/>

<c:set var="isYou" scope="session" value="${pageContext.request.userPrincipal.name == username}"/>


<head>
    <title>${username}</title>
</head>

<html>
<body>

<jsp:include page="head.jsp"/>

<c:choose>
    <c:when test="${isYou}">
        <jsp:include page="yourProfile.jsp"/>
    </c:when>
    <c:when test="${!isYou}">
        <jsp:include page="anothersProfile.jsp"/>
    </c:when>
</c:choose>

<br/><br/>
</body>
</html>