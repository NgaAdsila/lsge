package my.lsge.application.service.contexts;

import my.lsge.application.dto.admin.user.UserPasswordRes;
import org.thymeleaf.context.Context;

public class ResetPasswordByManagerMailContext extends BaseContext {

    public ResetPasswordByManagerMailContext(UserPasswordRes res, String subject, String systemShortName,
                                             String systemFullName, String homePageName, String homePageLink) {
        this.setMailTo(res.getUser().getEmail());
        this.setSubject(subject);
        this.setFormatPath("../templates/email/reset_password_by_manager_mail");

        Context context = new Context();
        context.setVariable("userFullName", res.getUser().getName());
        context.setVariable("systemShortName", systemShortName);
        context.setVariable("systemFullName", systemFullName);
        context.setVariable("homePageLink", homePageLink);
        context.setVariable("homePageName", homePageName);
        context.setVariable("email", res.getUser().getEmail());
        context.setVariable("rawPassword", res.getRawPassword());
        this.setContext(context);
    }
}
