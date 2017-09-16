package com.sjg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.provisioning.GroupManager;


public class UserGroupManager {
    private GroupManager groupManager;

    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    public List<UserGroupBean> getAll() {
        List<UserGroupBean> list = new ArrayList<UserGroupBean>();
        List<String> groups = groupManager.findAllGroups();

        for (String groupName : groups) {
            List<String> members = groupManager.findUsersInGroup(groupName);
            List<GrantedAuthority> authorities = groupManager
                .findGroupAuthorities(groupName);
            UserGroupBean bean = new UserGroupBean();

            bean.setName(groupName);
            bean.setMembers(members);
            bean.setAuthorities(authorities);
            list.add(bean);
        }

        return list;
    }

    public UserGroupBean get(String groupName) {
        List<String> members = groupManager.findUsersInGroup(groupName);
        List<GrantedAuthority> authorities = groupManager.findGroupAuthorities(groupName);
        UserGroupBean bean = new UserGroupBean();

        bean.setName(groupName);
        bean.setMembers(members);
        bean.setAuthorities(authorities);

        return bean;
    }

    public void save(String groupName, String[] authorities) {
        GrantedAuthority[] gas = new SimpleGrantedAuthority[authorities.length];

        for (int i = 0; i < authorities.length; i++) {
            gas[i] = new SimpleGrantedAuthority(authorities[i].trim());
        }
        groupManager.createGroup(groupName, Arrays.asList(gas));
    }

    public void remove(String groupName) {
        groupManager.deleteGroup(groupName);
    }

    public void update(String oldName, String groupName, String[] members,
        String[] authorities) {
        List<String> usernames = groupManager.findUsersInGroup(oldName);

        for (String username : usernames) {
            groupManager.removeUserFromGroup(username, oldName);
        }

        for (String member : members) {
            if ((member != null) && !member.equals("")) {
                groupManager.addUserToGroup(member, oldName);
            }
        }

        List<GrantedAuthority> gas = groupManager.findGroupAuthorities(groupName);

        for (GrantedAuthority ga : gas) {
            groupManager.removeGroupAuthority(oldName, ga);
        }

        for (int i = 0; i < authorities.length; i++) {
            String auth = authorities[i];

            if ((auth != null) && !auth.trim().equals("")) {
                GrantedAuthority ga = new GrantedAuthorityImpl(auth.trim());
                groupManager.addGroupAuthority(oldName, ga);
            }
        }

        groupManager.renameGroup(oldName, groupName);
    }
}
