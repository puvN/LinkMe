<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<meta http-equiv="CONTENT-TYPE" content="text/html" charset="UTF-8"/>

<html>
<head>
    <title></title>
</head>
<body>

<div class="div-table home-content">

    <div class="div-column left-column">
        <div class="div-row">
            <i class="fa fa-home gray-icon"> &nbsp </i> <a id="subscription"
                                                           href="${pageContext.request.contextPath}/home/${username}">/${username}</a>

            <div class="div-row">
                <i class="fa fa-envelope-o gray-icon">&nbsp</i>${email}
            </div>
            <img src="${avatarLink}"
                 class="avatarImg"/>
        </div>
        <div class="div-row">
            <div class="subscriptionsBlock">
                <i class="fa fa-users gray-icon"></i> <spring:message
                    code="label.subscriptions"/>: ${subscriptionsNumber}
                <c:forEach var="subscription" items="${subscriptions}">
                    <div class="subscr">
                        <i class="fa fa-user gray-icon subscr-icon"></i>

                        <div class="deleteSubscrBtn">
                            <i class="fa fa-times"></i>
                            &nbsp;
                        </div>
                        <br/><a id="subscriptionName"
                                href="${pageContext.request.contextPath}/home${subscription}">${subscription}</a><br/>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <div class="div-column centered-column" id="links">
        <div class="linksBlock">
            <div class="progress-icon"></div>
            <i class="fa fa-link gray-icon"></i>&nbsp<spring:message code="label.links"/>: ${linksNumber}<br/>
            <spring:message code="label.linksToAdd"/>: ${linksToAdd} <br/>
            <label>
                <div class="newLink-container">
                    <input class="newLink" id="newLink" type="text"
                           placeholder="<spring:message code="label.newLink"/>">
                </div>
            </label>
            <input class="pure-button pure-button-primary" id="addLink" type="submit"
                   value="<spring:message code="label.add"/>">
            <label>
                <div class="isPrivate-container">
                    <input type="checkbox" id="isPrivate"/>&nbsp<spring:message code="label.isPrivate"/>
                </div>
            </label>
            <br/>
            <c:if test="${not empty error}">
                <div class="error centered-error">${error}&nbsp
                    <input class="pure-button pure-button-primary buyLinks" type="button" disabled
                           value="<spring:message code="label.buyLinks"/>">
                </div>
            </c:if>
            <c:forEach var="link" items="${links}">
                <ul class="link">
                    <div class="linkDate">
                            ${link.linkDate}
                    </div>
                    <c:if test="${link.isPrivate}">
                        <div class="hidden-link">
                            <i class="fa fa-user-secret gray-icon"></i>
                        </div>
                    </c:if>
                    <div class="deleteLinkBtn">
                        <i class="fa fa-times"></i>
                        &nbsp;
                    </div>
                    <div class="linkTitle">
                            ${link.linkTitle}
                    </div>
                    <li id="linkId" style="display: none">${link.idLink}</li>
                    <br/>
                    <li id="linkVal" style="cursor: hand">
                        <i class="fa fa-external-link gray-icon"></i>

                        <div class="gradientEllipsis linkval">
                            <a id="link" target="_blank" href="${link.link}">${link.link}</a>

                            <div class="dimmer"></div>
                        </div>
                    </li>
                </ul>
            </c:forEach>
        </div>
    </div>

    <div class="div-column right-column">
        <div class="feed-block">
            <i class="fa fa-list-ul gray-icon"></i>&nbsp<spring:message code="label.updates"/> <br>
            <c:choose>
                <c:when test="${empty feed}">
                    <spring:message code="label.noUpdates"/>
                </c:when>
                <c:when test="${not empty feed}">
                    <c:forEach var="feedItem" items="${feed}">
                        <div class="feed-item">
                            <div class="linkDate feed-link-date">
                                    ${feedItem.value.linkDate}
                            </div>
                            <i class="fa fa-user gray-icon"></i>&nbsp<a
                                href="${feedItem.key.login}">/${feedItem.key.login}</a>

                            <div class="gradientEllipsis feed-link">
                                <i class="fa fa-external-link gray-icon feed-icon-link"></i>
                                <a target="_blank" href="${feedItem.value.link}">${feedItem.value.link}</a>

                                <div class="dimmer"></div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>
