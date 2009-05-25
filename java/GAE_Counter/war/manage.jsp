<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="javax.jdo.Query" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="org.sugyan.counter.PMF" %>
<%@ page import="org.sugyan.counter.model.Counter" %>
<%!
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	TimeZone zone = TimeZone.getTimeZone("JST");
	UserService userService = UserServiceFactory.getUserService();
	String logoutUrl = userService.createLogoutURL("/");
%>
<%
	dateFormat.setTimeZone(zone);
	User user = userService.getCurrentUser();
	
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery("SELECT FROM " + Counter.class.getName());
    query.setFilter("user == currentUser");
    query.declareParameters("com.google.appengine.api.users.UserService currentUser");
	List<?> counters = (List<?>)query.execute(user);
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
	if (counters.size() > 0) {
%>      
      <table>
        <tr>
          <th>カウンター名</th>
          <th>作成日</th>
          <th>カウント数</th>
        </tr>
<%
		for (Object obj : counters) {
	    	Counter counter = (Counter)obj;
%>
        <tr>
          <td>
            <a href="/config.jsp?id=<%= counter.getEncodedKey() %>">
              <c:out value="<%= counter.getName() %>" />
            </a>
          </td>
          <td><%= dateFormat.format(counter.getDate()) %></td>
          <td align="right">
            <a href="/record.jsp?id=<%= counter.getEncodedKey() %>">
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
	if (counters.size() < 3) {
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