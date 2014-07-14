package Bammerbom.UltimateCore.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdReport {
	public CmdReport(){
	}
	File file = UltimateFileLoader.DFreports;
	@SuppressWarnings("deprecation")
	public static void handle(CommandSender sender, String[] args){
		if(!r.isPlayer(sender)){
			return;
		}
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.default1 + "/report <Player> <Message> - " + r.default2 + "Report someone");
			sender.sendMessage(r.default1 + "/report view <ID> - " + r.default2 + "Views a report");
			sender.sendMessage(r.default1 + "/report list - " + r.default2 + "View a list of reports");
			sender.sendMessage(r.default1 + "/report claim <ID> - " + r.default2 + "Claim a report");
			sender.sendMessage(r.default1 + "/report finish <ID> - " + r.default2 + "Mark a report as finished");
			sender.sendMessage(r.default1 + "/report delete/remove <ID> - " + r.default2 + "Removed a report");
			return;
		}
		if(args[0].equalsIgnoreCase("view")){
			if(!r.checkArgs(args, 1) || !r.isNumber(args[1])){
				sender.sendMessage(r.default1 + "/report <Player> <Message> - " + r.default2 + "Report someone");
				sender.sendMessage(r.default1 + "/report view <ID> - " + r.default2 + "Views a report");
				sender.sendMessage(r.default1 + "/report list - " + r.default2 + "View a list of reports");
				sender.sendMessage(r.default1 + "/report claim <ID> - " + r.default2 + "Claim a report");
				sender.sendMessage(r.default1 + "/report finish <ID> - " + r.default2 + "Mark a report as finished");
				sender.sendMessage(r.default1 + "/report delete/remove <ID> - " + r.default2 + "Removed a report");
				return;
			}
			if(!Report.exists(Integer.parseInt(args[1]))){
				sender.sendMessage(r.default1 + "Report doesnt exists: " + args[1]);
				return;
			}
			Report report = Report.load(Integer.parseInt(args[1]));
			ChatColor c = ChatColor.DARK_RED;
			if(report.getState().equals(Report.State.FINISHED)){
				c = ChatColor.GREEN;
			}else if(report.getState().equals(Report.State.CLAIMED)){
				c = ChatColor.GOLD;
			}
			sender.sendMessage(r.default1 + "[" + r.default2 + report.getID() + r.default1 + "] " + c + "[" + report.getState().name() + (report.getClaimer() != null ? " by " + report.getClaimer().getName() : "")+ "] " + r.default2 + report.getReporter().getName() + r.default1 + " reported " + r.default2 + report.getReported().getName() + r.default1 + " for " + r.default2 + report.getReason());
			
		}else if(args[0].equalsIgnoreCase("list")){
			Report.State so = null;
			OfflinePlayer po = null;
			if(r.checkArgs(args, 1)){
				if(args[1].equalsIgnoreCase("-p") && r.checkArgs(args, 2)){
					OfflinePlayer pa = Bukkit.getOfflinePlayer(args[2]);
					if(!(pa == null || (!pa.isOnline() && !pa.hasPlayedBefore()))){
						po = pa;
					}
				}
				if(args[1].startsWith("-f") || args[1].startsWith("f")){
					so = Report.State.FINISHED;
				}else if(args[1].startsWith("-c") || args[1].startsWith("c")){
					so = Report.State.CLAIMED;
				}else if(args[1].startsWith("-n") || args[1].startsWith("n")){
					so = Report.State.NEW;
				}
			}
			sender.sendMessage(r.default1 + "Reports: " + r.default2 + ((po != null || so != null) ? r.getFinalArg(args, 1) : ""));
			for(Report report : Report.list()){
				if(so != null && !report.getState().equals(so)) continue;
				if(po != null){
					Boolean show = false;
					if(report.getClaimer() != null && report.getClaimer().equals(po)) show = true;
					if(report.getReported().equals(po) || report.getReporter().equals(po)) show = true;
					if(!show) continue;
				}
				ChatColor c = ChatColor.DARK_RED;
				if(report.getState().equals(Report.State.FINISHED)){
					c = ChatColor.GREEN;
				}else if(report.getState().equals(Report.State.CLAIMED)){
					c = ChatColor.GOLD;
				}
				sender.sendMessage(r.default1 + "[" + r.default2 + report.getID() + r.default1 + "] " + c + "[" + report.getState().name() + (report.getClaimer() != null ? " by " + report.getClaimer().getName() : "")+ "] " + r.default2 + report.getReporter().getName() + r.default1 + " reported " + r.default2 + report.getReported().getName() + r.default1 + " for " + r.default2 + report.getReason());
			}
		}else if(args[0].equalsIgnoreCase("claim")){
			if(!r.checkArgs(args, 1) || !r.isNumber(args[1])){
				sender.sendMessage(r.default1 + "/report <Player> <Message> - " + r.default2 + "Report someone");
				sender.sendMessage(r.default1 + "/report view <ID> - " + r.default2 + "Views a report");
				sender.sendMessage(r.default1 + "/report list - " + r.default2 + "View a list of reports");
				sender.sendMessage(r.default1 + "/report claim <ID> - " + r.default2 + "Claim a report");
				sender.sendMessage(r.default1 + "/report finish <ID> - " + r.default2 + "Mark a report as finished");
				sender.sendMessage(r.default1 + "/report delete/remove <ID> - " + r.default2 + "Removed a report");
				return;
			}
			if(!Report.exists(Integer.parseInt(args[1]))){
				sender.sendMessage(r.default1 + "Report doesnt exists: " + args[1]);
				return;
			}
			Report report = Report.load(Integer.parseInt(args[1]));
			report.setState(Report.State.CLAIMED);
			report.setClaimer(((Player) sender).getUniqueId());
		}else if(args[0].equalsIgnoreCase("finish")){
			if(!r.checkArgs(args, 1) || !r.isNumber(args[1])){
				sender.sendMessage(r.default1 + "/report <Player> <Message> - " + r.default2 + "Report someone");
				sender.sendMessage(r.default1 + "/report view <ID> - " + r.default2 + "Views a report");
				sender.sendMessage(r.default1 + "/report list - " + r.default2 + "View a list of reports");
				sender.sendMessage(r.default1 + "/report claim <ID> - " + r.default2 + "Claim a report");
				sender.sendMessage(r.default1 + "/report finish <ID> - " + r.default2 + "Mark a report as finished");
				sender.sendMessage(r.default1 + "/report delete/remove <ID> - " + r.default2 + "Removed a report");
				return;
			}
			if(!Report.exists(Integer.parseInt(args[1]))){
				sender.sendMessage(r.default1 + "Report doesnt exists: " + args[1]);
				return;
			}
			Report report = Report.load(Integer.parseInt(args[1]));
			report.setState(Report.State.FINISHED);
			report.setClaimer(((Player) sender).getUniqueId());
			
		}else{
			if(!r.checkArgs(args, 1)){
				sender.sendMessage(r.default1 + "/report <Player> <Message> - " + r.default2 + "Report someone");
				sender.sendMessage(r.default1 + "/report view <ID> - " + r.default2 + "Views a report");
				sender.sendMessage(r.default1 + "/report list - " + r.default2 + "View a list of reports");
				sender.sendMessage(r.default1 + "/report claim <ID> - " + r.default2 + "Claim a report");
				sender.sendMessage(r.default1 + "/report finish <ID> - " + r.default2 + "Mark a report as finished");
				sender.sendMessage(r.default1 + "/report delete/remove <ID> - " + r.default2 + "Removed a report");
				return;
			}
			String reporte = args[0];
			String reason = r.getFinalArg(args, 1);
			Integer id = Report.newID();
			Player reporter = (Player) sender;
			OfflinePlayer reported = Bukkit.getPlayer(reporte);
			if(reported == null || (!reported.hasPlayedBefore() && !reported.isOnline())){
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
				return;
			}
			Report report = new Report(id, System.currentTimeMillis(), reporter.getUniqueId(), reported.getUniqueId(), reason, Report.State.NEW);
			report.register();
		}
	}
}
class Report{
	private Integer id;
	private UUID reporter;
	private UUID reported;
	private UUID claimer;
	private String reason;
	private Long time;
	private State state;
	public Report(Integer id, Long time, UUID reporter, UUID reported, String reason, State state){
		this.id = id;
		this.reporter = reporter;
		this.reported = reported;
		this.reason = reason;
		this.time = time;
		this.state = state;
		this.claimer = null;
	}
	public void register(){
		UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.DFreports);
		conf.set("R" + id + ".reporter", reporter.toString());
		conf.set("R" + id + ".reported", reported.toString());
		conf.set("R" + id + ".time", time);
		conf.set("R" + id + ".reason", reason);
		conf.set("R" + id + ".state", "NEW");
		conf.save();
	}
	public static Integer newID(){
		Integer id = 0;
		for(Report report : list()){
			if(report.getID() > id){
				id = report.getID();
			}
		}
		id++;
		return id;
	}
	public static boolean exists(Integer id){
		return new UltimateConfiguration(UltimateFileLoader.DFreports).contains("R" + id);
	}
	public static Report load(Integer id){
		UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.DFreports);
		Report report = new Report(
				id, conf.getLong("R" + id + ".time"),
				UUID.fromString(conf.getString("R" + id + ".reporter")),
				UUID.fromString(conf.getString("R" + id + ".reported")),
				conf.getString("R" + id + ".reason"),
				State.valueOf(conf.getString("R" + id + ".state")));
		if(conf.contains("R" + id + ".claimer")){
			report.setClaimer(UUID.fromString(conf.getString("R" + id + ".claimer")));
		}
		return report;
	}
	public static List<Report> list(){
		UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.DFreports);
		ArrayList<Report> reports = new ArrayList<Report>();
		ArrayList<Integer> reports2 = new ArrayList<Integer>();
		for(String s : conf.getKeys(false)){
			reports2.add(Report.load(Integer.parseInt(s.replaceFirst("R", ""))).getID());
		}
		Collections.sort(reports2);
		//Collections.sort(reports2, Collections.reverseOrder());
        reports.clear();
		for(Integer a : reports2){
			reports.add(Report.load(a));
		}
		return reports;
	}
	public OfflinePlayer getReporter(){
		return Bukkit.getOfflinePlayer(reporter);
	}
	public OfflinePlayer getReported(){
		return Bukkit.getOfflinePlayer(reported);
	}
	public OfflinePlayer getClaimer(){
		if(claimer == null) return null;
		return Bukkit.getOfflinePlayer(claimer);
	}
	public void setClaimer(UUID claimer){
		this.claimer = claimer;
		UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.DFreports);
		conf.set("R" + id + ".claimer", claimer.toString());
		conf.save();
	}
	public String getReason(){
		return reason;
	}
	public Integer getID(){
		return id;
	}
	public Long getTime(){
		return time;
	}
	public State getState(){
		return state;
	}
	public void setState(State s){
		state = s;
		UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.DFreports);
		conf.set("R" + id + ".state", s.name());
		conf.save();
	}
	public static enum State{
		NEW, CLAIMED, FINISHED
	}
}
