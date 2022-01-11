package Main;

import java.sql.SQLException;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;


public class BotStartup {
	
	private BotStartup() throws LoginException, SQLException{
	
		JDABuilder.createDefault(Config.get("TOKEN"))				
				.setActivity(Activity.watching("Bleach"))
				.setStatus(OnlineStatus.ONLINE)
	            .addEventListeners(new Listener())
	            .build();
		
	}	

	public static void main(String[] args) throws LoginException, SQLException{
		new BotStartup();
		/*
		try {
			new BotStartup();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

}
