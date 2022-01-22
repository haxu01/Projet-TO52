package com.haoran.Brainstorming.controller.front;

import com.haoran.Brainstorming.model.Collect;
import com.haoran.Brainstorming.model.Tag;
import com.haoran.Brainstorming.model.Topic;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.ICollectService;
import com.haoran.Brainstorming.service.ITagService;
import com.haoran.Brainstorming.service.ITopicService;
import com.haoran.Brainstorming.service.IUserService;
import com.haoran.Brainstorming.util.IpUtil;
import com.haoran.Brainstorming.util.MyPage;
import com.haoran.Brainstorming.util.SensitiveWordUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/topic")
public class TopicController extends BaseController {

    @Resource
    private ITopicService topicService;
    @Resource
    private ITagService tagService;
    @Resource
    private IUserService userService;
    @Resource
    private ICollectService collectService;

    // détails du sujet
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model, HttpServletRequest request) {
        Topic topic = topicService.selectById(id);
        Assert.notNull(topic, "Topic n'existe pas");
        List<Tag> tags = tagService.selectByTopicId(id);
        User topicUser = userService.selectById(topic.getUserId());
        List<Collect> collects = collectService.selectByTopicId(id);
        if (getUser() != null) {
            Collect collect = collectService.selectByTopicIdAndUserId(id, getUser().getId());
            model.addAttribute("collect", collect);
        }
        // Vues du sujet +1
        String ip = IpUtil.getIpAddr(request);
        ip = ip.replace(":", "_").replace(".", "_");
        topic = topicService.updateViewCount(topic, ip);

        // filtre le contenu
        topic.setContent(SensitiveWordUtil.replaceSensitiveWord(topic.getContent(), "*", SensitiveWordUtil.MinMatchType));

        model.addAttribute("topic", topic);
        model.addAttribute("tags", tags);
        model.addAttribute("topicUser", topicUser);
        model.addAttribute("collects", collects);
        return render("topic/detail");
    }

    @GetMapping("/create")
    public String create(String tag, Model model) {
        model.addAttribute("tag", tag);
        return render("topic/create");
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Topic topic = topicService.selectById(id);
        Assert.isTrue(topic.getUserId().equals(getUser().getId()), "Error");
        List<Tag> tagList = tagService.selectByTopicId(id);
        String tags = StringUtils.collectionToCommaDelimitedString(tagList.stream().map(Tag::getName).collect(Collectors
                .toList()));

        model.addAttribute("topic", topic);
        model.addAttribute("tags", tags);
        return render("topic/edit");
    }

    @GetMapping("/tag/{name}")
    public String tag(@PathVariable String name, @RequestParam(defaultValue = "1") Integer pageNo, Model model) {
        Tag tag = tagService.selectByName(name);
        Assert.notNull(tag, "Tag n'existe pas");
        MyPage<Map<String, Object>> iPage = tagService.selectTopicByTagId(tag.getId(), pageNo);
        model.addAttribute("tag", tag);
        model.addAttribute("page", iPage);
        return render("tag/tag");
    }
}
