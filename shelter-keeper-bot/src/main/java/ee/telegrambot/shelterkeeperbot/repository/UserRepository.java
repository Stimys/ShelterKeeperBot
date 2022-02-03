package ee.telegrambot.shelterkeeperbot.repository;

import ee.telegrambot.shelterkeeperbot.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
@AllArgsConstructor
public class UserRepository {
    Map<Long, Map <Long, User>> users;
    /**
     * @param userId type Long
     * @param userName type String
     * @return new User object
     */
    public User saveUser (Long chatId, Long userId, String userName){
        User newUser = new User(userId, userName);

        getUsersByShelterId(chatId).put(userId, newUser);
        return newUser;
    }

    /**
     * Delete user from users map
     * @param userId type Long
     */
    public void deleteUser (Long chatId, Long userId){
        if(getUser(chatId, userId) != null) getUsersByShelterId(chatId).remove(userId);
    }

    /**
     * @param userId type Long
     * @return User object if it exists
     */
    public User getUser (Long chatId, Long userId) {
        return getUsersByShelterId(chatId).get(userId);
    }

    /**
     * @param chatId type Long
     * @return Map of users by chat Id
     */
    public Map<Long, User> getUsersByShelterId (Long chatId){
        users.computeIfAbsent(chatId, k -> new HashMap<>());
        return users.get(chatId);
    }
}
