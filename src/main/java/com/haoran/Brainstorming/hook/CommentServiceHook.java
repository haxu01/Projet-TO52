package com.haoran.Brainstorming.hook;

import org.aspectj.lang.annotation.Pointcut;


public class CommentServiceHook {

    @Pointcut("execution(public * com.haoran.Brainstorming.service.ICommentService.selectByTopicId(..))")
    public void selectByTopicId() {
    }

    @Pointcut("execution(public * com.haoran.Brainstorming.service.ICommentService.insert(..))")
    public void insert() {
    }

    @Pointcut("execution(public * com.haoran.Brainstorming.service.ICommentService.update(..))")
    public void update() {
    }

    @Pointcut("execution(public * com.haoran.Brainstorming.service.ICommentService.vote(..))")
    public void vote() {
    }

    @Pointcut("execution(public * com.haoran.Brainstorming.service.ICommentService.delete(..))")
    public void delete() {
    }

}
