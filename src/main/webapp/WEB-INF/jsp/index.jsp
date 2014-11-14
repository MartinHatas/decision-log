<!DOCTYPE HTML>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="header.jsp"></jsp:include>


<form:form action="/" method="post" commandName="search">
    <div class="textbox textbox--search">

        <form:input path="text" placeholder="FullText Search"/>

         
    </div>
    <input class="non-css" type="submit" value="Search"/>
</form:form>


<c:if test="savedSuccessfully">
    <div class="alert alert-success alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span
                class="sr-only">Close</span></button>
        Decision record <strong>saved</strong> successfully.
    </div>
</c:if>


<table class="table table--collapsible-rows">
    <thead>
    <tr>
        <th>Date</th>
        <th>Subject</th>
        <th>Reason</th>
        <c:if test="showRelevancy">
            <th>Relevancy</th>
        </c:if>
    </tr>
    </thead>
    <tbody>

    <c:forEach var="decision" items="${decisions}">
        <tr class="table-collapsible-row-header"><%--is-open--%>
            <td class="date">
                <div class="table-collapsible-row-toggle"></div>
                <c:out value="${decision.formattedDate}" escapeXml='false'/>
            </td>
            <td class="subject"><c:out value="${decision.subject}"/></td>
            <td class="summary">
                <div class="wrapper">
                    <c:out value="${decision.reason}"/>
                </div>
            </td>
            <c:if test="showRelevancy">
                <td class="relevancy">
                    <div class="wrapper">
                            <%--<c:out value="${decision.relevance}"/>--%>
                            ${decision.relevance>0.1?"&#9733;":""}${decision.relevance>0.3?"&#9733;":""}${decision.relevance>0.5?"&#9733;":""}${decision.relevance>0.7?"&#9733;":""}${decision.relevance>0.9?"&#9733;":""}
                        <div class="hider"></div>
                    </div>
                </td>
            </c:if>
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

                    <div class="detail">
                        <div class="heading">
                            Attendees:
                        </div>
                        <div class="content attendees">
                            <c:out value="${decision.formattedAttendees}" escapeXml='false'/>

                        </div>
                    </div>

                    <div class="detail">
                        <div class="heading">
                            Tags:
                        </div>
                        <div class="content tags">
                            <c:out value="${decision.formattedTags}" escapeXml='false'/>

                        </div>
                    </div>

                </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>


<c:if test="${empty decisions}">
    <div class="alert alert-warning alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span
                class="sr-only">Close</span></button>
        No decisions found. (Using search query "${search.text}")
    </div>
</c:if>

<c:if test="decisions.size == 0">
    nothing
</c:if>

<jsp:include page="footer.jsp"></jsp:include>