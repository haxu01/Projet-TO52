package com.haoran.Brainstorming.config;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;



@Component
public class ShiroTag {

    // Détermine si l'utilisateur actuel s'est connecté et authentifié
    public boolean isAuthenticated() {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    // Récupère le nom d'utilisateur de l'utilisateur actuel
    public String getPrincipal() {
        return (String) SecurityUtils.getSubject().getPrincipal();
    }

    // Détermine si l'utilisateur a le rôle xx
    public boolean hasRole(String name) {
        return SecurityUtils.getSubject().hasRole(name);
    }

    public boolean hasPermission(String name) {
        return !StringUtils.isEmpty(name) && SecurityUtils.getSubject().isPermitted(name);
    }

    // Détermine si l'utilisateur a la permission xx
    public boolean hasPermissionOr(String... name) {
        boolean[] permitted = SecurityUtils.getSubject().isPermitted(name);
        for (boolean b : permitted) {
            if (b) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPermissionAnd(String... name) {
        boolean[] permitted = SecurityUtils.getSubject().isPermitted(name);
        for (boolean b : permitted) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAllPermission(String... name) {
        return SecurityUtils.getSubject().isPermittedAll(name);
    }
}
