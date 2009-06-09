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
          <td align="right">作成日時：</td>
          <td><c:out value=" ${counter['date']}" /></td>
        </tr>
        <tr>
          <td align="right">アクセス数：</td>
          <td><c:out value="${counter['count']}" /></td>
        </tr>
        <tr>
          <td align="right">URL：</td>
          <td>
            <a href="/counter/${counter['key']}.PNG">
              http://java.latest.gae-counter.appspot.com/counter/<c:out value="${counter['key']}" />.PNG
            </a><br>
            <a href="/counter/${counter['key']}.JPEG">
              http://java.latest.gae-counter.appspot.com/counter/<c:out value="${counter['key']}" />.JPEG
            </a><br>
          </td>
        </tr>
        <tr>
          <td align="right">デザイン：</td>
          <td>
            <form method="post" action="/update">
              <input type="hidden" name="key" value="${counter['key']}">
              <select name="image"><c:forEach items="${images}" var="image">
                <option value="${image['key']}">
                  <c:out value="${image['name']}" />
                </option></c:forEach>
              </select>
              <input type="submit" value="変更する">
            </form>
          </td>
        </tr>
      </table>
      <form method="post" action="/delete">
        <input type="hidden" name="key" value="${counter['key']}">
        <p>
          <input type="submit" value="削除する"
                 onclick="javascript:return confirm('本当に削除しますか？') ? true : false">
        </p>
      </form>
    </div>
  </body>
</html>
