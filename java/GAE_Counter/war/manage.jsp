<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.users.User "%>
<%@ page import="com.google.appengine.api.users.UserService "%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory "%>
<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	if (user == null) {
	    String url = userService.createLoginURL(request.getRequestURI());
	    response.sendRedirect(url);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>GAE Counter</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/css/style.css" rel="stylesheet" type="text/css" />
  </head>
  <body>
  カウンターは３つまで作れます。
  <table>
    <tr>
      <th>カウンター名</th>
      <th>作成日</th>
      <th>アクセス数</th>
      <th colspan="2"></th>
    </tr>
   </table>
<!--    ( counters.size() < 3 ?
-->
   <hr>
   <h3>新規作成</h3>
   <form method="POST" action="/create">
     カウンター名：<input name="name">
     <input type="submit" value="作成する">
   </form>
  </body>
</html>