package com.haoran.Brainstorming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication(scanBasePackages = "com.haoran.Brainstorming",
        exclude = {DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class})
@EnableAspectJAutoProxy
public class PybbsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PybbsApplication.class, args);
    }
}
