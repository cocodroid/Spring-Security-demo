<%@ page language="java"
	import="java.util.*,org.springframework.security.core.*,org.springframework.security.core.context.*,org.springframework.security.core.userdetails.*"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'admin.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	</head>

	<body>
		欢迎管理员.
		<br>
		<%//获得当前登陆用户对应的对象
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			out.println("username: "+userDetails.getUsername()+"  password: "+userDetails.getPassword() +"  authorities: " + userDetails.getAuthorities()+"<br>");
			
			//获得当前登陆用户所拥有的所有权限GrantedAuthority[]
			Collection< ? extends GrantedAuthority> authorities = userDetails.getAuthorities();
			Iterator it = authorities.iterator();
			out.println("authorities : <br>");
			while(it.hasNext())
			{
				out.println(it.next() +"<br>");
			}
		
		%>
		<div>
			username :
			<sec:authentication property="name" />
		</div>
		<br>
		<a href="j_spring_security_logout">logout</a>
	</body>
</html>
