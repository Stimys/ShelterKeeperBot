package ee.telegrambot.shelterkeeperbot.botapi;

import ee.telegrambot.shelterkeeperbot.entity.UserEvent;
import ee.telegrambot.shelterkeeperbot.repository.UserEventRepository;
import ee.telegrambot.shelterkeeperbot.service.UserEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class EventHandler extends Thread{
    private final UserEventRepository usrEventRepo;
    private final UserEventService usrEventSrv;

    public EventHandler (UserEventRepository usrEventRepo, @Lazy  UserEventService usrEventSrv){
        this.usrEventRepo = usrEventRepo;
        this.usrEventSrv = usrEventSrv;
    }

    @Override
    public void run(){
        log.info("EventHandler thread - started");
        do{
            while(usrEventRepo.getUserEventList().size() < 1){      //If no events, thread waits
                Thread.yield();
            }
            Long curUnixTime = System.currentTimeMillis() / 1000;   //Get current time and convert it to UNIX time
            Map<Long, UserEvent> userEvents = new HashMap<>(usrEventRepo.getUserEventList());   //Copy all events to Map

            for(Map.Entry<Long, UserEvent> entry : userEvents.entrySet()){
                if(entry.getValue().getFinishTime() < curUnixTime) {
                    usrEventRepo.deleteUserEvent(entry.getKey());       //Delete user event from DB, if current time is over than event finish time
                    usrEventSrv.usrEventFinished(entry.getValue());     //Send user event which is finished
                }
            }
        } while(true);
    }
}
