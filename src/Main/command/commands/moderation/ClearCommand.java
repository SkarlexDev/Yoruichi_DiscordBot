package Main.command.commands.moderation;

import java.awt.Color;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Main.Config;
import Main.command.CommandContext;
import Main.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class ClearCommand implements ICommand {
	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final User user = ctx.getAuthor();
		List<String> args = ctx.getArgs();
		int delTime = 5;

		if (!user.getId().equals(Config.get("owner_id"))) {
			channel.sendMessage("You are not allowed to use this command!").queue();
			return;
		}
		
		if(args.size()==0) {			
			EmbedBuilder usage = new EmbedBuilder()
					.setColor(Color.ORANGE)
					.setTitle("Specify amount to delete")
					.setDescription("Usage: `" + "!" + "clear [# ammount of messages]`");
			channel.sendMessageEmbeds(usage.build()).queue();
			usage.clear();
			return;
		}
		String n1 = args.get(0);

		if (!n1.equalsIgnoreCase("all")) {
			try {
				Integer.parseInt(n1);
			} catch (Exception e) {
				channel.sendMessage("Invalid command use `!help clear`").queue((message) -> {
					message.delete().queueAfter(delTime, TimeUnit.SECONDS);
				});
				return;
			}
		} else {
			n1 = "100";
		}

		if (args.size() == 1) {
			try {
				if (Integer.parseInt(n1) == 1) {
					n1 = "2";
				}
				// max 100 messages && not older than 2 weeks
				List<Message> messages = channel.getHistory().retrievePast(Integer.parseInt(n1)).complete();
				channel.deleteMessages(messages).queue();

				// Success
				EmbedBuilder success = new EmbedBuilder()
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
					EmbedBuilder error = new EmbedBuilder()
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
			EmbedBuilder usage = new EmbedBuilder()
					.setColor(Color.ORANGE)
					.setTitle("Specify amount to delete")
					.setDescription("Usage: `" + "!" + "clear [# ammount of messages]`");
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
		return "Clears messages\n" + "Usage: `!clear [# ammount of messages]`";
	}
	@Override
	public String getCategory() {
		return "Moderation";
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