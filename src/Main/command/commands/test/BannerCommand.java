package Main.command.commands.test;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import Main.Yoruichi;
import Main.command.CommandContext;
import Main.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class BannerCommand implements ICommand {

	private Boolean state;

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		int size = ctx.getArgs().size();
		final List<String> args = ctx.getArgs();
		
		if (!this.state) {
			ctx.getDisabled(channel);
			return;
		}
		
		int imgSize = 900; //900
		Member user = ctx.getMember();
		URL bannerimg = null;
		
		if(size==1 || size==2 || size==3) {
			try {
				int sizeV = 0;
				
				for(int i=0; i<=2; i++) {
					if(this.argcheck(args.get(i))) {
						sizeV = Integer.parseInt(args.get(i));
						break;
					}
				}			
				
				if(sizeV<500  || sizeV>1000) {
					this.showHelp(ctx, channel);
					return;
				}else {
					imgSize = sizeV;
				}
				
			} catch (Exception e) {
				imgSize = 900;
			}
			try {
				user = ctx.getMessage().getMentionedMembers().get(0);
			} catch (Exception e) {
				
			}
			try {
				for(int i=0; i<=2; i++) {
					//TODO get upload file with disco url = https://cdn.discordapp.com/attachments/
					if(args.get(i).toString().length()>10 && (args.get(i).toString().contains("https://") || 
															  args.get(i).toString().contains("http://"))){
						bannerimg =	new URL(args.get(i).toString());
						break;
				}

				}
			} catch (Exception e) {
			}
			
		}
		
		URL url = null;
		try {
			url = new URL(user.getEffectiveAvatarUrl() + "?size=1024");
		} catch (MalformedURLException e1) {
			channel.sendMessage("error on base url").queue();
			return;
		}		
		ImageIO.setUseCache(false);

		try {
			
			if(bannerimg==null) { 
				bannerimg = new URL("https://i.imgur.com/rSIlN1t.png");
			}		
			
			BufferedImage testBanner = ImageIO.read(bannerimg);
			if(testBanner.getWidth()<800 || testBanner.getHeight() < 350) {
				channel.sendMessage("Sorry but your banner must be at last 800*350").queue();
				return;
			}
			
			BufferedImage banner = bannerSize(url, testBanner);
			BufferedImage circleBuffer = this.crop(url, imgSize);
			Graphics2D g2d = banner.createGraphics();
			g2d.drawImage(circleBuffer, 0, 150, null);	//-90 40	
			
			this.populateinfo(g2d,user);		

			byte[] profile = this.buffToByte(banner);
			this.sendFinalMsg(profile,channel);		

		} catch (Exception e) {
			channel.sendMessage("There is a problem with url").queue();
			return;
		}

	}

	@Override
	public String getName() {
		return "banner";
	}

	@Override
	public String getHelp() {
		return "Generate banner for user\n"
				+ "Default avatar size 900*900'\n"
				+ "Usage `" + Yoruichi.prefix + "" + this.getName() +  " (optional)[#size (500-1000)] / [@tag user] / [image url]`\n" 
				+ "Order of arg 1 & 2 & 3 doesn't matter or if there is only 1 arg, 2 args or none\n"
				+ "Aliases : `" + this.getAliases() + "`";
	}
	
	@Override
	public List<String> getAliases() {
		return List.of("b","ba","avatar");
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

	
	public boolean argcheck(String arg) {
		try {
			Integer.parseInt(arg);
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	
	public BufferedImage bannerSize(URL url, BufferedImage bg) throws IOException {
		int w = 1400; //1000
		int h = 720 ; //485
		BufferedImage banner = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = banner.createGraphics();
		g2.drawImage(bg, 0, 0,  w, h, null);
		return banner;
	}

	public BufferedImage crop(URL url, int imgSize) throws IOException {
		int width = imgSize;
		BufferedImage avatar = ImageIO.read(url);
		BufferedImage circleBuffer = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = circleBuffer.createGraphics();
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setClip(new RoundRectangle2D.Float(-100, 0, width, width, width, width));
		g2.drawImage(avatar, 120, 0, width/2, width/2, null);
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.dispose();	
		
		return circleBuffer;
	}
	
	public byte[] buffToByte(BufferedImage banner) throws IOException {
		// convert bufferedimage to bytes[]
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(banner, "png", baos);
		byte[] profile = baos.toByteArray();
		return profile;
	}
	
	public void sendFinalMsg(byte[] profile, TextChannel channel) {
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
		embed.setImage("attachment://img.png") // we specify this in sendFile as "*.png"
				.setTitle("Banner generator");
		channel.sendFile(profile, "img.png").setEmbeds(embed.build()).queue();
	}
	
	public Graphics2D populateinfo(Graphics2D g2d,Member user) {
		
		int y = 200; // border poz y (don't change) //50
		int x = 700; // border poz x // 525
		int y2 = 255; // text poz y (don't change) //100
		int x2 = 770; // text poz x //550
		
		List<String> details = new ArrayList<String>(this.filldata(user));		
		float a = 0.8f;
		g2d.setPaint(new Color(1, .3f, .7f, a)); 
		g2d.setFont(new Font("Uni Sans Heavy", Font.PLAIN, 40)); // 40

		for (int i = 0; i< 4; i++) {
			
			//g2d.fill(new Rectangle2D.Float(x, y, 500, 80)); // 450 70			
			g2d.fillRoundRect(x, y, 500, 80, 50, 100); // 450 70
			
			g2d.setPaint(new Color(1, 1, 1, 1f)); // text color
			g2d.drawString(details.get(i), x2, y2);
			
			g2d.setPaint(new Color(1, 0.5f, .8f, a)); // frame color
			y+=110;
			y2+=110;
		}

		return g2d;
	}
	
	public List<String> filldata(Member user){
		List<String> data = new ArrayList<String>();
		data.add("Name: " + user.getEffectiveName());
		data.add("Created: " + user.getTimeCreated().toString().substring(0,10));
		data.add("Joined: " + user.getTimeJoined().toString().substring(0,10));
		data.add("Role: " + user.getRoles().get(0).getName());
		
		return data;
	}
	
}
