package Main.command.commands.moderation;

import java.awt.Color;
import java.net.URL;
import java.util.List;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.util.MessageUtil;
import Main.util.YEnvi;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ClearCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		int size = ctx.getArgs().size();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}

		if (!ctx.checkCmdPermission(ctx.getMember(), this.getName())) {
			ctx.getPermissionDenied(channel);
			return;
		}
		if (ctx.getArgs().isEmpty()) {
			this.showHelp(ctx, channel);
			return;
		}

		String n1 = ctx.getArgs().get(0);

		if (!n1.equalsIgnoreCase("all")) {
			try {
				Integer.parseInt(n1);
			} catch (Exception e) {
				channel.sendMessage("Invalid command use `" + YEnvi.prefix + " help " + this.getName() + "`")
						.queue((message) -> {
							MessageUtil.autoDeleteMessage(message);
						});
				return;
			}
		} else {
			n1 = "100";
		}

		if (size == 1) {
			delete(n1, channel);
		} else {
			EmbedBuilder usage = EmbedUtils.getDefaultEmbed().setColor(Color.ORANGE)
					.setTitle("Specify amount to delete")
					.setDescription("Usage: `" + YEnvi.prefix + "" + this.getName() + " [# ammount of messages]`");
			channel.sendMessageEmbeds(usage.build()).queue(message -> {
				MessageUtil.autoDeleteMessage(message);
			});
		}
	}

	public void delete(String n1, TextChannel channel) {
		try {
			List<Message> messages;
			if (Integer.parseInt(n1) == 1) {
				messages = channel.getHistory().retrievePast(Integer.parseInt(n1) + 1).complete();
			} else {
				messages = channel.getHistory().retrievePast(Integer.parseInt(n1)).complete();
			}
			// max 100 messages && not older than 2 weeks
			channel.deleteMessages(messages).queue();

			// Success
			EmbedBuilder success = EmbedUtils.getDefaultEmbed().setColor(Color.GREEN)
					.setTitle("Successfully deleted " + n1 + " messags.");
			channel.sendMessageEmbeds(success.build()).queue((message) -> {
				MessageUtil.autoDeleteMessage(message);
			});
		} catch (Exception e) {

			System.out.println("Invalid " + n1);
			delete(String.valueOf(Integer.parseInt(n1) - 5), channel);

		}
	}

	@Override
	public String getName() {
		return "clear";
	}

	@Override
	public String getCategory() {
		return "Moderation";
	}

	@Override
	public String getHelp() {
		return "Clears messages\n" + "Usage: `" + YEnvi.prefix + "" + this.getName()
				+ " [# ammount of messages 0-100 / all = 100]`";
	}

	@Override
	public List<String> getAliases() {
		return List.of("c", "cls", "cl");
	}

	public static boolean isUrl(String url) {
		try {
			new URL(url);
			return true;
		} catch (Exception ignored) {
			return false;
		}
	}

}
