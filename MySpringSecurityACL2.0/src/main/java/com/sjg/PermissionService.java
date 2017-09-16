package com.sjg;

import java.util.List;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.transaction.annotation.Transactional;

public class PermissionService {
	private MutableAclService mutableAclService;

	public void setMutableAclService(MutableAclService mutableAclService) {
		this.mutableAclService = mutableAclService;
	}

	@Transactional
	public void addPermission(long id, String clz, String recipient, int mask) {
		PrincipalSid sid = new PrincipalSid(recipient);
		// Permission permission = BasePermission.buildFromMask(mask);
		Permission permission = null;
		if (mask == 4) {
			permission = BasePermission.CREATE;
		} else if (mask == 16) {
			permission = BasePermission.ADMINISTRATION;
		} else if (mask == 8) {
			permission = BasePermission.DELETE;
		} else {
			System.out.println("tsdf7r6wfwe7gfwef");
		}

		ObjectIdentity oid = null;

		if (clz.equals("account")) {
			oid = new ObjectIdentityImpl(Account.class, id);
		} else if (clz.equals("contract")) {
			oid = new ObjectIdentityImpl(Contract.class, id);
		} else if (clz.equals("message")) {
			oid = new ObjectIdentityImpl(Message.class, id);
		}

		MutableAcl acl;

		try {
			acl = (MutableAcl) mutableAclService.readAclById(oid);
		} catch (NotFoundException nfe) {
			acl = mutableAclService.createAcl(oid);
		}

		acl.insertAce(acl.getEntries().size(), permission, sid, true);
		mutableAclService.updateAcl(acl);
	}

	@Transactional
	public void deletePermission(long id, String clz, String recipient, int mask) {
		PrincipalSid sid = new PrincipalSid(recipient);
		// Permission permission = BasePermission.buildFromMask(mask);
		Permission permission = null;
		if (mask == 8)
			permission = BasePermission.DELETE;

		ObjectIdentity oid = null;

		if (clz.equals("account")) {
			oid = new ObjectIdentityImpl(Account.class, id);
		} else if (clz.equals("contract")) {
			oid = new ObjectIdentityImpl(Contract.class, id);
		} else if (clz.equals("message")) {
			oid = new ObjectIdentityImpl(Message.class, id);
		}

		MutableAcl acl = (MutableAcl) mutableAclService.readAclById(oid);
		/*
		 * AccessControlEntry[] entries = acl.getEntries();
		 * 
		 * for (int i = 0; i < entries.length; i++) { if (entries[i].getSid().equals(sid) && entries[i].getPermission().equals(permission)) { acl.deleteAce(i); } }
		 */
		List<AccessControlEntry> entries = acl.getEntries();

		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getSid().equals(sid) && entries.get(i).getPermission().equals(permission)) {
				acl.deleteAce(i);
			}
		}
		mutableAclService.updateAcl(acl);
	}
}
