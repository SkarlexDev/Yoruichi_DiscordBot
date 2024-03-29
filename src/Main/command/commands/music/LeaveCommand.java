package Main.command.commands.music;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.music.lavaplayer.GuildMusicManager;
import Main.music.lavaplayer.PlayerManager;
import Main.util.YEnvi;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand extends AbstractCommand implements ICommand {

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

		final Guild guild = ctx.getGuild();

		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

		musicManager.scheduler.queue.clear();
		musicManager.audioPlayer.stopTrack();

		final AudioManager audioManager = ctx.getGuild().getAudioManager();

		audioManager.closeAudioConnection();

		channel.sendMessageFormat("Ok I leave :(").queue(message -> {
			ctx.clear(message);
		});

	}

	@Override
	public String getName() {
		return "leave";
	}

	@Override
	public String getCategory() {
		return "Music";
	}

	@Override
	public String getHelp() {
		return "Bot leaves the coive channel\n" + "Usage: `" + YEnvi.prefix + "" + this.getName() + "`";
	}

}
