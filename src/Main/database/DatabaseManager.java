package Main.database;

public interface DatabaseManager {
    DatabaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildId);
    void setPrefix(long guildId, String newPrefix);
    Boolean getState(String commandId);
    void setState(String command,Boolean state);
    void close();
}
