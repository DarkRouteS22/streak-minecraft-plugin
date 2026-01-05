package dark.streak;

import org.bukkit.plugin.java.JavaPlugin;

public final class Streak extends JavaPlugin {
    public DatabaseManager databaseManager;


    @Override
    public void onEnable() {
        this.saveResource("config.yml", false);

        databaseManager = new DatabaseManager(this);
        databaseManager.init();

    }

    @Override
    public void onDisable() {}
}
