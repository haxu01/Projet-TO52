package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.config.websocket.MyWebSocket;
import com.haoran.Brainstorming.mapper.CollectMapper;
import com.haoran.Brainstorming.model.Collect;
import com.haoran.Brainstorming.model.Topic;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.*;
import com.haoran.Brainstorming.util.Message;
import com.haoran.Brainstorming.util.MyPage;
import com.haoran.Brainstorming.util.SensitiveWordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CollectService implements ICollectService {

    @Resource
    private CollectMapper collectMapper;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private ITagService tagService;
    @Resource
    @Lazy
    private ITopicService topicService;
    @Resource
    private INotificationService notificationService;

    @Resource
    @Lazy
    private IUserService userService;

    // Interroger combien de personnes ont mis le sujet en collection
    @Override
    public List<Collect> selectByTopicId(Integer topicId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getTopicId, topicId);
        return collectMapper.selectList(wrapper);
    }

    // Interroger si l'utilisateur a mis un sujet en collection
    @Override
    public Collect selectByTopicIdAndUserId(Integer topicId, Integer userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getTopicId, topicId).eq(Collect::getUserId, userId);
        List<Collect> collects = collectMapper.selectList(wrapper);
        if (collects.size() > 0) return collects.get(0);
        return null;
    }

    //collecter un sujet
    @Override
    public Collect insert(Integer topicId, User user) {
        Collect collect = new Collect();
        collect.setTopicId(topicId);
        collect.setUserId(user.getId());
        collect.setInTime(new Date());
        collectMapper.insert(collect);

        //notification
        Topic topic = topicService.selectById(topicId);
        topic.setCollectCount(topic.getCollectCount() + 1);
        topicService.update(topic, null);

        if (!user.getId().equals(topic.getUserId())) {
            notificationService.insert(user.getId(), topic.getUserId(), topicId, "COLLECT", null);
            User targetUser = userService.selectById(topic.getUserId());
        }

        return collect;
    }

    //supprimer une collection
    @Override
    public void delete(Integer topicId, Integer userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getTopicId, topicId).eq(Collect::getUserId, userId);
        collectMapper.delete(wrapper);
        Topic topic = topicService.selectById(topicId);
        topic.setCollectCount(topic.getCollectCount() - 1);
        topicService.update(topic, null);
    }

    // supprimer les enregistrements de collection en fonction de l'identifiant du sujet
    @Override
    public void deleteByTopicId(Integer topicId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getTopicId, topicId);
        collectMapper.delete(wrapper);
    }

    // supprimer les enregistrements de collection en fonction de l'identifiant de l'utilisateur
    @Override
    public void deleteByUserId(Integer userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getUserId, userId);
        collectMapper.delete(wrapper);
    }

    // Interroger le nombre de sujets collectés par l'utilisateur
    @Override
    public int countByUserId(Integer userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getUserId, userId);
        return collectMapper.selectCount(wrapper);
    }


    // Interroger les sujets collectés par l'utilisateur
    @Override
    public MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize) {
        MyPage<Map<String, Object>> page = new MyPage<>(pageNo, pageSize == null ? Integer.parseInt(systemConfigService
                .selectAllConfig().get("page_size").toString()) : pageSize);
        page = collectMapper.selectByUserId(page, userId);
        for (Map<String, Object> map : page.getRecords()) {
            Object content = map.get("content");
            map.put("content", StringUtils.isEmpty(content) ? null : SensitiveWordUtil.replaceSensitiveWord(content
                    .toString(), "*", SensitiveWordUtil.MinMatchType));
        }
        tagService.selectTagsByTopicId(page);
        return page;
    }
}
