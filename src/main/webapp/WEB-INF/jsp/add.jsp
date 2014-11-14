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
        <form:input path="subject" placeholder="Short description"  required="required"/>

    </div>

    <label for="reason">Reason</label>

    <div>
        <form:textarea path="reason" placeholder="Why we need decision" class="textbox" rows="5"/>
    </div>

    <label for="conclusions">Conclusions</label>

    <div>
        <form:textarea path="conclusions" placeholder="Why we decided for ..." class="textbox" rows="5"/>
    </div>

    <label for="attendees">Attendees</label>

    <div class="textbox">
        <form:input path="attendees" placeholder="Who to ask about details"/>
    </div>

    <label for="tags">Tags</label>

    <div class="textbox">
        <form:input path="tags" placeholder="Tags"/>
    </div>

    <input type="submit" value="SAVE RECORD" class="btn btn-light new-rdecision-submit" />

            </form:form>
</div>
<jsp:include page="footer.jsp"></jsp:include>
