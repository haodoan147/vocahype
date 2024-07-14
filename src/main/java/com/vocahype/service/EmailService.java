package com.vocahype.service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final Configuration configuration;

    private void sendEmail(final String subject, final String recipient, final String template, final String from, final String hash,
                           final boolean hasLogo, final List<Map<String, Object>> dataList, final String fileName) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );
            helper.setSubject(subject);
            helper.setTo(recipient.split(","));
            helper.setFrom(from);
            helper.setText(template, true);
            if (hasLogo) {
                helper.addInline("logo.png", new ClassPathResource("templates/its-logo.png"));
            }
            javaMailSender.send(mimeMessage);
            log.info("LIVE EMAIL ON: [ {} ] sent to [ {} ]", subject, recipient);
        } catch (MessagingException | MailSendException e) {
            log.error(e.getMessage());
        }
    }

    @Async
    public void sendEmail(final String subject, final String recipient, final String template) {
        sendEmail(subject, recipient, template, "Vocahype-no-reply", null, false, null, null);
    }

    public String getTemplate(final String template, final Map<String, Object> model) throws IOException, TemplateException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        freemarker.template.Template templateFreeMaker =
                new freemarker.template.Template("Not learn", template, configuration);
        templateFreeMaker.process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

}
