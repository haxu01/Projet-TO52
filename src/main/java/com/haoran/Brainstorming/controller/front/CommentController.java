package com.haoran.Brainstorming.controller.front;

import com.haoran.Brainstorming.model.Comment;
import com.haoran.Brainstorming.model.Topic;
import com.haoran.Brainstorming.service.ICommentService;
import com.haoran.Brainstorming.service.ITopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;


@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Resource
    private ICommentService commentService;
    @Resource
    private ITopicService topicService;

    //Modifier les commentaires
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Comment comment = commentService.selectById(id);
        Topic topic = topicService.selectById(comment.getTopicId());
        model.addAttribute("comment", comment);
        model.addAttribute("topic", topic);
        return render("comment/edit");
    }
}
