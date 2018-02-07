<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="resourcesConnect.jsp"/>
<html>
<head>
    <title>Link Me</title>
</head>
<body>

<jsp:include page="head.jsp"/>

<script>
    $(document).ready(function () {
        $(".search").focus();
    });
</script>

<div class="div-table">
    <div class="div-column centered-column">
        <div class="div-row centered-row">
            <div class="infoMsg">
                ${infoMsg}
                <br>
                <c:forEach var="similarUser" items="${similarUsers}">
                    <div class="subscr similarUser">
                        <div class="similarUserIconContainer">
                            <img class="similarUserIcon" src="${similarUser.avatarLink}">
                        </div>
                        <div class="similarUserLinkContainer">
                            <i class="fa fa-user gray-icon subscr-icon"></i>
                            <br/><a id="subscriptionName"
                                    href="${pageContext.request.contextPath}/home${similarUser.login}">/${similarUser.login}</a><br/>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    <div class="div-column centered-column">
        <br>

        <div class="errorMsg">
            ${errorMessage}
        </div>
        <div class="errorPic">
            ${errorPic}
        </div>
    </div>
</div>

</body>
</html>
