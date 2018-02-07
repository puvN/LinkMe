<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="resourcesConnect.jsp"/>

<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<html>
<head>
    <title>Linkedstor : password</title>
</head>
<body>
<div class="div-table">
    <div class="div-column left-column">
        <br>

        <div class="div-row">
            <div class="settings-box">
                <h4><spring:message code="header.changePassword"/>:</h4>

                <form id="restorePasswordForm" action="${pageContext.request.contextPath}/setNewPassword" method="post"
                      class="pure-form">
                    <div class="field-left">
                        <fieldset class="pure-group">
                            <input required title="<spring:message code="label.pwHint"/>" maxlength="20"
                                   class="pure-input-1" type='password' name="newPassword" id='newPassword'
                                   placeholder="<spring:message code="label.newPassword"/>"/>
                            <input title="" required maxlength="20" class="pure-input-1" type='password'
                                   name="matchingNewPassword" id='matchingNewPassword'
                                   placeholder="<spring:message code="label.repeatPassword"/>"/>
                        </fieldset>
                        <button id="changePassword" disabled type="submit"
                                class="pure-button pure-input-1-2 pure-button-primary">
                            <spring:message code="label.submit"/>
                        </button>
                        <input type="hidden" name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
