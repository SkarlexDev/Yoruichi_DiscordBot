package Main.command.commands.music;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Main.BotRun;
import Main.command.CommandContext;
import Main.command.ICommand;
import Main.music.lavaplayer.GuildMusicManager;
import Main.music.lavaplayer.PlayerManager;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class VolumeCommand implements ICommand{
	private Boolean state = true;
	
	@Override
	public void handle(CommandContext ctx) {
		int delTime = 3;
		final TextChannel channel = ctx.getChannel();
		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
		AudioPlayer audioPlayer = musicManager.audioPlayer;
		String current = String.valueOf(audioPlayer.getVolume());
		List<String> args = ctx.getArgs();
		
		if(!this.state) {
			ctx.getDisabled(channel);
			return;
		}
		
		if(args.size()==0) {			
			channel.sendMessage("Current volume: "+ current+ "\n" + "Change music volume\n" + "`"+BotRun.prefix+"volume [# 0-100]`").queue();
			return;
		}
		String n1 = args.get(0);
		try {
			Integer.parseInt(n1);
		} catch (Exception e) {
			channel.sendMessage("Invalid command use `"+BotRun.prefix+"volume [# 0-100]`").queue((message) -> {
				message.delete().queueAfter(delTime, TimeUnit.SECONDS);
			});
			return;
		}
		if (args.size() == 1) {
			
			if(Integer.parseInt(n1)<0) {
				channel.sendMessage("You can't use negative value! `"+BotRun.prefix+"volume [# 0-100]`").queue((message) -> {
					message.delete().queueAfter(delTime, TimeUnit.SECONDS);
				});
				return;
			}
			if(Integer.parseInt(n1)>100) {
				channel.sendMessage("You can't set volume more than 100 `"+BotRun.prefix+"volume [# 0-100]`").queue((message) -> {
					message.delete().queueAfter(delTime, TimeUnit.SECONDS);
				});
				return;
			}
			
			int newval = Integer.parseInt(n1);
			
			EmbedBuilder usage = EmbedUtils.getDefaultEmbed().
					setColor(Color.ORANGE)
					.setTitle("Volume updated")
					.setDescription("From: " + current + " to: " + newval);
			channel.sendMessageEmbeds(usage.build()).queue();
			usage.clear();
			
			PlayerManager.getInstance().setVolume(audioPlayer,newval);
		}
		
		
	}

	@Override
	public String getName() {
		return "volume";
	}

	@Override
	public String getHelp() {
		return "Change music volume\n" + "Usage: `"+BotRun.prefix+"volume [0-100]`\n"
				+ "Aliases: `" + this.getAliases()+ "`";
	}
	@Override
	public List<String> getAliases() {
		return List.of("v" ,"vol");
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
