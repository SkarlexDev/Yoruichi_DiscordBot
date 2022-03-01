package Main.command.commands.test;

import com.fasterxml.jackson.databind.JsonNode;

import Main.Yoruichi;
import Main.command.CommandContext;
import Main.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class TestCommand implements ICommand {
	private Boolean state;
	
	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
	
		if(!this.state) {
			ctx.getDisabled(channel);
			return;
		}

		EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
		
		WebUtils.ins.getJSONObject("https://www.boredapi.com/api/activity").async((json) -> {
			

			final JsonNode data = json.get("activity");		
			final JsonNode type = json.get("type");
			final JsonNode p = json.get("participants");		

			embed.setTitle("Bored Helper")
				.addField("Do this", qM(data), false)
				.addField("Type", qM(type), false)
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
		return "test";
	}

	@Override
	public String getHelp() {
		return "Shows a random activity\n"
				+ "Usage: `" + Yoruichi.prefix + "" + this.getName() + "`";
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
		ctx.commandHelper(channel, this.getHelp() , this.getName().toUpperCase());
	}
	
	public String qM(JsonNode name) {
		return name.toPrettyString().replaceAll("\"", "");
	}
}
