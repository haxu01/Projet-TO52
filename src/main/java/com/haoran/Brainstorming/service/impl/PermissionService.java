package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.mapper.PermissionMapper;
import com.haoran.Brainstorming.model.Permission;
import com.haoran.Brainstorming.model.RolePermission;
import com.haoran.Brainstorming.service.IPermissionService;
import com.haoran.Brainstorming.service.IRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
public class PermissionService implements IPermissionService {

    private static Map<String, List<Permission>> permissionsByRoleId = new HashMap<>();

    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private IRolePermissionService rolePermissionService;

    public void clearRolePermissionCache() {
        permissionsByRoleId.clear();
    }

    // Interroger toutes les autorisations en fonction de l'identifiant du rôle, cette méthode est appelée très fréquemment, cache-la en mémoire
    @Override
    public List<Permission> selectByRoleId(Integer roleId) {
        if (permissionsByRoleId.get("roleId_" + roleId) != null) return permissionsByRoleId.get("roleId_" + roleId);
        List<RolePermission> rolePermissions = rolePermissionService.selectByRoleId(roleId);
        List<Integer> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(Permission::getId, permissionIds);
        List<Permission> permissions = permissionMapper.selectList(wrapper);
        permissionsByRoleId.put("roleId_" + roleId, permissions);
        return permissions;
    }

    // Interroger les nœuds enfants en fonction du nœud parent
    @Override
    public List<Permission> selectByPid(Integer pid) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Permission::getPid, pid);
        return permissionMapper.selectList(wrapper);
    }

    @Override
    public Map<String, List<Permission>> selectAll() {
        Map<String, List<Permission>> map = new LinkedHashMap<>();
        // Interroger d'abord le nœud parent
        List<Permission> permissions = this.selectByPid(0);
        //puis le nœud enfant
        permissions.forEach(permission -> map.put(permission.getName(), this.selectByPid(permission.getId())));
        return map;
    }

    @Override
    public Permission insert(Permission permission) {
        this.clearPermissionsCache();
        permissionMapper.insert(permission);
        return permission;
    }

    @Override
    public Permission update(Permission permission) {
        this.clearPermissionsCache();
        permissionMapper.updateById(permission);
        return permission;
    }

    @Override
    public void delete(Integer id) {
        this.clearPermissionsCache();
        Permission permission = permissionMapper.selectById(id);
        // S'il s'agit du nœud parent, supprime tous les enregistrements de tous les rôles associés sous tous les nœuds enfants, sinon une erreur sera signalée
        if (permission.getPid() == 0) {
            List<Permission> permissions = this.selectByPid(permission.getId());
            permissions.forEach(permission1 -> {
                // Supprimer d'abord les données associées dans role_permission
                rolePermissionService.deleteByPermissionId(permission1.getId());
                // supprimer les nœuds enfants
                permissionMapper.deleteById(permission1.getId());
            });
        } else {
            rolePermissionService.deleteByPermissionId(id);
        }
        permissionMapper.deleteById(id);
    }

    private void clearPermissionsCache() {
        permissionsByRoleId = new HashMap<>();
    }
}
