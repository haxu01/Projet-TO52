package com.haoran.Brainstorming.plugin;

import com.haoran.Brainstorming.model.Comment;
import com.haoran.Brainstorming.model.Topic;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.model.vo.CommentsByTopic;
import com.haoran.Brainstorming.service.ISystemConfigService;
import com.haoran.Brainstorming.service.ITopicService;
import com.haoran.Brainstorming.util.JsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Aspect
public class RedisCachePlugin {

    @Resource
    private RedisService redisService;
    @Resource
    private ITopicService topicService;
    @Resource
    private ISystemConfigService systemConfigService;

    // ---------- topic cache start ----------

    @Around("com.haoran.Brainstorming.hook.TopicServiceHook.selectById()")
    public Object topicSelectById(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String topicJson = redisService.getString(String.format(RedisKeys.REDIS_TOPIC_KEY, proceedingJoinPoint.getArgs()[0]));
        if (topicJson == null) {
            Object topic = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            // cache dans redis
            redisService.setString(String.format(RedisKeys.REDIS_TOPIC_KEY, proceedingJoinPoint.getArgs()[0]), JsonUtil.objectToJson(topic));
            return topic;
        } else {
            return JsonUtil.jsonToObject(topicJson, Topic.class);
        }
    }

    @Around("com.haoran.Brainstorming.hook.TopicServiceHook.updateViewCount()")
    public Object topicUpdateViewCount(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Topic topic = (Topic) proceedingJoinPoint.getArgs()[0];
        String ip = (String) proceedingJoinPoint.getArgs()[1];
        String s = redisService.getString(String.format(RedisKeys.REDIS_TOPIC_VIEW_IP_ID_KEY, ip, topic.getId()));
        if (redisService.isRedisConfig()) {
            if (s == null) {
                topic.setView(topic.getView() + 1);
                topicService.update(topic, null);
                redisService.setString(String.format(RedisKeys.REDIS_TOPIC_VIEW_IP_ID_KEY, ip, topic.getId()), String.valueOf
                        (topic.getId()), Integer.parseInt(systemConfigService.selectAllConfig().get("topic_view_increase_interval").toString()));
            }
            return topic;
        } else {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        }
    }

    @Around("com.haoran.Brainstorming.hook.TopicServiceHook.vote()")
    public Object voteTopic(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // Supprimer les données de sujet mises en cache dans redis après avoir aimé
        Topic topic = (Topic) proceedingJoinPoint.getArgs()[0];
        redisService.delString(String.format(RedisKeys.REDIS_TOPIC_KEY, topic.getId()));
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

    @After("com.haoran.Brainstorming.hook.TopicServiceHook.update()")
    public void topicUpdate(JoinPoint joinPoint) {
        Topic topic = (Topic) joinPoint.getArgs()[0];
        redisService.setString(String.format(RedisKeys.REDIS_TOPIC_KEY, topic.getId()), JsonUtil.objectToJson(topic));
    }

    // ---------- topic cache end ----------

    // ---------- comment cache start ----------

    @Around("com.haoran.Brainstorming.hook.CommentServiceHook.selectByTopicId()")
    public Object commentSelectByTopicId(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Integer topicId = (Integer) proceedingJoinPoint.getArgs()[0];
        String commentsJson = redisService.getString(String.format(RedisKeys.REDIS_COMMENTS_KEY, topicId));
        if (commentsJson != null) {
            return JsonUtil.jsonToListObject(commentsJson, CommentsByTopic.class);
        } else {
            Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            redisService.setString(String.format(RedisKeys.REDIS_COMMENTS_KEY, topicId), JsonUtil.objectToJson(returnValue));
            return returnValue;
        }
    }

    @After("com.haoran.Brainstorming.hook.CommentServiceHook.insert()")
    public void commentInsert(JoinPoint joinPoint) {
        Comment comment = (Comment) joinPoint.getArgs()[0];
        redisService.delString(String.format(RedisKeys.REDIS_COMMENTS_KEY, comment.getTopicId()));
    }

    @After("com.haoran.Brainstorming.hook.CommentServiceHook.update()")
    public void commentUpdate(JoinPoint joinPoint) {
        Comment comment = (Comment) joinPoint.getArgs()[0];
        redisService.delString(String.format(RedisKeys.REDIS_COMMENTS_KEY, comment.getTopicId()));
    }

    @After("com.haoran.Brainstorming.hook.CommentServiceHook.delete()")
    public void commentDelete(JoinPoint joinPoint) {
        Comment comment = (Comment) joinPoint.getArgs()[0];
        redisService.delString(String.format(RedisKeys.REDIS_COMMENTS_KEY, comment.getTopicId()));
    }

    @Around("com.haoran.Brainstorming.hook.CommentServiceHook.vote()")
    public Object voteComment(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Comment comment = (Comment) proceedingJoinPoint.getArgs()[0];
        redisService.delString(String.format(RedisKeys.REDIS_COMMENTS_KEY, comment.getTopicId()));
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

    // ---------- comment cache end ----------

    // ---------- user cache start ----------

    @Around("com.haoran.Brainstorming.hook.UserServiceHook.selectByUsername()")
    public Object userSelectByUsername(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String username = (String) proceedingJoinPoint.getArgs()[0];
        String userJson = redisService.getString(String.format(RedisKeys.REDIS_USER_USERNAME_KEY, username));
        if (userJson != null) {
            return JsonUtil.jsonToObject(userJson, User.class);
        } else {
            Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            if (returnValue != null) {
                redisService.setString(String.format(RedisKeys.REDIS_USER_USERNAME_KEY, username), JsonUtil.objectToJson(returnValue));
            }
            return returnValue;
        }
    }

    @Around("com.haoran.Brainstorming.hook.UserServiceHook.selectByToken()")
    public Object userSelectByToken(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String token = (String) proceedingJoinPoint.getArgs()[0];
        String userJson = redisService.getString(String.format(RedisKeys.REDIS_USER_TOKEN_KEY, token));
        if (userJson != null) {
            return JsonUtil.jsonToObject(userJson, User.class);
        } else {
            Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            if (returnValue != null) {
                redisService.setString(String.format(RedisKeys.REDIS_USER_TOKEN_KEY, token), JsonUtil.objectToJson(returnValue));
            }
            return returnValue;
        }
    }

    @Around("com.haoran.Brainstorming.hook.UserServiceHook.selectById()")
    public Object userSelectById(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Integer id = (Integer) proceedingJoinPoint.getArgs()[0];
        String userJson = redisService.getString(String.format(RedisKeys.REDIS_USER_ID_KEY, id));
        if (userJson != null) {
            return JsonUtil.jsonToObject(userJson, User.class);
        } else {
            Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            if (returnValue != null) {
                redisService.setString(String.format(RedisKeys.REDIS_USER_ID_KEY, id), JsonUtil.objectToJson(returnValue));
            }
            return returnValue;
        }
    }

    @After("com.haoran.Brainstorming.hook.UserServiceHook.delRedisUser()")
    public void userDelRedisUser(JoinPoint joinPoint) {
        User user = (User) joinPoint.getArgs()[0];
        redisService.delString(String.format(RedisKeys.REDIS_USER_ID_KEY, user.getId()));
        redisService.delString(String.format(RedisKeys.REDIS_USER_USERNAME_KEY, user.getUsername()));
        redisService.delString(String.format(RedisKeys.REDIS_USER_TOKEN_KEY, user.getToken()));
    }

    // ---------- user cache end ----------

    static class RedisKeys {
        public static final String REDIS_TOPIC_KEY = "pybbs_topic_%s";
        public static final String REDIS_TOPIC_VIEW_IP_ID_KEY = "pybbs_topic_view_ip_%s_topic_%s";
        public static final String REDIS_COMMENTS_KEY = "pybbs_comments_%s";

        public static final String REDIS_USER_ID_KEY = "pybbs_user_id_%s";
        public static final String REDIS_USER_USERNAME_KEY = "pybbs_user_username_%s";
        public static final String REDIS_USER_TOKEN_KEY = "pybbs_user_token_%s";
    }
}
