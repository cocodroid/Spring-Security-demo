package com.sjg;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class PermissionServlet extends HttpServlet {
    private AclService aclService;
    private PermissionService permissionService;

    private AclService getAclService() {
        if (aclService == null) {
            ApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(this.getServletContext());
            aclService = (AclService) ctx.getBean("aclService");
        }

        return aclService;
    }

    private PermissionService getPermissionService() {
        if (permissionService == null) {
            ApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(this.getServletContext());
            permissionService = (PermissionService) ctx.getBean(
                    "permissionService");
        }

        return permissionService;
    }

    public void doGet(HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {
        try {
            process(request, response);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    public void doPost(HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {
        try {
            process(request, response);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    public void process(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        String action = request.getParameter("action");
        System.out.println("~~~~~~~~~~~~action~~~:"+action);
        if ("list".equals(action)) {
            this.list(request, response);
        } else if ("create".equals(action)) {
            this.create(request, response);
        } else if ("remove".equals(action)) {
            this.remove(request, response);
        } else if ("save".equals(action)) {
            this.save(request, response);
        } else {
            System.out.println("Unkown Action: " + action);
        }
    }

    //通过domain类的类型和id，获得对应acl中的所有ace信息
    public void list(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        Long id = Long.valueOf(request.getParameter("id"));
        String clz = request.getParameter("clz");
        Acl acl = null;

        if (clz.equals("account")) {
            Account account = new Account();
            account.setId(id);
            acl = getAclService()
                      .readAclById(new ObjectIdentityImpl(account));
        } else if (clz.equals("contract")) {
            Contract contract = new Contract();
            contract.setId(id);
            acl = getAclService()
                      .readAclById(new ObjectIdentityImpl(contract));
        } else if (clz.equals("message")) {
            Message message = new Message();
            message.setId(id);
            acl = getAclService()
                      .readAclById(new ObjectIdentityImpl(message));
        }

        request.setAttribute("acl", acl);
        request.getRequestDispatcher("/permission-list.jsp")
               .forward(request, response);
    }

    public void create(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        Long id = Long.valueOf(request.getParameter("id"));
        String clz = request.getParameter("clz");
        request.getRequestDispatcher("/permission-edit.jsp")
               .forward(request, response);
    }

    public void save(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        Long id = Long.valueOf(request.getParameter("id"));
        String clz = request.getParameter("clz");
        String recipient = request.getParameter("recipient");
        int mask = Integer.valueOf(request.getParameter("permission"));

        getPermissionService().addPermission(id, clz, recipient, mask);

        String url = "permission.do?action=list&id=" + id + "&clz=" + clz;
        response.sendRedirect(url);
    }

    public void remove(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        Long id = Long.valueOf(request.getParameter("id"));
        String clz = request.getParameter("clz");
        String recipient = request.getParameter("sid");
        int mask = Integer.valueOf(request.getParameter("permission"));

        getPermissionService().deletePermission(id, clz, recipient, mask);

        String url = "permission.do?action=list&id=" + id + "&clz=" + clz;
        response.sendRedirect(url);
    }
}
