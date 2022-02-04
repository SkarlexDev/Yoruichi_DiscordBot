package Main.command.commands.music;

import java.net.URL;
import java.util.List;

import Main.BotStartup;
import Main.command.CommandContext;
import Main.command.ICommand;
import Main.music.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if (ctx.getArgs().isEmpty()) {
            channel.sendMessage("Correct usage is `!!play <youtube link>`").queue();
            return;
        }

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        
        if (!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("Please join a voice channel").queue();
            return;
        }
        
        while (true) {
            if (!selfVoiceState.inVoiceChannel()) {
                if (memberVoiceState.inVoiceChannel()) {
                    AudioManager manager = ctx.getGuild().getAudioManager();
                    manager.openAudioConnection(memberVoiceState.getChannel());
                    break;
                } else {
                    ctx.getChannel().sendMessage("please join a voice channel").queue();
                    return;
                }
            }

            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                ctx.getChannel().sendMessage("You are in different channel use `"+BotStartup.prefix+"join`").queue();
                return;
            }
            break;
        }
        
        /*
        
        TODO: banned music + ban music command
        
        List<> bannedmusic = BannedMusic.getInstance().? get list // same as PlayerManager
        */
       
        String link = String.join(" ", ctx.getArgs());

        if (!isUrl(link)) {
            link = "ytsearch:" + link;
           
        }

        PlayerManager.getInstance().loadAndPlay(channel, link);
        
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Plays a song\n" +
                "Usage: `"+BotStartup.prefix+"play <youtube link>`\n"
        		+ "Aliases: `" + this.getAliases() + "`";
        
        
    }
    
    @Override
	public List<String> getAliases() {
		return List.of("p");
	}
    
    @Override
	public String getCategory() {
		return "Music";
	}

    public static boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}