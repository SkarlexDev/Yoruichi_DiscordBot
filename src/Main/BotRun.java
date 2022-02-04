package Main;

import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class BotRun {

	String Token = System.getenv("TOKEN");
	public static String prefix = System.getenv("PREFIX");
	public static String owner = System.getenv("OWNER_ID");
	
	BotRun() throws LoginException, SQLException{
		JDABuilder.createDefault(Token)				
		.setActivity(Activity.watching("Bleach"))
		.setStatus(OnlineStatus.ONLINE)
        .addEventListeners(new Listener())
        .build();	
	}
	
}
