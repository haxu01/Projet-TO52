package com.haoran.Brainstorming.controller.api;

import com.haoran.Brainstorming.model.OAuthUser;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.*;
import com.haoran.Brainstorming.util.MyPage;
import com.haoran.Brainstorming.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserApiController extends BaseApiController {

    @Resource
    private IUserService userService;
    @Resource
    private ITopicService topicService;
    @Resource
    private ICommentService commentService;
    @Resource
    private ICollectService collectService;
    @Resource
    private IOAuthUserService oAuthUserService;

    // informations personnelles de l'utilisateur
    @GetMapping("/{username}")
    public Result profile(@PathVariable String username) {
        // Interroger les informations personnelles de l'utilisateur
        User user = userService.selectByUsername(username);
        // Interroger les informations de l'utilisateur pour la connexion oauth
        List<OAuthUser> oAuthUsers = oAuthUserService.selectByUserId(user.getId());
        // Interroger le sujet de l'utilisateur
        MyPage<Map<String, Object>> topics = topicService.selectByUserId(user.getId(), 1, 10);
        // Interroger les commentaires auxquels l'utilisateur a participé
        MyPage<Map<String, Object>> comments = commentService.selectByUserId(user.getId(), 1, 10);
        //Interroger le nombre de collections
        Integer collectCount = collectService.countByUserId(user.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("oAuthUsers", oAuthUsers);
        map.put("topics", topics);
        map.put("comments", comments);
        map.put("collectCount", collectCount);
        return success(map);
    }

    //les sujet publié par l'utilisateur
    @GetMapping("/{username}/topics")
    public Result topics(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo) {
        User user = userService.selectByUsername(username);
        MyPage<Map<String, Object>> topics = topicService.selectByUserId(user.getId(), pageNo, null);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("topics", topics);
        return success(map);
    }

    //liste des commentaires de l'utilisateur
    @GetMapping("/{username}/comments")
    public Result comments(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo) {
        User user = userService.selectByUsername(username);
        MyPage<Map<String, Object>> comments = commentService.selectByUserId(user.getId(), pageNo, null);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("comments", comments);
        return success(map);
    }

    //les collections
    @GetMapping("/{username}/collects")
    public Result collects(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo) {
        User user = userService.selectByUsername(username);
        MyPage<Map<String, Object>> collects = collectService.selectByUserId(user.getId(), pageNo, null);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("collects", collects);
        return success(map);
    }
}
