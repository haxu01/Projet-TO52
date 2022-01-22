package com.haoran.Brainstorming.service;

import com.haoran.Brainstorming.model.Topic;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.util.MyPage;

import java.util.List;
import java.util.Map;


public interface ITopicService {
    //rechercher
    MyPage<Map<String, Object>> search(Integer pageNo, Integer pageSize, String keyword);

    // Rubrique de requête de pagination
    MyPage<Map<String, Object>> selectAll(Integer pageNo, String tab);

    // Interroger d'autres sujets de l'auteur du sujet
    List<Topic> selectAuthorOtherTopic(Integer userId, Integer topicId, Integer limit);

    // Interroger le sujet de l'utilisateur
    MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize);
    // enregistrer le sujet
    Topic insert(String title, String content, String tags, User user);

    // interroger le sujet par identifiant
    Topic selectById(Integer id);

    // Interroger les sujets en fonction du titre pour éviter les sujets répétés
    Topic selectByTitle(String title);

    // gérer le nombre de visites sur le sujet
    Topic updateViewCount(Topic topic, String ip);

    // mettre à jour le sujet
    void update(Topic topic, String tags);

    //supprimer le sujet
    void delete(Topic topic);

    // supprimer les sujets en fonction de l'identifiant de l'utilisateur
    void deleteByUserId(Integer userId);

    MyPage<Map<String, Object>> selectAllForAdmin(Integer pageNo, String startDate, String endDate, String username);

    // Demander le nombre de sujets ajoutés aujourd'hui
    int countToday();

    int vote(Topic topic, User user);
}
