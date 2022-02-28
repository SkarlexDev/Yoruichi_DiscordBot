package Main.command.commands.test;

import java.util.ArrayList;
import java.util.List;

import Main.command.CommandContext;
import Main.command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

public class MezzonicCommand implements ICommand {
	private Boolean state;

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}

		//List<String> values = new ArrayList<String>();

		channel.sendMessage(this.msg().toString()).queue();

	}

	@Override
	public String getName() {
		return "me";
	}

	@Override
	public String getHelp() {
		return "<Testing>";
	}

	@Override
	public String getCategory() {
		return "Testing";
	}

	@Override
	public void setState(Boolean state) {
		this.state = state;

	}

	@Override
	public Boolean getState() {
		return this.state;
	}

	@Override
	public void showHelp(CommandContext ctx, TextChannel channel) {
		ctx.commandHelper(channel, this.getHelp(), this.getName().toUpperCase());
	}

	public StringBuilder msg() {
		StringBuilder result = new StringBuilder();
		int lgh = 10;
		for (int i = 1; i <= lgh; i++) {
			for (int a = 1; a <= lgh; a++) {
				if (i > 1 && i < lgh && a > 1 && a < lgh) {
					result.append(":black_large_square:");
				} else {
					result.append(":red_square:");
				}
			}
			result.append("\n");
		}
		return result;
	}

}
