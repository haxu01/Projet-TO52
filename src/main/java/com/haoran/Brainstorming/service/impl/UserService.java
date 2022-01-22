package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.mapper.UserMapper;
import com.haoran.Brainstorming.model.User;
import com.haoran.Brainstorming.service.*;
import com.haoran.Brainstorming.util.MyPage;
import com.haoran.Brainstorming.util.SpringContextUtil;
import com.haoran.Brainstorming.util.StringUtil;
import com.haoran.Brainstorming.util.bcrypt.BCryptPasswordEncoder;
import com.haoran.Brainstorming.util.identicon.Identicon;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService implements IUserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    @Lazy
    private ICollectService collectService;
    @Resource
    @Lazy
    private ITopicService topicService;
    @Resource
    @Lazy
    private ICommentService commentService;
    @Resource
    private Identicon identicon;
    @Resource
    private INotificationService notificationService;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private ICodeService codeService;

    // Interroger l'utilisateur selon le nom d'utilisateur, qui est utilisé pour obtenir les informations de l'utilisateur et comparer le mot de passe
    @Override
    public User selectByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    // Génération récursive de jetons pour empêcher la duplication de tokens
    private String generateToken() {
        String token = UUID.randomUUID().toString();
        User user = this.selectByToken(token);
        if (user != null) {
            return this.generateToken();
        }
        return token;
    }

    /**
     *S'inscrire pour créer un utilisateur
     *
     * @param username
     * @param password
     * @param avatar
     * @param email
     * @param bio
     * @param website
     * @return
     */
    @Override
    public User addUser(String username, String password, String avatar, String email, String bio, String website,
                        boolean needActiveEmail) {
        String token = this.generateToken();
        User user = new User();
        user.setUsername(username);
        if (!StringUtils.isEmpty(password)) user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setToken(token);
        user.setInTime(new Date());
        if (avatar == null) avatar = identicon.generator(username);
        user.setAvatar(avatar);
        user.setEmail(email);
        user.setBio(bio);
        user.setWebsite(website);
        user.setActive(systemConfigService.selectAllConfig().get("user_need_active").equals("0"));
        userMapper.insert(user);
        return this.selectById(user.getId());
    }

    // Générer récursivement des noms d'utilisateur pour éviter les doublons
    private String generateUsername() {
        String username = StringUtil.randomString(6);
        if (this.selectByUsername(username) != null) {
            return this.generateUsername();
        }
        return username;
    }


    // Interroger l'utilisateur en fonction du token d'utilisateur
    @Override
    public User selectByToken(String token) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getToken, token);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User selectByMobile(String mobile) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getMobile, mobile);
        return userMapper.selectOne(wrapper);
    }

    // Interroger l'utilisateur en fonction de l'e-mail de l'utilisateur
    @Override
    public User selectByEmail(String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getEmail, email);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    // Interroger les scores des utilisateurs
    @Override
    public List<User> selectTop(Integer limit) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("score").last("limit " + limit);
        return userMapper.selectList(wrapper);
    }

    // mettre à jour les informations de l'utilisateur
    @Override
    public void update(User user) {
        userMapper.updateById(user);
        SpringContextUtil.getBean(UserService.class).delRedisUser(user);
    }

    // ------------------------------- admin ------------------------------------------

    @Override
    public IPage<User> selectAll(Integer pageNo, String username) {
        MyPage<User> page = new MyPage<>(pageNo, Integer.parseInt((String) systemConfigService.selectAllConfig().get("page_size")));
        page.setDesc("in_time");
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(username)) {
            wrapper.lambda().eq(User::getUsername, username);
        }
        return userMapper.selectPage(page, wrapper);
    }

    public User selectByIdNoCatch(Integer id) {
        return userMapper.selectById(id);
    }

    // Demander le nombre de sujets ajoutés aujourd'hui
    @Override
    public int countToday() {
        return userMapper.countToday();
    }

    //supprimer l'utilisateur
    @Override
    public void deleteUser(Integer id) {
        notificationService.deleteByUserId(id);
        collectService.deleteByUserId(id);
        commentService.deleteByUserId(id);
        topicService.deleteByUserId(id);
        codeService.deleteByUserId(id);
        User user = this.selectById(id);
        SpringContextUtil.getBean(UserService.class).delRedisUser(user);
        userMapper.deleteById(id);
    }

    // supprimer le cache redis
    @Override
    public void delRedisUser(User user) {

    }
}
