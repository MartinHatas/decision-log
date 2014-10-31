
<!DOCTYPE HTML>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>

    <body>
        <h1>Decision log</h1>


        <table>
            <c:forEach var="decision" items="${decisions}" >
                <tr>
                    <td><c:out value="${decision.name}"/></td>
                </tr>
            </c:forEach>

        </table>

    </body>





</html>