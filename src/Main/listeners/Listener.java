package Main.listeners;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Main.command.commands.CommandManager;
import Main.database.DatabaseManager;
import Main.database.MapByGuild;
import Main.util.YEnvi;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
	private final CommandManager manager = new CommandManager();;
	private String test = "";

	@Override
	public void onReady(@Nonnull ReadyEvent event) {
		if (test.isEmpty()) {

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

		final String prefix = MapByGuild.GUILD.computeIfAbsent(guildId, DatabaseManager.INSTANCE::getPrefix);

		if (YEnvi.prefix == null) {
			YEnvi.prefix = prefix;
		}

		String raw = event.getMessage().getContentRaw();

		if (raw.equalsIgnoreCase(prefix + "shutdown") && user.getId().equals(YEnvi.owner)) {

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
