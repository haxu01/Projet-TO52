package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.mapper.TagMapper;
import com.haoran.Brainstorming.model.Tag;
import com.haoran.Brainstorming.model.TopicTag;
import com.haoran.Brainstorming.service.ISystemConfigService;
import com.haoran.Brainstorming.service.ITagService;
import com.haoran.Brainstorming.service.ITopicTagService;
import com.haoran.Brainstorming.util.MyPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
public class TagService implements ITagService {

    @Resource
    private TagMapper tagMapper;
    @Resource
    private ITopicTagService topicTagService;
    @Resource
    private ISystemConfigService systemConfigService;

    @Override
    public void selectTagsByTopicId(MyPage<Map<String, Object>> page) {
        page.getRecords().forEach(map -> {
            List<TopicTag> topicTags = topicTagService.selectByTopicId((Integer) map.get("id"));
            if (!topicTags.isEmpty()) {
                List<Integer> tagIds = topicTags.stream().map(TopicTag::getTagId).collect(Collectors.toList());
                List<Tag> tags = this.selectByIds(tagIds);
                map.put("tags", tags);
            }
        });
    }

    @Override
    public Tag selectById(Integer id) {
        return tagMapper.selectById(id);
    }

    @Override
    public Tag selectByName(String name) {
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Tag::getName, name);
        return tagMapper.selectOne(wrapper);
    }

    @Override
    public List<Tag> selectByIds(List<Integer> ids) {
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(Tag::getId, ids);
        return tagMapper.selectList(wrapper);
    }

    // Interroger toutes les tags associés au sujet
    @Override
    public List<Tag> selectByTopicId(Integer topicId) {
        List<TopicTag> topicTags = topicTagService.selectByTopicId(topicId);
        if (!topicTags.isEmpty()) {
            List<Integer> tagIds = topicTags.stream().map(TopicTag::getTagId).collect(Collectors.toList());
            QueryWrapper<Tag> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(Tag::getId, tagIds);
            return tagMapper.selectList(wrapper);
        }
        return Lists.newArrayList();
    }

    // Traiter et enregistrer la balise renseignée lors de la création d'un sujet
    @Override
    public List<Tag> insertTag(String newTags) {
        String[] _tags = StringUtils.commaDelimitedListToStringArray(newTags);
        List<Tag> tagList = new ArrayList<>();
        for (String _tag : _tags) {
            Tag tag = this.selectByName(_tag);
            if (tag == null) {
                tag = new Tag();
                tag.setName(_tag);
                tag.setTopicCount(1);
                tag.setInTime(new Date());
                tagMapper.insert(tag);
            } else {
                tag.setTopicCount(tag.getTopicCount() + 1);
                tagMapper.updateById(tag);
            }
            tagList.add(tag);
        }
        return tagList;
    }

    // Définit le nombre de sujets du tag -1
    @Override
    public void reduceTopicCount(Integer id) {
        List<Tag> tags = this.selectByTopicId(id);
        tags.forEach(tag -> {
            tag.setTopicCount(tag.getTopicCount() - 1);
            tagMapper.updateById(tag);
        });
    }

    // Interroger le sujet associé au tag
    @Override
    public MyPage<Map<String, Object>> selectTopicByTagId(Integer tagId, Integer pageNo) {
        MyPage<Map<String, Object>> iPage = new MyPage<>(pageNo, Integer.parseInt(systemConfigService.selectAllConfig()
                .get("page_size").toString()));
        return tagMapper.selectTopicByTagId(iPage, tagId);
    }

    // Interroger la liste des tags
    @Override
    public IPage<Tag> selectAll(Integer pageNo, Integer pageSize, String name) {
        IPage<Tag> iPage = new MyPage<>(pageNo, pageSize == null ? Integer.parseInt(systemConfigService.selectAllConfig()
                .get("page_size").toString()) : pageSize);
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.lambda().eq(Tag::getName, name);
        }
        wrapper.orderByDesc("topic_count");
        return tagMapper.selectPage(iPage, wrapper);
    }

    @Override
    public void update(Tag tag) {
        tagMapper.updateById(tag);
    }

    @Override
    public void delete(Integer id) {
        tagMapper.deleteById(id);
    }

    // ---------------------------- admin ----------------------------

    //synchroniser le nombre de sujets pour les tags
    @Override
    public void async() {
        List<Tag> tags = tagMapper.selectList(null);
        tags.forEach(tag -> {
            List<TopicTag> topicTags = topicTagService.selectByTagId(tag.getId());
            tag.setTopicCount(topicTags.size());
            this.update(tag);
        });
    }

    // Interroger du nombre de tags ajoutés aujourd'hui
    @Override
    public int countToday() {
        return tagMapper.countToday();
    }
}
