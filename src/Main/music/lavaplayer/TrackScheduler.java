package Main.music.lavaplayer;

import java.awt.Color;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import Main.util.MessageUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class TrackScheduler extends AudioEventAdapter {
	public final AudioPlayer player;
	public final BlockingQueue<AudioTrack> queue;
	public boolean repeat = false;

	public TrackScheduler(AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
		this.repeat = false;
	}

	public void queue(AudioTrack track) {
		if (!this.player.startTrack(track, true)) {
			this.queue.offer(track);
		}
	}

	public void nextTrack() {
		this.player.startTrack(queue.poll(), false);
		try {
			nowPlay();
		} catch (Exception e) {
		}

	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		super.onTrackStart(player, track);
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			if (this.repeat) {
				this.player.startTrack(track.makeClone(), false);
				return;
			}
			nextTrack();
		}
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		super.onTrackException(player, track, exception);
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
		super.onTrackStuck(player, track, thresholdMs);
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs, StackTraceElement[] stackTrace) {
		super.onTrackStuck(player, track, thresholdMs, stackTrace);
		System.out.println("Track stuck");
	}

	public BlockingQueue<AudioTrack> getQueue() {
		return queue;
	}

	public void nowPlay() {
		try {
			TextChannel channel = PlayerManager.getInstance().channel;
			AudioTrackInfo info = this.player.getPlayingTrack().getInfo();
			EmbedBuilder eb = EmbedUtils.getDefaultEmbed().setColor(Color.green).setAuthor("TrackScheduler")
					.setDescription(" \uD83D\uDD0A Now playing: [" + info.title + "](" + info.uri + ") ["
							+ formatTime(this.player.getPlayingTrack().getDuration()) + "] By " + info.author);
			channel.sendMessageEmbeds(eb.build()).queue(message -> {
				MessageUtil.autoDeleteMessageLong(message);
			});
		} catch (Exception e) {

		}

	}

	public String formatTime(long timeInMillis) {
		final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
		final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
		final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
