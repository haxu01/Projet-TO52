package com.haoran.Brainstorming.mapper;

import com.haoran.Brainstorming.model.AdminUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


public interface AdminUserMapper extends BaseMapper<AdminUser> {

    List<Map<String, Object>> selectAll();
}
