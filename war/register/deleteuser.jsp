<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>alwrite Delete</title>
</head>
<body>

<p>delete</p>
<form method="post" action="deleteuser">
mailaddress<input type="text" name="mailaddress" value="test" /></br>
<input type="submit" value="delete all"/>
</form>

<hr>
<a href="../">Back</a>

</body>
</html>
