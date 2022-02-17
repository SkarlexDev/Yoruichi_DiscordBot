package Main.command.commands.games;

import java.awt.Color;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import Main.BotRun;
import Main.command.CommandContext;
import Main.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class RioCommand implements ICommand {
	private Boolean state = true;
	
	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		List<String> args = ctx.getArgs();
				
		if(!this.state) {
			ctx.getDisabled(channel);
			return;
		}
		if (ctx.getArgs().isEmpty()) {
			this.showHelp(ctx, channel);
			return;
		}

		if (args.size() >= 2) {

			String charname = args.get(0).toString();
			String realmname = null;
			if (args.size() == 2) {
				realmname = args.get(1);
			}
			if (args.size() == 3) {
				realmname = args.get(1) + args.get(2);
			}
			WebUtils.ins.getJSONObject("https://raider.io/api/v1/characters/profile?region=eu&realm=" + realmname
					+ "&name=" + charname + "&fields=guild%2Cmythic_plus_scores%2Cmythic_plus_ranks").async((json) -> {
						ObjectNode data = json;

						EmbedBuilder pr = EmbedUtils.getDefaultEmbed()
								.setTitle(data.get("name").asText(), data.get("profile_url").asText())
								.setColor(Color.GREEN)
								.setThumbnail(data.get("thumbnail_url").asText())
								.addField("Faction", data.get("faction").asText().toUpperCase(), true)
								.addField("Region", data.get("region").asText().toUpperCase(), true)
								.addField("Realm", data.get("realm").asText(), true)
								.addField("Race", data.get("race").asText(), true)
								.addField("Class", data.get("class").asText(), true)
								.addField("Achiev points", data.get("achievement_points").asText(), true)
								.addField("", "Rio score", false);

						JsonNode mplus = data.get("mythic_plus_scores");

						String tank = mplus.get("tank").asText();
						String dps = mplus.get("dps").asText();
						String healer = mplus.get("healer").asText();

						if (Double.parseDouble(tank) > 200) {
							pr.addField("Tank", tank, true);
						}
						if (Double.parseDouble(dps) > 200) {
							pr.addField("DPS", dps, true);
						}
						if (Double.parseDouble(healer) > 200) {
							pr.addField("Healer", healer, true);
						}
						pr.addField("Total Rio", mplus.get("all").asText(), true).addField("", "Rank", false);

						JsonNode rank = data.get("mythic_plus_ranks").get("overall");

						pr.addField("World", rank.get("world").asText(), true)
								.addField("Region", rank.get("region").asText(), true)
								.addField("Realm", rank.get("realm").asText(), true);

						channel.sendMessageEmbeds(pr.build()).queue();

					});
		} else {
			channel.sendMessage("Correct usage is `"+BotRun.prefix+"rio [name][realm]`").queue();
			return;
		}

	}

	@Override
	public String getName() {
		return "rio";
	}

	@Override
	public String getHelp() {
		return "Shows a wow raider.io stats\n"
				+ "Correct usage is `"+BotRun.prefix+"rio [name][realm]`";
	}

	@Override
	public String getCategory() {
		return "Games";
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
