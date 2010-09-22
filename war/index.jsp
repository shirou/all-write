<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>all-write</title>
<link rel="stylesheet" type="text/css" href="twocolumn.css" />
<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/all-write.js"></script>
<script type="text/javascript" src="js/jquery.textchange.min.js"></script>

<script type = "text/javascript">
    $(document).ready(function()
    {
    init();
    });
</script>
</head>
<body>

<div id="wrap">
  <div id="header">
All-Write -- Lucene on GAE/J test page --</br>
(if you want to just try, use test/test for login)
  </div>
  <div id="nav">
    <span id="loginbox" >
    email<input id="email" type="text" name="email" value="test" /> 
    pass<input id="password" type="password" name="password" value="test" /> 
    <input id="login" value="login" type="button" onClick="login()" />
    / <a href="register/">Create Account</a>
    </span>
  </div>
  <div id="sidebar">
    <input type="text" id="searchbox" value="" disabled="disabled"/>
    <input id="search" value="search" disabled="disabled" type="button"
    onClick="search()" />
    <input id="clear" value="clear" disabled="disabled" type="button"
    onClick="clearSearchBox()" />
    <table>
      <tbody id="notelist">
      </tbody>
    </table>
  </div>
  <div id="main">
    <input id="post" value="POST" disabled="disabled" type="button"
    onClick="postNote()" />
    <input id="new" value="NEW" disabled="disabled" type="button"
    onClick="newNote()" />
    <input id="delete" value="delete" disabled="disabled" type="button"
    onClick="deleteNote()" />
    <textarea id="maintext" disabled="disabled"></textarea>
  </div>
  <div id="footer">
    <p> Supported
    by <a href="http://code.google.com/appengine/">Google App
    Engine</a>, <a href="http://sites.google.com/site/slim3appengine/">slim3</a>,
    <a href="http://lucene.apache.org/">lucene</a></p>
  </div>
</div>

</body>
</html>

