package com.haoran.Brainstorming.config;

import com.haoran.Brainstorming.directive.*;
import com.haoran.Brainstorming.util.BaseModel;
import com.haoran.Brainstorming.util.LocaleMessageSourceUtil;
import freemarker.template.TemplateModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Configuration
public class FreemarkerConfig {

    private Logger log = LoggerFactory.getLogger(FreeMarkerConfig.class);

    @Resource
    private freemarker.template.Configuration configuration;
    @Resource
    private TopicListDirective topicListDirective;
    @Resource
    private OtherTopicDirective otherTopicDirective;
    @Resource
    private NotificationsDirective notificationsDirective;
    @Resource
    private ScoreDirective scoreDirective;
    @Resource
    private SearchDirective searchDirective;
    @Resource
    private TagsDirective tagsDirective;
    @Resource
    private UserTopicsDirective userTopicsDirective;
    @Resource
    private UserCommentsDirective userCommentsDirective;
    @Resource
    private UserCollectsDirective userCollectsDirective;
    @Resource
    private TopicCommentsDirective topicCommentsDirective;
    @Resource
    private SocialDirective socialDirective;
    @Resource
    private BaseModel baseModel;
    @Resource
    private ShiroTag shiroTag;
    @Resource
    private LocaleMessageSourceUtil localeMessageSourceUtil;

    @PostConstruct
    public void setSharedVariable() throws TemplateModelException {

        //Injecter la configuration globale dans freemarker
        configuration.setSharedVariable("model", baseModel);
        // authentification shiro
        configuration.setSharedVariable("sec", shiroTag);
        configuration.setSharedVariable("tag_topics", topicListDirective);
        configuration.setSharedVariable("tag_other_topic", otherTopicDirective);
        configuration.setSharedVariable("tag_notifications", notificationsDirective);
        configuration.setSharedVariable("tag_score", scoreDirective);
        configuration.setSharedVariable("tag_search", searchDirective);
        configuration.setSharedVariable("tag_tags", tagsDirective);
        configuration.setSharedVariable("tag_user_topics", userTopicsDirective);
        configuration.setSharedVariable("tag_user_comments", userCommentsDirective);
        configuration.setSharedVariable("tag_user_collects", userCollectsDirective);
        configuration.setSharedVariable("tag_topic_comments", topicCommentsDirective);
        configuration.setSharedVariable("tag_social_list", socialDirective);

        configuration.setSharedVariable("i18n", localeMessageSourceUtil);
    }

}
