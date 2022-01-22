package com.haoran.Brainstorming.hook;

import org.aspectj.lang.annotation.Pointcut;


public class UserServiceHook {

    @Pointcut("execution(public * com.haoran.Brainstorming.service.IUserService.selectByUsername(..))")
    public void selectByUsername() {
    }

    @Pointcut("execution(public * com.haoran.Brainstorming.service.IUserService.selectByToken(..))")
    public void selectByToken() {
    }

    @Pointcut("execution(public * com.haoran.Brainstorming.service.IUserService.selectById(..))")
    public void selectById() {
    }

    @Pointcut("execution(public * com.haoran.Brainstorming.service.IUserService.delRedisUser(..))")
    public void delRedisUser() {
    }

}
