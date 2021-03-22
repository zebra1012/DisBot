package my_bot;

import javax.security.auth.login.LoginException;

import my_bot.command.Common_Commands;
import my_bot.command.Guild_Commands;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;



public class Bot {
	public static String prefix="!";
	public static void main(String[] args) throws LoginException {
		JDABuilder builder = JDABuilder.createDefault(args[0]);
		builder.setStatus(OnlineStatus.ONLINE);
		builder.setActivity(Activity.playing("!¸í·É"));
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		
		builder.addEventListeners(new Common_Commands());
		builder.addEventListeners(new Guild_Commands());
		
		builder.build();

	}

}
