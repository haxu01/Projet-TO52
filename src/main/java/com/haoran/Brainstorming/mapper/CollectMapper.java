package com.haoran.Brainstorming.mapper;

import com.haoran.Brainstorming.model.Collect;
import com.haoran.Brainstorming.util.MyPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface CollectMapper extends BaseMapper<Collect> {

    MyPage<Map<String, Object>> selectByUserId(MyPage<Map<String, Object>> iPage, @Param("userId") Integer userId);
}
