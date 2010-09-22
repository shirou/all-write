<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Register</title>
</head>
<body>
<p>register</p>
<form method="post" action="register">
mailaddress<input type="text" name="mailaddress" value="test" /></br>
password<input type="password" name="password" /></br>
<input type="submit" value="register"/>
</form>
<hr>
<a href="../">Back</a>

</body>
</html>
