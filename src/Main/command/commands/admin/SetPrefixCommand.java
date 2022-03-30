package Main.command.commands.admin;

import java.util.List;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.database.DatabaseManager;
import Main.database.MapByGuild;
import Main.util.YEnvi;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetPrefixCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();

		if (!ctx.checkCmdPermission(ctx.getMember(), this.getName())) {
			ctx.getPermissionDenied(channel);
			return;
		}

		if (ctx.getArgs().isEmpty()) {
			this.showHelp(ctx, channel);
			return;
		}

		final String newPrefix = String.join("", args);
		updatePrefix(ctx.getGuild().getIdLong(), newPrefix, this.getName());

		channel.sendMessageFormat("New prefix has been set to `%s`", newPrefix).queue(message -> {
			ctx.clear(message);
		});

	}

	private void updatePrefix(long guildId, String newPrefix, String command) {
		MapByGuild.GUILD.put(guildId, newPrefix);
		DatabaseManager.INSTANCE.setPrefix(guildId, newPrefix);
		YEnvi.prefix = newPrefix;
	}

	@Override
	public String getName() {
		return "setprefix";
	}

	@Override
	public String getCategory() {
		return "Admin";
	}

	@Override
	public String getHelp() {
		return "Changes server command prefix\n" + "Usage: `" + YEnvi.prefix + "" + this.getName() + " <#newPrefix>`";
	}

	@Override
	public List<String> getAliases() {
		return List.of("set");
	}

}
