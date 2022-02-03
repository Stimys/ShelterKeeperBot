package ee.telegrambot.shelterkeeperbot.service;

import ee.telegrambot.shelterkeeperbot.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Service
@AllArgsConstructor
public class ShelterMsgService {
    private MessageService msgService;

    public SendMessage addNewShelterMsg (Long chatId){return msgService.getReplyMessage(chatId, "reply.start.newShelter");}

    public SendMessage existShelterMsg(Long chatId){return msgService.getReplyMessage(chatId, "reply.start");}

    public SendMessage shelterNotExists(Long chatId){ return msgService.getReplyMessage(chatId, "reply.shelter.notExists");}

    public SendMessage isEmpty(Long chatId){ return msgService.getReplyMessage(chatId, "reply.shelter.isEmpty");}

    public SendMessage printAllUsers(Long chatId, Map<Long, User> usersList){
        SendMessage replyMsg = new SendMessage();
        replyMsg.setChatId(Long.toString(chatId));

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Список жителей:\n");
        for(Map.Entry<Long, User> entry : usersList.entrySet()) {
            stringBuilder.append(entry.getValue().getName()).append("\n");
        }

        replyMsg.setText(stringBuilder.toString());
        return replyMsg;
    }
}
