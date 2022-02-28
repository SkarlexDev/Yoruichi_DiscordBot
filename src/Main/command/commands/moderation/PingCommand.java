package Main.command.commands.moderation;

import Main.command.CommandContext;
import Main.command.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

public class PingCommand implements ICommand {
	private Boolean state;
	
    @Override
    public void handle(CommandContext ctx) {
    	final TextChannel channel = ctx.getChannel();

        if(!this.state) {
			ctx.getDisabled(channel);
			return;
		}
        
        JDA jda = ctx.getJDA();
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
	
	@Override
	public void showHelp(CommandContext ctx, TextChannel channel) {
		ctx.commandHelper(channel, this.getHelp() , this.getName().toUpperCase());
	}

}
