package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.mapper.RoleMapper;
import com.haoran.Brainstorming.model.Role;
import com.haoran.Brainstorming.model.RolePermission;
import com.haoran.Brainstorming.service.IRolePermissionService;
import com.haoran.Brainstorming.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class RoleService implements IRoleService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private IRolePermissionService rolePermissionService;
    @Resource
    private PermissionService permissionService;

    @Override
    public Role selectById(Integer roleId) {
        return roleMapper.selectById(roleId);
    }

    @Override
    public List<Role> selectAll() {
        return roleMapper.selectList(null);
    }

    @Override
    public void insert(String name, Integer[] permissionIds) {
        Role role = new Role();
        role.setName(name);
        roleMapper.insert(role);
        insertRolePermissions(role, permissionIds);
    }

    @Override
    public void update(Integer id, String name, Integer[] permissionIds) {
        // mettre à jour le rôle
        Role role = this.selectById(id);
        role.setName(name);
        roleMapper.updateById(role);
        // Supprimer l'association de l'autorisation de rôle
        rolePermissionService.deleteByRoleId(id);
        // rétablir l'association
        insertRolePermissions(role, permissionIds);
    }

    private void insertRolePermissions(Role role, Integer[] permissionIds) {
        for (Integer permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionId(permissionId);
            rolePermissionService.insert(rolePermission);
        }
        // vider le cache
        permissionService.clearRolePermissionCache();
    }

    @Override
    public void delete(Integer id) {
        // supprimer l'association
        rolePermissionService.deleteByRoleId(id);
        roleMapper.deleteById(id);
    }
}
