package Main;

public class BotStartup {

	public static void main(String[] args) {
		try {
			if (Yoruichi.INSTANCE == null) {
				new Yoruichi();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
