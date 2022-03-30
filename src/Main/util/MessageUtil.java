package Main.util;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.entities.Message;

public class MessageUtil {

	private final static int delay = 10;
	private final static int delayLong = 60;

	public static void autoDeleteMessage(Message message) {
		if (message != null) {
			message.delete().queueAfter(delay, TimeUnit.SECONDS);
		}
	}

	public static void autoDeleteMessageLong(Message message) {
		if (message != null) {
			message.delete().queueAfter(delayLong, TimeUnit.SECONDS);
		}
	}


}
