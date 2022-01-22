package com.haoran.Brainstorming.config.service;

import com.haoran.Brainstorming.model.SensitiveWord;
import com.haoran.Brainstorming.service.ISensitiveWordService;
import com.haoran.Brainstorming.util.SensitiveWordUtil;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@DependsOn("mybatisPlusConfig")
public class SensitiveWordFilterService {

    @Resource
    private ISensitiveWordService sensitiveWordService;

    // initialise le filtre
    @PostConstruct
    public void init() {
        List<SensitiveWord> sensitiveWords = sensitiveWordService.selectAll();
        Set<String> sensitiveWordSet = new HashSet<>();
        for (SensitiveWord sensitiveWord : sensitiveWords) {
            sensitiveWordSet.add(sensitiveWord.getWord());
        }
        SensitiveWordUtil.init(sensitiveWordSet);
    }
}
