package ee.telegrambot.shelterkeeperbot.service;

import ee.telegrambot.shelterkeeperbot.entity.Shelter;
import ee.telegrambot.shelterkeeperbot.entity.User;
import ee.telegrambot.shelterkeeperbot.repository.ShelterRepository;
import ee.telegrambot.shelterkeeperbot.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private ShelterRepository shelterRepository;
    private UserMsgService msgService;

    public SendMessage addNewUser(Shelter shelter, Long userId, String userName) {
            if(getUser(shelter.getShelterId(), userId) == null){
                if(userName.equals("")) userName = "Житель" + userId;
                User newUser = userRepository.saveUser(shelter.getShelterId(), userId, userName);
                log.info("New user added with id: {}, to shelter: {}, name: {}", newUser.getPlayerId(), shelter.getShelterId(), newUser.getName());
                return msgService.addNewUser(shelter.getShelterId(), newUser.getName());
            } else{
                return msgService.userExists(shelter.getShelterId(), userName);
            }
    }

    public User getUser(Long chatId, Long userId){
       return userRepository.getUser(chatId, userId);
    }

    public SendMessage deleteUser (Long chatId, Long userId){
        User user = getUser(chatId, userId);
        if(user != null){
            userRepository.deleteUser(chatId, user.getPlayerId());
            log.info("User has been deleted - id: {}, from shelter: {}", userId, chatId);
            return msgService.deleteUser(chatId, user.getName());
        }
        return msgService.userNotExists(chatId);
    }

    public Map<Long, User> getAllUsers (Long chatId){
        return userRepository.getUsersByShelterId(chatId);
    }
}
