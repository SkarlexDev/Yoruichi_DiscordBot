package Main.command.commands.moderation;

import java.awt.Color;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import Main.BotRun;
import Main.CommandManager;
import Main.command.CommandContext;
import Main.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class SwitchCommand implements ICommand{
	public final Boolean state = true;

	private final CommandManager manager;


	public SwitchCommand(CommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		List<String> args = ctx.getArgs();
		int size = ctx.getArgs().size();
		if (args.isEmpty()) {
			
			StringBuilder Fun = new StringBuilder();
			StringBuilder Moderation = new StringBuilder();
			StringBuilder Music = new StringBuilder();
			StringBuilder Games = new StringBuilder();

			List<ICommand> allcommands = manager.getCommands();
			
			for (ICommand cmd : allcommands) 
			{ 
				String it = cmd.getName();
				Boolean enabled = cmd.getState();
				//System.out.println(it + " " + enabled);
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
			}
			
			EmbedBuilder info = EmbedUtils.getDefaultEmbed().
					setColor(Color.GREEN).
					setAuthor("Command Manager", null, ctx.getSelfUser().getAvatarUrl())
					.addField("Fun", Fun.toString(), false)
					.addField("Moderation", Moderation.toString(), false)
					.addField("Music", Music.toString(), false)
					.addField("Games", Games.toString(), false);

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

			try {
				command.setState(!command.getState());
				String status = command.getState()==true ? "Enabled" : "Disabled";
				channel.sendMessage("Command is now: " + status).queue();
			} catch (Exception e) {
				channel.sendMessage("Sadly you cannot disable this command!").queue();
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
				+ "Ussage: `" + BotRun.prefix + "switch [command name/alias]`";
	}

	@Override
	public String getCategory() {
		return "Moderation";
	}
	
	@Override
	public void setState(Boolean state) throws Exception {
		throw new InvalidAttributesException();
	}

	@Override
	public Boolean getState() {
		return this.state;
	}
	
	@Override
	public List<String> getAliases() {
		return List.of("s","sw");
	}

}
