package Main.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import Main.command.ICommand;
import Main.command.commands.admin.SetPrefixCommand;
import Main.command.commands.admin.SwitchCommand;
import Main.command.commands.fun.AnimalCommand;
import Main.command.commands.fun.BannerCommand;
import Main.command.commands.fun.BoredCommand;
import Main.command.commands.fun.FakeCommand;
import Main.command.commands.fun.JokeCommand;
import Main.command.commands.fun.MemeCommand;
import Main.command.commands.games.RioCommand;
import Main.command.commands.misc.HelpCommand;
import Main.command.commands.misc.PingCommand;
import Main.command.commands.moderation.ActivityCommand;
import Main.command.commands.moderation.ClearCommand;
import Main.command.commands.moderation.KickCommand;
import Main.command.commands.music.JoinCommand;
import Main.command.commands.music.LeaveCommand;
import Main.command.commands.music.PauseResumeCommand;
import Main.command.commands.music.PlayCommand;
import Main.command.commands.music.QueueCommand;
import Main.command.commands.music.RepeatCommand;
import Main.command.commands.music.SkipCommand;
import Main.command.commands.music.StopCommand;
import Main.command.commands.music.VolumeCommand;
import Main.command.commands.test.SayCommand;
import Main.database.DatabaseManager;
import Main.database.MapByGuild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandManager {
	private final List<ICommand> commands = new ArrayList<>();

	public CommandManager() {

		// misc
		addCommand(new PingCommand());
		addCommand(new HelpCommand(this));
		// fun
		addCommand(new JokeCommand());
		addCommand(new MemeCommand());
		addCommand(new AnimalCommand());
		addCommand(new BannerCommand());
		addCommand(new FakeCommand());
		addCommand(new BoredCommand());
		// moderation
		addCommand(new KickCommand());
		addCommand(new ClearCommand());
		addCommand(new ActivityCommand());
		// admin
		addCommand(new SetPrefixCommand());
		addCommand(new SwitchCommand(this));

		// music
		addCommand(new JoinCommand());
		addCommand(new LeaveCommand());
		addCommand(new PlayCommand());
		addCommand(new StopCommand());
		addCommand(new SkipCommand());
		addCommand(new QueueCommand());
		addCommand(new VolumeCommand());
		addCommand(new RepeatCommand());
		addCommand(new PauseResumeCommand());

		// games
		addCommand(new RioCommand());
		// testing
		addCommand(new SayCommand());

	}

	private void addCommand(ICommand cmd) {
		try {
			boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));
			cmd.setState(MapByGuild.COMMANDS.computeIfAbsent(cmd.getName(), DatabaseManager.INSTANCE::getState));

			if (nameFound) {
				throw new IllegalArgumentException("A command with this name is already present");
			}
			// if there is any error change SQLiteDataSource -> config.setMaximumPoolSize(x)
			// value
			commands.add(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ICommand> getCommands() {
		return commands;
	}

	@Nullable
	public ICommand getCommand(String search) {
		String searchLower = search.toLowerCase();

		for (ICommand cmd : this.commands) {
			if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
				return cmd;
			}
		}

		return null;
	}

	public void handle(GuildMessageReceivedEvent event, String prefix) {
		String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(prefix), "")
				.split("\\s+");

		String invoke = split[0].toLowerCase();

		ICommand cmd = this.getCommand(invoke);

		if (cmd != null) {
			List<String> args = Arrays.asList(split).subList(1, split.length);

			CommandContext ctx = new CommandContext(event, args);
			cmd.handle(ctx);
		}
	}

}
