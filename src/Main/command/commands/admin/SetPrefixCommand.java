package Main.command.commands.admin;

import java.util.List;

import Main.MapByGuild;
import Main.Yoruichi;
import Main.command.CommandContext;
import Main.command.ICommand;
import Main.database.DatabaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetPrefixCommand implements ICommand{

	private Boolean state;
	
	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();
		
		
		if(!ctx.getMember().getId().equals(Yoruichi.owner)) {
			channel.sendMessage("This command is limited to owner").queue();
			return;
		}
		
		if(!ctx.checkRolePermision(ctx.getMember().getRoles())) {
			ctx.getPermisionDenied(channel);
			return;
		}		

		if (ctx.getArgs().isEmpty()) {
			this.showHelp(ctx, channel);
			return;
		}
		
		
		final String newPrefix = String.join("", args);
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix , this.getName());
        channel.sendMessageFormat("New prefix has been set to `%s`", newPrefix).queue();
		
	}
	
	private void updatePrefix(long guildId, String newPrefix, String command) {
        MapByGuild.PREFIXES.put(guildId, newPrefix);
        DatabaseManager.INSTANCE.setPrefix(guildId, newPrefix);
        Yoruichi.prefix = newPrefix;
    }

	@Override
	public String getName() {
		return "setprefix";
	}

	@Override
	public String getHelp() {
		return "Changes server command prefix\n"
				+ "Usage: `"  + Yoruichi.prefix + "" + this.getName() +  " <#newPrefix>`";

	}

	@Override
	public String getCategory() {
		return "Admin";
	}
	
	@Override
	public List<String> getAliases() {
		return List.of("set");
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
