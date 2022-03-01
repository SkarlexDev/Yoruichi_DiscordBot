package Main.command.commands.fun;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import Main.Yoruichi;
import Main.command.CommandContext;
import Main.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.internal.utils.IOUtil;

public class AnimalCommand implements ICommand {
	private Boolean state;
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

		if (args.size() == 1) {

			WebUtils.ins.getJSONObject("https://apis.duncte123.me/animal/" + args.get(0) + "").async((json) -> {
				if (!json.get("success").asBoolean()) {
					channel.sendMessage("Something went wrong, try again later or use `"  + Yoruichi.prefix + " help " + this.getName() +  "`").queue();
					System.out.println(json);
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
					channel.sendMessage("File not found try again").queue();
				} catch (IOException e) {
					e.printStackTrace();
				}


			});

		} else {
			EmbedBuilder usage = EmbedUtils.getDefaultEmbed()
					.setColor(Color.RED)
					.setTitle("Type only 1 animal")
					.setDescription("Usage: "
							+ "`"  + Yoruichi.prefix + "" + this.getName() + " [# llama cat duck alpaca seal camel dog fox lizard bird wolf panda discord-monster ]`");
			channel.sendMessageEmbeds(usage.build()).queue();
			usage.clear();
			return;
		}

	}

	@Override
	public String getName() {
		return "animal";
	}

	@Override
	public String getHelp() {
		return "Shows a random animal image\n"
				+ "Usage: `" + Yoruichi.prefix + "" + this.getName() + " [# llama cat duck alpaca seal camel dog fox lizard bird wolf panda discord-monster ]`\n"
				+ "Aliases: `" + this.getAliases() + "`";
	}

	@Override
	public String getCategory() {
		return "Fun";
	}

	@Override
	public List<String> getAliases() {
		return List.of("a", "anim", "pet");
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
