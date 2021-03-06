<%@page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <title>edit</title>
  </head>
  <body>
    <table width="100%">
      <tr>
        <td><a href="permission.do?action=list">Back</a></td>
        <td align="right">
          username: <sec:authentication property="name"/>
          |
          <a href="j_spring_security_logout">logout</a>
        </td>
      </tr>
    </table>
    <hr>
    <fieldset>
      <legend>Permission Info</legend>
      <form method="post" action="permission.do?action=${param.action == 'create' ? 'save' : 'save'}">
        <input type="hidden" name="id" value="${param.id}">
        <input type="hidden" name="clz" value="${param.clz}">
        <table>
          <tr>
            <td>Recipient:</td>
            <td>
              <select name="recipient">
                <option value="user">user</option>
                <option value="admin">admin</option>
                <option value="ROLE_USER">ROLE_USER</option>
                <option value="ROLE_ADMIN">ROLE_ADMIN</option>
              </select>
            </td>
          </tr>
          <tr>
            <td>Permission:</td>
            <td>
              <select name="permission">
                <option value="16">administration</option>
                <option value="1">read</option>
                <option value="8">delete</option>
              </select>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <input type="submit">
              <input type="reset">
            </td>
          </tr>
        </table>
      </form>
    </fieldset>
  </body>
</html>