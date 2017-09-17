package com.sjg.springsecurity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.AnyRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;


public class JdbcFilterInvocationDefinitionSourceFactoryBean extends JdbcDaoSupport implements FactoryBean {

	private String resourceQuery;

	public boolean isSingleton() {
		return true;
	}

	public Class getObjectType() {
		// return FilterInvocationDefinitionSource.class; // 3.x之前的版本使用
		return FilterInvocationSecurityMetadataSource.class;
	}

	public Object getObject() {
//		return new DefaultFilterInvocationDefinitionSource(this.getUrlMatcher(), this.buildRequestMap());
		DefaultFilterInvocationSecurityMetadataSource definitionSource = new DefaultFilterInvocationSecurityMetadataSource(this.buildRequestMap());
		System.out.println("~~~~definitionSource:~~~"+definitionSource);
		return definitionSource;
	}

	protected Map<String, String> findResources() {
		ResourceMapping resourceMapping = new ResourceMapping(getDataSource(), resourceQuery);
		Map<String, String> resourceMap = new LinkedHashMap<String, String>();
		for (Resource resource : (List<Resource>) resourceMapping.execute()) {
			String url = resource.getUrl();
			String role = resource.getRole();
			System.out.println("resourceQuery:~~~"+resourceQuery);
			System.out.println("url:````"+url+" ; role:`````"+role);
			if (resourceMap.containsKey(url)) {
				String value = resourceMap.get(url);
				resourceMap.put(url, value + "," + role);
			} else {
				resourceMap.put(url, role);
			}
		}
		return resourceMap;
	}

	/*
	 * 
	 * protected LinkedHashMap<RequestKey, ConfigAttributeDefinition> buildRequestMap() { LinkedHashMap<RequestKey, ConfigAttributeDefinition> requestMap = null;
	 * 
	 * requestMap = new LinkedHashMap<RequestKey, ConfigAttributeDefinition>(); ConfigAttributeEditor editor = new ConfigAttributeEditor(); Map<String, String> resourceMap = this.findResources(); for (Map.Entry<String, String> entry : resourceMap.entrySet()) { RequestKey key = new RequestKey(entry.getKey(), null); editor.setAsText(entry.getValue()); requestMap.put(key, (ConfigAttributeDefinition) editor.getValue()); } return requestMap; }
	 */
	protected LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> buildRequestMap() {
		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = null;
		requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
		
//		PropertyEditorSupport editor = new PropertyEditorSupport();
		Map<String, String> resourceMap = this.findResources();
		for (Map.Entry<String, String> entry : resourceMap.entrySet()) {
			RequestMatcher key = new AntPathRequestMatcher(entry.getKey(), null);
//			editor.setAsText(entry.getValue());
			Collection<ConfigAttribute> attrs = new ArrayList<ConfigAttribute>();
			attrs = SecurityConfig.createListFromCommaDelimitedString(entry.getValue());
			System.out.println("key:~~~~"+key);
			requestMap.put(key,attrs);
//			requestMap.put(key, (Collection<ConfigAttribute>) editor.getValue());
		}
		System.out.println("requestMap:~~~~"+requestMap);
		return requestMap;
	}

	/**
	 * protected UrlMatcher getUrlMatcher() { return new AntUrlPathMatcher(); }
	 */
	/**
     * 提供Ant Style的URLMatcher.
    */
    protected RequestMatcher getUrlMatcher() {
    	System.out.println("new AnyRequestMatcher()"+new AnyRequestMatcher());
        return new AnyRequestMatcher();
    }

	public void setResourceQuery(String resourceQuery) {
		this.resourceQuery = resourceQuery;
	}

}