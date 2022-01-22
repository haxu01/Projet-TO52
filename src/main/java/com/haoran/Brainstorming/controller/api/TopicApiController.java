package com.haoran.Brainstorming.controller.api;

import com.haoran.Brainstorming.exception.ApiAssert;
import com.haoran.Brainstorming.model.Collect;
import com.haoran.Brainstorming.model.Tag;
import com.haoran.Brainstorming.model.Topic;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.model.vo.CommentsByTopic;
import com.haoran.Brainstorming.service.*;
import com.haoran.Brainstorming.util.IpUtil;
import com.haoran.Brainstorming.util.Result;
import com.haoran.Brainstorming.util.SensitiveWordUtil;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/topic")
public class TopicApiController extends BaseApiController {

    @Resource
    private ITopicService topicService;
    @Resource
    private ITagService tagService;
    @Resource
    private ICommentService commentService;
    @Resource
    private IUserService userService;
    @Resource
    private ICollectService collectService;

    //détaille de sujet
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // Interroger les détails du sujet
        Topic topic = topicService.selectById(id);
        // Interroger les tags associés au sujet
        List<Tag> tags = tagService.selectByTopicId(id);
        // Interroger les commentaires du sujet
        List<CommentsByTopic> comments = commentService.selectByTopicId(id);
        // Intereoger les informations sur l'auteur du sujet
        User topicUser = userService.selectById(topic.getUserId());
        // Interroger combien de coleection le sujet a
        List<Collect> collects = collectService.selectByTopicId(id);
        User user = getApiUser(false);
        if (user != null) {
            Collect collect = collectService.selectByTopicIdAndUserId(id, user.getId());
            map.put("collect", collect);
        }
        // Vues du sujet +1
        String ip = IpUtil.getIpAddr(request);
        ip = ip.replace(":", "_").replace(".", "_");
        topic = topicService.updateViewCount(topic, ip);
        topic.setContent(SensitiveWordUtil.replaceSensitiveWord(topic.getContent(), "*", SensitiveWordUtil.MinMatchType));

        map.put("topic", topic);
        map.put("tags", tags);
        map.put("comments", comments);
        map.put("topicUser", topicUser);
        map.put("collects", collects);
        return success(map);
    }

    // enregistrer le sujet
    @PostMapping
    public Result create(@RequestBody Map<String, String> body) {
        User user = getApiUser();
        String title = body.get("title");
        String content = body.get("content");
        String tag = body.get("tag");
        ApiAssert.notEmpty(title, "Titre");
        ApiAssert.isNull(topicService.selectByTitle(title), "error");
        Topic topic = topicService.insert(title, content, tag, user);
        topic.setContent(SensitiveWordUtil.replaceSensitiveWord(topic.getContent(), "*", SensitiveWordUtil.MinMatchType));
        return success(topic);
    }


    //modifier le sujet
    @PutMapping(value = "/{id}")
    public Result edit(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        User user = getApiUser();
        String title = body.get("title");
        String content = body.get("content");
        ApiAssert.notEmpty(title, "Titre");
        Topic topic = topicService.selectById(id);
        ApiAssert.isTrue(topic.getUserId().equals(user.getId()), "Vous ne pouvez pas changer les topics des autres");
        topic.setTitle(Jsoup.clean(title, Whitelist.none().addTags("video")));
        topic.setContent(content);
        topic.setModifyTime(new Date());
        topicService.update(topic, null);
        topic.setContent(SensitiveWordUtil.replaceSensitiveWord(topic.getContent(), "*", SensitiveWordUtil.MinMatchType));
        return success(topic);
    }

    //supprimer le sujet
    @DeleteMapping("{id}")
    public Result delete(@PathVariable Integer id) {
        User user = getApiUser();
        Topic topic = topicService.selectById(id);
        ApiAssert.isTrue(topic.getUserId().equals(user.getId()), "Vous ne pouvez pas changer les topics des autres");
        topicService.delete(topic);
        return success();
    }

    //aimer le sujet
    @GetMapping("/{id}/vote")
    public Result vote(@PathVariable Integer id) {
        User user = getApiUser();
        Topic topic = topicService.selectById(id);
        ApiAssert.notNull(topic, "Le Topic a peut-être été supprimé");
        ApiAssert.notTrue(topic.getUserId().equals(user.getId()), "Bravo");
        int voteCount = topicService.vote(topic, getApiUser());
        return success(voteCount);
    }
}
