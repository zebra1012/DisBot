package my_bot.command;

import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Guild_Commands extends ListenerAdapter {
	String[] goodbye_Messages = { "[member] 퇴장", "Bye [member]", "See you Again [member]" };

	String[] welcome_Messages = { "[member] 입장", "Welcome, [member]" };

	//@Override
	//public void onGuildMessageReceived(GuildMessageReceivedEvent event) { // 메시지 받으면 리액션 날려줌
	//	event.getMessage().addReaction("❤").queue();
	//}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) { // 퇴장하면 메시지 날려줌
		Random rand = new Random();
		int number = rand.nextInt(goodbye_Messages.length);

		EmbedBuilder leave = new EmbedBuilder();
		leave.setColor(0x66d8ff);
		leave.setDescription(goodbye_Messages[number].replace("[member]", event.getMember().getAsMention()));
		event.getGuild().getDefaultChannel().sendMessage(leave.build()).queue();
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) { // Guild 입장하면 메시지 날려줌
		Random rand = new Random();
		int number = rand.nextInt(welcome_Messages.length);

		EmbedBuilder join = new EmbedBuilder();
		join.setColor(0x66d8ff);
		join.setDescription(welcome_Messages[number].replace("[member]", event.getMember().getAsMention()));
		event.getGuild().getDefaultChannel().sendMessage(join.build()).queue();

		// Add role
		// event.getGuild().addRoleToMember(event.getMember(),
		// event.getGuild().getRolesByName("회원", true).get(0)).complete();
	}
}
//	@Override
//	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) { //리액션 취소기능
//		if(event.getReactionEmote().getName().equals("❤") && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
//			if(event.getMember().getUser().equals(event.getChannel().getHistory().getMessageById(event.getMessageId()).getAuthor())) {
//				//작성자
//				event.getChannel().getHistory().getMessageById(event.getMessageId()).delete().queue();
//			}else {
//				//아님
//				event.getReaction().removeReaction().queue();
//			}
//		}
//}
