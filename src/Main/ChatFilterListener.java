package Main;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChatFilterListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split(" ");
		int deltaTime = 60;
		StringBuilder fl = new StringBuilder();
		
		for (String arg : args) {
			fl.append(" ").append(arg);
		}

		if (fl.toString().toLowerCase().contains("se merita")) {

			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Se merita Tracker")
					.setThumbnail(event.getAuthor().getAvatarUrl())
					.addField("Victim", event.getAuthor().getName(), false)
					.addField("Input", fl.toString(), false)
					.addField("Muted", "60 sec", false)
					.setFooter("For help do nothing");
			
			/*
			 TODO: change mute to timeout			 
			 Temp mute
			 need support for 5.0.0-alpha.4 (Timeout)			 
			 */
			event.getChannel().sendMessageEmbeds(embed.build()).queue();
			event.getGuild().addRoleToMember(event.getAuthor().getId(), event.getGuild().getRoleById("734041934938505267")).queue();
			event.getGuild().removeRoleFromMember(event.getAuthor().getId(), event.getGuild().getRoleById("734041934938505267")).queueAfter(deltaTime, TimeUnit.SECONDS);
			event.getChannel().sendMessage(event.getAuthor().getAsMention() + " unmuted!").queueAfter(deltaTime + 1, TimeUnit.SECONDS);
			
		}
	}

}
