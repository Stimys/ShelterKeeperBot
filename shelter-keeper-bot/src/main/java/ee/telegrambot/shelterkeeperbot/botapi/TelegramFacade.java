package ee.telegrambot.shelterkeeperbot.botapi;

import ee.telegrambot.shelterkeeperbot.cache.UserDataCache;
import ee.telegrambot.shelterkeeperbot.service.MessageService;
import ee.telegrambot.shelterkeeperbot.service.ShelterService;
import ee.telegrambot.shelterkeeperbot.service.UserEventService;
import ee.telegrambot.shelterkeeperbot.type.EventType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class TelegramFacade {
    private final UserDataCache userDataCache;
    private final MessageService messageService;
    private final ShelterService shelterService;
    private final UserEventService userEventService;

    public BotApiMethod<?> handleUpdate (Update update) {
        SendMessage replayMsg = null;

        if(update.hasCallbackQuery()){
            log.info("New callbackQuery from user: {} with data: {}", update.getCallbackQuery().getFrom().getId(), update.getCallbackQuery().getData());
            return processCallBackQuery(update.getCallbackQuery());
        }

        Message inputMsg = update.getMessage();
        if (inputMsg != null) {
            log.info("New message from User: {} ,userId: {} , chatId: {} ,  with text: {}", inputMsg.getFrom().getUserName(), inputMsg.getFrom().getId(), inputMsg.getChatId(), inputMsg.getText());
            replayMsg = handleInputMessage(inputMsg);
        } else {
            log.info("Input UPDATE is NULL");
        }
        return replayMsg;
    }

    private SendMessage handleInputMessage(Message inputMsg) {
        String msgText = inputMsg.getText();
        Long userId = inputMsg.getFrom().getId();
        Long chatId = inputMsg.getChatId();
        BotState botState;
        SendMessage replay = null;

        //if(msgText.equals(""))  return messageService.getReplyMessage(userId, "reply.error");


            switch (msgText){
                case "/start":
                    replay = shelterService.addNewShelter(chatId);
                    break;
                case "/help":
                    replay = messageService.getReplyMessage(chatId, "reply.help");
                    break;
                case "/join":
                    replay = shelterService.addNewUserToShelter(chatId, userId, inputMsg.getFrom().getFirstName());
                    replay.setReplyToMessageId(inputMsg.getMessageId());
                    break;
                case "/leave":
                    replay = shelterService.deleteUserFromShelter(chatId, userId);
                    replay.setReplyToMessageId(inputMsg.getMessageId());
                    break;
                case "/residents":
                    replay = shelterService.getAllUsersInMsg(chatId);
                    break;
                case "/cancel":
                    userDataCache.removeUserState(userId);
                    replay = messageService.getReplyMessage(chatId, "reply.user.event.wasteland.question.cancel");
                    break;
                case "/wasteland":
                    //replay = userEventService.addNewUserEvent(chatId, userId, EventType.WASTELAND);
                    if(userDataCache.getUserBotState(userId) == null){      //Check if user already in cache
                        replay = messageService.getReplyMessage(chatId, "reply.user.event.wasteland.question");
                        userDataCache.setUserCurState(userId, BotState.CALLBACK);
                        replay.setReplyMarkup(getInlineMsgButtons());
                    }
                    else {
                      replay = messageService.getReplyMessage(chatId, "reply.user.event.wasteland.question.busy");
                    }
                    replay.setReplyToMessageId(inputMsg.getMessageId());
                    break;
                default:
                    //Do nothing, if msg isn't command
            }

        return replay;
    }

    private BotApiMethod<?> processCallBackQuery (CallbackQuery callbackQuery){
        long chatId = callbackQuery.getMessage().getChatId();
        long userId = callbackQuery.getFrom().getId();
        SendMessage callBackReplay = null;

        //TODO: bot should check if shelter created, user is in shelter before send reply with callback (maybe move all this code to userEventService class)
        if(userDataCache.getUserBotState(userId) == BotState.CALLBACK){
            if(callbackQuery.getData().equals("wastelandYes")) {
                userDataCache.removeUserState(userId);
                callBackReplay = userEventService.addNewUserEvent(chatId, userId, EventType.WASTELAND);
            } else if(callbackQuery.getData().equals("wastelandNo")){
                userDataCache.removeUserState(userId);
                callBackReplay = messageService.getReplyMessage(chatId, "reply.user.event.wasteland.question.no");
            }
        } else {
            callBackReplay = messageService.getReplyMessage(chatId, "reply.user.event.wasteland.question.error");
            callBackReplay.setReplyToMessageId(callbackQuery.getMessage().getMessageId());
        }


        return callBackReplay;
    }

    private InlineKeyboardMarkup getInlineMsgButtons(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton btnYes = new InlineKeyboardButton();
        btnYes.setText("Да");
        btnYes.setCallbackData("wastelandYes");

        InlineKeyboardButton btnNo = new InlineKeyboardButton();
        btnNo.setText("Нет");
        btnNo.setCallbackData("wastelandNo");

        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
        buttonsRow.add(btnYes);
        buttonsRow.add(btnNo);

        List<List<InlineKeyboardButton>> allRows = new ArrayList<>();
        allRows.add(buttonsRow);

        inlineKeyboardMarkup.setKeyboard(allRows);

        return inlineKeyboardMarkup;
    }
}
