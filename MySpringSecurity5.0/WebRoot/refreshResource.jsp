<%@page import="org.springframework.context.ApplicationContext"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.beans.factory.FactoryBean"%>


<%@page
	import="org.springframework.security.web.access.intercept.FilterSecurityInterceptor"%>

<%@page
	import="org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource"%>
<% 
	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application); 
	
    FactoryBean factoryBean = (FactoryBean)ctx.getBean("&filterInvocationDefinitionSource"); 
	
	FilterInvocationSecurityMetadataSource fids = (FilterInvocationSecurityMetadataSource)factoryBean.getObject(); 
	
	FilterSecurityInterceptor filter = (FilterSecurityInterceptor)ctx.getBean("filterSecurityInterceptor"); 

	filter.setSecurityMetadataSource(fids); 
	%>
<jsp:forward page="/" />