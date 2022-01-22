package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.mapper.RolePermissionMapper;
import com.haoran.Brainstorming.model.RolePermission;
import com.haoran.Brainstorming.service.IRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class RolePermissionService implements IRolePermissionService {

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    // Interroger tous les enregistrements associés aux autorisations de rôle en fonction de l'ID de rôle
    @Override
    public List<RolePermission> selectByRoleId(Integer roleId) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RolePermission::getRoleId, roleId);
        return rolePermissionMapper.selectList(wrapper);
    }

    // supprimer la relation basée sur l'identifiant du rôle
    @Override
    public void deleteByRoleId(Integer roleId) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(wrapper);
    }

    // supprimer l'association en fonction de l'identifiant de permission
    @Override
    public void deleteByPermissionId(Integer permissionId) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RolePermission::getPermissionId, permissionId);
        rolePermissionMapper.delete(wrapper);
    }

    @Override
    public void insert(RolePermission rolePermission) {
        rolePermissionMapper.insert(rolePermission);
    }

}
