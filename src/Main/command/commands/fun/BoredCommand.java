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

public class BoredCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}

		EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

		WebUtils.ins.getJSONObject("https://www.boredapi.com/api/activity").async((json) -> {

			final JsonNode data = json.get("activity");
			final JsonNode type = json.get("type");
			final JsonNode p = json.get("participants");

			embed.setTitle("Bored Helper").addField("Do this", qM(data), false).addField("Type", qM(type), false)
					.addField("Participants", qM(p), false);

			WebUtils.ins.getJSONObject("https://catfact.ninja/fact").async((json2) -> {

				final JsonNode cat = json2.get("fact");
				embed.addField("Cat fact", qM(cat), false);
				channel.sendMessageEmbeds(embed.build()).queue();
			});

		});

	}

	@Override
	public String getName() {
		return "bored";
	}

	@Override
	public String getCategory() {
		return "Fun";
	}

	@Override
	public String getHelp() {
		return "Shows a random activity\n" + "Usage: `" + YEnvi.prefix + "" + this.getName() + "`";
	}

	public String qM(JsonNode name) {
		return name.toPrettyString().replaceAll("\"", "");
	}
}
