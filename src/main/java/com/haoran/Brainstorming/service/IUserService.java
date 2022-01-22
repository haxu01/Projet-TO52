package com.haoran.Brainstorming.service;

import com.haoran.Brainstorming.model.User;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface IUserService {
    // Interroger l'utilisateur selon le nom d'utilisateur, qui est utilisé pour obtenir les informations de l'utilisateur et comparer le mot de passe
    User selectByUsername(String username);

    User addUser(String username, String password, String avatar, String email, String bio, String website,
                 boolean needActiveEmail);

    // Interroger l'utilisateur en fonction du token d'utilisateur
    User selectByToken(String token);

    User selectByMobile(String mobile);

    User selectByEmail(String email);

    User selectById(Integer id);

    // Interroger les scores des utilisateurs
    List<User> selectTop(Integer limit);

    // mettre à jour les informations de l'utilisateur
    void update(User user);

    IPage<User> selectAll(Integer pageNo, String username);

    User selectByIdNoCatch(Integer id);

    // Demander le nombre de sujets ajoutés aujourd'hui
    int countToday();

    // supprimer les utilisateurs
    void deleteUser(Integer id);

    // supprimer le cache redis
    void delRedisUser(User user);
}
