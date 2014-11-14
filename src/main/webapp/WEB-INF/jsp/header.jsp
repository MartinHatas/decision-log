<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<link rel="stylesheet" type="text/css" href="css/mogwai.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="css/decisionlog.css" media="screen"/>
<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/mogwai.js"></script>
<script type="text/javascript" src="js/decisionlog.js"></script>
<html>

<head>


</head>

<body>
<div class="html l-app">
    <div class="body l-app-page">
        <div class="l-app-page">
            <div class="app-header">
                <div class="app-header-left">
                    <img src="gfx/decision.png" class="app-header-logo"/>

                    <div class="app-header-btn app-header-btn--pasive-icon-by-overlay${pageContext.request.servletPath=='/WEB-INF/jsp/index.jsp'?' is-active':''}">
                        <div class="app-header-btn-icon-active">
                            <div class="icon icon--small icon--dashboard-invert"></div>
                        </div>
                        <a class="app-header-btn-label" href="/">DASHBOARD</a>
                    </div>
                    <div class="app-header-btn app-header-btn--pasive-icon-by-overlay${pageContext.request.servletPath=='/WEB-INF/jsp/add.jsp'?' is-active':''}">
                        <div class="app-header-btn-icon-active">
                            <div class="icon icon--small icon--add"></div>
                        </div>
                        <a href="/add-decision" class="app-header-btn-label">CREATE NEW DECISION</a>
                    </div>

                </div>
            </div>
