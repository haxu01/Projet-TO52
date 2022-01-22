package com.haoran.Brainstorming.service;

import com.haoran.Brainstorming.model.SystemConfig;

import java.util.List;
import java.util.Map;


public interface ISystemConfigService {
    Map<String, String> selectAllConfig();

    // récupère la valeur par clé
    SystemConfig selectByKey(String key);

    Map<String, Object> selectAll();

    // Après avoir mis à jour les paramètres système, effacez le cache de selectAllConfig()
    void update(List<Map<String, String>> list);

    // mettre à jour les données selon la clé
    void updateByKey(String key, SystemConfig systemConfig);
}
