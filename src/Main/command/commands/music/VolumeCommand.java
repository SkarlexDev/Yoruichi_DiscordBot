package Main.command.commands.music;

import java.awt.Color;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.music.lavaplayer.GuildMusicManager;
import Main.music.lavaplayer.PlayerManager;
import Main.util.YEnvi;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class VolumeCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
		AudioPlayer audioPlayer = musicManager.audioPlayer;
		String current = String.valueOf(audioPlayer.getVolume());
		List<String> args = ctx.getArgs();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}

		if (args.size() == 0) {
			channel.sendMessage("Current volume: " + current + "\n" + "Change music volume\n" + "`" + this.helpMsg())
					.queue(message -> {
						ctx.clear(message);
					});
			return;
		}
		String n1 = args.get(0);
		try {
			Integer.parseInt(n1);
		} catch (Exception e) {
			channel.sendMessage("Invalid command use `" + this.helpMsg()).queue((message) -> {
				ctx.clear(message);
			});
			return;
		}
		if (args.size() == 1) {

			if (Integer.parseInt(n1) < 0) {
				channel.sendMessage("You can't use negative value! `" + this.helpMsg()).queue((message) -> {
					ctx.clear(message);
				});
				return;
			}
			if (Integer.parseInt(n1) > 100) {
				channel.sendMessage("You can't set volume more than 100 `" + this.helpMsg()).queue((message) -> {
					ctx.clear(message);
				});
				return;
			}

			int newval = Integer.parseInt(n1);

			EmbedBuilder usage = EmbedUtils.getDefaultEmbed().setColor(Color.ORANGE).setTitle("Volume updated")
					.setDescription("From: " + current + " to: " + newval);
			channel.sendMessageEmbeds(usage.build()).queue(message -> {
				ctx.clear(message);
			});

			PlayerManager.getInstance().setVolume(audioPlayer, newval);

		}

	}

	@Override
	public String getName() {
		return "volume";
	}

	@Override
	public String getCategory() {
		return "Music";
	}

	@Override
	public String getHelp() {
		return "Change music volume\n" + "Usage: `" + this.helpMsg() + "\n" + "Aliases: `" + this.getAliases() + "`";
	}

	@Override
	public List<String> getAliases() {
		return List.of("v", "vol");
	}

	public String helpMsg() {
		return YEnvi.prefix + "" + this.getName() + " [#0-100]`";
	}

}
