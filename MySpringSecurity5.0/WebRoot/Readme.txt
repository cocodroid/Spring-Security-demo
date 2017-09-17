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
	
	
	
	4.用户控制信息
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
		
		
		
	 5.	管理会话
		多个用户不能使用同一个账号同时登陆系统
		1)添加监听器
				监听session生命周期的监听器主要用来收集在线用户的信息，比如统计在线用户数之类的事。
	    2)添加过滤器
	            <concurrent-session-control/>
	            3.0的:
	            	<session-management>
         				<concurrency-control />
        		    </session-management>
	    3)控制策略
	    		默认：后登陆的将先登录的踢出系统
				  
				后面的用户禁止登陆
				<concurrent-session-control exception-if-maximum-exceeded="true"/> 
				这个参数用来控制是否在会话数目超过最大限制时抛出异常，默认值是
				false，也就是不抛出异常，而是把之前的session都销毁掉，所以之前
				登陆的用户就会被踢出系统了。

	*6.单点登录SSO
		把N个应用的登录系统整合在一起，这样一来无论用户登录了任何一个应用，都可以直接以登录过
		的身份访问其他应用，不用每次访问其他系统再去登陆一遍了。
	  
	    1). 添加依赖
			<dependency> 
			<groupId>org.springframework.security</groupId> 
			<artifactId>spring-security-cas-client</artifactId> 
			<version>2.0.5.RELEASE</version> 
			</dependency> 
			
			spring-security-cas-client-2.0.5.RELEASE.jar 
			spring-dao-2.0.8.jar 
			aopalliance-1.0.jar 
			cas-client-core-3.1.5.jar 
			
		2). 修改applicationContext.xml	
			<http auto-config='true' entry-point-ref="casProcessingFilterEntryPoint">
			<intercept-url pattern="/admin.jsp" access="ROLE_ADMIN" /> 
			<intercept-url pattern="/index.jsp" access="ROLE_USER" /> 
			<intercept-url pattern="/" access="ROLE_USER" /> 
			<logout logout-success-url="/cas-logout.jsp"/>
			</http> 
			
			添加一个 entry-point-ref 引用 cas 提供的
			casProcessingFilterEntryPoint，这样在验证用户登录时就用上 cas 提供的机制了。 
			修改注销页面，将注销请求转发给 cas 处理。 
			<a href="https://localhost:9443/cas/logout">Logout of CAS</a>
						