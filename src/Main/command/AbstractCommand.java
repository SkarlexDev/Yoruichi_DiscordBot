package Main.command;

public abstract class AbstractCommand {

	public Boolean state;
	
	public Boolean getState() {
		return this.state;
	};
	
	public void setState(Boolean state) {
		this.state = state;
	}

}
