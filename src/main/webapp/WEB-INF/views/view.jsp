<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Logs</title>
</head>
<body>
<h1>
	Log Messages
</h1>

<ul>
<c:forEach var="entry" items="${entries}">
<li>${entry.entry.message}</li>
</c:forEach>
</ul>
</body>
</html>
