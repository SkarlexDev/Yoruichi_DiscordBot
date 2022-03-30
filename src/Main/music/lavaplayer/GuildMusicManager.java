package Main.music.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;

public class GuildMusicManager {

	public final AudioPlayer audioPlayer;

	public final TrackScheduler scheduler;

	public GuildMusicManager(AudioPlayerManager manager) {
		manager = new DefaultAudioPlayerManager();
		audioPlayer = manager.createPlayer();
		scheduler = new TrackScheduler(audioPlayer);
		audioPlayer.addListener(scheduler);
	}

	public AudioPlayerSendHandler getSendHandler() {
		return new AudioPlayerSendHandler(audioPlayer);
	}
}
