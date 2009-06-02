<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@page import="com.google.appengine.api.datastore.DatastoreService"%>
<%@page import="com.google.appengine.api.datastore.Query"%>
<%@page import="org.sugyan.counter.model.NumberImage"%>
<%@page import="com.google.appengine.api.datastore.PreparedQuery"%>
<%@page import="com.google.appengine.api.datastore.Entity"%>
<%@page import="com.google.appengine.api.datastore.KeyFactory"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>GAE Counter</title>
	</head>
	<body>
		<form method="post" action="/admin/create">
			<input type="text" name="name">
			<input type="submit" value="create">
		</form>
		<br>
		<form method="post" action="/admin/delete">
			<table border="1">
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
				</tr>
<%
	DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	Query query = new Query(NumberImage.KIND);
	PreparedQuery preparedQuery = datastoreService.prepare(query);
	for (Entity entity : preparedQuery.asIterable()) {
	    NumberImage numberImage = new NumberImage(entity);
%>
				<tr>
					<td><input type="checkbox"></td>
					<td><%= numberImage.getName() %></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
<%
	}
%>
			</table>
			<input type="submit" value="delete">
		</form>
		<hr>
		<form method="post" action="/admin/upload" enctype="multipart/form-data">
			<select name="image">
<%
	for (Entity entity : preparedQuery.asIterable()) {
    	NumberImage numberImage = new NumberImage(entity);
%>
				<option value="<%= KeyFactory.keyToString(entity.getKey()) %>"><%= numberImage.getName() %></option>
<%
	}
%>
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
		</form>
	</body>
</html>