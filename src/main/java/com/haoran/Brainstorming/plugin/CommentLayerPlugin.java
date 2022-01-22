package com.haoran.Brainstorming.plugin;

import com.haoran.Brainstorming.model.vo.CommentsByTopic;
import com.haoran.Brainstorming.service.impl.SystemConfigService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Component
@Aspect
public class CommentLayerPlugin {

    @Resource
    private SystemConfigService systemConfigService;

    @Around("com.haoran.Brainstorming.hook.CommentServiceHook.selectByTopicId()")
    public Object selectByTopicId(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<CommentsByTopic> newComments = (List<CommentsByTopic>) proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        if (systemConfigService.selectAllConfig().get("comment_layer").equals("1")) {
            //Afficher les commentaires par layer
            return this.sortByLayer(newComments);
        }
        return newComments;
    }

    //Sorte par layer
    private List<CommentsByTopic> sortByLayer(List<CommentsByTopic> comments) {
        List<CommentsByTopic> newComments = new ArrayList<>();
        for (CommentsByTopic comment : comments) {
            if (comment.getCommentId() == null) {
                newComments.add(comment);
            } else {
                int idIndex = -1, commentIdIndex = -1;
                boolean idIndexFlag = false, commentIdIndexFlag = false;
                for (int i = newComments.size() - 1; i >= 0; i--) {
                    if (!idIndexFlag && comment.getCommentId().equals(newComments.get(i).getId())) {
                        idIndex = i;
                        idIndexFlag = true;
                    }
                    if (!commentIdIndexFlag && comment.getCommentId().equals(newComments.get(i).getCommentId())) {
                        commentIdIndex = i;
                        commentIdIndexFlag = true;
                    }
                }
                if (idIndex == -1) {
                    newComments.add(comment);
                } else {
                    int layer = newComments.get(idIndex).getLayer();
                    comment.setLayer(layer + 1);
                    int count = 0;
                    if (commentIdIndex != -1) {
                        for (CommentsByTopic newComment : newComments) {
                            if (newComments.get(commentIdIndex).getId().equals(newComment.getCommentId())) count++;
                        }
                    }
                    newComments.add(commentIdIndex == -1 ? idIndex + 1 : commentIdIndex + 1 + count, comment);
                }
            }
        }
        return newComments;
    }
}
