<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>GAE Counter</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/css/style.css" rel="stylesheet" type="text/css" />
  </head>
  <body>
    <div align="right">
      <c:out value="${user['name']}" />さん
      <a href="${user['url']}">Sign out</a>
    </div>
    <div id="main">
      <p>カウンターは３つまで作れます。</p><c:if test="${fn:length(counters) > 0}">
      <table>
        <tr>
          <th>カウンター名</th>
          <th>作成日</th>
          <th>カウント数</th>
        </tr><c:forEach items="${counters}" var="counter">
        <tr>
          <td>
            <a href="/config/${counter['key']}">
              <c:out value="${counter['name']}" />
            </a>
          </td>
          <td><c:out value="${counter['date']}" /></td>
          <td align="right">
            <a href="/record/${counter['key']}">
              <c:out value="${counter['count']}" />
            </a>
          </td>
        </tr></c:forEach>
      </table></c:if><c:if test="${fn:length(counters) < 3}">
      <h2>新規作成</h2>
      <form method="POST" action="/create">
        <p>
          カウンター名：<input name="name" maxlength="100">
          <input type="submit" value="作成する">
        </p>
      </form></c:if>
    </div>
  </body>
</html>