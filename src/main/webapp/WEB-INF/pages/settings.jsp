<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="resourcesConnect.jsp"/>

<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<html>
<head>
    <title>Settings</title>
</head>
<body>

<jsp:include page="head.jsp"/>

<div class="div-table settings-content">
    <div class="div-column left-column">
        <h2>
            <i class="fa fa-cog"></i>&nbsp<spring:message code="label.settings"/>
        </h2>
        <div id="actionResult">
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            <c:if test="${not empty msg}">
                <div class="msg">${msg}</div>
            </c:if>
        </div>
        <div class="div-row">
            <div class="settings-box">
                <h4><spring:message code="header.changePassword"/>:</h4>

                <form id="changePasswordForm" action="${pageContext.request.contextPath}/changePassword" method="post" class="pure-form">
                    <div class="field-left">
                        <fieldset class="pure-group">
                            <input title="" required maxlength="20" class="pure-input-1" type='password'
                                   name="oldPassword" id='oldPassword'
                                   placeholder="<spring:message code="label.oldPassword"/>">
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
        <div class="div-row">
            <div class="settings-box">
                <h4><spring:message code="header.avatarLink"/>:</h4>

                <div class="gradientEllipsis">
                    ${avatarLink}
                    <div class="dimmer"></div>
                </div>

                <form id="setAvatarLinkForm" class="pure-form" action="${pageContext.request.contextPath}/setAvatarLink"
                      method="post">
                    <div class="field-left">
                        <fieldset>
                            <input class="pure-input-1" type="text" name="newAvatarLink" id="newAvatarLink"
                                   placeholder="Url">
                        </fieldset>
                        <button name="submit" type="submit" disabled id="setAvatarLink"
                                class="pure-button pure-input-1-2 pure-button-primary">
                            <spring:message code="label.submit"/>
                        </button>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                </form>
            </div>
        </div>
        <div class="div-row">
            <div class="settings-box">
                <h4><spring:message code="header.changeEmail"/></h4>
                ${email}
                <form id="changeEmailForm" action="${pageContext.request.contextPath}/changeEmail" method="post"
                      class="pure-form">
                    <div class="field-left">
                        <fieldset>
                            <input maxlength="254" title="" class="pure-input-1" type="text" name="newEmail"
                                   id="newEmail"
                                   placeholder="E-mail">
                        </fieldset>
                        <button name="submit" type="submit" disabled id="changeEmail"
                                class="pure-button pure-input-1-2 pure-button-primary">
                            <spring:message code="label.submit"/>
                        </button>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </div>
    <div class="div-column centered-column">
        <img src="${avatarLink}"
             class="settings-avatarImg"/>
    </div>
</div>
</body>
</html>
