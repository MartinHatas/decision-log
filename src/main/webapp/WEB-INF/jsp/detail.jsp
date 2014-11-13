<!DOCTYPE HTML>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="header.jsp"></jsp:include>
<div class="detail-box">
    <c:forEach var="decision" items="${decisions}">
        <tr class="table-collapsible-row-header is-open"><%--is-open--%>
            <td class="date">
                <div class="table-collapsible-row-toggle"></div>
                <c:out value="${decision.date}"/>
            </td>
            <td class="subject"><c:out value="${decision.subject}"/></td>
            <td class="summary">
                <div class="wrapper">
                    <c:out value="${decision.reason}"/>
                </div>
            </td>
                <%--<td class="relevancy">--%>
                <%--<div class="wrapper">--%>
                <%--<c:out value="${decision.relevance}"/>--%>
                <%--&lt;%&ndash;&#9733;&#9733;&#9733;&#9733;&#9733;&ndash;%&gt;--%>
                <%--<div class="hider"></div>--%>
                <%--</div>--%>
                <%--</td>--%>
        </tr>

        <tr class="table-collapsible-row-body">
            <td colspan="4">
                <div class="content">
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
            </td>
        </tr>
    </c:forEach>
</div>
<jsp:include page="footer.jsp"></jsp:include>