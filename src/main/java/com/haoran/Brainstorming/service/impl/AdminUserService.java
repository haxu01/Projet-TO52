package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.mapper.AdminUserMapper;
import com.haoran.Brainstorming.model.AdminUser;
import com.haoran.Brainstorming.service.IAdminUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class AdminUserService implements IAdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    // Interroger l'utilisateur par nom d'utilisateur
    @Override
    public AdminUser selectByUsername(String username) {
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AdminUser::getUsername, username);
        return adminUserMapper.selectOne(wrapper);
    }

    // Interroger tous les utilisateurs en arrière-plan
    @Override
    public List<Map<String, Object>> selectAll() {
        return adminUserMapper.selectAll();
    }

    @Override
    public void update(AdminUser adminUser) {
        adminUserMapper.updateById(adminUser);
    }

    @Override
    public void insert(AdminUser adminUser) {
        adminUserMapper.insert(adminUser);
    }

    @Override
    public void delete(Integer id) {
        adminUserMapper.deleteById(id);
    }

    @Override
    public AdminUser selectById(Integer id) {
        return adminUserMapper.selectById(id);
    }

    // Interroge l'utilisateur associé à l'arrière-plan en fonction de l'identifiant du rôle
    @Override
    public List<AdminUser> selectByRoleId(Integer roleId) {
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AdminUser::getRoleId, roleId);
        return adminUserMapper.selectList(wrapper);
    }
}
