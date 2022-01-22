package com.haoran.Brainstorming.service;

import com.haoran.Brainstorming.model.Tag;
import com.haoran.Brainstorming.util.MyPage;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;


public interface ITagService {
    void selectTagsByTopicId(MyPage<Map<String, Object>> page);

    Tag selectById(Integer id);

    Tag selectByName(String name);

    List<Tag> selectByIds(List<Integer> ids);

    // Interroger toutes les tags associés au sujet
    List<Tag> selectByTopicId(Integer topicId);
    // Traiter et enregistrer le tag renseigné lors de la création d'un sujet
    List<Tag> insertTag(String newTags);

    // Définit le nombre de sujets du tag -1
    void reduceTopicCount(Integer id);

    // Interroger le sujet associé au tag
    MyPage<Map<String, Object>> selectTopicByTagId(Integer tagId, Integer pageNo);

    // Interroger la liste des tags
    IPage<Tag> selectAll(Integer pageNo, Integer pageSize, String name);

    void update(Tag tag);

    // S'il y a encore des données liées dans la table topic_tag, la suppression ici signalera une erreur
    void delete(Integer id);

    //synchroniser le nombre de sujets pour les tags
    void async();

    // Requête du nombre de tags ajoutés aujourd'hui
    int countToday();
}
