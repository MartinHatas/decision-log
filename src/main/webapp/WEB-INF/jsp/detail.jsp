<!DOCTYPE HTML>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp"></jsp:include>

<div class="detail-box">
    <c:forEach var="decision" items="${decisions}">

        <div class="content">
            <div class="detail">
                <div class="heading">
                    Date:
                </div>
                <div class="content">
                    <div class="date">
                        <%--<c:out value="${decision.date}"/>--%>
                    </div>
                </div>
            </div>

            <div class="detail">
                <div class="heading">
                    Subject:
                </div>
                <div class="content">
                    <div class="subject">
                        <c:out value="${decision.subject}"/>
                    </div>
                </div>
            </div>

            <div class="detail">
                <div class="heading">
                    Reason:
                </div>
                <div class="content">
                    <c:out value="${decision.reason}" escapeXml='false'/>
                </div>
            </div>

            <div class="detail">
                <div class="heading">
                    Conclusion:
                </div>
                <div class="content">
                    <c:out value="${decision.conclusions}" escapeXml='false'/>

                </div>
            </div>
        </div>
    </c:forEach>
</div>
<jsp:include page="footer.jsp"></jsp:include>