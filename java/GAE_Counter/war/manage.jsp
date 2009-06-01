<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.PreparedQuery" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Query.FilterOperator" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="org.sugyan.counter.model.Counter" %>
<%!
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	TimeZone zone = TimeZone.getTimeZone("JST");
	UserService userService = UserServiceFactory.getUserService();
	String logoutUrl = userService.createLogoutURL("/");
%>
<%
	DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	dateFormat.setTimeZone(zone);
	User user = userService.getCurrentUser();

    Query query = new Query(Counter.KIND)
    	.addFilter(Counter.USER, FilterOperator.EQUAL, user)
    	.addFilter(Counter.ACTIVE, FilterOperator.EQUAL, Boolean.TRUE);
    
    PreparedQuery counterQuery = datastoreService.prepare(query);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>GAE Counter</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/css/style.css" rel="stylesheet" type="text/css" />
  </head>
  <body>
    <div align="right">
<% if (user != null) { %>
      <%= user.getNickname()  %>さん
<% } %>
      <a href="<%= logoutUrl %>">Sign out</a>
    </div>
    <div id="main">
      <p>カウンターは３つまで作れます。</p>
<%
	if (counterQuery.countEntities() > 0) {
%>      
      <table>
        <tr>
          <th>カウンター名</th>
          <th>作成日</th>
          <th>カウント数</th>
        </tr>
<%
		for (Entity entity : counterQuery.asIterable()) {
	    	Key key = entity.getKey();
	    	Counter counter = new Counter(entity);
%>
        <tr>
          <td>
            <a href="/config.jsp?id=<%= KeyFactory.keyToString(key) %>">
              <c:out value="<%= counter.getName() %>" />
            </a>
          </td>
          <td><%= dateFormat.format(counter.getDate()) %></td>
          <td align="right">
            <a href="/record.jsp?id=<%= KeyFactory.keyToString(key) %>">
              <%= counter.getCount() %>
            </a>
          </td>
        </tr>
<%
		}
%>    
      </table>
<%
	}
%>
<%
	if (counterQuery.countEntities() < 3) {
%>
      <h2>新規作成</h2>
      <form method="POST" action="/create">
        <p>
          カウンター名：<input name="name">
          <input type="submit" value="作成する">
        </p>
      </form>
<%
	}
%>
    </div>
  </body>
</html>