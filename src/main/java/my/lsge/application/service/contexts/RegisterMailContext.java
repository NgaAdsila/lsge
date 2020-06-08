package my.lsge.application.service.contexts;

import my.lsge.application.dto.auth.SignUpReq;
import org.thymeleaf.context.Context;

public class RegisterMailContext extends BaseContext {

    public RegisterMailContext(SignUpReq req, String subject, String systemShortName,
                               String systemFullName, String homePageName, String homePageLink) {
        this.setMailTo(req.getEmail());
        this.setSubject(subject);
        this.setFormatPath("../templates/email/register_mail");

        Context context = new Context();
        context.setVariable("userFullName", req.getName());
        context.setVariable("systemShortName", systemShortName);
        context.setVariable("mail", req.getEmail());
        context.setVariable("userName", req.getUsername());
        context.setVariable("systemFullName", systemFullName);
        context.setVariable("homePageLink", homePageLink);
        context.setVariable("homePageName", homePageName);
        this.setContext(context);
    }
}
