package ee.telegrambot.shelterkeeperbot.cache;

import ee.telegrambot.shelterkeeperbot.botapi.BotState;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache {
    //TODO: Separate user data cache for each chatId (shelter)
    private Map<Long, BotState> usersBotStates = new HashMap<>();

    public BotState getUserBotState (Long userId){
        return usersBotStates.get(userId);
    }

    public void setUserCurState (Long userId, BotState state){
        usersBotStates.put(userId, state);
    }

    public void removeUserState (Long userId){
        usersBotStates.remove(userId);
    }
}
