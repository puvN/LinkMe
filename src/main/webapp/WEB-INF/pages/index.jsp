<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="resourcesConnect.jsp"/>

<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<html>
<head>
    <script src='https://www.google.com/recaptcha/api.js'></script>
</head>
<body onload='document.loginForm.username.focus();'>
<title>Linkedstor</title>

<div class="div-table">
    <div class="div-column centered-column">
        <div class="div-row centered-row">
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            <c:if test="${not empty msg}">
                <div class="msg">${msg}</div>
            </c:if>
            <form name='loginForm' id="loginForm" class="pure-form"
                  action="<c:url value='/j_spring_security_check' />" method='POST'>
                <fieldset>
                    <input maxlength="20" title="" required type='text' class="" name='username'
                           id="username"
                           placeholder="<spring:message code="label.user"/>">
                    <input maxlength="20" title="" required type='password' class="" name='password'
                           id="logpassword"
                           placeholder="<spring:message code="label.password"/>"/>
                    <button id="sign-in" disabled name="submit" type="submit"
                            class="pure-button pure-button-primary">
                        <i class="fa fa-sign-in"></i>
                        <spring:message code="label.login"/>
                    </button>
                    <input type="hidden" name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                </fieldset>
            </form>
            <a href="${pageContext.request.contextPath}/forgotPassword" id="forgotPassword">
                <spring:message code="label.forgotPassword"/>?</a>
        </div>
        <div class="div-row centered-row">
            <div class="welcome-container">
                <div class="title-container">
                    <br>//linkedstor.
                    <div class="gray-icon beta">beta</div>
                    <div class="line"></div>
                    <div class="second-header"><spring:message code="header.linksHere"/></div>
                </div>
                <div id="thumbnails">
                    <ul class="clearfix">
                        <li class="thumbnail-image"><a
                                href="${pageContext.request.contextPath}/resources/images/main/image1.png">
                            <img src="${pageContext.request.contextPath}/resources/images/main/image1-thumbnail.png">
                        </a></li>
                        <li class="thumbnail-image">
                            <a href="${pageContext.request.contextPath}/resources/images/main/image2.png">
                                <img src="${pageContext.request.contextPath}/resources/images/main/image2-thumbnail.png">
                            </a>
                        </li>
                        <li class="thumbnail-image">
                            <a href="${pageContext.request.contextPath}/resources/images/main/image3.png">
                                <img src="${pageContext.request.contextPath}/resources/images/main/image3-thumbnail.png">
                            </a>
                        </li>
                        <li class="thumbnail-image">
                            <a href="${pageContext.request.contextPath}/resources/images/main/image4.png">
                                <img src="${pageContext.request.contextPath}/resources/images/main/image4-thumbnail.png">
                            </a>
                        </li>
                    </ul>
                </div>
                <br>

                <div class="line"></div>
            </div>
        </div>
        <div class="div-row centered-row">
            <div id="reg-box">
                <form name="regForm" class="pure-form" id="regForm" action="${pageContext.request.contextPath}/register"
                      method="POST">
                    <fieldset class="pure-group">
                        <legend>
                            <div class="second-header"><spring:message code="header.signUp"/></div>
                        </legend>
                        <input maxlength="20" required title="<spring:message code="label.userHint"/>" type='text'
                               class="pure-input-1" name='login' id="login"
                               placeholder="<spring:message code="label.user"/>">
                        <input maxlength="254" required title="<spring:message code="label.emailHint"/>" type='email'
                               class="pure-input-1" name='email' id="email"
                               placeholder="<spring:message code="label.email"/>">
                        <input maxlength="20" required title="<spring:message code="label.pwHint"/>" type='password'
                               class="pure-input-1" name='password' id="password"
                               placeholder="<spring:message code="label.password"/>">
                        <input maxlength="20" required type='password' class="pure-input-1" name='matchingPassword'
                               id="matchingPassword"
                               placeholder="<spring:message code="label.repeatPassword"/>">
                    </fieldset>
                    <div class="g-recaptcha" title="" id="grecaptcha"
                         data-sitekey="6Lf-eQ0TAAAAALiwlV_iG3gCS3CQdmpv-fhLp63G"></div>
                    <button id="register" disabled type="submit" class="pure-button pure-input-1-2">
                        <i class="fa fa-user-plus"></i>
                        <spring:message code="label.register"/>
                    </button>

                    <input type="hidden" name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                </form>
                <div id="agreement">
                <label for="agree">
                    <input class="pure-checkbox" id="agree" type="checkbox">
                </label>
                <spring:message code="label.iAgree"/>&nbsp;<button id="terms" title="" class="pure-button pure-input-1-3">
                    <spring:message code="label.terms"/>
                </button>
                </div>
            </div>
        </div>
        <!-- <div class="div-row">
          <div class="second-header mobile"><spring:message code="header.soon"/>:</div>
          <div class="available-on-play"></div>
          <div class="available-on-appstore"></div>
        </div> -->
    </div>
</div>
</body>
</html>