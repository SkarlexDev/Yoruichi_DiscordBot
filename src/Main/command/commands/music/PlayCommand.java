package Main.command.commands.music;

import java.net.URL;
import java.util.List;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.music.lavaplayer.PlayerManager;
import Main.util.YEnvi;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}

		if (ctx.getArgs().isEmpty()) {
			this.showHelp(ctx, channel);
			return;
		}

		final Member self = ctx.getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();
		final Member member = ctx.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();

		if (!memberVoiceState.inVoiceChannel()) {
			channel.sendMessage("Please join a voice channel").queue(message -> {
				ctx.clear(message);
			});
			return;
		}

		while (true) {
			if (!selfVoiceState.inVoiceChannel()) {
				if (memberVoiceState.inVoiceChannel()) {
					AudioManager manager = ctx.getGuild().getAudioManager();
					manager.openAudioConnection(memberVoiceState.getChannel());
					break;
				} else {
					ctx.getChannel().sendMessage("please join a voice channel").queue(message -> {
						ctx.clear(message);
					});
					return;
				}
			}

			if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
				ctx.getChannel().sendMessage("You are in different channel use `" + YEnvi.prefix + "join`")
						.queue(message -> {
							ctx.clear(message);
						});
				return;
			}
			break;
		}

		String link = String.join(" ", ctx.getArgs());

		if (!isUrl(link)) {
			link = "ytsearch:" + link;

		}

		PlayerManager.getInstance().loadAndPlay(channel, link);

	}

	@Override
	public String getName() {
		return "play";
	}

	@Override
	public String getCategory() {
		return "Music";
	}

	@Override
	public String getHelp() {
		return "Plays a song\n" + "Usage: `" + YEnvi.prefix + "" + this.getName() + " <youtube link>`\n" + "Aliases: `"
				+ this.getAliases() + "`";
	}

	@Override
	public List<String> getAliases() {
		return List.of("p");
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
