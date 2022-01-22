package com.haoran.Brainstorming.service;

import com.haoran.Brainstorming.model.RolePermission;

import java.util.List;


public interface IRolePermissionService {
    // Interroger tous les enregistrements associés aux autorisations de rôle en fonction de l'ID de rôle
    List<RolePermission> selectByRoleId(Integer roleId);

    // supprimer la relation basée sur l'identifiant du rôle
    void deleteByRoleId(Integer roleId);

    // supprimer l'association en fonction de l'identifiant de permission
    void deleteByPermissionId(Integer permissionId);

    void insert(RolePermission rolePermission);
}
