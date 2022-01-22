package com.haoran.Brainstorming.mapper;

import com.haoran.Brainstorming.model.Comment;
import com.haoran.Brainstorming.model.vo.CommentsByTopic;
import com.haoran.Brainstorming.util.MyPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentsByTopic> selectByTopicId(@Param("topicId") Integer topicId);

    MyPage<Map<String, Object>> selectByUserId(MyPage<Map<String, Object>> iPage, @Param("userId") Integer userId);

    MyPage<Map<String, Object>> selectAllForAdmin(MyPage<Map<String, Object>> iPage, @Param("startDate") String
            startDate, @Param("endDate") String endDate, @Param("username") String username);

    int countToday();
}
