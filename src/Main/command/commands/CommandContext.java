package Main.command.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Main.util.MessageUtil;
import Main.util.YEnvi;
import me.duncte123.botcommons.commands.ICommandContext;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandContext implements ICommandContext {
	private final GuildMessageReceivedEvent event;
	private final List<String> args;
	private final List<String> permissions;

	public CommandContext(GuildMessageReceivedEvent event, List<String> args) {
		this.event = event;
		this.args = args;
		this.permissions = Arrays.asList("Owner", "Co-Founder", "SuperAdmin");

		try {
			clear(event.getMessage());
		} catch (Exception e) {
			// msg already deleted
		}
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
		channel.sendMessage("This command is disabled!").queue(message -> {
			clear(message);
		});
	}

	// help handler
	public void commandHelper(TextChannel channel, String getHelp, String name) {
		EmbedBuilder usage = EmbedUtils.getDefaultEmbed().setColor(Color.YELLOW).setTitle("Command Helper - " + name)
				.setDescription(getHelp);
		channel.sendMessageEmbeds(usage.build()).queue(message -> {
			clearLong(message);
		});
	}

	// Permission verification
	public Boolean checkCmdPermission(Member member, String name) {
		// creator permission
		if(member.getId().equals(YEnvi.owner)){
			return true;
		}
		if (member.isOwner()) {
			return true;
		}
		System.out.println(permissions);

		List<Role> memberRoles = new ArrayList<Role>(member.getRoles());
		if (memberRoles.stream().filter((r) -> this.checkValue(r)).anyMatch(r -> true)) {
			System.out.println("permission granted");
			return true;
		} else {
			System.out.println("permission denied");
			return false;
		}

	}

	public Boolean checkValue(Role r) {
		for (String perm : permissions) {
			if (r.getName().replaceAll(" ", "").equalsIgnoreCase(perm)) {
				return true;
			}
		}
		return false;
	}

	// reject Permission msg
	public void getPermissionDenied(TextChannel channel) {
		channel.sendMessage("You are not allowed to use this command!").queue(message -> {
			clear(message);
		});
	}

	// delete msg after seconds
	public void clear(Message message) {
		MessageUtil.autoDeleteMessage(message);
	}

	public void clearLong(Message message) {
		MessageUtil.autoDeleteMessageLong(message);
	}

}
