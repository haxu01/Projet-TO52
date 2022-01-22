package com.haoran.Brainstorming.service;

import com.haoran.Brainstorming.model.Role;

import java.util.List;

public interface IRoleService {
    Role selectById(Integer roleId);

    List<Role> selectAll();

    void insert(String name, Integer[] permissionIds);

    void update(Integer id, String name, Integer[] permissionIds);

    void delete(Integer id);
}
