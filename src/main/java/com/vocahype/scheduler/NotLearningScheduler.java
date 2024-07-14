package com.vocahype.scheduler;

import com.vocahype.dto.WordToLearnDTO;
import com.vocahype.entity.User;
import com.vocahype.entity.UserWordComprehension;
import com.vocahype.exception.UserFriendlyException;
import com.vocahype.service.AIService;
import com.vocahype.service.EmailService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Component
@RequiredArgsConstructor
public class NotLearningScheduler {

    private final EmailService emailService;
    private final AIService aiService;

    @Async
    @Scheduled(cron = "0 0 */6 * * *")
    public void createMeterNextPartitions() {
        log.info("Learning Partitionings start...");
        Map<User, List<UserWordComprehension>> listWordNotLearn = aiService.getListWordNotLearn();
        for (Map.Entry<User, List<UserWordComprehension>> listByUser : listWordNotLearn.entrySet()) {

            List<WordToLearnDTO> wordToLearn = listByUser.getValue().stream()
                    .map(u -> new WordToLearnDTO(u.getUserWordComprehensionID().getWord()))
                    .collect(Collectors.toList());

            Map<String, Object> model = new HashMap<>();
            model.put("user", listByUser.getKey().getFirstName());
            model.put("appUrl", "https://vocahype.netlify.app/");
            model.put("list", wordToLearn);
            try {
                String template = emailService.getTemplate("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Vocabulary Reminder</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;\n" +
                        "            background-color: #f4f4f4;\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "        .container {\n" +
                        "            width: 100%;\n" +
                        "            max-width: 600px;\n" +
                        "            margin: 0 auto;\n" +
                        "            background-color: #ffffff;\n" +
                        "            border-radius: 8px;\n" +
                        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                        "            overflow: hidden;\n" +
                        "        }\n" +
                        "        .header {\n" +
                        "            background-color: #0073e6;\n" +
                        "            color: #ffffff;\n" +
                        "            padding: 20px;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        .content {\n" +
                        "            padding: 20px;\n" +
                        "        }\n" +
                        "        .word-list {\n" +
                        "            list-style: none;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "        .word-list li {\n" +
                        "            background-color: #f9f9f9;\n" +
                        "            margin-bottom: 10px;\n" +
                        "            padding: 10px;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "            border-radius: 4px;\n" +
                        "        }\n" +
                        "        .word-list li a {\n" +
                        "            color: #0073e6;\n" +
                        "            text-decoration: none;\n" +
                        "        }\n" +
                        "        .footer {\n" +
                        "            background-color: #f4f4f4;\n" +
                        "            padding: 20px;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        .footer a {\n" +
                        "            color: #0073e6;\n" +
                        "            text-decoration: none;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <div class=\"header\">\n" +
                        "            <h1>Hi ${user}!</h1>\n" +
                        "        </div>\n" +
                        "        <div class=\"content\">\n" +
                        "            <p>We hope you're having a fantastic day!</p>\n" +
                        "            <p>Don't forget to learn your new words today. Here are some interesting words waiting for you:</p>\n" +
                        "            <ul class=\"word-list\">\n" +
                        "                <#if list?has_content && (list?size > 0)>" +
                        "                    <#list list as word>\n" +
                        "                        <li><a href=\"${word.url}\">${word.text}</a></li>\n" +
                        "                    </#list>\n" +
                        "                <#else>\n" +
                        "                    <b>No Data</b>\n" +
                        "                </#if>" +
                        "            </ul>\n" +
                        "            <p>Click on the words to explore their meanings and usage. Keep up the great work!</p>\n" +
                        "            <p>Happy learning,</p>\n" +
                        "            <p>The Vocabulary App Team</p>\n" +
                        "        </div>\n" +
                        "        <div class=\"footer\">\n" +
                        "            <p><a href=\"${appUrl}\">Open the Vocabulary App</a></p>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>\n", model);
                emailService.sendEmail("Let's learn some new words today!", listByUser.getKey().getLoginName(), template);
            } catch (IOException | TemplateException e) {
                log.info(e.getMessage());
                throw new UserFriendlyException("Error sending email");
            }
        }
        log.info("Learning Partitionings completed!");
    }

}
