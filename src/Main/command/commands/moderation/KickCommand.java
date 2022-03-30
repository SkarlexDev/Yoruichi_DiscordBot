package Main.command.commands.moderation;


import java.util.List;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.util.YEnvi;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class KickCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		int size = ctx.getArgs().size();
		final List<String> args = ctx.getArgs();
		final Message msg = ctx.getMessage();
		final Member member = ctx.getMember();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}
		
		if (!ctx.checkCmdPermission(ctx.getMember(), this.getName())) {
			ctx.getPermissionDenied(channel);
			return;
		}

		if (size < 2 || msg.getMentionedMembers().isEmpty()) {
			this.showHelp(ctx, channel);
			return;
		}

		final Member target = msg.getMentionedMembers().get(0);

		if (!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)) {
			channel.sendMessage("You are missing permission to kick this member").queue(message->{
				ctx.clear(message);
			});
			return;
		}

		final Member selfMember = ctx.getSelfMember();

		if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
			channel.sendMessage("I am missing permissions to kick that member").queue(message->{
				ctx.clear(message);
			});
			return;
		}

		final String reason = String.join(" ", args.subList(1, args.size()));

		ctx.getGuild().kick(target, reason).reason(reason).queue(
				(__) -> channel.sendMessage("Kick was successful").queue(),
				(error) -> channel.sendMessageFormat("Could not kick %s", error.getMessage()).queue(message->{
					ctx.clear(message);
				}));
	}

	@Override
	public String getName() {
		return "kick";
	}

	@Override
	public String getCategory() {
		return "Moderation";
	}

	@Override
	public String getHelp() {
		return "Kick a member off the server.\n" + "Usage: `" + YEnvi.prefix + "" + this.getName()
				+ " <@user> <reason>`";
	}

}
