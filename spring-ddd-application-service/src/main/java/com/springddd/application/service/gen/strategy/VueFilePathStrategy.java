package com.springddd.application.service.gen.strategy;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Set;

@Component
public class VueFilePathStrategy implements FilePathStrategy {
    private static final Set<String> SUPPORTED = Set.of(
            "index.vue", "recycle.vue", "form.vue", "api.ts", "i18n.en.json", "i18n.locale.json"
    );

    @Override
    public boolean supports(String templateName) {
        return SUPPORTED.contains(templateName);
    }

    @Override
    public String generatePath(String templateName, Map<String, Object> context, String projectName) {
        String moduleName = (String) context.get("moduleName");
        String requestName = (String) context.get("requestName");

        return switch (templateName) {
            case "index.vue" -> "apps/web-ele/src/views/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "index.vue";
            case "recycle.vue" -> "apps/web-ele/src/views/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "recycle.vue";
            case "form.vue" -> "apps/web-ele/src/views/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "form.vue";
            case "api.ts" -> "apps/web-ele/src/api/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "index.ts";
            case "i18n.en.json" -> "apps/web-ele/src/locales/langs/en-US/"
                    + requestName + ".json";
            case "i18n.locale.json" -> "apps/web-ele/src/locales/langs/zh-CN/"
                    + requestName + ".json";
            default -> null;
        };
    }
}











