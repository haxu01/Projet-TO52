package com.haoran.Brainstorming.controller.front;

import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.ISystemConfigService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class BaseController {

    @Resource
    private ISystemConfigService systemConfigService;

    protected String redirect(String path) {
        return "redirect:" + path;
    }

    protected User getUser() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        HttpSession session = request.getSession();
        return (User) session.getAttribute("_user");
    }

    // Uniquement pour le rendu du chemin du modèle de la page d'accueil, le fond reste inchangé
    protected String render(String path) {
        return String.format("theme/%s/%s", systemConfigService.selectAllConfig().get("theme").toString(), path);
    }

}
