package Main.command.commands.fun;

import com.fasterxml.jackson.databind.JsonNode;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.util.YEnvi;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class JokeCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}

		WebUtils.ins.getJSONObject("https://apis.duncte123.me/joke").async((json) -> {
			if (!json.get("success").asBoolean()) {
				channel.sendMessage("Something went wrong, try again later").queue();
				System.out.println(json);
				return;
			}

			final JsonNode data = json.get("data");
			final String title = data.get("title").asText();
			final String url = data.get("url").asText();
			final String body = data.get("body").asText();

			final EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setTitle(title, url).setDescription(body);

			channel.sendMessageEmbeds(embed.build()).queue();
		});

	}

	@Override
	public String getName() {
		return "joke";
	}

	@Override
	public String getCategory() {
		return "Fun";
	}

	@Override
	public String getHelp() {
		return "Shows a random joke\n" + "Usage: `" + YEnvi.prefix + "" + this.getName() + "`";
	}

}
