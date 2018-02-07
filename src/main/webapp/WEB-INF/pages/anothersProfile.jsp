<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title></title>
</head>
<body>

<div class="div-table home-content">
    <div class="div-column left-column">
        <div class="div-row">
            <i class="fa fa-user gray-icon"> &nbsp </i><a id="subscription"
                                                          href="/LinkMe/home/${username}">/${username}</a>

            <div class="div-row">
                <i class="fa fa-envelope-o gray-icon">&nbsp</i>${email}
            </div>

            <img src="${avatarLink}"
                 class="avatarImg"/>
        </div>

        <div class="div-row">
            <div id="subscribeButton">
                <input class="pure-button pure-button-primary" id="subscribe" type="submit"
                       value="${subscribe}" ${subscribeDisabled}/>
            </div>
        </div>

        <div class="subscriptionsBlock">
            <i class="fa fa-users gray-icon"></i> <spring:message code="label.subscriptions"/>: ${subscriptionsNumber}
            <c:forEach var="subscription" items="${subscriptions}">
                <div class="subscr">
                    <i class="fa fa-user gray-icon subscr-icon"></i>
                    <br/><a id="subscriptionName"
                            href="${pageContext.request.contextPath}/home${subscription}">${subscription}</a><br/>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="div-column centered-column">
        <div class="linksBlock" id="links">
            <i class="fa fa-link gray-icon"></i>&nbsp<spring:message code="label.links"/>: ${linksNumber}<br/>
            <c:forEach var="link" items="${links}">
                <ul class="link">
                    <div class="linkDate">
                            ${link.linkDate}
                    </div>
                    <div class="linkTitle">
                            ${link.linkTitle}
                    </div>
                    <br>
                    <li id="linkVal" style="cursor: hand">
                        <i class="fa fa-external-link gray-icon"></i>

                        <div class="gradientEllipsis linkval">
                            <a id="link" href="${link.link}">${link.link}</a>

                            <div class="dimmer"></div>
                        </div>
                    </li>
                </ul>
            </c:forEach>
            <br/>
        </div>
    </div>
</div>
</body>
</html>
