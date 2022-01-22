package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.config.websocket.MyWebSocket;
import com.haoran.Brainstorming.mapper.CommentMapper;
import com.haoran.Brainstorming.model.Comment;
import com.haoran.Brainstorming.model.Topic;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.model.vo.CommentsByTopic;
import com.haoran.Brainstorming.service.*;
import com.haoran.Brainstorming.util.Message;
import com.haoran.Brainstorming.util.MyPage;
import com.haoran.Brainstorming.util.SensitiveWordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@Transactional
public class CommentService implements ICommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    @Resource
    private CommentMapper commentMapper;
    @Resource
    @Lazy
    private ITopicService topicService;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    @Lazy
    private IUserService userService;
    @Resource
    private INotificationService notificationService;

    // Interroger les commentaires en fonction de l'identifiant du sujet
    @Override
    public List<CommentsByTopic> selectByTopicId(Integer topicId) {
        List<CommentsByTopic> commentsByTopics = commentMapper.selectByTopicId(topicId);
        // Filtrer le contenu du commentaire avant d'écrire sur redis
        for (CommentsByTopic commentsByTopic : commentsByTopics) {
            commentsByTopic.setContent(SensitiveWordUtil.replaceSensitiveWord(commentsByTopic.getContent(), "*",
                    SensitiveWordUtil.MinMatchType));
        }
        return commentsByTopics;
    }

    // supprimer les commentaires associés lors de la suppression d'un sujet
    @Override
    public void deleteByTopicId(Integer topicId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Comment::getTopicId, topicId);
        commentMapper.delete(wrapper);
    }

    // supprimer les enregistrements de commentaires en fonction de l'identifiant de l'utilisateur
    @Override
    public void deleteByUserId(Integer userId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Comment::getUserId, userId);
        commentMapper.delete(wrapper);
    }

    // enregistre le commentaire
    @Override
    public Comment insert(Comment comment, Topic topic, User user) {
        commentMapper.insert(comment);
        // Le nombre de commentaires sur le sujet +1
        topic.setCommentCount(topic.getCommentCount() + 1);
        topicService.update(topic, null);
        // ajouter des points utilisateur
        user.setScore(user.getScore() + Integer.parseInt(systemConfigService.selectAllConfig().get
                ("create_comment_score").toString()));
        userService.update(user);

        // Notification
        // Notifier l'auteur du commentaire
        if (comment.getCommentId() != null) {
            Comment targetComment = this.selectById(comment.getCommentId());
            if (!user.getId().equals(targetComment.getUserId())) {
                notificationService.insert(user.getId(), targetComment.getUserId(), topic.getId(), "REPLY", comment
                        .getContent());

                User targetUser = userService.selectById(targetComment.getUserId());
            }
        }
        // Envoie une notification à l'auteur du sujet
        if (!user.getId().equals(topic.getUserId())) {
            notificationService.insert(user.getId(), topic.getUserId(), topic.getId(), "COMMENT", comment.getContent());
            User targetUser = userService.selectById(topic.getUserId());

        }

        return comment;
    }

    @Override
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    //Modifier un commentaire
    @Override
    public void update(Comment comment) {
        commentMapper.updateById(comment);
    }

    //aimer un commentaire
    @Override
    public int vote(Comment comment, User user) {
        String upIds = comment.getUpIds();
        // Convertit la chaîne de l'identifiant d'utilisateur similaire en une collection
        Set<String> strings = StringUtils.commaDelimitedListToSet(upIds);
        // Ajouter le nouvel identifiant d'utilisateur similaire à la collection, utilisez set ici, juste pour dédupliquer, si l'identifiant de l'utilisateur existe déjà dans la collection, cette action est considérée comme annulant l'identifiant similaire
        Integer userScore = user.getScore();
        if (strings.contains(String.valueOf(user.getId()))) {
            strings.remove(String.valueOf(user.getId()));
            userScore -= Integer.parseInt(systemConfigService.selectAllConfig().get("up_comment_score").toString());
        } else {// comme comportement
            strings.add(String.valueOf(user.getId()));
            userScore += Integer.parseInt(systemConfigService.selectAllConfig().get("up_comment_score").toString());
        }
        // Puis séparer ces identifiants par des virgules pour former une chaîne
        comment.setUpIds(StringUtils.collectionToCommaDelimitedString(strings));
        //modifier le commentaire
        this.update(comment);
        //ajouter les points d'utilisateur
        user.setScore(userScore);
        userService.update(user);
        return strings.size();
    }

    //supprimer un commentaire
    @Override
    public void delete(Comment comment) {
        if (comment != null) {
            // nombre de commentaires de sujet -1
            Topic topic = topicService.selectById(comment.getTopicId());
            topic.setCommentCount(topic.getCommentCount() - 1);
            topicService.update(topic, null);
            // soustraire des points utilisateur
            User user = userService.selectById(comment.getUserId());
            user.setScore(user.getScore() - Integer.parseInt(systemConfigService.selectAllConfig().get
                    ("delete_comment_score").toString()));
            userService.update(user);
            //supprimer le commentaire
            commentMapper.deleteById(comment.getId());
        }
    }

    // Interroger les commentaires des utilisateurs
    @Override
    public MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize) {
        MyPage<Map<String, Object>> iPage = new MyPage<>(pageNo, pageSize == null ? Integer.parseInt(systemConfigService
                .selectAllConfig().get("page_size").toString()) : pageSize);
        MyPage<Map<String, Object>> page = commentMapper.selectByUserId(iPage, userId);
        for (Map<String, Object> map : page.getRecords()) {
            Object content = map.get("content");
            map.put("content", StringUtils.isEmpty(content) ? null : SensitiveWordUtil.replaceSensitiveWord(content
                    .toString(), "*", SensitiveWordUtil.MinMatchType));
        }
        return page;
    }

    // ---------------------------- admin ----------------------------

    @Override
    public MyPage<Map<String, Object>> selectAllForAdmin(Integer pageNo, String startDate, String endDate, String username) {
        MyPage<Map<String, Object>> iPage = new MyPage<>(pageNo, Integer.parseInt((String) systemConfigService.selectAllConfig().get("page_size")));
        return commentMapper.selectAllForAdmin(iPage, startDate, endDate, username);
    }

    // Demander le nombre de sujets ajoutés aujourd'hui
    @Override
    public int countToday() {
        return commentMapper.countToday();
    }
}
