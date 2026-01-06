package dark.streak.databaseEntities;

import java.time.LocalDate;
import java.util.UUID;

public class PlayerData {
    public UUID playerUUID;
    public LocalDate lastDayCompleted;

    public int currentStreak;
    public int bestStreak;
    public int freezes;
    public int allPlaytime;
    public int status;

    public PlayerData(UUID playerUUID, LocalDate lastDayCompleted, int currentStreak, int bestStreak, int freezes, int allPlaytime, int status) {
        this.playerUUID = playerUUID;
        this.lastDayCompleted = lastDayCompleted;
        this.currentStreak = currentStreak;
        this.bestStreak = bestStreak;
        this.freezes = freezes;
        this.allPlaytime = allPlaytime;
        this.status = status;
    }
}
