package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.mapper.TopicMapper;
import com.haoran.Brainstorming.model.Tag;
import com.haoran.Brainstorming.model.Topic;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.*;
import com.haoran.Brainstorming.util.MyPage;
import com.haoran.Brainstorming.util.SensitiveWordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@Transactional
public class TopicService implements ITopicService {

    @Resource
    private TopicMapper topicMapper;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private ITopicTagService topicTagService;
    @Resource
    private ITagService tagService;
    @Resource
    @Lazy
    private ICollectService collectService;
    @Resource
    @Lazy
    private ICommentService commentService;
    @Resource
    @Lazy
    private IUserService userService;
    @Resource
    private INotificationService notificationService;
    @Resource
    private IndexedService indexedService;

    @Override
    public MyPage<Map<String, Object>> search(Integer pageNo, Integer pageSize, String keyword) {
        if (pageSize == null)
            pageSize = Integer.parseInt(systemConfigService.selectAllConfig().get("page_size").toString());
        MyPage<Map<String, Object>> page = new MyPage<>(pageNo, pageSize);
        return topicMapper.search(page, keyword);
    }

    @Override
    public MyPage<Map<String, Object>> selectAll(Integer pageNo, String tab) {
        MyPage<Map<String, Object>> page = new MyPage<>(pageNo, Integer.parseInt(systemConfigService.selectAllConfig()
                .get("page_size").toString()));
        page = topicMapper.selectAll(page, tab);
        // Interroger des tags de sujet
        tagService.selectTagsByTopicId(page);
        return page;
    }

    // Interroger d'autres sujets de l'auteur du sujet
    @Override
    public List<Topic> selectAuthorOtherTopic(Integer userId, Integer topicId, Integer limit) {
        QueryWrapper<Topic> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("in_time");
        if (topicId != null) {
            wrapper.lambda().ne(Topic::getId, topicId);
        }
        if (limit != null) wrapper.last("limit " + limit);
        return topicMapper.selectList(wrapper);
    }

    // Interroger le sujet de l'utilisateur
    @Override
    public MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize) {
        MyPage<Map<String, Object>> iPage = new MyPage<>(pageNo, pageSize == null ? Integer.parseInt(systemConfigService
                .selectAllConfig().get("page_size").toString()) : pageSize);
        MyPage<Map<String, Object>> page = topicMapper.selectByUserId(iPage, userId);
        for (Map<String, Object> map : page.getRecords()) {
            Object content = map.get("content");
            map.put("content", StringUtils.isEmpty(content) ? null : SensitiveWordUtil.replaceSensitiveWord(content
                    .toString(), "*", SensitiveWordUtil.MinMatchType));
        }
        return page;
    }

    // enregistrer le sujet
    @Override
    public Topic insert(String title, String content, String tags, User user) {
        Topic topic = new Topic();
        topic.setTitle(Jsoup.clean(title, Whitelist.simpleText()));
        topic.setStyle(systemConfigService.selectAllConfig().get("content_style"));
        topic.setContent(content);
        topic.setInTime(new Date());
        topic.setUserId(user.getId());
        topic.setTop(false);
        topic.setGood(false);
        topic.setView(1);
        topic.setCollectCount(0);
        topic.setCommentCount(0);
        topicMapper.insert(topic);

        user.setScore(user.getScore() + Integer.parseInt(systemConfigService.selectAllConfig().get("create_topic_score").toString()));
        userService.update(user);
        if (!StringUtils.isEmpty(tags)) {
            //enregidtrer le tag
            List<Tag> tagList = tagService.insertTag(Jsoup.clean(tags, Whitelist.none()));
            // Gérer l'association entre les tags et les sujets
            topicTagService.insertTopicTag(topic.getId(), tagList);
        }
        // indexer le sujet
        indexedService.indexTopic(String.valueOf(topic.getId()), topic.getTitle(), topic.getContent());
        return topic;
    }

    // interroger le sujet par identifiant
    @Override
    public Topic selectById(Integer id) {
        return topicMapper.selectById(id);
    }

    // Interroger les sujets en fonction du titre pour éviter les sujets répétés
    @Override
    public Topic selectByTitle(String title) {
        QueryWrapper<Topic> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Topic::getTitle, title);
        return topicMapper.selectOne(wrapper);
    }

    // gérer le nombre de visites sur le sujet
    @Override
    public Topic updateViewCount(Topic topic, String ip) {
        topic.setView(topic.getView() + 1);
        this.update(topic, null);
        return topic;
    }

    //mettre à jour le sujet
    @Override
    public void update(Topic topic, String tags) {
        topicMapper.updateById(topic);
        if (!StringUtils.isEmpty(tags)) {
            tagService.reduceTopicCount(topic.getId());
            if (!StringUtils.isEmpty(tags)) {
                List<Tag> tagList = tagService.insertTag(Jsoup.clean(tags, Whitelist.none()));
                topicTagService.insertTopicTag(topic.getId(), tagList);
            }
        }
        indexedService.indexTopic(String.valueOf(topic.getId()), topic.getTitle(), topic.getContent());
    }

    //supprimer le sujet
    @Override
    public void delete(Topic topic) {
        Integer id = topic.getId();
        notificationService.deleteByTopicId(id);
        collectService.deleteByTopicId(id);
        commentService.deleteByTopicId(id);
        tagService.reduceTopicCount(id);
        topicTagService.deleteByTopicId(id);
        User user = userService.selectById(topic.getUserId());
        user.setScore(user.getScore() - Integer.parseInt(systemConfigService.selectAllConfig().get("delete_topic_score")
                .toString()));
        userService.update(user);
        indexedService.deleteTopicIndex(String.valueOf(topic.getId()));
        topicMapper.deleteById(id);
    }

    // supprimer les sujets en fonction de l'identifiant de l'utilisateur
    @Override
    public void deleteByUserId(Integer userId) {
        QueryWrapper<Topic> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Topic::getUserId, userId);
        List<Topic> topics = topicMapper.selectList(wrapper);
        topics.forEach(topic -> {
            indexedService.deleteTopicIndex(String.valueOf(topic.getId()));
            commentService.deleteByTopicId(topic.getId());
            collectService.deleteByTopicId(topic.getId());
            tagService.reduceTopicCount(topic.getId());
            topicTagService.deleteByTopicId(topic.getId());
        });
        topicMapper.delete(wrapper);
    }

    // ---------------------------- admin ----------------------------

    @Override
    public MyPage<Map<String, Object>> selectAllForAdmin(Integer pageNo, String startDate, String endDate, String
            username) {
        MyPage<Map<String, Object>> iPage = new MyPage<>(pageNo, Integer.parseInt((String) systemConfigService
                .selectAllConfig().get("page_size")));
        return topicMapper.selectAllForAdmin(iPage, startDate, endDate, username);
    }

    // Demander le nombre de sujets ajoutés aujourd'hui
    @Override
    public int countToday() {
        return topicMapper.countToday();
    }

    // ---------------------------- api ----------------------------

    @Override
    public int vote(Topic topic, User user) {
        String upIds = topic.getUpIds();
        Set<String> strings = StringUtils.commaDelimitedListToSet(upIds);
        Integer userScore = user.getScore();
        if (strings.contains(String.valueOf(user.getId()))) {
            strings.remove(String.valueOf(user.getId()));
            userScore -= Integer.parseInt(systemConfigService.selectAllConfig().get("up_topic_score").toString());
        } else {
            strings.add(String.valueOf(user.getId()));
            userScore += Integer.parseInt(systemConfigService.selectAllConfig().get("up_topic_score").toString());
        }
        topic.setUpIds(StringUtils.collectionToCommaDelimitedString(strings));
        this.update(topic, null);
        user.setScore(userScore);
        userService.update(user);
        return strings.size();
    }

}
