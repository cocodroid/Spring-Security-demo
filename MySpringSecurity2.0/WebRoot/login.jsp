<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户登录</title>
	</head>
	<body onLoad="document.f.j_username.focus();">
	 <div align="center">
		<c:if test="${not empty param.login_error}">
			<font color="red"> 登录失败，请重试.<br /> <br /> 原因:<c:out
					value="${SPRING_SECURITY_LAST_EXCEPTION.message}" /> </font>
		</c:if>
		<form name="f" action="${pageContext.request.contextPath}/j_spring_security_check"
			method="POST" style="width:260px;text-align:center;">
			<table>
				<tr>
					<td>
						用户名:
					</td>
					<td>
						<input type='text' name='j_username' style="width:150px;"
							value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>' />
					</td>
				</tr>
				<tr>
					<td>
						密 码:
					</td>
					<td>
						<input type='password' name='j_password' style="width:150px;">
					</td>
				</tr>
				<tr>
					<td>
						<input type="checkbox" name="_spring_security_remember_me">
					</td>
					<td>
						两周内自动登录
					</td>
				</tr>
				<tr>
					<td colspan='2' align="center">
						<input name="submit" type="submit">
						<input name="reset" type="reset">
					</td>
				</tr>
			</table>
		</form>
		<a href="j_spring_security_logout">logout</a>
		</div>
	</body>
</html>