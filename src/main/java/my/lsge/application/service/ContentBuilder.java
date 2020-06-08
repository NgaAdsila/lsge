package my.lsge.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class ContentBuilder {
    private TemplateEngine templateEngine;

    @Autowired
    public ContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(String formatPath, Context context) {
        return templateEngine.process(formatPath, context);
    }
}
