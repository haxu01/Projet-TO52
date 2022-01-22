package com.haoran.Brainstorming.directive;

import com.haoran.Brainstorming.plugin.SocialPlugin;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;


@Component
public class SocialDirective implements TemplateDirectiveModel {

    @Resource
    private SocialPlugin socialPlugin;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody
            templateDirectiveBody) throws TemplateException, IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
        environment.setVariable("socialList", builder.build().wrap(socialPlugin.getAllAvailableSocialPlatform()));
        templateDirectiveBody.render(environment.getOut());
    }
}
