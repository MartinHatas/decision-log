<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="header.jsp"></jsp:include>


<div class="form-box">
        <form:form action="add-decision" method="post" commandName="decision">

    <label for="subject">Subject</label>

    <div class="textbox">
        <form:input path="subject" placeholder="Short description"/>

    </div>

    <label for="reason">Reason</label>

    <div class="textbox">
        <form:input path="reason" placeholder="reason"/>
    </div>

    <label for="conclusions">Conclusions</label>

    <div class="textbox">
        <form:input path="conclusions" placeholder="conclusions"/>
    </div>

    <label for="attendees">Attendees</label>

    <div class="textbox">
        <form:input path="attendees" placeholder="attendees"/>
    </div>

    <label for="tags">Tags</label>

    <div class="textbox">
        <form:input path="tags" placeholder="tags"/>
    </div>
    <input type="submit" value="Register"/>

            </form:form>
</div>
<jsp:include page="footer.jsp"></jsp:include>
