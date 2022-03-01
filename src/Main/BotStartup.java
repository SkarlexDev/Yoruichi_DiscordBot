package Main;

public class BotStartup {

	public static void main(String[] args) {
		try {
			if (Yoruichi.INSTANCE == null) {
				new Yoruichi(args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
