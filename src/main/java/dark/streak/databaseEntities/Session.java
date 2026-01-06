package dark.streak.databaseEntities;

import java.util.UUID;

public class Session {
    public UUID uuid;
    public int wantToPlay;
    public int timePlayed;

    public Session(UUID uuid, int wantToPlay, int timePlayed) {
        this.uuid = uuid;
        this.wantToPlay = wantToPlay;
        this.timePlayed = timePlayed;
    }
}
