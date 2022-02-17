package Main.command.commands.admin;

import java.awt.Color;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Main.BotRun;
import Main.command.CommandContext;
import Main.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class ClearCommand implements ICommand {
	private Boolean state = true;
	
	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		int size = ctx.getArgs().size();
		int delTime = 5;

		if(!ctx.checkRolePermision(ctx.getMember().getRoles())) {
			ctx.getPermisionDenied(channel);
			return;
		}		
		if(!this.state) {
			ctx.getDisabled(channel);
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
				channel.sendMessage("Invalid command use `"+BotRun.prefix+"help clear`").queue((message) -> {
					message.delete().queueAfter(delTime, TimeUnit.SECONDS);
				});
				return;
			}
		} else {
			n1 = "100";
		}

		if (size == 1) {
			try {
				List<Message> messages;
				if (Integer.parseInt(n1) == 1) {
					messages = channel.getHistory().retrievePast(Integer.parseInt(n1) + 1).complete();
				}
				else {
					messages = channel.getHistory().retrievePast(Integer.parseInt(n1)).complete();
				}
				// max 100 messages && not older than 2 weeks
				channel.deleteMessages(messages).queue();

				// Success
				EmbedBuilder success = EmbedUtils.getDefaultEmbed()
						.setColor(Color.GREEN)
						.setTitle("Successfully deleted " + n1 + " messags.");
				MessageEmbed msg = success.build();
				channel.sendMessageEmbeds(msg).queue((message) -> {
					message.delete().queueAfter(delTime, TimeUnit.SECONDS);
				});
				success.clear();
			} catch (IllegalArgumentException e) {
				if (e.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {
					// Too many messages
					EmbedBuilder error = EmbedUtils.getDefaultEmbed()
							.setColor(Color.RED)
							.setTitle("Too many messages selected")
							.setDescription("You can delete between 1-100 messages.");
					channel.sendMessageEmbeds(error.build()).queue();
					error.clear();

				} else {
					channel.sendMessage(e.toString()).queue();
				}
			}
		} else {
			// Usage of command
			EmbedBuilder usage = EmbedUtils.getDefaultEmbed()
					.setColor(Color.ORANGE)
					.setTitle("Specify amount to delete")
					.setDescription("Usage: `" + BotRun.prefix + "clear [# ammount of messages]`");
			channel.sendMessageEmbeds(usage.build()).queue();
			usage.clear();
		}
	}

	@Override
	public String getName() {
		return "clear";
	}

	@Override
	public String getHelp() {
		return "Clears messages\n" + "Usage: `"+BotRun.prefix+"clear [# ammount of messages 0-100 / all = 100]`";
	}
	@Override
	public String getCategory() {
		return "Admin";
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
		ctx.commandHelper(channel, this.getHelp() , this.getName().toUpperCase());
	}
}
