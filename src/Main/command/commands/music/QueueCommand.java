package Main.command.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import Main.command.CommandContext;
import Main.command.ICommand;
import Main.lavaplayer.GuildMusicManager;
import Main.lavaplayer.PlayerManager;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.TextChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueCommand implements ICommand {
    @SuppressWarnings("unused")
	@Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;
        
        
        String customerror = "``ERROR net.dv8tion.jda.api.JDA - One of the EventListeners had an uncaught exception\r\n" +
        		"at Main.command.commands.music.QueueCommand.handle(QueueCommand.java:31)\r\n" + 
        		"at Main.CommandManager.handle(CommandManager.java:78)\r\n" + 	
        		"at net.dv8tion.jda.internal.hooks.EventManagerProxy.handle(EventManagerProxy.java:70)\r\n" + 
        		"at net.dv8tion.jda.internal.JDAImpl.handleEvent(JDAImpl.java:160)``";
        
        
        channel.sendMessage(customerror).queue();
        channel.sendMessage(ctx.getGuild().getMemberById("606132708028186634").getAsMention()).queue();
        
        try {
    		TimeUnit.SECONDS.sleep(2);
    		ctx.getJDA().shutdown();
    		BotCommons.shutdown(ctx.getJDA());
    		System.exit(0);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
        
        
        /*
        if (queue.isEmpty()) {
            channel.sendMessage("The queue is currently empty").queue();
            return;
        }

        final int trackCount = Math.min(queue.size(), 20);
        final List<AudioTrack> trackList = new ArrayList<>(queue);
        final MessageAction messageAction = channel.sendMessage("**Current Queue:**\n");

        for (int i = 0; i <  trackCount; i++) {
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();

            messageAction.append('#')
                    .append(String.valueOf(i + 1))
                    .append(" `")
                    .append(String.valueOf(info.title))
                    .append(" by ")
                    .append(info.author)
                    .append("` [`")
                    .append(formatTime(track.getDuration()))
                    .append("`]\n");
        }

        if (trackList.size() > trackCount) {
            messageAction.append("And `")
                    .append(String.valueOf(trackList.size() - trackCount))
                    .append("` more...");
        }

        messageAction.queue();
        */
    }

    @SuppressWarnings("unused")
	private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return "shows the queued up songs";
    }
    
    @Override
	public String getCategory() {
		return "Music";
	}
}
