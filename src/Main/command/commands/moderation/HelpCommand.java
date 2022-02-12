package Main.command.commands.moderation;

import java.awt.Color;
import java.util.*;

import Main.BotRun;
import Main.CommandManager;
import Main.command.CommandContext;
import Main.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand implements ICommand {

	private final CommandManager manager;


	public HelpCommand(CommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void handle(CommandContext ctx) {
		List<String> args = ctx.getArgs();
		TextChannel channel = ctx.getChannel();
		if (args.isEmpty()) {
			
			StringBuilder Fun = new StringBuilder();
			StringBuilder Moderation = new StringBuilder();
			StringBuilder Music = new StringBuilder();
			StringBuilder Games = new StringBuilder();

			List<ICommand> allcommands = manager.getCommands();
			
			for (ICommand cmd : allcommands) 
			{ 
				String it = cmd.getName();
			    if (cmd.getCategory().equalsIgnoreCase("Fun")) {
			    	Fun.append('`').append(it).append("` ");
				}
			    if (cmd.getCategory().equalsIgnoreCase("Moderation")) {
					Moderation.append('`').append(it).append("` ");
				}
				if (cmd.getCategory().equalsIgnoreCase("Music")) {
					Music.append('`').append(it).append("` ");
				}
				if (cmd.getCategory().equalsIgnoreCase("Games")) {
					Games.append('`').append(it).append("` ");
				}
			}
			
			EmbedBuilder info = EmbedUtils.getDefaultEmbed().
					setColor(Color.GREEN).
					setAuthor("Help command", null,	ctx.getSelfUser().getAvatarUrl())
					.addField("Fun", Fun.toString(), false)
					.addField("Moderation", Moderation.toString(), false)
					.addField("Music", Music.toString(), false)
					.addField("Games", Games.toString(), false);

			channel.sendMessageEmbeds(info.build()).queue();
			return;
		}

		String search = args.get(0);
		ICommand command = manager.getCommand(search);

		if (command == null) {
			channel.sendMessage("Nothing found for " + search).queue();
			return;
		}

		channel.sendMessage(command.getHelp()).queue();
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getHelp() {
		return "Shows the list with commands in the bot\n" + "Usage: `" + BotRun.prefix + "help [command]`";
	}

	@Override
	public List<String> getAliases() {
		return List.of("commands", "cmds", "commandlist");
	}

	@Override
	public String getCategory() {
		return "Ignore";
	}
}