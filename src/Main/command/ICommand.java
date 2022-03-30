package Main.command;

import java.util.List;

import Main.command.commands.CommandContext;
import net.dv8tion.jda.api.entities.TextChannel;

public interface ICommand {

	void handle(CommandContext ctx);

	String getName();

	String getCategory();

	String getHelp();

	Boolean getState();

	void setState(Boolean state);

	default List<String> getAliases() {
		return List.of();
	}

	default void showHelp(CommandContext ctx, TextChannel channel) {
		ctx.commandHelper(channel, this.getHelp(), this.getName().toUpperCase());
	};

}