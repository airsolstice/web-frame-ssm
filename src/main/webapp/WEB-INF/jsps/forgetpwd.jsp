<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>忘记密码</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body>
  	<div><font color="red">${error }</font></div>
   	<form action="<c:url value ='/forgetpwd'/>" method="post">
   		email:<input type="text" id="email" name="email" value="${email }"><font color="red">${errors.email }</font><br>
   		新密码:<input type="text" id="pwd" name="pwd" value=""><font color="red">${errors.pwd }</font><br>
    	<input type="submit" value="提交">
   	</form>
  </body>
</html>
