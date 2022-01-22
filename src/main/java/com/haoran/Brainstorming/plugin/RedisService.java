package com.haoran.Brainstorming.plugin;

import com.haoran.Brainstorming.config.service.BaseService;
import com.haoran.Brainstorming.model.SystemConfig;
import com.haoran.Brainstorming.service.ISystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;


@Component
@DependsOn("mybatisPlusConfig")
public class RedisService implements BaseService<JedisPool> {

    @Resource
    private ISystemConfigService systemConfigService;
    private JedisPool jedisPool;
    private final Logger log = LoggerFactory.getLogger(RedisService.class);

    public void setJedis(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPool instance() {
        try {
            if (this.jedisPool != null) return this.jedisPool;
            // Obtenir la connexion à redis
            // host
            SystemConfig systemConfigHost = systemConfigService.selectByKey("redis_host");
            String host = systemConfigHost.getValue();
            // port
            SystemConfig systemConfigPort = systemConfigService.selectByKey("redis_port");
            String port = systemConfigPort.getValue();
            // password
            SystemConfig systemConfigPassword = systemConfigService.selectByKey("redis_password");
            String password = systemConfigPassword.getValue();
            password = StringUtils.isEmpty(password) ? null : password;
            // database
            SystemConfig systemConfigDatabase = systemConfigService.selectByKey("redis_database");
            String database = systemConfigDatabase.getValue();
            // timeout
            SystemConfig systemConfigTimeout = systemConfigService.selectByKey("redis_timeout");
            String timeout = systemConfigTimeout.getValue();

            if (!this.isRedisConfig()) {
                log.info("error");
                return null;
            }
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            // Configurer le nombre maximum d'instances inactives dans le pool de connexion jedis, la valeur par défaut est 8
            jedisPoolConfig.setMaxIdle(7);
            // Configurer le nombre maximum d'instances créées par le pool de connexion jedis, la valeur par défaut est 8
            jedisPoolConfig.setMaxTotal(20);
            //Lors de l'emprunt (introduction) d'une instance de jedis, s'il faut effectuer l'opération de validation à l'avance; si c'est vrai, l'instance de jedis obtenue est disponible;
            jedisPoolConfig.setTestOnBorrow(true);
            // renvoie une instance jedis au pool, s'il faut vérifier la disponibilité de la connexion (ping())
            jedisPoolConfig.setTestOnReturn(true);
            jedisPool = new JedisPool(jedisPoolConfig, host, Integer.parseInt(port), Integer.parseInt(timeout), password,
                    Integer.parseInt(database));
            log.info("Success");
            return this.jedisPool;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
    }

    // Détermine si redis est configuré
    public boolean isRedisConfig() {
        SystemConfig systemConfigHost = systemConfigService.selectByKey("redis_host");
        String host = systemConfigHost.getValue();
        // port
        SystemConfig systemConfigPort = systemConfigService.selectByKey("redis_port");
        String port = systemConfigPort.getValue();
        // database
        SystemConfig systemConfigDatabase = systemConfigService.selectByKey("redis_database");
        String database = systemConfigDatabase.getValue();
        // timeout
        SystemConfig systemConfigTimeout = systemConfigService.selectByKey("redis_timeout");
        String timeout = systemConfigTimeout.getValue();

        return !StringUtils.isEmpty(host) && !StringUtils.isEmpty(port) && !StringUtils.isEmpty(database) && !StringUtils
                .isEmpty(timeout);
    }

    // récupère la valeur de la chaîne
    public String getString(String key) {
        JedisPool instance = this.instance();
        if (StringUtils.isEmpty(key) || instance == null) return null;
        Jedis jedis = instance.getResource();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    public void setString(String key, String value) {
        this.setString(key, value, 300);
    }

    /**
     *Enregistrer les données avec le délai d'expiration dans redis et supprimer-les automatiquement lorsqu'elles expirent
     *
     * @param key
     * @param value
     * @param expireTime
     */
    public void setString(String key, String value, int expireTime) {
        JedisPool instance = this.instance();
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value) || instance == null) return;
        Jedis jedis = instance.getResource();
        SetParams params = new SetParams();
        params.px(expireTime * 1000);
        jedis.set(key, value, params);
        jedis.close();
    }

    public void delString(String key) {
        JedisPool instance = this.instance();
        if (StringUtils.isEmpty(key) || instance == null) return;
        Jedis jedis = instance.getResource();
        jedis.del(key);
        jedis.close();
    }



}
