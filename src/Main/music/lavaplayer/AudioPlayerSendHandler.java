package Main.music.lavaplayer;

import java.nio.ByteBuffer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {

	private final AudioPlayer audioPlayer;
	private AudioFrame frame;

	public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
		this.frame = new MutableAudioFrame();
	}

	@Override
	public boolean canProvide() {
		this.frame = this.audioPlayer.provide();
		return this.frame != null;
	}

	@Override
	public ByteBuffer provide20MsAudio() {
		return ByteBuffer.wrap(this.frame.getData());
	}

	@Override
	public boolean isOpus() {
		return true;
	}
}