package ee.telegrambot.shelterkeeperbot.repository;

import ee.telegrambot.shelterkeeperbot.entity.Shelter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Slf4j
@AllArgsConstructor
public class ShelterRepository {
    private Map<Long, Shelter> shelters;

    public Shelter addShelter(Long shelterId){
        Shelter newShelter = new Shelter(shelterId);
        shelters.put(newShelter.getShelterId(), newShelter);
        log.info("New shelter created: {}",newShelter.getShelterId());
        return newShelter;
    }

    public void deleteShelter(Long shelterId){
        shelters.remove(shelterId);
        if(getShelter(shelterId) == null) log.info("Shelter deleted: {}", shelterId);
    }

    public Shelter getShelter (Long chatId){
        return shelters.get(chatId);
    }
}
