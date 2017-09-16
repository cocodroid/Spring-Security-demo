package com.sjg.voter;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.acls.AclEntryVoter;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.Permission;


public class IdParameterAclEntryVoter extends AclEntryVoter {
    public IdParameterAclEntryVoter(AclService aclService,
        String processConfigAttribute, Permission[] requirePermission) {
        super(aclService, processConfigAttribute, requirePermission);
    }

    @Override
    protected Object getDomainObjectInstance(Object secureObject) {
        Object[] args;
        Class[] params;

        if (secureObject instanceof MethodInvocation) {
            MethodInvocation invocation = (MethodInvocation) secureObject;
            params = invocation.getMethod().getParameterTypes();
            args = invocation.getArguments();
        } else {
            JoinPoint jp = (JoinPoint) secureObject;
            params = ((CodeSignature) jp.getStaticPart().getSignature())
                .getParameterTypes();
            args = jp.getArgs();
        }

        for (int i = 0; i < params.length; i++) {
            if (getProcessDomainObjectClass().isAssignableFrom(params[i])) {
                return args[i];
            }
        }

        MethodInvocation invocation = (MethodInvocation) secureObject;
        Method method = invocation.getMethod();

        Serializable id = null;

        for (int i = 0; i < params.length; i++) {
            if (Serializable.class.isAssignableFrom(params[i])) {
                id = (Serializable) invocation.getArguments()[i];

                break;
            }
        }

        if (id == null) {
            throw new AuthorizationServiceException("MethodInvocation: "
                + invocation + " did not provide any ID argument.");
        }

        if (method.isAnnotationPresent(AclDomainAware.class)) {
            try {
                Class domainClass = method.getAnnotation(AclDomainAware.class)
                                          .value();
                Object instance = domainClass.newInstance();
                Method setter = domainClass.getDeclaredMethod("setId",
                        new Class[] {id.getClass()});
                setter.invoke(instance, new Object[] {id});

                return instance;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        throw new AuthorizationServiceException("Secure object: "
            + secureObject + " did not provide any argument of type: "
            + getProcessDomainObjectClass());
    }
}
