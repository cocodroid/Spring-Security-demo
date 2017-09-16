package com.sjg.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;


public class DefaultAclHandler implements AclHandler {
    private MutableAclService mutableAclService;

    public void setMutableAclService(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    @Transactional
    public void create(Authentication authentication, Object object,
         Object returnedObject)
        throws AccessDeniedException {
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        MutableAcl acl = mutableAclService.createAcl(oid);
        acl.insertAce(0, BasePermission.ADMINISTRATION,
            new PrincipalSid(getUsername()), true);
    }

    @Transactional
    public void delete(Authentication authentication, Object object,
         Object returnedObject)
        throws AccessDeniedException {
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        mutableAclService.deleteAcl(oid, false);
    }

    public boolean supports(Object domainObject, Object returnedObject) {
        return true;
    }

    protected String getUsername() {
        Authentication auth = SecurityContextHolder.getContext()
                                                   .getAuthentication();

        if (auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        } else {
            return auth.getPrincipal().toString();
        }
    }
}
