package com.sjg.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;


public interface AclHandler {
    void create(Authentication authentication, Object object,
         Object returnedObject)
        throws AccessDeniedException;

    void delete(Authentication authentication, Object object,
         Object returnedObject)
        throws AccessDeniedException;

    boolean supports(Object domainObject, Object returnedObject);
}
