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
public class ShelterService {
    private ShelterRepository shelterRepository;
    private UserRepository userRepository;
    private ShelterMsgService msgService;
    private UserService userService;

    public SendMessage addNewShelter (Long chatId){
        if(getShelter(chatId) == null){
            Shelter newShelter = shelterRepository.addShelter(chatId);
            return msgService.addNewShelterMsg(newShelter.getShelterId());
        }else{
            return msgService.existShelterMsg(chatId);
        }
    }

    public SendMessage addNewUserToShelter(Long chatId, Long userId, String userName){
        if(getShelter(chatId) != null){
            return userService.addNewUser(getShelter(chatId), userId, userName);
        }
        return msgService.shelterNotExists(chatId);
    }

    public Shelter getShelter (Long chatId){
        return shelterRepository.getShelter(chatId);
    }

    public SendMessage getAllUsersInMsg (Long chatId){
        Shelter shelter = getShelter(chatId);

        if(shelter != null){
            Map<Long, User> users = userRepository.getUsersByShelterId(shelter.getShelterId());
            if(users != null){
                if (users.size() > 0) return msgService.printAllUsers(shelter.getShelterId(), users);
            }
            return msgService.isEmpty(shelter.getShelterId());
        }

        return msgService.shelterNotExists(chatId);
    }

    public SendMessage deleteUserFromShelter (Long chatId, Long userId){
        Shelter shelter = getShelter(chatId);
        if(shelter != null) return userService.deleteUser(shelter.getShelterId(), userId);

        return msgService.shelterNotExists(chatId);
    }
}
