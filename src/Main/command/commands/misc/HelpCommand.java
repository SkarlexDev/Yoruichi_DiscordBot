package Main.command.commands.misc;

import java.awt.Color;
import java.util.List;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.command.commands.CommandManager;
import Main.util.YEnvi;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand extends AbstractCommand implements ICommand {
	private final CommandManager manager;

	public HelpCommand(CommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void handle(CommandContext ctx) {
		List<String> args = ctx.getArgs();
		TextChannel channel = ctx.getChannel();

		if (args.isEmpty()) {

			StringBuilder Misc = new StringBuilder();
			StringBuilder Fun = new StringBuilder();
			StringBuilder Moderation = new StringBuilder();
			StringBuilder Music = new StringBuilder();
			StringBuilder Games = new StringBuilder();
			StringBuilder Admin = new StringBuilder();
			StringBuilder Testing = new StringBuilder();

			List<ICommand> allcommands = manager.getCommands();

			for (ICommand cmd : allcommands) {
				String it = cmd.getName();
				if (cmd.getCategory().equalsIgnoreCase("Misc")) {
					Misc.append('`').append(it).append("` ");
				}
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
				if (cmd.getCategory().equalsIgnoreCase("Admin")) {
					Admin.append('`').append(it).append("` ");
				}
				if (cmd.getCategory().equalsIgnoreCase("Testing")) {
					Testing.append('`').append(it).append("` ");
				}
			}

			EmbedBuilder info = EmbedUtils.getDefaultEmbed().setColor(Color.GREEN)
					.setAuthor("Help command", null, ctx.getSelfUser().getAvatarUrl())
					.addField("Misc", Misc.toString(), false).addField("Fun", Fun.toString(), false)
					.addField("Moderation", Moderation.toString(), false).addField("Music", Music.toString(), false)
					.addField("Games", Games.toString(), false).addField("Admin", Admin.toString(), false);

			if (Testing.length() > 0) {
				info.addField("Testing", Testing.toString(), false);
			}

			channel.sendMessageEmbeds(info.build()).queue(message -> {
				ctx.clearLong(message);
			});
			return;
		}

		String search = args.get(0);
		ICommand command = manager.getCommand(search);

		if (command == null) {
			channel.sendMessage("Nothing found for " + search).queue(message -> {
				ctx.clear(message);
			});
			return;
		}
		command.showHelp(ctx, channel);
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getCategory() {
		return "Misc";
	}

	@Override
	public String getHelp() {
		return "Shows the list with commands in the bot\n" + "Usage: `" + YEnvi.prefix + "" + this.getName()
				+ " [command]`";
	}

	@Override
	public List<String> getAliases() {
		return List.of("commands", "cmds", "commandlist");
	}
}
