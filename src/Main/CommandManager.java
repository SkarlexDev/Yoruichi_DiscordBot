package Main;

import javax.annotation.Nullable;

import Main.command.CommandContext;
import Main.command.ICommand;
import Main.command.commands.fun.*;
import Main.command.commands.games.RioCommand;
import Main.command.commands.moderation.*;
import Main.command.commands.music.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        // fun 
        addCommand(new JokeCommand());
        addCommand(new MemeCommand());
        addCommand(new AnimalCommand());
        // moderation 
        addCommand(new KickCommand());
        addCommand(new ClearCommand());
        addCommand(new ActivityCommand());
        addCommand(new SwitchCommand(this));
        // music
        addCommand(new JoinCommand());
        addCommand(new LeaveCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new SkipCommand());
        addCommand(new QueueCommand());
        addCommand(new VolumeCommand());
        
        //other
        addCommand(new RioCommand());
        
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
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

    void handle(GuildMessageReceivedEvent event, String prefix) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
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