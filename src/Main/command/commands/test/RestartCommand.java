package Main.command.commands.test;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import com.fasterxml.jackson.databind.JsonNode;

import Main.BotStartup;
import Main.Yoruichi;
import Main.command.CommandContext;
import Main.command.ICommand;
import Main.database.DatabaseManager;
import Main.music.lavaplayer.AudioPlayerSendHandler;
import Main.music.lavaplayer.GuildMusicManager;
import Main.music.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;

public class RestartCommand implements ICommand {
	private Boolean state;

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();

		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}
		
		if(!ctx.getMember().getId().equals(Yoruichi.owner)) {
			channel.sendMessage("This command is limited to owner").queue();
			return;
		}
		
		if(!ctx.checkRolePermision(ctx.getMember().getRoles())) {
			ctx.getPermisionDenied(channel);
			return;
		}		
		
		try {
			this.restart(ctx, channel);
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getName() {
		return "restart";
	}

	@Override
	public String getHelp() {
		return "Restarts the bot\n"
				+ "Usage: `"  + Yoruichi.prefix + "" + this.getName() + "`";
	}

	@Override
	public String getCategory() {
		return "Testing";
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
		ctx.commandHelper(channel, this.getHelp(), this.getName().toUpperCase());
	}

	public String qM(JsonNode name) {
		return name.toPrettyString().replaceAll("\"", "");
	}

	@SuppressWarnings("static-access")
	public void restart(CommandContext ctx, TextChannel channel) throws LoginException, SQLException, InterruptedException, IOException {
		if (Yoruichi.INSTANCE != null) {
			channel.sendMessage("Bot restarting in: 10 seconds").queue();
			StringBuilder cmd = new StringBuilder();
	        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
	        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
	            cmd.append(jvmArg + " ");
	        }
	        cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
	        cmd.append(BotStartup.class.getName()).append(" ");
	        for (String arg : Yoruichi.INSTANCE.args()) {
	            cmd.append(arg).append(" ");
	        }
	        Thread.currentThread().sleep(1000); // 10 seconds delay before restart
	        Runtime.getRuntime().exec(cmd.toString());
	        System.exit(0);

		}
	}

}
