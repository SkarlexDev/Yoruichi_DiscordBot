package Main.command.commands.moderation;

import java.awt.Color;
import java.util.List;

import Main.command.AbstractCommand;
import Main.command.ICommand;
import Main.command.commands.CommandContext;
import Main.util.YEnvi;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;

public class ActivityCommand extends AbstractCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		int size = ctx.getArgs().size();
		final List<String> args = ctx.getArgs();

		if (!state) {
			ctx.getDisabled(channel);
			return;
		}

		if (!ctx.checkCmdPermission(ctx.getMember(), this.getName())) {
			ctx.getPermissionDenied(channel);
			return;
		}

		if (args.isEmpty() || size == 1) {
			this.showHelp(ctx, channel);
			return;
		}

		String activityType = ctx.getArgs().get(0).toLowerCase();
		String activity;
		String type;

		if (size >= 2) {
			if (size == 2) {
				activity = ctx.getArgs().get(1);
			} else {
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
				ctx.getJDA().getPresence().setActivity(Activity.streaming(activity, null));
				break;
			default:
				type = "Watching";
				activity = "Bleach";
				ctx.getJDA().getPresence().setActivity(Activity.watching("Bleach"));
			}


			EmbedBuilder usage = EmbedUtils.getDefaultEmbed().setColor(Color.GREEN).setTitle("Activity")
					.setDescription("Changed activity to: " + type + " " + activity);
			channel.sendMessageEmbeds(usage.build()).queue(message -> {
				ctx.clear(message);
			});
		}

	}

	@Override
	public String getName() {
		return "activity";
	}

	@Override
	public String getCategory() {
		return "Moderation";
	}

	@Override
	public String getHelp() {
		return "Changes bot activity\n" + "Usage: `" + YEnvi.prefix + "" + this.getName()
				+ " [#type (w,c,l,p,s)][activity name]`\n"
				+ "Shortcuts for `[(Watching - w, Competing - c, Listening - l, Playing - p, Streaming - s)]`\n"
				+ "Aliases: `" + this.getAliases() + "`";

	}

	@Override
	public List<String> getAliases() {
		return List.of("act");
	}

}
