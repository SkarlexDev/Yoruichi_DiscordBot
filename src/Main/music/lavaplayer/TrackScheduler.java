package Main.music.lavaplayer;

import java.awt.Color;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class TrackScheduler extends AudioEventAdapter {
	public final AudioPlayer player;
	public final BlockingQueue<AudioTrack> queue;

	public TrackScheduler(AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
	}

	public void queue(AudioTrack track) {
		if (!this.player.startTrack(track, true)) {
			this.queue.offer(track);
		}
	}

	public void nextTrack() {
		this.player.startTrack(this.queue.poll(), false);
		try {
			nowPlay();
		} catch (Exception e) {
			// do nothing
		}

	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			nextTrack();
		}
	}

	public void nowPlay() {
		try {
			TextChannel channel = PlayerManager.getInstance().channel;
			AudioTrackInfo info = this.player.getPlayingTrack().getInfo();
			EmbedBuilder eb = EmbedUtils.getDefaultEmbed()
					.setColor(Color.green)
					.setAuthor("Now playing: ")
					.setTitle(info.title,info.uri)
					.setDescription("By " + info.author);			
			channel.sendMessageEmbeds(eb.build()).queue();
		} catch (Exception e) {

		}
		
		
		
	}
}