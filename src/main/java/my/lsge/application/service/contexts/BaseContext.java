package my.lsge.application.service.contexts;

import lombok.Getter;
import lombok.Setter;
import org.thymeleaf.context.Context;

@Getter
@Setter
public class BaseContext {
    private Context context;

    private String mailTo;

    private String subject;

    private String formatPath;
}
