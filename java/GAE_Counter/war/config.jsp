<%@ page language="java"
		 contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"
		 errorPage="error.jsp"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="org.sugyan.counter.model.Counter" %>
<%!
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    TimeZone zone = TimeZone.getTimeZone("JST");
	UserService userService = UserServiceFactory.getUserService();
	String logoutUrl = userService.createLogoutURL("/");
%>
<%
	User user = null;
	if (userService.isUserLoggedIn()) {
	    user = userService.getCurrentUser();
	} else {
	    response.sendError(403);
	}
	DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	String keyString = request.getParameter("id");
	Key key = KeyFactory.stringToKey(keyString);
	
	Counter counter = new Counter(datastoreService.get(key));
	if (!userService.getCurrentUser().equals(counter.getUser())) {
	    response.sendError(403);
	}
    dateFormat.setTimeZone(zone);	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/css/style.css" rel="stylesheet" type="text/css" />
    <title>GAE Counter</title> 
  </head>
  <body>
    <div align="right">
<% if (user != null) { %>
      <%= user.getNickname()  %>さん
<% } %>
      <a href="<%= logoutUrl %>">Sign out</a>
    </div>
    <div id="main">
      <h2><c:out value="<%= counter.getName() %>" /></h2>
      <table>
        <tr>
          <td align="right">作成日時：</td>
          <td><%= dateFormat.format(counter.getDate()) %></td>
        </tr>
        <tr>
          <td align="right">アクセス数：</td>
          <td><%= counter.getCount() %></td>
        </tr>
        <tr>
          <td align="right">URL：</td>
          <td>
            <a href="/counter/<%= keyString %>.png">
              http://java.latest.gae-counter.appspot.com/counter/<%= keyString %>.png
            </a><br />
            <a href="/counter/<%= keyString %>.jpg">
              http://java.latest.gae-counter.appspot.com/counter/<%= keyString %>.jpg
            </a><br />
          </td>
        </tr>
      </table>
      <form method="POST" action="/destroy">
        <input type="hidden" name="key" value="<%= keyString %>">
        <p>
          <input type="submit" value="削除する"
                 onclick="javascript:return confirm('本当に削除しますか？') ? true : false">
        </p>
      </form>
    </div>
  </body>
</html>
