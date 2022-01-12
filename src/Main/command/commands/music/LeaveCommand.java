package Main.command.commands.music;

import Main.command.CommandContext;
import Main.command.ICommand;
import Main.music.lavaplayer.GuildMusicManager;
import Main.music.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
    	final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("I need to be in a voice channel for this to work").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("You need to be in the same voice channel as me for this to work").queue();
            return;
        }
        
        final Guild guild = ctx.getGuild();
        
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        
        musicManager.scheduler.queue.clear();
        musicManager.audioPlayer.stopTrack();
    
        
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        
        audioManager.closeAudioConnection();
        
        channel.sendMessageFormat("Ok I leave :(").queue();
        
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Bot leaves the coive channel";
    }
    
    @Override
	public String getCategory() {
		return "Music";
	}
}
