package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.mapper.TopicTagMapper;
import com.haoran.Brainstorming.model.Tag;
import com.haoran.Brainstorming.model.TopicTag;
import com.haoran.Brainstorming.service.ITopicTagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class TopicTagService implements ITopicTagService {

    @Resource
    private TopicTagMapper topicTagMapper;

    @Override
    public List<TopicTag> selectByTopicId(Integer topicId) {
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTopicId, topicId);
        return topicTagMapper.selectList(wrapper);
    }

    @Override
    public List<TopicTag> selectByTagId(Integer tagId) {
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTagId, tagId);
        return topicTagMapper.selectList(wrapper);
    }

    @Override
    public void insertTopicTag(Integer topicId, List<Tag> tagList) {
        // Supprimer d'abord tous les enregistrements correspondant à topicId
        this.deleteByTopicId(topicId);
        // boucle pour enregistrer l'association correspondante
        tagList.forEach(tag -> {
            TopicTag topicTag = new TopicTag();
            topicTag.setTopicId(topicId);
            topicTag.setTagId(tag.getId());
            topicTagMapper.insert(topicTag);
        });
    }

    // supprimer tous les enregistrements de tag associés du sujet
    @Override
    public void deleteByTopicId(Integer id) {
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTopicId, id);
        topicTagMapper.delete(wrapper);
    }
}
