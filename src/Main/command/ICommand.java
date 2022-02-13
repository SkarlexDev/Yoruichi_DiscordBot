package Main.command;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    String getHelp();
    
    String getCategory();
    
    void setState(Boolean state) throws Exception;    
    Boolean getState();
    
    default List<String> getAliases() {
        return List.of();
    }

	
    
    
}