package com.haoran.Brainstorming.service;

import com.haoran.Brainstorming.model.Comment;
import com.haoran.Brainstorming.model.Topic;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.model.vo.CommentsByTopic;
import com.haoran.Brainstorming.util.MyPage;

import java.util.List;
import java.util.Map;


public interface ICommentService {
    // Interroger les commentaires en fonction de l'identifiant du sujet
    List<CommentsByTopic> selectByTopicId(Integer topicId);

    // supprimer les commentaires associés lors de la suppression d'un sujet
    void deleteByTopicId(Integer topicId);

    // supprimer les enregistrements de commentaires en fonction de l'identifiant de l'utilisateur
    void deleteByUserId(Integer userId);

    // enregistrer le commentaire
    Comment insert(Comment comment, Topic topic, User user);

    Comment selectById(Integer id);

    //mettre à jour le commentaire
    void update(Comment comment);

    // aimer le commentaire
    int vote(Comment comment, User user);

    //supprimer le commentaire
    void delete(Comment comment);

    // Interroger les commentaires des utilisateurs
    MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize);

    MyPage<Map<String, Object>> selectAllForAdmin(Integer pageNo, String startDate, String endDate, String username);

    // Demander le nombre de sujets ajoutés aujourd'hui
    int countToday();
}
