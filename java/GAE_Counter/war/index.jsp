<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	String linkUrl;
	String action;
	if (user != null) {
	    linkUrl = userService.createLogoutURL(request.getRequestURI());
	    action  = "Sign out";
	} else {
	    linkUrl = userService.createLoginURL(request.getRequestURI());
	    action  = "Sign in";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ja" xml:lang="ja">
  <head>
    <title>GAE Counter</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/css/style.css" rel="stylesheet" type="text/css" />
  <title>Insert title here</title>
</head>
<body>
  <h1>GAE Counter (Java version)</h1>
  <div align="right">
<% if (user != null) { %>
    <%= user.getNickname()  %>さん
<% } %>
    <a href="<%= linkUrl %>">
      <%= action %>
    </a>
  </div>
  <div id="main">
    <p><a href="http://gae-counter.appspot.com">Python版はこちら</a></p>
    <div align="center">
        <a href="/manage.jsp">
          管理画面へ
        </a>
    </div>
    <h2>説明</h2>
    <ul>
      <li>Google App Engine(Java版)で動くアクセスカウンターです。</li>
      <li>Googleアカウントでログインすると使えます。</li>
      <li>アクセスされるたびに数字が増加する画像データを提供します。</li>
      <li>アクセス記録を取って簡単なアクセス解析もできます。</li>
    </ul>
    <h2>ソース</h2>
    <p>GitHubで公開しています。</p>
    <p>
      <a href="http://github.com/sugyan/GAE_Counter/tree/master">
        http://github.com/sugyan/GAE_Counter/tree/master
      </a>
    </p>
    <h2>作者</h2>
    <p>すぎゃーん</p>
    <p>
      <a href="http://d.hatena.ne.jp/sugyan/">http://d.hatena.ne.jp/sugyan/</a><br>
      <a href="http://twitter.com/sugyan">http://twitter.com/sugyan</a>
    </p>
  </div>
</body>
</html>