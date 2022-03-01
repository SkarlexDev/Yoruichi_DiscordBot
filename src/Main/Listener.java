package Main;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Main.database.DatabaseManager;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
	private final CommandManager manager = new CommandManager();
	String pref = "";

	@Override
	public void onReady(@Nonnull ReadyEvent event) {
		if(pref.isEmpty()) {
			
		}
		LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
	}

	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		User user = event.getAuthor();

		if (user.isBot() || event.isWebhookMessage()) {
			return;
		}

		final long guildId = event.getGuild().getIdLong();
		// some delay on first use
		final String prefix = MapByGuild.PREFIXES.computeIfAbsent(guildId, DatabaseManager.INSTANCE::getPrefix);
		if(Yoruichi.prefix==null) {
			Yoruichi.prefix = prefix;
		}
		String raw = event.getMessage().getContentRaw();

		if (raw.equalsIgnoreCase(prefix + "shutdown") && user.getId().equals(Yoruichi.owner)) {

			event.getChannel().sendMessage("Shutting down!").queue();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			LOGGER.info("Shutting down");
			System.err.println("Shutting down!");
			event.getJDA().shutdown();
			BotCommons.shutdown(event.getJDA());
			System.exit(0);

		}

		if (raw.startsWith(prefix)) {
			manager.handle(event, prefix);
		}
	}

}
