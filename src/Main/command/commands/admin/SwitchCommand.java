package Main.command.commands.admin;

import java.awt.Color;
import java.util.List;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.command.commands.CommandManager;
import Main.database.DatabaseManager;
import Main.util.MessageUtil;
import Main.util.YEnvi;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class SwitchCommand extends AbstractCommand implements ICommand {
	private final CommandManager manager;

	public SwitchCommand(CommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void handle(CommandContext ctx) {

		final TextChannel channel = ctx.getChannel();
		int size = ctx.getArgs().size();
		List<String> args = ctx.getArgs();

		if (!ctx.checkCmdPermission(ctx.getMember(), this.getName())) {
			ctx.getPermissionDenied(channel);
			return;
		}

		if (size == 0) {

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
				Boolean enabled = cmd.getState();
				String status = enabled == true ? "Enabled" : "Disabled";

				if (cmd.getCategory().equalsIgnoreCase("Misc")) {
					Misc.append('`').append(it).append(": " + status).append("` \n");
				}
				if (cmd.getCategory().equalsIgnoreCase("Fun")) {
					Fun.append('`').append(it).append(": " + status).append("` \n");
				}
				if (cmd.getCategory().equalsIgnoreCase("Moderation")) {
					Moderation.append('`').append(it).append(": " + status).append("` \n");
				}
				if (cmd.getCategory().equalsIgnoreCase("Music")) {
					Music.append('`').append(it).append(": " + status).append("` \n");
				}
				if (cmd.getCategory().equalsIgnoreCase("Games")) {
					Games.append('`').append(it).append(": " + status).append("` \n");
				}
				if (cmd.getCategory().equalsIgnoreCase("Admin")) {
					Admin.append('`').append(it).append(": " + status).append("` \n");
				}
				if (cmd.getCategory().equalsIgnoreCase("Testing")) {
					Testing.append('`').append(it).append(": " + status).append("` \n");
				}
			}

			EmbedBuilder info = EmbedUtils.getDefaultEmbed().setColor(Color.GREEN)
					.setAuthor("Command Manager", null, ctx.getSelfUser().getAvatarUrl())
					.addField("Fun", Fun.toString(), true).addField("Moderation", Moderation.toString(), true)
					.addField("Misc", Misc.toString(), true).addField("Music", Music.toString(), true)
					.addField("Admin", Admin.toString(), true).addField("Games", Games.toString(), true);

			if (Testing.length() > 0) {
				info.addField("Testing", Testing.toString(), true);
			} else {
				info.addBlankField(true);
			}

			channel.sendMessageEmbeds(info.build()).queue(message -> {
				ctx.clearLong(message);
			});
			return;
		}

		if (size == 1) {
			String search = args.get(0);
			ICommand command = manager.getCommand(search);

			if (command == null) {
				channel.sendMessage("Nothing found for " + search).queue(message -> {
					ctx.clear(message);
				});
				return;
			}

			command.setState(!command.getState());
			String status = command.getState() == true ? "Enabled" : "Disabled";
			if (command.getName().equals(this.getName()) || command.getName().equals("help")) {
				channel.sendMessage("Sadly you cannot disable this command!").queue(message -> {
					MessageUtil.autoDeleteMessage(message);
				});
			} else {
				DatabaseManager.INSTANCE.setState(command.getName(), command.getState());
				channel.sendMessage("Command `" + command.getName() + "` is now: " + status).queue(message -> {
					ctx.clear(message);
				});
			}

			
		}
	}

	@Override
	public String getName() {
		return "switch";
	}

	@Override
	public String getCategory() {
		return "Admin";
	}

	@Override
	public String getHelp() {
		return "Disables/Enables commands\n" + "Usage: `" + YEnvi.prefix + "" + this.getName()
				+ " [command name/alias]`\n" + "Aliases: `" + this.getAliases() + "`";
	}

	@Override
	public List<String> getAliases() {
		return List.of("sw");
	}

}
