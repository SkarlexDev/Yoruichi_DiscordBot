package Main.command;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import me.duncte123.botcommons.commands.ICommandContext;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandContext implements ICommandContext {
    private final GuildMessageReceivedEvent event;
    private final List<String> args;
    private final List<Role> permision;
    

    public CommandContext(GuildMessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.args = args;
		this.permision = Arrays.asList(
				event.getGuild().getRoleById(702755992689180712L), // owner
				event.getGuild().getRoleById(827941362539429900L), // Co-Founder
				event.getGuild().getRoleById(784067629211189249L) // Super Admin
				);      
    }

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    public List<String> getArgs() {
        return this.args;
    }   

    public void getDisabled(TextChannel channel) {
    	channel.sendMessage("This command is disabled!").queue();
    }
    
    public void commandHelper(TextChannel channel, String getHelp, String name) {
    	EmbedBuilder usage = EmbedUtils.getDefaultEmbed()
				.setColor(Color.YELLOW)
				.setTitle("Command Helper - " + name)
				.setDescription(getHelp);
		channel.sendMessageEmbeds(usage.build()).queue();
    }
    
	public Boolean checkRolePermision(List<Role> roles) {
		return CollectionUtils.containsAny(roles,permision);
	}
	
	public void getPermisionDenied(TextChannel channel) {
    	channel.sendMessage("You are not allowed to use this command!").queue();
    }
	
}