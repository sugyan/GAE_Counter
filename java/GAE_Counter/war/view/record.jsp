<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/css/style.css" rel="stylesheet" type="text/css" />
    <title>GAE Counter</title> 
  </head>
  <body>
    <div align="right">
      <c:out value="${user['name']}" />さん
      <a href="${user['url']}">Sign out</a>
    </div>
    <div id="main">
      <h2><c:out value="${counter['name']}" /></h2>
      <table>
        <tr>
          <th>Count</th>
          <th>Date Time</th>
          <th>IP Address</th>
          <th>User Agent</th>
          <th>Referer</th>
        </tr><c:forEach items="${records}" var="record">
        <tr>
          <td align="right"><c:out value="${record['count']}" /></td>
          <td align="left"><c:out value="${record['date']}" /></td>
          <td align="right"><c:out value="${record['remote']}" /></td>
          <td align="left"><c:out value="${record['agent']}" /></td>
          <td align="left">
            <a href="${record['referer_full']}">
              <c:out value="${record['referer']}" />
            </a>
          </td>
        </tr></c:forEach>
      </table>
    </div>
  </body>
</html>
