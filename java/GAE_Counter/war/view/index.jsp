<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ja" xml:lang="ja">
  <head>
    <title>GAE Counter</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/css/style.css" rel="stylesheet" type="text/css" />
  </head>
  <body>
    <h1><a href="/">GAE Counter</a></h1>
    <div align="right"><c:if test="${user['name'] != null}">
      <c:out value="${user['name']}" />さん</c:if>
      <a href="${user['url']}">
        <c:out value="${user['action']}" />
      </a>
    </div>
    <div id="main">
      <div align="center">
        <a href="/home">
          <strong>管理画面へ</strong>
        </a>
      </div>
      <h2>説明</h2>
      <ul>
        <li>Google App Engine(Java版)で動くアクセスカウンターです。(Python版も作成中)</li>
        <li>Googleアカウントでログインすると使えます。</li>
        <li>アクセスされるたびに数字が増加する画像データを提供します。</li>
        <li>アクセス記録を取って簡単なアクセス解析もできます。</li>
      </ul>
      <h2>ソースコード</h2>
      <p>GitHubで公開しています。ご自由に改造／改良してください。</p>
      <p>
        <a href="http://github.com/sugyan/GAE_Counter/tree/master">
          http://github.com/sugyan/GAE_Counter/tree/master
        </a>
      </p>
      <h2>作者</h2>
      <p>
        すぎゃーん<br>
        <a href="http://d.hatena.ne.jp/sugyan/">http://d.hatena.ne.jp/sugyan/</a><br>
        <a href="http://twitter.com/sugyan">http://twitter.com/sugyan</a>
      </p>
    </div>
  </body>
</html>