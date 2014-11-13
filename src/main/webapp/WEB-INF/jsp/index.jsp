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
<div class="l-app">
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


        <table class="table table--collapsible-rows">
            <thead>
            <tr>
                <th>Source File</th>
                <th>Format</th>
                <th>Upload Date</th>
            </tr>
            </thead>
            <tbody>
            <tr class="table-collapsible-row-header is-open">
                <td>
                    <div class="table-collapsible-row-toggle"></div>
                    file.pdf
                </td>
                <td>pdf</td>
                <td>3/10/2012</td>
            </tr>
            <tr class="table-collapsible-row-body">
                <td colspan="3">
                    <div class="content">Lorem ipsum Consectetur sint in in minim magna magna elit nulla elit magna
                        labore laborum fugiat in magna proident labore cupidatat voluptate veniam eiusmod anim cupidatat
                        sint voluptate ullamco dolore labore dolore pariatur anim ad cupidatat
                        ut culpa. Lorem ipsum Ut ut exercitation quis dolor aliqua commodo laboris deserunt exercitation
                        in ex cupidatat ut Duis ad.
                    </div>
                </td>
            </tr>
            <tr class="table-collapsible-row-header is-containing-error is-open">
                <td>
                    <div class="table-collapsible-row-toggle"></div>
                    file.pdf
                </td>
                <td>pdf</td>
                <td>3/10/2012</td>
            </tr>
            <tr class="table-collapsible-row-body">
                <td colspan="3">
                    <div class="content">Lorem ipsum Consectetur sint in in minim magna magna elit nulla elit magna
                        labore laborum fugiat in magna proident labore cupidatat voluptate veniam eiusmod anim cupidatat
                        sint voluptate ullamco dolore labore dolore pariatur anim ad cupidatat
                        ut culpa. Lorem ipsum Ut ut exercitation quis dolor aliqua commodo laboris deserunt exercitation
                        in ex cupidatat ut Duis ad.
                    </div>
                </td>
            </tr>
            <tr class="table-collapsible-row-header">
                <td>
                    <div class="table-collapsible-row-toggle"></div>
                    file.pdf
                </td>
                <td>pdf</td>
                <td>3/10/2012</td>
            </tr>
            <tr class="table-collapsible-row-body">
                <td colspan="3">
                    <div class="content">Lorem ipsum Consectetur sint in in minim magna magna elit nulla elit magna
                        labore laborum fugiat in magna proident labore cupidatat voluptate veniam eiusmod anim cupidatat
                        sint voluptate ullamco dolore labore dolore pariatur anim ad cupidatat
                        ut culpa. Lorem ipsum Ut ut exercitation quis dolor aliqua commodo laboris deserunt exercitation
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


    </div>
</div>
</body>


</html>