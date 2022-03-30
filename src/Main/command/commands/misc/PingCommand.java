package Main.command.commands.misc;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.util.YEnvi;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

public class PingCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}

		JDA jda = ctx.getJDA();
		jda.getRestPing().queue((ping) -> ctx.getChannel()
				.sendMessageFormat("Reset ping: %sms\nWS ping: %sms", ping, jda.getGatewayPing()).queue(message -> {
					ctx.clearLong(message);
				}));
	}

	@Override
	public String getName() {
		return "ping";
	}

	@Override
	public String getCategory() {
		return "Misc";
	}

	@Override
	public String getHelp() {
		return "Shows the current ping from the bot to the discord servers\n" + "Usage `" + YEnvi.prefix + ""
				+ this.getName() + "`";
	}

}
