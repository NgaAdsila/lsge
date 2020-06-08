package my.lsge.application.service;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.service.contexts.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Slf4j
@Component
public class EmailService {

    @Autowired
    public JavaMailSender mailSender;

    @Autowired
    private ContentBuilder contentBuilder;

    public void sendMailText(BaseContext context) {
        try {
            String content = contentBuilder.build(context.getFormatPath(), context.getContext());
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(context.getMailTo());
            message.setSubject(context.getSubject());
            message.setText(content);
            mailSender.send(message);
            log.info(String.format("Send mail to %s successfully!", context.getMailTo()));
        } catch (Exception e) {
            log.error(String.format("Send mail to %s error: %s", context.getMailTo(), e.getMessage()));
        }
    }

    public void sendMailHtml(BaseContext context) {
        try {
            String content = contentBuilder.build(context.getFormatPath(), context.getContext());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(context.getMailTo());
            helper.setSubject(context.getSubject());
            helper.setText(content, true);
            mailSender.send(message);
            log.info(String.format("Send mail to %s successfully!", context.getMailTo()));
        } catch (Exception e) {
            log.error(String.format("Send mail to %s error: %s", context.getMailTo(), e.getMessage()));
        }
    }
}
