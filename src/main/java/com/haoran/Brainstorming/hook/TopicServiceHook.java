package com.haoran.Brainstorming.hook;

import org.aspectj.lang.annotation.Pointcut;


public class TopicServiceHook {

    @Pointcut("execution(public * com.haoran.Brainstorming.service.ITopicService.search(..))")
    public void search() {
    }

    @Pointcut("execution(public * com.haoran.Brainstorming.service.ITopicService.selectById(..))")
    public void selectById() {
    }

    @Pointcut("execution(public * com.haoran.Brainstorming.service.ITopicService.update(..))")
    public void update() {
    }

    @Pointcut("execution(public * com.haoran.Brainstorming.service.ITopicService.vote(..))")
    public void vote() {
    }

    @Pointcut("execution(public * com.haoran.Brainstorming.service.ITopicService.updateViewCount(..))")
    public void updateViewCount() {
    }

}
