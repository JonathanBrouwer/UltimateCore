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
	Player reporter;
	Player reported;
	public Report(Integer id, String KEY){
		
	}
	public void register(){
		
	}
	public Player getReporter(){
		return reporter;
	}
	public Player getReported(){
		return reported;
	}
}
