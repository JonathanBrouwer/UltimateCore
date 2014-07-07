package Bammerbom.UltimateCore.Commands;

import java.io.File;
import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdReport {
	HashMap<Integer, Report> reports = new HashMap<Integer, Report>();
	public CmdReport(){
	}
	File file = UltimateFileLoader.DFreports;
	public static void handle(CommandSender sender, String[] args){
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.default1 + "/report <Player> <Message> - " + r.default2 + "Report someone");
			sender.sendMessage(r.default1 + "/report view <ID> - " + r.default2 + "Views a report");
			sender.sendMessage(r.default1 + "/report list - " + r.default2 + "View a list of reports");
			sender.sendMessage(r.default1 + "/report finish <ID> - " + r.default2 + "Mark a report as finished");
			sender.sendMessage(r.default1 + "/report delete/remove <ID> - " + r.default2 + "Removed a report");
			return;
		}
		if(args[0].equalsIgnoreCase("view")){
			
		}else if(args[0].equalsIgnoreCase("list")){
			
		}else if(args[0].equalsIgnoreCase("finish")){
			
		}else{
			//String reported = args[0];
			//String reason = r.getFinalArg(args, 1);
		}
	}
}
class Report{
	Integer id;
	Player reporter;
	Player reported;
	String reason;
	Long time;
	public Report(Integer id, Long time, Player reporter, Player reported, String reason){
		this.id = id;
		this.reporter = reporter;
		this.reported = reported;
		this.reason = reason;
		this.time = time;
	}
	public void register(){
		
	}
	public static void load(Integer id){
		
	}
	public Player getReporter(){
		return reporter;
	}
	public Player getReported(){
		return reported;
	}
	public String getReason(){
		return reason;
	}
	public Long getTime(){
		return time;
	}
}
