package com.haoran.Brainstorming.controller.api;

import com.haoran.Brainstorming.exception.ApiAssert;
import com.haoran.Brainstorming.model.Collect;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.ICollectService;
import com.haoran.Brainstorming.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/collect")
public class CollectApiController extends BaseApiController {

    @Resource
    private ICollectService collectService;


    //collecter un sujet
    @PostMapping("/{topicId}")
    public Result get(@PathVariable Integer topicId) {
        User user = getApiUser();
        Collect collect = collectService.selectByTopicIdAndUserId(topicId, user.getId());
        ApiAssert.isNull(collect, "Chaque Topic ne peut pas etre collecte qu'une seule fois");
        collectService.insert(topicId, user);
        return success();
    }

    //supprimer une collection
    @DeleteMapping("/{topicId}")
    public Result delete(@PathVariable Integer topicId) {
        User user = getApiUser();
        Collect collect = collectService.selectByTopicIdAndUserId(topicId, user.getId());
        ApiAssert.notNull(collect, "error");
        collectService.delete(topicId, user.getId());
        return success();
    }
}
