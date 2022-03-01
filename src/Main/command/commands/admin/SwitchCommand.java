package Main.command.commands.admin;

import java.awt.Color;
import java.util.List;

import Main.CommandManager;
import Main.Yoruichi;
import Main.command.CommandContext;
import Main.command.ICommand;
import Main.database.DatabaseManager;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class SwitchCommand implements ICommand{
	private Boolean state;
	private final CommandManager manager;

	public SwitchCommand(CommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		List<String> args = ctx.getArgs();
		int size = ctx.getArgs().size();		
		
		if(!ctx.checkRolePermision(ctx.getMember().getRoles())) {
			ctx.getPermisionDenied(channel);
			return;
		}		
		
		if (args.isEmpty()) {
			
			StringBuilder Fun = new StringBuilder();
			StringBuilder Moderation = new StringBuilder();
			StringBuilder Music = new StringBuilder();
			StringBuilder Games = new StringBuilder();
			StringBuilder Admin = new StringBuilder();
			StringBuilder Testing = new StringBuilder();

			List<ICommand> allcommands = manager.getCommands();
			
			for (ICommand cmd : allcommands) 
			{ 
				String it = cmd.getName();
				Boolean enabled = cmd.getState();
				String status = enabled==true ? "Enabled" : "Disabled";
								
				
			    if (cmd.getCategory().equalsIgnoreCase("Fun")) {
			    	Fun.append('`').append(it).append(": " + status).append("` \n");
				}
			    if (cmd.getCategory().equalsIgnoreCase("Moderation")) {
					Moderation.append('`').append(it).append(": " + status).append("` \n");
				}
				if (cmd.getCategory().equalsIgnoreCase("Music")) {
					Music.append('`').append(it).append(": " + status).append("` \n");
				}
				if (cmd.getCategory().equalsIgnoreCase("Games")) {
					Games.append('`').append(it).append(": " + status).append("` \n");
				}
				if (cmd.getCategory().equalsIgnoreCase("Admin")) {
					Admin.append('`').append(it).append(": " + status).append("` \n");
				}
				if (cmd.getCategory().equalsIgnoreCase("Testing")) {
					Testing.append('`').append(it).append(": " + status).append("` \n");
				}
			}
			
			EmbedBuilder info = EmbedUtils.getDefaultEmbed().
					setColor(Color.GREEN).
					setAuthor("Command Manager", null, ctx.getSelfUser().getAvatarUrl())
					.addField("Admin", Admin.toString(), true)
					.addField("Music", Music.toString(), true)
					.addField("Fun", Fun.toString(), true)
					.addField("Moderation", Moderation.toString(), true)
					.addField("Games", Games.toString(), true);
				if(Testing.length()>0) {
					info.addField("Testing", Testing.toString(),false);
				}else {
					info.addBlankField(true);
				}

			channel.sendMessageEmbeds(info.build()).queue();
			return;
		}
		
		if(size == 1) {
			String search = args.get(0);
			ICommand command = manager.getCommand(search);

			if (command == null) {
				channel.sendMessage("Nothing found for " + search).queue();
				return;
			}

			command.setState(!command.getState());
			String status = command.getState()==true ? "Enabled" : "Disabled";
			if(command.getName().equals(this.getName()) || command.getName().equals("help")) {
				channel.sendMessage("Sadly you cannot disable this command!").queue();
			}else {
				DatabaseManager.INSTANCE.setState(command.getName(),command.getState());
				channel.sendMessage("Command is now: " + status).queue();			
			}			
		}	
	}

	@Override
	public String getName() {
		return "switch";
	}

	@Override
	public String getHelp() {
		return "Disables/Enables commands\n"
				+ "Usage: `"  + Yoruichi.prefix + "" + this.getName() +  " [command name/alias]`";
	}

	@Override
	public String getCategory() {
		return "Admin";
	}
	
	@Override
	public void setState(Boolean state){
		this.state = state;
	}

	@Override
	public Boolean getState() {
		return this.state;
	}
	
	@Override
	public List<String> getAliases() {
		return List.of("sw");
	}
	
	@Override
	public void showHelp(CommandContext ctx, TextChannel channel) {
		ctx.commandHelper(channel, this.getHelp() , this.getName().toUpperCase());
	}

}
