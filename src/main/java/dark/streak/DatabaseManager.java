package dark.streak;

import dark.streak.databaseEntities.PlayerData;
import dark.streak.databaseEntities.Session;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;


public class DatabaseManager {
    private final JavaPlugin plugin;
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
        boolean ssl = plugin.getConfig().getBoolean("database.use-ssl");

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
                freezes INT NOT NULL,
                last_completed_day DATE NOT NULL,
                streak_status INT NOT NULL,
                all_playtime INT NOT NULL
            );
            """;

        String session = """
            CREATE TABLE IF BOT EXISTS session (
                uuid CHAR(36) PRIMARY KEY,
                playtime INT NOT NULL,
                want_to_play INT NOT NULL,
                PRIMARY KEY (uuid)
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
            st.execute(session);

        } catch (SQLException e) {
            plugin.getLogger().severe("Error creating MySQL tables or connecting to DataBase");
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    public Session createSession(UUID uuid) {
        return null;
    }

    public Session getSession(UUID uuid) throws SQLException {
        String selectSQL = "SELECT * FROM session WHERE uuid = ?";
        try (Connection conn = getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
            selectStmt.setString(1, uuid.toString());

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return new Session(
                            uuid,
                            rs.getInt("want_to_play"),
                            rs.getInt("playtime")
                    );
                } else { return null; }
            }
        }
    }

    public PlayerData getPlayerData(UUID uuid) throws SQLException {
        String selectSQL = "SELECT * FROM player_streaks WHERE uuid = ?";
        String insertSQL = "INSERT INTO player_streaks " +
                "(uuid, current_streak, best_streak, freezes, last_completed_day, streak_status, all_playtime) " +
                "VALUES (?, 0, 0, 0, CURDATE(), 0, 0)";

        try (Connection conn = getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
             selectStmt.setString(1, uuid.toString());

             try (ResultSet rs = selectStmt.executeQuery()) {
                 if (rs.next()) {
                     return new PlayerData(
                             uuid,
                             rs.getDate("last_completed_day").toLocalDate(),
                             rs.getInt("current_streak"),
                             rs.getInt("best_streak"),
                             rs.getInt("freezes"),
                             rs.getInt("all_playtime"),
                             rs.getInt("streak_status")
                     );
                 } else {
                     try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                         insertStmt.setString(1, uuid.toString());
                         insertStmt.executeUpdate();
                     }
                     return new PlayerData(
                             uuid,
                             LocalDate.now(),
                             0,
                             0,
                             0,
                             0,
                             0
                     );
                 }
             }
        }
    }
}
