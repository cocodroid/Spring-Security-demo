	SpringSecurity1.0
	1.自定义用户表结构
	2.自定义登陆界面
    3.数据库读取资源
   
问题：
	系统会在初始化时一次将所有资源加载到内存中，即使在数据库中修改了资源信息，
	系统也不会再次去从数据库中读取资源信息。这就造成了每次修改完数据库后，
	都需要重启系统才能时资源配置生效。
	
解决：
	如果数据库中的资源出现的变化，需要刷新内存中已加载的资源信息时，refreshResource.jsp
	<%@page import="org.springframework.context.ApplicationContext"%> 
	<%@page 
	import="org.springframework.web.context.support.WebApplicationContextUtils"%> 
	<%@page import="org.springframework.beans.factory.FactoryBean"%> 
	<%@page 
	import="org.springframework.security.intercept.web.FilterSecurityInterceptor"%> 
	<%@page 
	import="org.springframework.security.intercept.web.FilterInvocationDefinitionSource
	"%> 
	<% 
	ApplicationContext ctx = 
	WebApplicationContextUtils.getWebApplicationContext(application); 
	FactoryBean factoryBean = (FactoryBean) 
	ctx.getBean("&filterInvocationDefinitionSource"); 
	FilterInvocationDefinitionSource fids = (FilterInvocationDefinitionSource) 
	factoryBean.getObject(); 
	FilterSecurityInterceptor filter = (FilterSecurityInterceptor) 
	ctx.getBean("filterSecurityInterceptor"); 
	filter.setObjectDefinitionSource(fids); 
	%> 
	<jsp:forward page="/"/>
	
	
	
	*4.用户控制信息
		 1)MD5密码加密
		 2)盐值加密
		 3)用户信息缓存
		 4)自定义访问拒绝页面
		 5)动态管理资源结合自定义登录页面
		 6)判断用户是否登录 IS_AUTHENTICATED_FULLY
			<http auto-config='true'> 
			<intercept-url pattern="/admin.jsp" access="ROLE_ADMIN" /> 
			<intercept-url pattern="/**" access="IS_AUTHENTICATED_FULLY" /> 
			</http>
				
	
	
	
	
	
	
	