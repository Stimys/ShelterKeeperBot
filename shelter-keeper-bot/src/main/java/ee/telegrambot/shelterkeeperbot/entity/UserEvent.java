package ee.telegrambot.shelterkeeperbot.entity;

import ee.telegrambot.shelterkeeperbot.type.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEvent {
    private Long chatId;
    private Long userId;
    private EventType type;
    private Long finishTime;
}
