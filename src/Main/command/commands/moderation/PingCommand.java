package Main.command.commands.moderation;

import Main.command.CommandContext;
import Main.command.ICommand;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {
	public Boolean state = true;
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        if(!this.state) {
			ctx.getChannel().sendMessage("This command is disabled!").queue();
			return;
		}
        
        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                .sendMessageFormat("Reset ping: %sms\nWS ping: %sms", ping, jda.getGatewayPing()).queue()
        );
    }

    @Override
    public String getHelp() {
        return "Shows the current ping from the bot to the discord servers";
    }

    @Override
    public String getName() {
        return "ping";
    }
    
    @Override
	public String getCategory() {
		return "Moderation";
	}
    @Override
	public void setState(Boolean state) {
		this.state = state;
		
	}

	@Override
	public Boolean getState() {
		return this.state;
	}

}
