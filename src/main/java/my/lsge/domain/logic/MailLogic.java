package my.lsge.domain.logic;

import my.lsge.application.dto.auth.ForgetPasswordRes;
import my.lsge.application.dto.auth.SignUpReq;
import my.lsge.application.service.EmailService;
import my.lsge.application.service.contexts.ForgetPasswordMailContext;
import my.lsge.application.service.contexts.RegisterMailContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MailLogic extends BaseLogic {
    @Autowired
    private EmailService emailService;

    @Value("${system.short_name}")
    private String systemShortName;

    @Value("${system.full_name}")
    private String systemFullName;

    @Value("${system.home_page.name}")
    private String homePageName;

    @Value("${system.home_page.link}")
    private String homePageLink;

    @Async
    public void sendRegisterMail(SignUpReq req) {
        if (req == null || StringUtils.isBlank(req.getEmail())) {
            return;
        }
        RegisterMailContext context = new RegisterMailContext(req, language.getString("mail.subject.register"),
                systemShortName, systemFullName, homePageName, homePageLink);
        emailService.sendMailHtml(context);
    }

    @Async
    public void sendForgetPasswordMail(ForgetPasswordRes res) {
        if (!res.getResponse().getStatusCode().equals(HttpStatus.OK) || res.getUser() == null
                || StringUtils.isBlank(res.getUser().getEmail())) {
            return;
        }
        ForgetPasswordMailContext context = new ForgetPasswordMailContext(res, language.getString("mail.subject.forget_password"),
                systemShortName, systemFullName, homePageName, homePageLink);
        emailService.sendMailHtml(context);
    }
}
