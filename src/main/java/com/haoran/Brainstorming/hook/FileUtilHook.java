package com.haoran.Brainstorming.hook;

import org.aspectj.lang.annotation.Pointcut;

public class FileUtilHook {

    @Pointcut("execution(public * com.haoran.Brainstorming.util.FileUtil.upload(..))")
    public void upload() {
    }

}
