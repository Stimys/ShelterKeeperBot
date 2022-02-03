package ee.telegrambot.shelterkeeperbot;

import ee.telegrambot.shelterkeeperbot.botapi.TelegramFacade;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class ShelterKeeperBot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private final TelegramFacade telegramFacade;

    public ShelterKeeperBot (TelegramFacade telegramFacade){
        this.telegramFacade = telegramFacade;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public void sendMsg(SendMessage message){
        try{
            execute(message);
        } catch (TelegramApiException e){
            log.error("Send message exception\n" + e);
        }
    }

    public void setWebHookPath(String webHookPath){
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName){
        this.botUserName = botUserName;
    }

    public void setBotToken (String botToken){
        this.botToken = botToken;
    }
}
