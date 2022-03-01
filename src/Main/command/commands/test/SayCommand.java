package Main.command.commands.test;

import java.util.List;

import Main.Yoruichi;
import Main.command.CommandContext;
import Main.command.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SayCommand implements ICommand {

	private Boolean state;

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final int size = ctx.getArgs().size();
		final Message message = ctx.getMessage();
		final List<String> args = ctx.getArgs();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}
		//temp
		if (!ctx.getMember().getId().equals(Yoruichi.owner)) {
			channel.sendMessage("This command is limited to owner").queue();
			return;
		}

		if (message.getMentionedChannels().isEmpty()) {
			this.showHelp(ctx, channel);
			return;
		}
		
		if(size<2) {
			this.showHelp(ctx, channel);
			return;
		}

		StringBuilder msgb = new StringBuilder();
		for (int i = 1; i < size; i++) {
			msgb.append(" ").append(ctx.getArgs().get(i));
		}
		String msg = msgb.toString();

		final TextChannel target = message.getMentionedChannels().get(0);
		target.sendMessage(msg).queue();

	}

	@Override
	public String getName() {
		return "say";
	}

	@Override
	public String getHelp() {
		return "Say your text\n" + "Usage: `" + Yoruichi.prefix + "" + this.getName() + " [#Mention channel][#text]`";
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
	public List<String> getAliases() {
		return List.of("s");
	}

	@Override
	public void showHelp(CommandContext ctx, TextChannel channel) {
		ctx.commandHelper(channel, this.getHelp(), this.getName().toUpperCase());
	}

}
