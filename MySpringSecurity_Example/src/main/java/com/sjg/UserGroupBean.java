package com.sjg;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class UserGroupBean {
    private String name;
    private List<String> members;
    private List<GrantedAuthority> authorities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members2) {
        this.members = members2;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities2) {
        this.authorities = authorities2;
    }

    public String getMember() {
        if (members == null) {
            return "";
        }

        StringBuffer buff = new StringBuffer();

        for (String mem : members) {
            buff.append(mem).append(",");
        }

        if (members.size() != 0) {
            buff.deleteCharAt(buff.length() - 1);
        }

        return buff.toString();
    }

    public String getAuthority() {
        if (authorities == null) {
            return "";
        }

        StringBuffer buff = new StringBuffer();

        for (GrantedAuthority auth : authorities) {
            buff.append(auth.getAuthority()).append(",");
        }

        if (authorities.size() != 0) {
            buff.deleteCharAt(buff.length() - 1);
        }

        return buff.toString();
    }
}
