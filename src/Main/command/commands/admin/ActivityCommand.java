package Main.command.commands.admin;

import java.awt.Color;
import java.util.List;

import Main.Yoruichi;
import Main.command.CommandContext;
import Main.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;

public class ActivityCommand implements ICommand{
	private Boolean state;
	
	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		int size = ctx.getArgs().size();
		
		if(!ctx.checkRolePermision(ctx.getMember().getRoles())) {
			ctx.getPermisionDenied(channel);
			return;
		}		
		if(!this.state) {
			ctx.getDisabled(channel);
			return;
		}
		if (ctx.getArgs().isEmpty() || size == 1) {
			this.showHelp(ctx, channel);
			return;
		}

		
		String activityType = ctx.getArgs().get(0).toLowerCase();
		String activity;
		String type;
		
		if(size >= 2) {
			if(size == 2) {
				activity = ctx.getArgs().get(1);				
			}else {
				StringBuilder active = new StringBuilder();
				for (int i = 1; i < size; i++) {
					active.append(" ").append(ctx.getArgs().get(i)); 					
				}
				activity = active.toString();
			}
			switch (activityType) {
	         case "w":
	        	 type = "Watching";
	        	 ctx.getJDA().getPresence().setActivity(Activity.watching(activity));
	             break;
	         case "c":
	        	 type = "Competing";
	        	 ctx.getJDA().getPresence().setActivity(Activity.competing(activity));
	             break;
	         case "l":
	        	 type = "Listening";
	        	 ctx.getJDA().getPresence().setActivity(Activity.listening(activity));
	         	break;
	         case "p":
	        	 type = "Playing";
	        	 ctx.getJDA().getPresence().setActivity(Activity.playing(activity));
		        break;
	         case "s":
	        	 type = "Streaming";
	        	 ctx.getJDA().getPresence().setActivity(Activity.streaming(activity,null));
		        break;
	         default:
	        	 type = "Watching";
	        	 activity = "Bleach";
	        	 ctx.getJDA().getPresence().setActivity(Activity.watching("Bleach"));
			}
	

			EmbedBuilder usage = EmbedUtils.getDefaultEmbed()
					.setColor(Color.GREEN)
					.setTitle("Activity")
					.setDescription("Changed activity to: " + type +" "+ activity);
			channel.sendMessageEmbeds(usage.build()).queue();
			usage.clear();
		}
		
		
	}

	@Override
	public String getName() {
		return "activity";
	}

	@Override
	public String getHelp() {
		return "Changes bot activity\n"
				+ "Ussage: `"+Yoruichi.prefix+"activity [#type (w,c,l,p,s)][activity name]`\n"
				+ "Shortcuts for `[(Watching - w, Competing - c, Listening - l, Playing - p, Streaming - s)]`\n"
				+ "Aliases: `" + this.getAliases() + "`";

	}

	@Override
	public String getCategory() {
		return "Admin";
	}
	
	@Override
	public List<String> getAliases() {
		return List.of("act");
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
