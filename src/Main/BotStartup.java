package Main;

import java.sql.SQLException;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;


public class BotStartup {
	
	String Token = System.getenv("TOKEN");
	public static String prefix = System.getenv("PREFIX");
	public static String owner = System.getenv("OWNER_ID");
	
	private BotStartup() throws LoginException, SQLException{
	
		
		JDABuilder.createDefault(Token)				
				.setActivity(Activity.watching("Bleach"))
				.setStatus(OnlineStatus.ONLINE)
	            .addEventListeners(new Listener())
	            .build();	
	}	

	public static void main(String[] args) throws LoginException, SQLException{
		try {
			new BotStartup();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
