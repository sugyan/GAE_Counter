<%@ page language="java"
		 contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"
		 errorPage="error.jsp"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="org.sugyan.counter.model.AccessRecordComparator" %>
<%@ page import="org.sugyan.counter.model.Counter" %>
<%@ page import="org.sugyan.counter.model.JavaAccessRecord" %>
<%@ page import="org.sugyan.counter.PMF" %>
<%!
	int limitLength = 40;
	UserService userService = UserServiceFactory.getUserService();
	String logoutUrl = userService.createLogoutURL("/");
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    TimeZone zone = TimeZone.getTimeZone("JST");
%>
<%
	User user = null;
	if (userService.isUserLoggedIn()) {
	    user = userService.getCurrentUser();
	} else {
	    response.sendError(403);
	}
	Key key = KeyFactory.stringToKey(request.getParameter("id"));
	PersistenceManager pm = PMF.get().getPersistenceManager();
	Counter counter = pm.getObjectById(Counter.class, key);
	User currentUser = userService.getCurrentUser();
	if (!currentUser.equals(counter.getUser())) {
	    response.sendError(403);
	}
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
          <th>Count</th>
          <th>Date Time</th>
          <th>IP Address</th>
          <th>User Agent</th>
          <th>Referer</th>
        </tr>
<%
	format.setTimeZone(zone);
	List<JavaAccessRecord> records = counter.getRecords();
	Collections.sort(records, 
	        Collections.reverseOrder(new AccessRecordComparator.DateComparator()));
	for (JavaAccessRecord record : records) {
		String userAgent = record.getUserAgent();
		String referer   = record.getReferer().toString();
		if (userAgent.length() > limitLength) {
		    userAgent = userAgent.substring(0, limitLength) + "...";
		}
		if (referer.length() > limitLength) {
		    referer = referer.substring(0, limitLength) + "...";
		}
%>
        <tr>
          <td align="right"><%= record.getCount() %></td>
          <td align="left"><%= format.format(record.getDateTime()) %></td>
          <td align="right"><%= record.getRemoteAddr() %></td>
          <td align="left"><%= userAgent %></td>
          <td align="left">
            <a href="<%= record.getReferer() %>">
              <%= referer %>
            </a>
          </td>
        </tr>
<%
	}
%>
      </table>
    </div>
  </body>
</html>
