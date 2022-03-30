package Main.command.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.music.lavaplayer.GuildMusicManager;
import Main.music.lavaplayer.PlayerManager;
import Main.util.YEnvi;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class SkipCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final Member self = ctx.getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}

		if (!selfVoiceState.inVoiceChannel()) {
			channel.sendMessage("I need to be in a voice channel for this to work").queue(message -> {
				ctx.clear(message);
			});
			return;
		}

		final Member member = ctx.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();

		if (!memberVoiceState.inVoiceChannel()) {
			channel.sendMessage("You need to be in a voice channel for this command to work").queue(message -> {
				ctx.clear(message);
			});
			return;
		}

		if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
			channel.sendMessage("You need to be in the same voice channel as me for this to work").queue(message -> {
				ctx.clear(message);
			});
			return;
		}

		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
		final AudioPlayer audioPlayer = musicManager.audioPlayer;

		if (audioPlayer.getPlayingTrack() == null) {
			channel.sendMessage("There is no track playing currently").queue(message -> {
				ctx.clear(message);
			});
			return;
		}

		musicManager.scheduler.nextTrack();
		channel.sendMessage("Skipped the current track").queue(message -> {
			ctx.clear(message);
		});
	}

	@Override
	public String getName() {
		return "skip";
	}

	@Override
	public String getCategory() {
		return "Music";
	}

	@Override
	public String getHelp() {
		return "Skips the current track\n" + "Usage: `" + YEnvi.prefix + "" + this.getName() + "`";
	}

}
