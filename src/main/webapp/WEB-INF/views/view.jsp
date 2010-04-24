<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!doctype html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Logs</title>
<!--<script type="text/javascript" language="javascript"-->
<!--	src="cc.wily.logviewer.presentation.viewer.nocache.js"></script>-->
</head>
<body>
<h1>Log Messages</h1>

<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
	style="position: absolute; width: 0; height: 0; border: 0"></iframe>

<div id="labelContainer"></div>

<ul>
	<c:forEach var="entry" items="${entries}">
		<li>${entry.entry.message}</li>
	</c:forEach>
</ul>
</body>
</html>
