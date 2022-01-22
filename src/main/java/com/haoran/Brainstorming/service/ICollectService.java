package com.haoran.Brainstorming.service;

import com.haoran.Brainstorming.model.Collect;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.util.MyPage;

import java.util.List;
import java.util.Map;

public interface ICollectService {
    // Requête combien de personnes ont mis le sujet en collection
    List<Collect> selectByTopicId(Integer topicId);

    // Requête si l'utilisateur a mis un sujet en collection
    Collect selectByTopicIdAndUserId(Integer topicId, Integer userId);

    //collecter un sujet
    Collect insert(Integer topicId, User user);

    //supprimer une collection
    void delete(Integer topicId, Integer userId);

    // supprimer les enregistrements collecté en fonction de l'identifiant du sujet
    void deleteByTopicId(Integer topicId);

    // supprimer les enregistrements collecté en fonction de l'identifiant de l'utilisateur
    void deleteByUserId(Integer userId);

    // Interroger le nombre de sujets collectés par l'utilisateur
    int countByUserId(Integer userId);

    // Interroger les sujets collectés des utilisateurs
    MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize);
}
