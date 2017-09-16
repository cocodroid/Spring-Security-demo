package com.sjg.afterinvocation;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;

import com.sjg.handler.AclHandler;


public class CreateAclEntryAfterInvocationProvider
    extends AbstractAclEntryAfterInvocationProvider {
    private static Log logger = LogFactory.getLog(CreateAclEntryAfterInvocationProvider.class);

    public CreateAclEntryAfterInvocationProvider(AclService aclService,
        List<Permission> requirePermission) {
        super(aclService, "AFTER_ACL_CREATE", requirePermission);
    }

    public Object decide(Authentication authentication, Object object,Collection<ConfigAttribute> config,Object returnedObject)
        throws AccessDeniedException {
        //Iterator iter = config.getConfigAttributes().iterator();
        //Iterator iter = (Iterator) attributes;
    	Iterator iter = config.iterator();
        
    	while (iter.hasNext()) {
            ConfigAttribute attr = (ConfigAttribute) iter.next();

            if (this.supports(attr)) {
                Object domainObject = getDomainObjectInstance(object,
                        processDomainObjectClass);

                Iterator cit = this.handlers.iterator();

                while (cit.hasNext()) {
                    AclHandler handler = (AclHandler) cit.next();

                    if (handler.supports(domainObject, returnedObject)) {
                        handler.create(authentication, domainObject,
                             returnedObject);

                        break;
                    }
                }
            }
        }

        return returnedObject;
    }

    public Object getDomainObjectInstance(Object secureObject,
        Class processDomainObjectClass) {
        MethodInvocation invocation = (MethodInvocation) secureObject;

        // Check if this MethodInvocation provides the required argument
        Method method = invocation.getMethod();
        Class[] params = method.getParameterTypes();

        for (int i = 0; i < params.length; i++) {
            if (processDomainObjectClass.isAssignableFrom(params[i])) {
                return invocation.getArguments()[i];
            }
        }

        throw new AuthorizationServiceException("MethodInvocation: "
            + invocation + " did not provide any argument of type: "
            + processDomainObjectClass);
    }
}
