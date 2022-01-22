package com.haoran.Brainstorming.service;

import com.haoran.Brainstorming.model.AdminUser;

import java.util.List;
import java.util.Map;


public interface IAdminUserService {
    // Interroger l'utilisateur par nom d'utilisateur
    AdminUser selectByUsername(String username);

    // Interroger tous les utilisateurs en arrière-plan
    List<Map<String, Object>> selectAll();

    void update(AdminUser adminUser);

    void insert(AdminUser adminUser);

    void delete(Integer id);

    AdminUser selectById(Integer id);

    // Interroger l'utilisateur associé à l'arrière-plan en fonction de l'identifiant du rôle
    List<AdminUser> selectByRoleId(Integer roleId);
}
