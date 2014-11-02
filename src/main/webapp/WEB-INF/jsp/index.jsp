<!DOCTYPE HTML>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>

<head>

    <script src="webjars/polymer-platform/0.4.1/platform.js" ></script>

    <link rel="import" href="components/font-roboto/roboto.html">
    <link rel="import" href="components/core-toolbar/core-toolbar.html">
    <link rel="import" href="components/core-header-panel/core-header-panel.html">
    <link rel="import" href="components/core-icons/core-icons.html">
    <link rel="import" href="components/paper-icon-button/paper-icon-button.html">
    <link rel="import" href="components/decision-card/decision-card.html">

    <style>

        html,body {
            height: 100%;
            margin: 0;
            background-color: #E5E5E5;
            font-family: 'RobotoDraft', sans-serif;
        }

        core-toolbar {
            background-color: #03A9F4;
        }
        decision-card {
            margin-bottom: 30px;
        }
        .container {
            width: 80%;
            margin: 50px auto;
        }


    </style>

</head>

<body fullbleed vertical layout unresolved>
    <core-header-panel flex>

        <core-toolbar id="mainheader">
            <span flex>Project Decision log</span>
            <paper-icon-button id="searchbutton" icon="search"></paper-icon-button>
        </core-toolbar>

        <div class="container" layout vertical center>

            <decision-card>
                <h2>Dlouhé transakce</h2>
                <p><strong>Date:</strong> 21.10.2014</p>
                <p><strong>Problem:</strong> Máme příliš dlouhé transakce, co s tím budem dělat?</p>
                <p><strong>Conscusions: </strong> Máme příliš dlouhé transakce, co s tím budem dělat?</p>
                <p><strong>Attendees: </strong> Hynek, Martin, Vašek, Ondra, Lucka</p>
                <p><strong>Tags: </strong> #Transakce #Databáze #Performance #Peaks</p>
            </decision-card>

        </div>
    </core-header-panel>
</body>

<%--<table>--%>
<%--<c:forEach var="decision" items="${decisions}" >--%>
<%--<tr>--%>
<%--<td><c:out value="${decision.name}"/></td>--%>
<%--</tr>--%>
<%--</c:forEach>--%>

<%--</table>--%>


</html>