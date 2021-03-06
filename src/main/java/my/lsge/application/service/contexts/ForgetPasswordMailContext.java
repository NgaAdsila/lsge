package my.lsge.application.service.contexts;

import my.lsge.application.dto.auth.ForgetPasswordRes;
import org.thymeleaf.context.Context;

public class ForgetPasswordMailContext extends BaseContext {

    public ForgetPasswordMailContext(ForgetPasswordRes res, String subject, String systemShortName,
                                     String systemFullName, String homePageName, String homePageLink,
                                     String resetLink) {
        this.setMailTo(res.getUser().getEmail());
        this.setSubject(subject);
        this.setFormatPath("../templates/email/forget_password_mail");

        Context context = new Context();
        context.setVariable("userFullName", res.getUser().getName());
        context.setVariable("systemShortName", systemShortName);
        context.setVariable("resetLink", resetLink);
        context.setVariable("systemFullName", systemFullName);
        context.setVariable("homePageLink", homePageLink);
        context.setVariable("homePageName", homePageName);
        this.setContext(context);
    }
}
