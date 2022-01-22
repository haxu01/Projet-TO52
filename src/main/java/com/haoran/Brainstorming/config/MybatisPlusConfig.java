package com.haoran.Brainstorming.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;


@Configuration
@MapperScan("com.haoran.Brainstorming.mapper")
public class MybatisPlusConfig {


    @Bean("mybatisSqlSession")
    @DependsOn("flywayConfig")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        //Enregistrer la carte
        configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory());
        // ajouter une source de données
        sqlSessionFactory.setDataSource(dataSource);

        sqlSessionFactory.setConfiguration(configuration);

        // ajouter le plugin de pagination
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setSqlParser(new JsqlParserCountOptimize());
        sqlSessionFactory.setPlugins(new Interceptor[]{paginationInterceptor});

        return sqlSessionFactory.getObject();
    }

}
