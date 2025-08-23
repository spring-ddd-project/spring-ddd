package com.springddd;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@SpringBootApplication
public class SpringDDDLauncherApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDDDLauncherApplication.class, args);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    @Bean
    public TemplateEngine templateEngine() {
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", getClass().getClassLoader());
        TemplateEngine engine = TemplateEngine.create(codeResolver, ContentType.Html);
        engine.setBinaryStaticContent(false);
        engine.cleanAll();

        return engine;
    }


}
