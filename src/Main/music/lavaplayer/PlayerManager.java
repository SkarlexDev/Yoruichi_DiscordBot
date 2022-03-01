package Main.music.lavaplayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;
    public TextChannel channel;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            //AudioPlayer audioPlayer = guildMusicManager.audioPlayer;

            return guildMusicManager;
        });
    }
    
    public void setVolume(AudioPlayer audioPlayer, int volume) {
    	audioPlayer.setVolume(volume);
    	
    }
    public void loadAndPlay(TextChannel channel, String trackUrl) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        
        this.channel = channel;
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                channel.sendMessage("Adding to queue: `")
                        .append(track.getInfo().title)
                        .append("` by `")
                        .append(track.getInfo().author)
                        .append('`')
                        .queue();
                
                musicManager.scheduler.nowPlay();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();
                if (playlist.isSearchResult()) {
                    channel.sendMessage("Adding to queue: `")
                    .append(String.valueOf(1))
                    .append("` tracks from playlist `")
                    .append(playlist.getName())
                    .append('`')
                    .queue();
                    musicManager.scheduler.queue(tracks.get(0));
                    
                    musicManager.scheduler.nowPlay();
                }
                else
                {
                	channel.sendMessage("Adding to queue: `")
                    .append(String.valueOf(tracks.size()))
                    .append("` tracks from playlist `")
                    .append(playlist.getName())
                    .append('`')
                    .queue();
                	
                	musicManager.scheduler.nowPlay(); 
                	
                	for (final AudioTrack track : tracks) {
                		musicManager.scheduler.queue(track);
                	}
                }           
  
            }

            @Override
            public void noMatches() {
                //
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                //
            }            
            
        });
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

}