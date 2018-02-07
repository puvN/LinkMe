<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="resourcesConnect.jsp"/>
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<html>
<head>
    <title></title>
</head>
<body>

<div class="div-table">
    <div class="div-column left-column">
        <br>

        <div class="div-row">
            <div class="second-header"><spring:message code="header.restorePassword"/>:</div>
            <form class="pure-form" action="${pageContext.request.contextPath}/resetPassword" method="POST">
                <div class="field-center">
                    <fieldset class="pure-group">
                        <label>
                            <input maxlength="20" required type='text' class="pure-input-1" name='username'>
                        </label>
                    </fieldset>
                </div>
                <button id="sendLetter" name="submit" type="submit"
                        class="pure-button pure-input-1-2 pure-button-primary">
                    <i class="fa fa-paper-plane-o"></i>
                    <spring:message code="label.sendLetter"/>
                </button>
                <input type="hidden" name="${_csrf.parameterName}"
                       value="${_csrf.token}"/>
            </form>
        </div>
    </div>
</div>
</body>
</html>
