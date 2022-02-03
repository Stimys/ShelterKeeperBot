package ee.telegrambot.shelterkeeperbot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@AllArgsConstructor
public class UserMsgService {
    private MessageService msgService;

    public SendMessage addNewUser (Long chatId, String userName){
        return msgService.getReplyMessage(chatId,"reply.user.newUser", userName);
    }

    public SendMessage deleteUser (Long chatId, String userName){
        return msgService.getReplyMessage(chatId, "reply.user.deleteUser", userName);
    }

    public SendMessage userExists (Long chatId, String userName){
        return msgService.getReplyMessage(chatId , "reply.user.userExists", userName);
    }

    public SendMessage userNotExists (Long chatId){
        return msgService.getReplyMessage(chatId, "reply.user.notExists");
    }

    public SendMessage shelterNotFound(Long chatId){
        return msgService.getReplyMessage(chatId, "reply.shelter.notExists");
    }
}
