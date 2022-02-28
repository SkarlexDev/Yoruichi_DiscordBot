package Main;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GameTracker extends ListenerAdapter{

	@Override
	public void onUserActivityStart(UserActivityStartEvent event) {
		
		Member target = event.getMember();
		TextChannel channel = event.getGuild().getTextChannelById("734036756495859752");
		String game = event.getNewActivity().getName().toLowerCase();
		
		if(game.contains("banned game")) {
			channel.sendMessage(target.getAsMention() + " Plays: " + game.toUpperCase()).queue();
			//event.getGuild().kick(target).queueAfter(25, TimeUnit.SECONDS);
		}
		
	}
}
