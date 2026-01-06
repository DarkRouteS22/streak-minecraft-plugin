package dark.streak;

import dark.streak.databaseEntities.PlayerData;
import org.bukkit.Bukkit;

public class Placeholder {
    public static String formatString(String str, PlayerData playerData) {
        return str
                .replace("{player}", Bukkit.getPlayer(playerData.playerUUID).getName())
                .replace("{streak}", String.valueOf(playerData.currentStreak));
    }
}
