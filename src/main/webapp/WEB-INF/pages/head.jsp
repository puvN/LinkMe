<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>


<html>
<head>
    <title></title>
</head>
<body>
<c:url value="/j_spring_security_logout" var="logoutUrl"/>

<script>
    function formSubmit() {
        document.getElementById("logoutForm").submit();
        location.replace();
    }
</script>

<div class="head-content">
    <a class="a-link link-home"
       href="${pageContext.request.contextPath}/home/${pageContext.request.userPrincipal.name}">
        <i class="fa fa-home"></i><spring:message code="label.goHome"/></a>

    <a class="a-link link-logout" href="javascript:formSubmit()">
        <i class="fa fa-sign-out"></i><spring:message code="label.logout"/></a>

    <a class="a-link link-settings" href="${pageContext.request.contextPath}/settings">
        <i class="fa fa-cog"></i><spring:message code="label.settings"/></a>

    <form action="${logoutUrl}" method="post" id="logoutForm">
        <input type="hidden"
               name="${_csrf.parameterName}"
               value="${_csrf.token}"/>
    </form>

    <div class="line long-line"></div>

    <form class="pure-form">
        <input id="searchField" disabled maxlength="20" class="pure-input-rounded search" type="text"
               placeholder="<spring:message code="label.search"/>">
        <span><i class="fa fa-search search-icon"></i></span>
    </form>

    <div class="back-to-top">
        <i class="fa fa-arrow-up fa-2x white-up-arrow"></i>
    </div>
</div>

</body>
</html>
