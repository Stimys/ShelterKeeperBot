package ee.telegrambot.shelterkeeperbot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Long playerId;
    private String name;
}
