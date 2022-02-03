package ee.telegrambot.shelterkeeperbot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class UserEventMsgService {
    private MessageService msgService;

    public SendMessage usrEventWastelandStart(Long chatId, String userName, Long finishTime){ return msgService.getReplyMessage(chatId, "reply.user.event.wasteland.start", userName, getEventDurationTime(finishTime)); }

    public SendMessage userIsBusy(Long chatId) { return msgService.getReplyMessage(chatId,"reply.user.event.userIsBusy");}

    public SendMessage eventCreationError (Long chatId) {return msgService.getReplyMessage(chatId, "reply.user.event.creationError");}

    public SendMessage eventFinishError (Long chatId, String userName) {return msgService.getReplyMessage(chatId, "reply.user.event.finishError", userName);}

    public SendMessage usrEventWastelandCompleted(Long chatId, String userName) {return msgService.getReplyMessage(chatId, "reply.user.event.wastelandCompleted", userName);}

    public SendMessage usrEventInfirmaryStart (Long chatId, String userName, Long finishTime) {return msgService.getReplyMessage(chatId, "reply.user.event.infirmary.start", userName, getEventDurationTime(finishTime));}

    public SendMessage usrEventInfirmaryEnd (Long chatId, String userName) {return msgService.getReplyMessage(chatId, "reply.user.event.infirmary.end", userName);}

    private String getEventDurationTime (Long unixFinishTime){
        String outTime = "";
        long eventDurationTimeSec = unixFinishTime - (System.currentTimeMillis() / 1000);   //Get event duration time in sec
        long hours = 0, minutes = 0, seconds = 0;

        hours = TimeUnit.SECONDS.toHours(eventDurationTimeSec);
        eventDurationTimeSec -= TimeUnit.HOURS.toSeconds(hours);

        minutes = TimeUnit.SECONDS.toMinutes(eventDurationTimeSec);
        eventDurationTimeSec -= TimeUnit.MINUTES.toSeconds(minutes);

        seconds = eventDurationTimeSec;

        if (hours > 0) { outTime+= hours + " ч";}

        if(!outTime.equals("")) outTime+= ", ";

        if (minutes > 0){ outTime+= minutes + " мин";}

        if(!outTime.equals("")) outTime+= ", ";

        if (seconds > 0) { outTime += seconds + " сек";}

        return outTime;
    }
}
