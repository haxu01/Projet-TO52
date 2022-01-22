package com.haoran.Brainstorming.controller.api;

import com.haoran.Brainstorming.exception.ApiAssert;
import com.haoran.Brainstorming.model.Comment;
import com.haoran.Brainstorming.model.Topic;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.ICommentService;
import com.haoran.Brainstorming.service.ISystemConfigService;
import com.haoran.Brainstorming.service.ITopicService;
import com.haoran.Brainstorming.util.Result;
import com.haoran.Brainstorming.util.SensitiveWordUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/api/comment")
public class CommentApiController extends BaseApiController {

    @Resource
    private ICommentService commentService;
    @Resource
    private ITopicService topicService;
    @Resource
    private ISystemConfigService systemConfigService;

    //créer un commentaire
    @PostMapping
    public Result create(@RequestBody Map<String, String> body) {
        User user = getApiUser();
        String content = body.get("content");
        Integer topicId = StringUtils.isEmpty(body.get("topicId")) ? null : Integer.parseInt(body.get("topicId"));
        Integer commentId = StringUtils.isEmpty(body.get("commentId")) ? null : Integer.parseInt(body.get("commentId"));
        ApiAssert.notEmpty(content, "Add comments svp");
        ApiAssert.notNull(topicId, "Topic ID？");
        Topic topic = topicService.selectById(topicId);
        ApiAssert.notNull(topic, "Le Topic a peut-être été supprimé");
        // Assemble l'objet commentaire
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setStyle(systemConfigService.selectAllConfig().get("content_style"));
        comment.setContent(content);
        comment.setInTime(new Date());
        comment.setTopicId(topic.getId());
        comment.setUserId(user.getId());
        comment = commentService.insert(comment, topic, user);
        // filtre le contenu des commentaires
        comment.setContent(SensitiveWordUtil.replaceSensitiveWord(comment.getContent(), "*", SensitiveWordUtil.MinMatchType));
        return success(comment);
    }

    //modifier un commentaire
    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        User user = getApiUser();
        String content = body.get("content");
        ApiAssert.notNull(id, "Topic ID？");
        ApiAssert.notEmpty(content, "Add comments svp");
        Comment comment = commentService.selectById(id);
        ApiAssert.notNull(comment, "Le Topic a peut-être été supprimé");
        ApiAssert.isTrue(comment.getUserId().equals(user.getId()), "Vous ne pouvez pas rédiger les topics des autres");
        comment.setContent(content);
        commentService.update(comment);
        comment.setContent(SensitiveWordUtil.replaceSensitiveWord(comment.getContent(), "*", SensitiveWordUtil
                .MinMatchType));
        return success(comment);
    }

    //supprimer un commentaire
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        User user = getApiUser();
        Comment comment = commentService.selectById(id);
        ApiAssert.notNull(comment, "Le Topic a peut-être été supprimé");
        ApiAssert.isTrue(comment.getUserId().equals(user.getId()), "Vous ne pouvez pas supprimé les topics des autres");
        commentService.delete(comment);
        return success();
    }

    //aimer un commentaire
    @GetMapping("/{id}/vote")
    public Result vote(@PathVariable Integer id) {
        User user = getApiUser();
        Comment comment = commentService.selectById(id);
        ApiAssert.notNull(comment, "Le Topic a peut-être été supprimé");
        ApiAssert.notTrue(comment.getUserId().equals(user.getId()), "Bravo！");
        int voteCount = commentService.vote(comment, user);
        return success(voteCount);
    }
}
