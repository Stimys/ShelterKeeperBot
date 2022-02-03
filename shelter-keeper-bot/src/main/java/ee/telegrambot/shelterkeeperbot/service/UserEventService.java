package ee.telegrambot.shelterkeeperbot.service;

import ee.telegrambot.shelterkeeperbot.ShelterKeeperBot;
import ee.telegrambot.shelterkeeperbot.botapi.EventHandler;
import ee.telegrambot.shelterkeeperbot.entity.UserEvent;
import ee.telegrambot.shelterkeeperbot.repository.UserEventRepository;
import ee.telegrambot.shelterkeeperbot.type.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@Slf4j
public class UserEventService {
    private final UserEventRepository userEventRepository;
    private final UserEventMsgService userEventMsgService;
    private final UserService userSrv;
    private final UserMsgService userMsgSrv;
    private final ShelterService shelterSrv;
    private final ShelterMsgService shelterMsgSrv;
    private final EventHandler usrEventHandler;
    private final ShelterKeeperBot bot;

    public UserEventService (UserEventRepository userEventRepository, UserEventMsgService userEventMsgService, UserService userSrv, UserMsgService userMsgSrv, ShelterService shelterSrv, ShelterMsgService shelterMsgSrv, EventHandler usrEventHandler, @Lazy ShelterKeeperBot bot){
        this.userEventRepository = userEventRepository;
        this.userEventMsgService = userEventMsgService;
        this.userSrv = userSrv;
        this.userMsgSrv = userMsgSrv;
        this.shelterSrv = shelterSrv;
        this.shelterMsgSrv = shelterMsgSrv;
        this.usrEventHandler = usrEventHandler;
        this.bot = bot;
    }

    public SendMessage addNewUserEvent(Long chatId, Long userId, EventType eventType){
        if(shelterSrv.getShelter(chatId) != null){          //Check if shelter exists
            if(userSrv.getUser(chatId, userId) != null){    //Check if user has joined to the shelter
                if(userEventRepository.getUserEventByUserId(userId) == null){       //Check if user doesn't have event
                    UserEvent newUserEvent = new UserEvent(chatId, userId, eventType, getEventFinishTime(1));              //Creating a new events
                    if(userEventRepository.addUserEvent(newUserEvent) != 0){        //Adding event to the DB
                        if(!usrEventHandler.isAlive()) usrEventHandler.start();     //Start thread if its dead
                        return msgEventStarted(newUserEvent);      //Returning message new event created
                    }
                    return userEventMsgService.eventCreationError(chatId);
                } else {
                    return userEventMsgService.userIsBusy(chatId);
                }
            } else{
                return userMsgSrv.userNotExists(chatId);
            }
        }
        return shelterMsgSrv.shelterNotExists(chatId);
    }

    private SendMessage msgEventFinished (UserEvent event){
        String userName = userSrv.getUser(event.getChatId(), event.getUserId()).getName();

        switch (event.getType()){
            case WASTELAND:
                return addNewUserEvent(event.getChatId(), event.getUserId(), EventType.INFIRMARY);
            case INFIRMARY:
                return userEventMsgService.usrEventInfirmaryEnd(event.getChatId(), userName);
        }

        return userEventMsgService.eventFinishError(event.getChatId(), userName);
    }

    private SendMessage msgEventStarted(UserEvent event){
        String userName = userSrv.getUser(event.getChatId(), event.getUserId()).getName();

        switch (event.getType()){
            case WASTELAND:
                return userEventMsgService.usrEventWastelandStart(event.getChatId(), userName, event.getFinishTime());
            case INFIRMARY:
                return userEventMsgService.usrEventInfirmaryStart(event.getChatId(), userName, event.getFinishTime());
        }

        return userEventMsgService.eventCreationError(event.getChatId());
    }

    public void usrEventFinished (UserEvent event){
        bot.sendMsg(msgEventFinished(event));
    }

    private Long getEventFinishTime (int weight) {
        if (weight == 0) weight++;

        int min = 10 * weight, max = 20 * weight;
        int range = max - min + 1;

        long curUnixTime = System.currentTimeMillis() / 1000;
        long eventDurationSec = (long) (Math.random() * range) + min;

        return curUnixTime + eventDurationSec;
    }
}
