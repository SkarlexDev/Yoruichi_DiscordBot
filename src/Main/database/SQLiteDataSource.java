package Main.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class SQLiteDataSource implements DatabaseManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteDataSource.class);
    private final HikariDataSource ds;

    public SQLiteDataSource() {
        try {
            
            final File dbFile = new File("Database.db");
            
            if (!dbFile.exists()) {
                if (dbFile.createNewFile()) {
                    LOGGER.info("Created database file");
                } else {
                    LOGGER.info("Could not create database file");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:Database.db");
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(3000);
        config.setMaxLifetime(1800000);
        config.setMinimumIdle(0);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);

        try (final Statement statement = getConnection().createStatement()) {
            final String defaultPrefix = System.getenv("PREFIX");

            // language=SQLite
            statement.execute("CREATE TABLE IF NOT EXISTS guild_settings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "prefix VARCHAR(255) NOT NULL DEFAULT '" + defaultPrefix + "'," +
                    "ActivityType VARCHAR(255) NOT NULL DEFAULT '" + "watching" + "'," +
                    "Activity VARCHAR(255) NOT NULL DEFAULT '" + "Bleach" + "'" +
                    ");");
            statement.execute("CREATE TABLE IF NOT EXISTS commands_settings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "command_id VARCHAR(20) NOT NULL," +
                    "status BOOLEAN NOT NULL DEFAULT '" + 1 + "'" +
                    ");");

            LOGGER.info("Table initialised");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPrefix(long guildId) {
        try (final PreparedStatement preparedStatement = getConnection()
                // language=SQLite
                .prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prefix");
                }
            }

            try (final PreparedStatement insertStatement = getConnection()
                    // language=SQLite
                    .prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")) {

                insertStatement.setString(1, String.valueOf(guildId));

                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return System.getenv("PREFIX");
    }

    @Override
    public void setPrefix(long guildId, String newPrefix) {
        try (final PreparedStatement preparedStatement = getConnection()
                // language=SQLite
                .prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {

            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    // commands state
    
	@Override
	public Boolean getState(String commandId) {
		try (final PreparedStatement preparedStatement = getConnection()
                // language=SQLite
                .prepareStatement("SELECT status FROM commands_settings WHERE command_id = ?")) {

            preparedStatement.setString(1, commandId);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                	return resultSet.getBoolean("status");
                }
            }

            try (final PreparedStatement insertStatement = getConnection()
                    // language=SQLite
                    .prepareStatement("INSERT INTO commands_settings(command_id) VALUES(?)")) {

                insertStatement.setString(1, commandId);

                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
	

	@Override
	public void setState(String command, Boolean state) {
		try (final PreparedStatement preparedStatement = getConnection()
                // language=SQLite
                .prepareStatement("UPDATE commands_settings SET status = ? WHERE command_id = ?")) {

            preparedStatement.setBoolean(1, state);
            preparedStatement.setString(2, String.valueOf(command));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	@Override
	public void close() {
		this.ds.close();
	}
	
}
