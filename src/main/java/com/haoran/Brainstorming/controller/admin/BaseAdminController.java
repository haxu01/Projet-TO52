package com.haoran.Brainstorming.controller.admin;

import com.haoran.Brainstorming.controller.api.BaseApiController;
import com.haoran.Brainstorming.model.AdminUser;
import com.haoran.Brainstorming.service.IAdminUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;

public class BaseAdminController extends BaseApiController {

    @Resource
    private IAdminUserService adminUserService;


    protected AdminUser getAdminUser() {
        Subject subject = SecurityUtils.getSubject();
        String principal = (String) subject.getPrincipal();
        return adminUserService.selectByUsername(principal);
    }

}
