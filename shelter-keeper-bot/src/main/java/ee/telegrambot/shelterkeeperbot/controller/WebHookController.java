package ee.telegrambot.shelterkeeperbot.controller;

import ee.telegrambot.shelterkeeperbot.ShelterKeeperBot;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {
    private final ShelterKeeperBot bot;

    public WebHookController (ShelterKeeperBot bot){
        this.bot = bot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived (@RequestBody Update update){
        return bot.onWebhookUpdateReceived(update);
    }
}
