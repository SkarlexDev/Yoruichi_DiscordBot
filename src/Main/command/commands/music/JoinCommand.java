package Main.command.commands.music;

import Main.command.CommandContext;
import Main.command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ICommand {
	private Boolean state = true;
	
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        if(!this.state) {
			ctx.getDisabled(channel);
			return;
		}
        
        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
            return;
        }    
        
        while (true) {
            if (!selfVoiceState.inVoiceChannel()) {
                if (memberVoiceState.inVoiceChannel()) {
                	audioManager.openAudioConnection(memberVoiceState.getChannel());
                    channel.sendMessageFormat("Connecting to `\uD83D\uDD0A %s`", memberChannel.getName()).queue();
                    break;
                } else {
                    ctx.getChannel().sendMessage("please join a voice channel").queue();
                    return;
                }
            }

            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            	audioManager.openAudioConnection(memberChannel);
                channel.sendMessageFormat("Connecting to `\uD83D\uDD0A %s`", memberChannel.getName()).queue();
            }
            break;
        }        
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "Makes the bot join your voice channel";
    }
    
    @Override
	public String getCategory() {
		return "Music";
	}
    
    @Override
	public void setState(Boolean state) {
		this.state = state;
		
	}

	@Override
	public Boolean getState() {
		return this.state;
	}
	
	@Override
	public void showHelp(CommandContext ctx, TextChannel channel) {
		ctx.commandHelper(channel, this.getHelp() , this.getName().toUpperCase());
	}
}
