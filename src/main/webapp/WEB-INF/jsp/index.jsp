<!DOCTYPE HTML>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="css/mogwai.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="css/decisionlog.css" media="screen"/>
<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="js/mogwai.js"></script>
<html>

<head>


</head>

<body>
<div class="html l-app">
    <div class="body l-app-page">
        <div class="l-app-page">
            <div class="app-header">
                <div class="app-header-left">
                    <img src="gfx/page-header-logo.png" class="app-header-logo"/>

                    <div class="app-header-btn app-header-btn--pasive-icon-by-overlay is-active">
                        <div class="app-header-btn-icon-active">
                            <div class="icon icon--small icon--dashboard-invert"></div>
                        </div>
                        <div class="app-header-btn-label">DASHBOARD</div>
                    </div>
                    <div class="app-header-btn app-header-btn--pasive-icon-by-overlay">
                        <div class="app-header-btn-icon-active">
                            <div class="icon icon--small icon--dashboard-invert"></div>
                        </div>
                        <div class="app-header-btn-label">CREATE NEW DECISION</div>
                    </div>

                </div>
            </div>

            <div class="textbox textbox--search">
                <input type="text" placeholder="FullText Search "/>
            </div>

            <%-- === DECISIONS . DECISIONS . DECISIONS . DECISIONS . DECISIONS . DECISIONS . DECISIONS  === --%>


            <table class="table table--collapsible-rows">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Subject</th>
                    <th>Summary</th>
                    <th>Relevancy</th>
                </tr>
                </thead>
                <tbody>
                <%--this.subject = subject;--%>
                <%--this.reason = reason;--%>
                <%--this.conclusions = conclusions;--%>
                <%--this.date = date;--%>
                <%--this.attendees = attendees;--%>
                <%--this.tags = tags;--%>
                <%--relevance--%>
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
                        <td class="relevancy">
                            <div class="wrapper">
                                <c:out value="${decision.relevance}"/>
                                    <%--&#9733;&#9733;&#9733;&#9733;&#9733;--%>
                                <div class="hider"></div>
                            </div>
                        </td>
                    </tr>

                    <tr class="table-collapsible-row-body">
                        <td colspan="4">
                            <div class="content">
                                <div class="detail">
                                    <div class="heading">

                                    </div>
                                    <div class="content">
                                        <c:out value="${decision.reason}"/>

                                    </div>
                                </div>
                                <div class="detail">
                                    <div class="heading">

                                    </div>
                                    <div class="content">
                                        <c:out value="${decision.conclusions}"/>

                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>


        </div>
    </div>
</div>
</body>


</html>