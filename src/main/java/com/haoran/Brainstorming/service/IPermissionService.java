package com.haoran.Brainstorming.service;

import com.haoran.Brainstorming.model.Permission;

import java.util.List;
import java.util.Map;


public interface IPermissionService {

    // Appeler-le lors de la mise à jour des autorisations associées au rôle, effacez le cache et laissez les autorisations prendre effet en temps réel
    void clearRolePermissionCache();

    // Interroger toutes les autorisations en fonction de l'ID de rôle
    List<Permission> selectByRoleId(Integer roleId);

    // Interroger les nœuds enfants en fonction du nœud parent
    List<Permission> selectByPid(Integer pid);

    Map<String, List<Permission>> selectAll();

    Permission insert(Permission permission);

    Permission update(Permission permission);

    void delete(Integer id);
}
