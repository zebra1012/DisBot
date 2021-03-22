package my_bot.command;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import my_bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class Common_Commands  extends ListenerAdapter   {
	AudioPlayerManager manager;
	AudioPlayer player;
	TrackScheduler TS;
	public void onMessageReceived(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+",2);
		Guild guild = event.getGuild();
		if(args[0].equalsIgnoreCase(Bot.prefix+"정보")) { //정보뿌리기
			EmbedBuilder info = new EmbedBuilder();
			info.setTitle("(●'◡'●) 미도리봇");
			info.setColor(0xf45642);
			info.setFooter("고마워 미도리..");
			event.getChannel().sendMessage(info.build()).queue();
			info.clear();
		}
		if(args[0].equalsIgnoreCase(Bot.prefix+"청소")) { //채팅 삭제
				try {
					List<Message> msgs = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])).complete();
					for(Message msg: msgs ) {
						event.getChannel().deleteMessageById(msg.getId()).queue();
					}
					//EmbedBuilder success = new EmbedBuilder();
					//success.setColor(0xff3923);
					//success.setTitle("삭제 완료");
					//event.getChannel().sendMessage(success.build()).queue();;
				}catch(Exception e){
						EmbedBuilder error = new EmbedBuilder();
						InputStream is =null;
						try {
						is = new URL("https://w.wallhaven.cc/full/wq/wallhaven-wqedm7.jpg").openStream();
						} catch(Exception E) {}
						error.setColor(0xff3923);
						error.setImage("attachment://image.jpg").setDescription("일각실각데차앗!");
						event.getChannel().sendFile(is,"image.jpg").embed(error.build()).queue();
				}
		}
		if(args[0].equalsIgnoreCase(Bot.prefix+"명령")) {
			EmbedBuilder commandList = new EmbedBuilder();
			commandList.setDescription("!명령 명령어목록\n\n"
					+ "!정보 미도리봇정보\n\n!청소 # #만큼 메시지 정리\n\n!재생 # #재생\n\n !다음곡 플레이리스트 다음곡 재생 \n\n !알람초 # #초 후 알람 메시지 발송\n\n"+
					"!알람분 # #분 후 알람 메시지 발송");
			event.getChannel().sendMessage(commandList.build()).queue();
		}
		if(args[0].equalsIgnoreCase(Bot.prefix+"접속")){
			List<VoiceChannel> list =event.getGuild().getVoiceChannels();
			VoiceChannel vc= event.getGuild().getVoiceChannelsByName("General", true).get(0);
			AudioManager am  = event.getGuild().getAudioManager();
			am.openAudioConnection(vc);
		}
		if(args[0].equalsIgnoreCase(Bot.prefix+"나가")) {
			event.getJDA().getAudioManagers().get(0).closeAudioConnection();
		}
		if(args[0].equalsIgnoreCase(Bot.prefix+"재생")) {
			if(manager==null) {
			manager = new DefaultAudioPlayerManager(); //오디오매니저
			AudioSourceManagers.registerLocalSource(manager); // 로컬파일
			AudioSourceManagers.registerRemoteSources(manager); //URI
			player = manager.createPlayer();
			
			AudioManager audiomanager =guild.getAudioManager();
			audiomanager.setSendingHandler(new AudioPlayerSendHandler(player));
			VoiceChannel vc = guild.getVoiceChannelsByName("General", true).get(0);
			audiomanager.openAudioConnection(vc);
			TS = new TrackScheduler(player);
			player.addListener(TS);
			}
			
			manager.loadItem(args[1], new AudioLoadResultHandler() {
				
				@Override
				public void trackLoaded(AudioTrack track) {
					guild.getTextChannels().get(0).sendMessage("큐에 추가됨 "+track.getInfo().title).queue();
					TS.queue(track);
					//player.startTrack(track, true);
					
				}
				
				@Override
				public void playlistLoaded(AudioPlaylist playlist) {
					AudioTrack AT = playlist.getSelectedTrack();
					if(AT==null) {
						AT = playlist.getTracks().get(0);
					}
					guild.getTextChannels().get(0).sendMessage("다음 곡 " + AT.getInfo().title).queue();
					player.startTrack(AT, true);
				}
				
				@Override
				public void noMatches() {
				}
				
				@Override
				public void loadFailed(FriendlyException exception) {
					
				}
			});
		}
		if(args[0].equalsIgnoreCase(Bot.prefix+"다음곡")) {
			if(TS==null) {
				EmbedBuilder info = new EmbedBuilder();
				info.setDescription("다음곡이 없는데스우...");
				guild.getTextChannels().get(0).sendMessage(info.build()).queue();
				
			}
			else {
				TS.nextTrack();
			}
		}
		if(args[0].equalsIgnoreCase(Bot.prefix+"초알람")) {
			long time = Long.parseLong(args[1]);
			guild.getTextChannels().get(0).sendMessage(time +"초가 지난데스").queueAfter(time, TimeUnit.SECONDS);
		}
		if(args[0].equalsIgnoreCase(Bot.prefix+"분알람")) {
			long time = Long.parseLong(args[1]);
			guild.getTextChannels().get(0).sendMessage(time +"분이 지난데스").queueAfter(time, TimeUnit.MINUTES);
		}
		if(args[0].equals(Bot.prefix+"원시마마")) {
			InputStream IS=null;
			try {
				URL url = new URL("https://www.dogdrip.net/dvs/d/20/05/28/e601f984de4fa	13798a92f1c6a09d975.png");
				IS = url.openStream();
			}catch (Exception E) {
			}
			event.getTextChannel().sendFile(IS, "마마.jpg").queue();
			
		}
	}

}
