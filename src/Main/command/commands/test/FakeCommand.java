package Main.command.commands.test;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;

import Main.Yoruichi;
import Main.command.CommandContext;
import Main.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class FakeCommand implements ICommand{
private Boolean state;
	
	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
	
		if(!this.state) {
			ctx.getDisabled(channel);
			return;
		}
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
		
		WebUtils.ins.getJSONObject("https://randomuser.me/api").async((json) -> {
			
			
			final JsonNode data = json.get("results").get(0);
			final JsonNode nameJ = data.get("name");
			final JsonNode lJ = data.get("location");
			final String name = (nameJ.get("title").asText()) + ". " + 
								(nameJ.get("first").asText()) + " " +
								(nameJ.get("last").asText());			

			
			final String location = (lJ.get("street").get("name").asText()) + " " +
									(lJ.get("street").get("number").asText());
			
			final String gender = data.get("gender").asText();
			
			
			embed.setTitle("Fake User Generator")
				.addField("Name", name, false)
				.addField("Gender", gender, false)
				.addField("Location", location, false)
				.addField("Country", lJ.get("country").asText(),true)
				.addField("State", lJ.get("state").asText(),true)
				.addField("City", lJ.get("city").asText(),true)
				.addField("Born: ", born(data.get("dob").get("date")) , false)
				.addField("Email", data.get("email").asText().replaceAll("@example.com", "@gmail.com"),false)
				.addField("Phone" , data.get("phone").asText(), false)
				.setImage(data.get("picture").get("large").asText());
			
					
			channel.sendMessageEmbeds(embed.build()).queue();

				
		});	
		

	}

	@Override
	public String getName() {
		return "fake";
	}

	@Override
	public String getHelp() {
		return "Generate a random fake user\n"
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
	
	public String born(JsonNode born) {
		String temp = born.asText();
		LocalDate current_date = LocalDate.now();
		Integer yearnow = Integer.parseInt(current_date.toString().substring(0,4));
		Integer year = Integer.parseInt(temp.substring(0,4));
		
		if(yearnow-year > 40) {
			year = year + 35;
		}
	
		String bornDate = (year + "" + temp.substring(4,temp.length()));
		return bornDate;
	}
}

	

