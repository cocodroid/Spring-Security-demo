package com.sjg.afterinvocation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.acls.afterinvocation.AbstractAclProvider;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;

import com.sjg.handler.AclHandler;


public abstract class AbstractAclEntryAfterInvocationProvider
    extends AbstractAclProvider implements MessageSourceAware {
    private static Log logger = LogFactory.getLog(AbstractAclEntryAfterInvocationProvider.class);
    protected MessageSourceAccessor messages = SpringSecurityMessageSource
        .getAccessor();
    protected List handlers;

    public AbstractAclEntryAfterInvocationProvider(AclService aclService,
        String processConfigAttribute, List<Permission> requirePermission) {
        super(aclService, processConfigAttribute, requirePermission);
    }

    public abstract Object decide(Authentication authentication,
        Object object,Collection<ConfigAttribute> attributes,Object returnedObject) throws AccessDeniedException;

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public List getHandlers() {
        return handlers;
    }

    public void setHandlers(List newList) {
        checkIfValidList(newList);

        Iterator iter = newList.iterator();

        while (iter.hasNext()) {
            Object currentObject = null;

            try {
                currentObject = iter.next();

                AclHandler attemptToCast = (AclHandler) currentObject;
            } catch (ClassCastException cce) {
                throw new IllegalArgumentException("AclCreator "
                    + currentObject.getClass().getName()
                    + " must implement AclCreator");
            }
        }

        this.handlers = newList;
    }

    private void checkIfValidList(List listToCheck) {
        if ((listToCheck == null) || (listToCheck.size() == 0)) {
            throw new IllegalArgumentException(
                "A list of AfterInvocationProviders is required");
        }
    }

    public abstract Object getDomainObjectInstance(Object secureObject,
        Class processDomainObjectClass);
}
