package ee.telegrambot.shelterkeeperbot.repository;

import ee.telegrambot.shelterkeeperbot.entity.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
@Slf4j
public class UserEventRepository {
    private Long idCounter = 0L;
    private final Map <Long, UserEvent> userEvents;

    public UserEventRepository (Map<Long, UserEvent> userEvents){
        this.userEvents = userEvents;
    }

    public Long addUserEvent (UserEvent event){
        idCounter++;
        userEvents.put(idCounter, event);
        log.info("New event created with id: {}, for shelter: {}, user: {}, type: {}, event will finish at: {}", idCounter, event.getChatId(), event.getUserId(), event.getType() ,event.getFinishTime());
        return idCounter;
    }

    public void deleteUserEvent (Long eventId){
        userEvents.remove(eventId);
        log.info("Event completed: {}, events to go: {}", eventId, userEvents.size());
    }

    public Map<Long, UserEvent> getUserEventList (){
        return userEvents;
    }

    public UserEvent getUserEvent (Long eventId){
        return userEvents.get(eventId);
    }

    public UserEvent getUserEventByUserId(Long userId) {
        long lUserId = userId;
        for(Map.Entry<Long, UserEvent> entry : userEvents.entrySet()){
            UserEvent tempEvent = entry.getValue();
            long lUserFromEvent = tempEvent.getUserId();
            if(lUserId == lUserFromEvent) return tempEvent;
        }
        return null;
    }
}
