package Main.command;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    String getHelp();
    
    String getCategory();

    default List<String> getAliases() {
        return List.of();
    }
}