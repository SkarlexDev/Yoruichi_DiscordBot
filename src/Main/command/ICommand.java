package Main.command;

import java.util.List;

import net.dv8tion.jda.api.entities.TextChannel;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    String getHelp();
    
    String getCategory();
    
    void setState(Boolean state) throws Exception;
    
    void showHelp(CommandContext ctx, TextChannel channel);
    
    Boolean getState();
    
    default List<String> getAliases() {
        return List.of();
    }

	
    
    
}