package Main.command.commands.test;

import java.awt.Color;
import java.util.List;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.util.YEnvi;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class SayCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final int size = ctx.getArgs().size();
		final Message msg = ctx.getMessage();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}
		// temp
		if (!ctx.getMember().getId().equals(YEnvi.owner)) {
			channel.sendMessage("This command is limited to owner").queue(message -> {
				ctx.clear(message);
			});
			return;
		}
		
		if (msg.getMentionedChannels().isEmpty()) {
			this.showHelp(ctx, channel);
			return;
		}

		if (size < 2) {
			this.showHelp(ctx, channel);
			return;
		}

		StringBuilder msgb = new StringBuilder();
		for (int i = 1; i < size; i++) {
			msgb.append(" ").append(ctx.getArgs().get(i));
		}
		String msgf = msgb.toString();

		final TextChannel target = msg.getMentionedChannels().get(0);
		target.sendMessage(msgf).queue();
	
	}

	private void msgd(MessageEmbed build) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return "say";
	}

	@Override
	public String getCategory() {
		return "Testing";
	}

	@Override
	public String getHelp() {
		return "Say your text\n" + "Usage: `" + YEnvi.prefix + "" + this.getName() + " [#Mention channel][#text]`";
	}

	@Override
	public List<String> getAliases() {
		return List.of("s");
	}

}
