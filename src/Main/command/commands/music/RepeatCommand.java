package Main.command.commands.music;

import java.util.List;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.music.lavaplayer.GuildMusicManager;
import Main.music.lavaplayer.PlayerManager;
import Main.util.YEnvi;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class RepeatCommand extends AbstractCommand implements ICommand {

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

		final boolean newRepeat = !musicManager.scheduler.repeat;
		musicManager.scheduler.repeat = newRepeat;

		channel.sendMessageFormat("Player has been set to **%s**", newRepeat ? "repeat" : "not repeat")
				.queue(message -> {
					ctx.clear(message);
				});

	}

	@Override
	public String getName() {
		return "repeat";
	}

	@Override
	public String getCategory() {
		return "Music";
	}

	@Override
	public String getHelp() {
		return "Loops the current song\n" + "Usage: `" + YEnvi.prefix + "" + this.getName() + "`";
	}

	@Override
	public List<String> getAliases() {
		return List.of("r");
	}

}
