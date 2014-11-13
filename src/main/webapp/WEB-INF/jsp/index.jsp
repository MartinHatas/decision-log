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

            <%--this.subject = subject;--%>
            <%--this.reason = reason;--%>
            <%--this.conclusions = conclusions;--%>
            <%--this.date = date;--%>
            <%--this.attendees = attendees;--%>
            <%--this.tags = tags;--%>
            <%--relevance--%>

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
                <tr class="table-collapsible-row-header "><%--is-open--%>
                    <td class="date">
                        <div class="table-collapsible-row-toggle"></div>
                        file.pdf
                    </td>
                    <td class="subject">Decision Log</td>
                    <td class="summary">
                        <div class="wrapper">
                            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Amet architecto, delectus
                            distinctio doloribus excepturi facere, fugiat ipsam laudantium, magni odit omnis
                            perspiciatis quisquam saepe. Accusantium dicta ducimus, eligendi ex excepturi totam veniam?
                            Accusamus asperiores aut doloribus exercitationem illo non officia! Amet deserunt doloribus
                            nam obcaecati omnis sed tenetur! Dicta, iusto.
                        </div>
                    </td>
                    <td class="relevancy">
                        <div class="wrapper">
                            &#9733;&#9733;&#9733;&#9733;&#9733;
                            <div class="hider"></div>
                        </div>
                    </td>
                </tr>
                `
                <tr class="table-collapsible-row-body">
                    <td colspan="3">
                        <div class="content">Lorem ipsum Consectetur sint in in minim magna magna elit nulla elit magna
                            labore laborum fugiat in magna proident labore cupidatat voluptate veniam eiusmod anim
                            cupidatat
                            sint voluptate ullamco dolore labore dolore pariatur anim ad cupidatat
                            ut culpa. Lorem ipsum Ut ut exercitation quis dolor aliqua commodo laboris deserunt
                            exercitation
                            in ex cupidatat ut Duis ad.
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
            <%--<table>--%>
            <%--<c:forEach var="decision" items="${decisions}" >--%>
            <%--<tr>--%>
            <%--<td><c:out value="${decision.name}"/></td>--%>
            <%--</tr>--%>
            <%--</c:forEach>--%>
            <%--</table>--%>


            <%--<div class="l-view-content-with-footer">--%>
            <%--<div class="l-view-content-with-footer-body">Content</div>--%>
            <%--<div class="l-view-content-with-footer-footer">Footer</div>--%>
            <%--</div>--%>

        </div>
    </div>
</div>
</body>


</html>