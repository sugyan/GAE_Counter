<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.PreparedQuery" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%
	DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	String kind = request.getParameter("kind");
	if (kind == null) {
	    response.sendError(400);
	    return;
	}
	Query query = new Query(kind);
	PreparedQuery preparedQuery = datastoreService.prepare(query);
	int count = preparedQuery.countEntities();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Datastore View</title>
	</head>
	<body>
		<h1><%= kind %></h1>
		<h2><%= count %>件</h2>
<%
	if (count > 0) {
		out.println("<table border=\"1\">");
		out.print("<tr>");
		out.print("<th>key</th>");
		// プロパティ名
		List<String> properties =
		    new ArrayList<String>(preparedQuery.asIterator().next().getProperties().keySet());
		for (String property : properties) {
		    out.print("<th>");
			%><c:out value="<%= property %>" /><%
		    out.print("</th>");
		}
		out.println("</tr>");
		for (Entity entity : preparedQuery.asIterable()) {
		    out.print("<tr>");
		    out.print("<td>" + entity.getKey() + "</td>");
			for (String property : properties) {
			    out.print("<td>");
				%><c:out value="<%= entity.getProperty(property) %>" /><%
			    out.print("</td>");
			}
			out.println("</tr>");
		}
		out.println("</table>");
	}
%>
	</body>
</html>