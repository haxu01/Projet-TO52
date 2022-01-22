package com.haoran.Brainstorming.service.impl;

import com.haoran.Brainstorming.mapper.SystemConfigMapper;
import com.haoran.Brainstorming.model.SystemConfig;
import com.haoran.Brainstorming.service.ISystemConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
public class SystemConfigService implements ISystemConfigService {

    @Resource
    private SystemConfigMapper systemConfigMapper;

    private static Map<String, String> SYSTEM_CONFIG;

    @Override
    public Map<String, String> selectAllConfig() {
        if (SYSTEM_CONFIG != null) return SYSTEM_CONFIG;
        List<SystemConfig> systemConfigs = systemConfigMapper.selectList(null);
        SYSTEM_CONFIG = systemConfigs.stream()
                .filter(systemConfig -> systemConfig.getPid() != 0)
                .collect(Collectors.toMap(SystemConfig::getKey, SystemConfig::getValue));
        return SYSTEM_CONFIG;
    }

    // récupère la valeur par clé
    @Override
    public SystemConfig selectByKey(String key) {
        QueryWrapper<SystemConfig> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SystemConfig::getKey, key);
        return systemConfigMapper.selectOne(wrapper);
    }

    @Override
    public Map<String, Object> selectAll() {
        Map<String, Object> map = new LinkedHashMap<>();
        List<SystemConfig> systemConfigs = systemConfigMapper.selectList(null);
        // D'abord extraire tous les nœuds parents
        List<SystemConfig> p = systemConfigs.stream().filter(systemConfig -> systemConfig.getPid() == 0).collect
                (Collectors.toList());
        // Traverser le nœud parent pour obtenir tous les nœuds enfants sous le nœud parent
        p.forEach(systemConfig -> {
            List<SystemConfig> collect = systemConfigs.stream()
                    .filter(systemConfig1 -> systemConfig1.getPid().equals(systemConfig.getId()))
                    .collect(Collectors.toList());
            map.put(systemConfig.getDescription(), collect);
        });
        return map;
    }

    // Après avoir mis à jour les paramètres système, effacez le cache de selectAllConfig()
    @Override
    public void update(List<Map<String, String>> list) {
        for (Map<String, String> map : list) {
            String key = map.get("name");
            String value = map.get("value");
            // Si le mot de passe n'a pas changé, ne le changer pas
            if ((key.equals("mail_password") && value.equals("*******")) || (key.equals("redis_password") && value.equals
                    ("*******")) || (key.equals("oauth_github_client_secret") && value.equals("*******"))) {
                continue;
            }
            SystemConfig systemConfig = new SystemConfig();
            systemConfig.setKey(key);
            systemConfig.setValue(value);
            QueryWrapper<SystemConfig> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SystemConfig::getKey, systemConfig.getKey());
            systemConfigMapper.update(systemConfig, wrapper);
        }
        SYSTEM_CONFIG = null;
    }


    // mettre à jour les données selon la clé
    @Override
    public void updateByKey(String key, SystemConfig systemConfig) {
        QueryWrapper<SystemConfig> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SystemConfig::getKey, key);
        systemConfigMapper.update(systemConfig, wrapper);
        SYSTEM_CONFIG = null;
    }

}
