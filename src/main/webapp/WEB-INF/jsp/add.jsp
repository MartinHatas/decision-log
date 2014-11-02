<!DOCTYPE HTML>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>

<head>

    <script src="webjars/polymer-platform/0.4.1/platform.js" ></script>

    <link rel="import" href="components/font-roboto/roboto.html">
    <link rel="import" href="components/paper-input/paper-input.html">
</head>

<body>

<div class="decisions" layout vertical flex>
    <h1>Add new decision</h1>
    <div><paper-input floatingLabel="Name"></paper-input></div>
    <div><paper-input floatingLabel="Date"></paper-input></div>
    <div><paper-input floatingLabel="Problem"></paper-input></div>
    <div><paper-input floatingLabel="Conclusion"></paper-input></div>
    <div><paper-input floatingLabel="Attendees"></paper-input></div>
    <div><paper-input floatingLabel="Tags"></paper-input></div>

</div>



</body>
</html>
