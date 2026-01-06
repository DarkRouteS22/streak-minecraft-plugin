package dark.streak;

import dark.streak.databaseEntities.PlayerData;
import dark.streak.databaseEntities.Session;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class StreakData {
    private static List<String> joinMsgs;
    private static Random rand;
    public static LocalTime updateTime;
    public static List<Session> sessions;
    private static List<Integer> dailyPlaytime;

    public static void init(JavaPlugin plugin) {
        rand = new Random();
        joinMsgs = plugin.getConfig().getStringList("message.streak-join");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        updateTime = LocalTime.parse(
                plugin.getConfig().getString("update-time"),
                formatter
        );
        dailyPlaytime = plugin.getConfig().getIntegerList("daily-playtime");
    }

    public static String JoinMessage(PlayerData playerData) {
        return Placeholder.formatString(
                joinMsgs.get(rand.nextInt(joinMsgs.size())),
                playerData
        );
    }

    public static List<Session> getSessions() {return sessions;}
    public int getPlaytime() {return dailyPlaytime.get(rand.nextInt(dailyPlaytime.size()));}
}
