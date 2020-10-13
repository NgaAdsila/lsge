package my.lsge.application.service.contexts;

import my.lsge.application.dto.auth.ForgetPasswordRes;
import org.thymeleaf.context.Context;

public class ResetPasswordMailContext extends BaseContext {

    public ResetPasswordMailContext(ForgetPasswordRes res, String subject, String systemShortName,
                                    String systemFullName, String homePageName, String homePageLink) {
        this.setMailTo(res.getUser().getEmail());
        this.setSubject(subject);
        this.setFormatPath("../templates/email/reset_password_mail");

        Context context = new Context();
        context.setVariable("userFullName", res.getUser().getName());
        context.setVariable("systemShortName", systemShortName);
        context.setVariable("systemFullName", systemFullName);
        context.setVariable("homePageLink", homePageLink);
        context.setVariable("homePageName", homePageName);
        this.setContext(context);
    }
}
