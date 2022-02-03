package ee.telegrambot.shelterkeeperbot.config;

import ee.telegrambot.shelterkeeperbot.ShelterKeeperBot;
import ee.telegrambot.shelterkeeperbot.botapi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public ShelterKeeperBot shelterKeeperBot (TelegramFacade telegramFacade){

        ShelterKeeperBot shelterKeeperBot = new ShelterKeeperBot(telegramFacade);
        shelterKeeperBot.setBotUserName(botUserName);
        shelterKeeperBot.setBotToken(botToken);
        shelterKeeperBot.setWebHookPath(webHookPath);

        return shelterKeeperBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
