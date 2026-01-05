package dark.streak;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseManager {
    private JavaPlugin plugin;
    private String jdbcUrl;
    private String user;
    private String password;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    public void init() {
        String host = plugin.getConfig().getString("database.host");
        int port = plugin.getConfig().getInt("database.port");
        String name = plugin.getConfig().getString("database.name");
        boolean ssl = plugin.getConfig().getBoolean("database.SSL");

        this.user = plugin.getConfig().getString("database.user");
        this.password = plugin.getConfig().getString("database.password");

        this.jdbcUrl =
            "jdbc:mysql://" + host + ":" + port + "/" + name +
            "?useSSL=" + ssl +
            "&serverTimezone=UTC" +
            "&characterEncoding=utf8";

        String playerStreaks = """
            CREATE TABLE IF NOT EXISTS player_streaks (
                uuid CHAR(36) PRIMARY KEY,
                current_streak INT NOT NULL,
                best_streak INT NOT NULL,
                freezes_used INT NOT NULL,
                last_completed_day DATE NOT NULL
            );
            """;

        String streakDays = """
            CREATE TABLE IF NOT EXISTS streak_days (
                uuid CHAR(36) NOT NULL,
                date DATE NOT NULL,
                status ENUM('COMPLETED','FROZEN','MISSED','RESTORED') NOT NULL,
                PRIMARY KEY (uuid, date)
            );
            """;

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            st.execute(playerStreaks);
            st.execute(streakDays);

        } catch (SQLException e) {
            plugin.getLogger().severe("Error creating MySQL tables");
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }
}
