package Main;

import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import Main.listeners.Listener;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Yoruichi {

	public static void main(String[] args) throws LoginException, SQLException {
		new Yoruichi(args);

	}

	public static Yoruichi INSTANCE;

	String Token = System.getenv("TOKEN");
	public static String owner = System.getenv("OWNER_ID");

	public Yoruichi(String[] args) throws LoginException, SQLException {
		INSTANCE = this;
		JDABuilder.createDefault(Token).setActivity(Activity.watching("Bleach")).setStatus(OnlineStatus.ONLINE)
				.enableIntents(GatewayIntent.GUILD_PRESENCES)
				.enableCache(CacheFlag.ACTIVITY)
				.enableIntents(GatewayIntent.GUILD_MEMBERS)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.addEventListeners(new Listener())
				// .addEventListeners(new ChatFilterListener())
				// .addEventListeners(new GameTracker()) // testing
				.build();

		EmbedUtils.setEmbedBuilder(() -> new EmbedBuilder().setFooter("Created By Skarlex"));

	}

}
