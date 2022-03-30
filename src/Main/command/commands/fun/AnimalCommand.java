package Main.command.commands.fun;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.util.YEnvi;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.internal.utils.IOUtil;

public class AnimalCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		int size = ctx.getArgs().size();
		List<String> args = ctx.getArgs();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}

		if (args.isEmpty()) {
			this.showHelp(ctx, channel);
			return;
		}

		if (size == 1) {

			WebUtils.ins.getJSONObject("https://apis.duncte123.me/animal/" + args.get(0) + "").async((json) -> {
				if (!json.get("success").asBoolean()) {
					channel.sendMessage("Something went wrong, try again later or use `" + YEnvi.prefix + " help "
							+ this.getName() + "`").queue(message -> {
								ctx.clear(message);
							});
					return;
				}

				final JsonNode data = json.get("data");
				final String url = data.get("file").asText();

				EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
				byte[] file;
				try {
					file = IOUtil.readFully(new URL(url).openStream());
					embed.setImage("attachment://animal.png") // we specify this in sendFile as "animal.png"
							.setTitle(data.get("id").asText());
					channel.sendFile(file, "animal.png").setEmbeds(embed.build()).queue();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					channel.sendMessage("File not found try again").queue(message -> {
						ctx.clear(message);
					});
				} catch (IOException e) {
					e.printStackTrace();
				}

			});

		} else {
			EmbedBuilder usage = EmbedUtils.getDefaultEmbed().setColor(Color.RED).setTitle("Type only 1 animal")
					.setDescription("Usage: " + "`" + YEnvi.prefix + "" + this.getName()
							+ " [# llama cat duck alpaca seal camel dog fox lizard bird wolf panda discord-monster ]`");
			channel.sendMessageEmbeds(usage.build()).queue(message -> {
				ctx.clear(message);
			});
			return;
		}

	}

	@Override
	public String getName() {
		return "animal";
	}

	@Override
	public String getCategory() {
		return "Fun";
	}

	@Override
	public String getHelp() {
		return "Shows a random animal image\n" + "Usage: `" + YEnvi.prefix + "" + this.getName()
				+ " [# llama cat duck alpaca seal camel dog fox lizard bird wolf panda discord-monster ]`\n"
				+ "Aliases: `" + this.getAliases() + "`";
	}

	@Override
	public List<String> getAliases() {
		return List.of("a", "anim", "pet");
	}

}
