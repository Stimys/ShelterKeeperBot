package ee.telegrambot.shelterkeeperbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@Service
public class MessageService {
    private final LocaleMessageService localeMessageService;

    public MessageService(LocaleMessageService messageService) {
        this.localeMessageService = messageService;
    }

    public SendMessage getReplyMessage(Long chatId, String replyMessage) {
        return new SendMessage(Long.toString(chatId), localeMessageService.getMessage(replyMessage));
    }

    public SendMessage getReplyMessage(Long chatId, String replyMessage, Object... args) {
        return new SendMessage(Long.toString(chatId), localeMessageService.getMessage(replyMessage, args));
    }
}
