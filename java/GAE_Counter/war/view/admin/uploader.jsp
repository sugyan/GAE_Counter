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
    <div id="main">
      <h2>新規作成</h2>
        <form method="post" action="/admin/uploader?action=CREATE">
          <p>
            <input type="text" name="name">
            <input type="submit" value="create">
          </p>
        </form>
      <h2>確認／削除</h2>
      <form method="post" action="/admin/uploader?action=DELETE">
        <table>
          <tr>
            <th></th>
            <th>name</th>
            <th>0</th>
            <th>1</th>
            <th>2</th>
            <th>3</th>
            <th>4</th>
            <th>5</th>
            <th>6</th>
            <th>7</th>
            <th>8</th>
            <th>9</th>
          </tr><c:forEach items="${images}" var="image">
          <tr>
            <td><input type="checkbox" name="delete" value="${image['key']}"></td>
            <td><c:out value="${image['name']}" /></td><c:forEach begin="0" end="9" var="num">
            <td><img src="/admin/view/${image['key']}/${num}"></td></c:forEach>
          </tr></c:forEach>
        </table>
        <p><input type="submit" value="delete"></p>
      </form>
      <h2>編集</h2>
      <form method="post" action="/admin/uploader?action=UPLOAD" enctype="multipart/form-data">
        <p>
          <select name="image"><c:forEach items="${images}" var="image">
            <option value="${image['key']}"><c:out value="${image['name']}" /></option></c:forEach>
          </select>
          <br>
          <select name="num">
            <option value="0">0</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
            <option value="6">6</option>
            <option value="7">7</option>
            <option value="8">8</option>
            <option value="9">9</option>
          </select>
          <input type="file" name="file">
          <input type="submit">
        </p>
      </form>
    </div>
  </body>
</html>