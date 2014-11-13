<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<jsp:include page="header.jsp"></jsp:include>


    <form:form action="add-decision" method="post" commandName="decision"><table border="0">
                <tr>
                    <td colspan="2" align="center"><h2>Spring MVC Form Demo - Registration</h2></td>
                </tr>
                <tr>
                    <td>User Name:</td>
                    <td><form:input path="subject" /></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><form:input path="reason" /></td>
                </tr>
                <tr>
                    <td>E-mail:</td>
                    <td><form:input path="conclusions" /></td>
                </tr>
                <tr>
                    <td>Profession:</td>
                    <td><form:input path="attendees" /></td>
                </tr>
                <tr>
                    <td>Profession:</td>
                    <td><form:input path="tags" /></td>
                </tr>
                <tr>
                    <td colspan="2" align="center"><input type="submit" value="Register" /></td>
                </tr>
            </table>
        </form:form>

<jsp:include page="footer.jsp"></jsp:include>
