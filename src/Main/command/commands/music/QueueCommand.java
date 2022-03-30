package Main.command.commands.music;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.music.lavaplayer.GuildMusicManager;
import Main.music.lavaplayer.PlayerManager;
import Main.util.YEnvi;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class QueueCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
		final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}
		if (queue.isEmpty()) {
			channel.sendMessage("There is nothing in queue").queue(message -> {
				ctx.clear(message);
			});
			return;
		}

		final int trackCount = Math.min(queue.size(), 10);
		final List<AudioTrack> trackList = new ArrayList<>(queue);
		final MessageAction messageAction = channel.sendMessage("**Current Queue:**\n");

		for (int i = 0; i < trackCount; i++) {
			final AudioTrack track = trackList.get(i);
			final AudioTrackInfo info = track.getInfo();

			messageAction.append('#').append(String.valueOf(i + 1)).append(" `").append(String.valueOf(info.title))
					.append(" by ").append(info.author).append("` [`")
					.append(musicManager.scheduler.formatTime(track.getDuration())).append("`]\n");
		}

		if (trackList.size() > trackCount) {
			messageAction.append("And `").append(String.valueOf(trackList.size() - trackCount)).append("` more...");
		}

		messageAction.queue(message -> {
			ctx.clear(message);
		});

	}

	@Override
	public String getName() {
		return "queue";
	}

	@Override
	public String getCategory() {
		return "Music";
	}

	@Override
	public String getHelp() {
		return "Shows the queued up songs\n" + "Usage: `" + YEnvi.prefix + "" + this.getName() + "`";
	}

	@Override
	public List<String> getAliases() {
		return List.of("q");
	}

}
