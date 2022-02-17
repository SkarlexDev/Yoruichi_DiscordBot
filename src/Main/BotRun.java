package Main;

import javax.security.auth.login.LoginException;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class BotRun {

	String Token = System.getenv("TOKEN");
	public static String prefix = System.getenv("PREFIX");
	public static String owner = System.getenv("OWNER_ID");
	
	private static volatile BotRun instance;
    public static BotRun getInstance() throws LoginException {
        if (instance == null) {
            synchronized (BotRun .class) {
                if (instance == null) {
                    instance = new BotRun();
                }
            }
        }
        return instance;
    }    
    
    
	BotRun() throws LoginException{
		JDABuilder.createDefault(Token)				
		.setActivity(Activity.watching("Bleach"))
		.setStatus(OnlineStatus.ONLINE)
		.enableIntents(GatewayIntent.GUILD_PRESENCES)
		.enableCache(CacheFlag.ACTIVITY)
        .addEventListeners(new Listener())
        .addEventListeners(new ChatFilterListener())
        //.addEventListeners(new GameTracker()) // testing
        .build();		
		EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                        .setFooter("Created By Skarlex")
        );
				
	}
	
	
	
}
