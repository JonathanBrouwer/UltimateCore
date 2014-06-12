package Bammerbom.UltimateCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.API.UCworld.WorldFlag;
import Bammerbom.UltimateCore.Commands.CmdBack;
import Bammerbom.UltimateCore.Commands.CmdBan;
import Bammerbom.UltimateCore.Commands.CmdButcher;
import Bammerbom.UltimateCore.Commands.CmdCi;
import Bammerbom.UltimateCore.Commands.CmdEffect;
import Bammerbom.UltimateCore.Commands.CmdEnchant;
import Bammerbom.UltimateCore.Commands.CmdEnderchest;
import Bammerbom.UltimateCore.Commands.CmdExtinguish;
import Bammerbom.UltimateCore.Commands.CmdFeed;
import Bammerbom.UltimateCore.Commands.CmdFly;
import Bammerbom.UltimateCore.Commands.CmdFreeze;
import Bammerbom.UltimateCore.Commands.CmdFullheal;
import Bammerbom.UltimateCore.Commands.CmdGTool;
import Bammerbom.UltimateCore.Commands.CmdGive;
import Bammerbom.UltimateCore.Commands.CmdGm;
import Bammerbom.UltimateCore.Commands.CmdGod;
import Bammerbom.UltimateCore.Commands.CmdHat;
import Bammerbom.UltimateCore.Commands.CmdHeal;
import Bammerbom.UltimateCore.Commands.CmdHome;
import Bammerbom.UltimateCore.Commands.CmdIP;
import Bammerbom.UltimateCore.Commands.CmdInv;
import Bammerbom.UltimateCore.Commands.CmdJump;
import Bammerbom.UltimateCore.Commands.CmdKick;
import Bammerbom.UltimateCore.Commands.CmdKill;
import Bammerbom.UltimateCore.Commands.CmdKillAll;
import Bammerbom.UltimateCore.Commands.CmdKittycannon;
import Bammerbom.UltimateCore.Commands.CmdLag;
import Bammerbom.UltimateCore.Commands.CmdList;
import Bammerbom.UltimateCore.Commands.CmdMe;
import Bammerbom.UltimateCore.Commands.CmdMobTP;
import Bammerbom.UltimateCore.Commands.CmdMore;
import Bammerbom.UltimateCore.Commands.CmdMsg;
import Bammerbom.UltimateCore.Commands.CmdMute;
import Bammerbom.UltimateCore.Commands.CmdNick;
import Bammerbom.UltimateCore.Commands.CmdPing;
import Bammerbom.UltimateCore.Commands.CmdPlugin;
import Bammerbom.UltimateCore.Commands.CmdPowertool;
import Bammerbom.UltimateCore.Commands.CmdRegion;
import Bammerbom.UltimateCore.Commands.CmdReload;
import Bammerbom.UltimateCore.Commands.CmdRemoveAll;
import Bammerbom.UltimateCore.Commands.CmdRepair;
import Bammerbom.UltimateCore.Commands.CmdSave;
import Bammerbom.UltimateCore.Commands.CmdSay;
import Bammerbom.UltimateCore.Commands.CmdSetSpawn;
import Bammerbom.UltimateCore.Commands.CmdSmite;
import Bammerbom.UltimateCore.Commands.CmdSpawn;
import Bammerbom.UltimateCore.Commands.CmdSpawnmob;
import Bammerbom.UltimateCore.Commands.CmdSpeed;
import Bammerbom.UltimateCore.Commands.CmdStop;
import Bammerbom.UltimateCore.Commands.CmdTime;
import Bammerbom.UltimateCore.Commands.CmdTop;
import Bammerbom.UltimateCore.Commands.CmdTp;
import Bammerbom.UltimateCore.Commands.CmdTpall;
import Bammerbom.UltimateCore.Commands.CmdUC;
import Bammerbom.UltimateCore.Commands.CmdVanish;
import Bammerbom.UltimateCore.Commands.CmdWarp;
import Bammerbom.UltimateCore.Commands.CmdWeather;
import Bammerbom.UltimateCore.Commands.CmdWorkbench;
import Bammerbom.UltimateCore.Commands.CmdWorld;
import Bammerbom.UltimateCore.Commands.CmdXP;
import Bammerbom.UltimateCore.Events.EventAFK;
import Bammerbom.UltimateCore.Events.EventMinecraftServers;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class UltimateCommands implements Listener{
	static Plugin plugin;
	public UltimateCommands(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		//fixCommandOverriding();
	}
	public static void executecommand(CommandSender sender, String message){
		try{
			Bukkit.dispatchCommand(sender, message);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static void executecommand(CommandSender sender, String label, String[] args){
		executecommand(sender, label + " " + r.getFinalArg(args, 0));
	}
	public static boolean onCmd(final CommandSender sender, Command cmd, final String label, final String[] args){	 
		if(checkOverridden(sender, cmd, label, args)) return true;
		shortCut(sender, label, args);
				 if(label.equalsIgnoreCase("afk")){
					 EventAFK.handle(sender, args);
				 }else if(label.equalsIgnoreCase("xp")){
					 CmdXP.handle(sender, args);
				 }else if(label.equalsIgnoreCase("setxp")){
					 CmdXP.handle2(sender, args);
				 }
				 else if(label.equalsIgnoreCase("setlevel")){
					 CmdXP.handle3(sender, args);
				 }
				 else if(label.equalsIgnoreCase("list")){
					 CmdList.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("spawn")){
					 CmdSpawn.handle(sender,  label, args);
				 }
				 else if(label.equalsIgnoreCase("warp")){
					 CmdWarp.warp(sender,  label, args);
				 }
				 else if(label.equalsIgnoreCase("setwarp")){
					 CmdWarp.setWarp(sender,  label, args);
				 }
				 else if(label.equalsIgnoreCase("delwarp")){
					 CmdWarp.delWarp(sender,  label, args);
				 }
				 else if(label.equalsIgnoreCase("home")){
					 CmdHome.home(sender,  label, args);
				 }
				 else if(label.equalsIgnoreCase("sethome")){
					 CmdHome.setHome(sender,  label, args);
				 }
				 else if(label.equalsIgnoreCase("delhome")){
					 CmdHome.delHome(sender,  label, args);
				 }
				 else if(label.equalsIgnoreCase("weather")){
					 CmdWeather.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("region")){
					 CmdRegion.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("tp") || label.equalsIgnoreCase("tpa") || label.equalsIgnoreCase("tpaccept")){
					 CmdTp.handle(sender,  label, args);
				 }
				 else if(label.equalsIgnoreCase("plugin")){
					 CmdPlugin.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("gtool")){
					 CmdGTool.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("jump") || label.equalsIgnoreCase("jumpto")){
					 CmdJump.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("gamemode") || label.equalsIgnoreCase("gm")){
					 CmdGm.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("back")){
					 CmdBack.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("fly")){
					 CmdFly.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("god")){
					 CmdGod.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("feed")){
					 CmdFeed.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("repair")){
					 CmdRepair.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("ci") || label.equalsIgnoreCase("clear")){
					 CmdCi.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("ping")){
					 CmdPing.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("hat")){
					 CmdHat.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("say")){
					 CmdSay.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("heal")){
					 CmdHeal.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("kill") || label.equalsIgnoreCase("suicide")){
					 CmdKill.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("save")){
					 CmdSave.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("smite")){
					 CmdSmite.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("time")){
					 CmdTime.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("enderchest")){
					 CmdEnderchest.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("give")){
					 CmdGive.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("kittycannon")){
					 CmdKittycannon.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("nick")){
					 CmdNick.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("powertool") || label.equalsIgnoreCase("pt")){
					 CmdPowertool.handle(sender, args, plugin);
				 }
				 else if(label.equalsIgnoreCase("inv") || label.equalsIgnoreCase("invsee")){
					 CmdInv.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("ram") || label.equalsIgnoreCase("lag") || label.equalsIgnoreCase("tps")){
					 CmdLag.handle(sender,  label, args);
				 }
				 else if(label.equalsIgnoreCase("world")){
					 if(r.checkArgs(args, 0) == false){
							sender.sendMessage(r.default1 + "/world commands:");
							sender.sendMessage(r.default2 + "/world " + r.default2 + "create <Name> [Type...] [Type...]");
							sender.sendMessage(r.default1 + "Types:" + r.default2 + " flat, largebiomes, amplified, normal, nether, end, nostructures, [SEED]");
							sender.sendMessage(r.default2 + "/world " + r.default2 + "import <Name> [Normal/Nether/End]");
							sender.sendMessage(r.default2 + "/world " + r.default2 + "remove <Name>");
							sender.sendMessage(r.default2 + "/world " + r.default2 + "list");
							sender.sendMessage(r.default2 + "/world " + r.default2 + "tp <World>");
							sender.sendMessage(r.default2 + "/world " + r.default2 + "flag <World> <Flag> <Allow/Deny>");
							sender.sendMessage(r.default1 + "Flags: " + r.default2 + StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
							sender.sendMessage(r.default2 + "/world " + r.default2 + "reset <World>");
						 return true;
					 }
					 if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("add")){ CmdWorld.create(sender,  args); }
					 if(args[0].equalsIgnoreCase("import") || args[0].equalsIgnoreCase("imp")){ CmdWorld.importw(sender,  args); }
					 if(args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("list")){ CmdWorld.list(sender,  args);; }
					 if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")){ CmdWorld.remove(sender,  args); }
					 if(args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("tele")){ CmdWorld.tp(sender,  args); }
					 if(args[0].equalsIgnoreCase("reset")){ CmdWorld.reset(sender, args); }
					 if(args[0].equalsIgnoreCase("flag")){ CmdWorld.flag(sender, args); }
				 }
				 else if(label.equalsIgnoreCase("mcservers") || label.equalsIgnoreCase("mcstats")){
					 EventMinecraftServers.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("ban")){
					 CmdBan.ban(sender, args);
				 }
				 else if(label.equalsIgnoreCase("unban")){
					 CmdBan.unban(sender, args);
				 }
				 else if(label.equalsIgnoreCase("kick")){
					 CmdKick.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("mute")){
					 CmdMute.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("unmute")){
					 CmdMute.unmute(sender, args);
				 }
				 else if(label.equalsIgnoreCase("freeze")){
					 CmdFreeze.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("unfreeze")){
					 CmdFreeze.unfreeze(sender, args);
				 }
				 else if(label.equalsIgnoreCase("msg") || label.equalsIgnoreCase("whisper") || label.equalsIgnoreCase("w")){
					 CmdMsg.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("r")){
					 CmdMsg.handle2(sender, args);
				 }
				 else if(label.equalsIgnoreCase("me")){
					 CmdMe.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("stop")){
					 CmdStop.run(sender, args);
				 }
				 else if(label.equalsIgnoreCase("reload") || label.equalsIgnoreCase("rl")){
					 CmdReload.reload(sender, args);
				 }
				 else if(label.equalsIgnoreCase("setspawn")){
					 CmdSetSpawn.handle(sender,  label, args);
				 }
				 else if(label.equalsIgnoreCase("speed")){
					 CmdSpeed.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("more")){
					 CmdMore.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("butcher")){
					 CmdButcher.handle(sender, args);
					 
				 }
				 else if(label.equalsIgnoreCase("killall")){
					 CmdKillAll.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("removeall")){
					 CmdRemoveAll.handle(sender, args);
				 }
				 else if(label.equalsIgnoreCase("v") || label.equalsIgnoreCase("vanish")){
					 CmdVanish.handle(sender, args);
				 }else if(label.equalsIgnoreCase("spawnmob")){
					 CmdSpawnmob.handle(sender, args);
				 }else if(label.equalsIgnoreCase("enchant")){
					 CmdEnchant.handle(sender, args);
				 }else if(label.equalsIgnoreCase("item") || label.equalsIgnoreCase("i")){
					 CmdGive.handle2(sender, args);
				 }else if(label.equalsIgnoreCase("effect")){
					 CmdEffect.handle(sender, args);
				 }else if(label.equalsIgnoreCase("top")){
					 CmdTop.handle(sender, args);
				 }else if(label.equalsIgnoreCase("et") || label.equalsIgnoreCase("entityteleport") || label.equalsIgnoreCase("mobtp")){
					 CmdMobTP.handle(sender, label, args);
				 }else if(label.equalsIgnoreCase("uc") || label.equalsIgnoreCase("ultimatecore") || label.equalsIgnoreCase("core")){
					 CmdUC.core(sender, args);
				 }else if(label.equalsIgnoreCase("ip")){
					 CmdIP.handle(sender, args);
				 }else if(label.equalsIgnoreCase("wb") || label.equalsIgnoreCase("wbench") || label.equalsIgnoreCase("workbench")){
					 CmdWorkbench.handle(sender, args);
				 }else if(label.equalsIgnoreCase("fullheal")){
					 CmdFullheal.handle(sender, args);
				 }else if(label.equalsIgnoreCase("extinguish") || label.equalsIgnoreCase("unfire")){
					 CmdExtinguish.handle(sender, args);
				 }else if(label.equalsIgnoreCase("tpall")){
					 CmdTpall.handle(sender, args);
				 }
				 else{
					 return true;
				 }
				 return true;
	}
	public static void shortCut(CommandSender sender, String label, String[] args){
		if(label.equalsIgnoreCase("gmc")|| label.equalsIgnoreCase("c") || label.equalsIgnoreCase("creative")){
			executecommand(sender, "gamemode creative " + r.getFinalArg(args, 0));
		}else if(label.equalsIgnoreCase("gms") || label.equalsIgnoreCase("s")|| label.equalsIgnoreCase("survival")){
			executecommand(sender, "gamemode survival " + r.getFinalArg(args, 0));
		}else if(label.equalsIgnoreCase("gma") || label.equalsIgnoreCase("a") || label.equalsIgnoreCase("adventure")){
			executecommand(sender, "gamemode adventure " + r.getFinalArg(args, 0));
		}else if(label.equalsIgnoreCase("bring") || label.equalsIgnoreCase("tphere")){
			if(!r.checkArgs(args, 0)){ sender.sendMessage(r.default1 + "/" + label + " <Player>"); return; }
			if(!r.isPlayer(sender)) return;
			executecommand(sender, "tp " + sender.getName() + " " + args[0]);
		}else if(label.equalsIgnoreCase("day")){
			executecommand(sender, "time day");
		}else if(label.equalsIgnoreCase("night")){
			executecommand(sender, "time night");
		}else if(label.equalsIgnoreCase("sun")){
			executecommand(sender, "weather sun");
		}else if(label.equalsIgnoreCase("rain")){
			executecommand(sender, "weather rain");
		}else if(label.equalsIgnoreCase("storm")){
			executecommand(sender, "weather storm");
		}else if(label.equalsIgnoreCase("warplist")){
			executecommand(sender, "warp");
		}
		
	}
	//Label, overriden by
	private static final transient Map<PluginCommand, PluginCommand> overriddenList = new HashMap<PluginCommand, PluginCommand>();
	public static void fixCommands(){
		for(Plugin pl : Bukkit.getPluginManager().getPlugins()){
			if(pl.isEnabled() && !pl.equals(plugin)) addPlugin(pl);
		}
	}
	public static void addPlugin(Plugin pl){
		if(pl.getName().contains("Essentials")) return;
		List<Command> commands = PluginCommandYamlParser.parse(pl);
		for(Command command : commands){
			PluginCommand pc = (PluginCommand) command;
			List<String> labels = new ArrayList<String>(pc.getAliases());
			labels.add(pc.getName());
			PluginCommand uc;
			uc = plugin.getServer().getPluginCommand("ultimatecore:" + pc.getName());
			if(uc == null){
				uc = plugin.getServer().getPluginCommand(pc.getName().toLowerCase(Locale.ENGLISH));
			}
			if((uc != null) && uc.getPlugin().equals(plugin)){
				for(String label : labels){
					if(label.equalsIgnoreCase(uc.getLabel())){
						overriddenList.put(uc, pc);
						r.log(ChatColor.WHITE + "Command overridden: " + label + " (" + pc.getPlugin() + ")");
					}
				}
			}
		}
	}
	public static void removePlugin(Plugin pl){
		List<Command> commands = PluginCommandYamlParser.parse(pl);
		for(Command command : commands){
			PluginCommand pc = (PluginCommand) command;
			List<String> labels = new ArrayList<String>(pc.getAliases());
			labels.add(pc.getName());
			PluginCommand uc;
			uc = plugin.getServer().getPluginCommand("ultimatecore:" + pc.getName());
			if(uc == null){
				uc = plugin.getServer().getPluginCommand(pc.getName().toLowerCase(Locale.ENGLISH));
			}
			if((uc != null) && uc.getPlugin().equals(plugin)){
				for(String label : labels){
					if(label.equalsIgnoreCase(uc.getLabel())){
						if(overriddenList.containsKey(uc)){
							r.log(ChatColor.WHITE + "Command un-overridden: " + label + " (" + pc.getPlugin() + ")");
							overriddenList.remove(uc);
						}else{
							r.log(r.error + "Failed to re-register overridden command: " + uc.getLabel() + " (" + pc.getPlugin().getName() + ")");
						}
					}
				}
			}
		}
	}
	public static boolean checkOverridden(final CommandSender sender, Command cmd, final String label, final String[] args){	
		PluginCommand uc = (PluginCommand) cmd;
		if(overriddenList.containsKey(uc)){
			PluginCommand pc = overriddenList.get(uc);
			pc.getExecutor().onCommand(sender, cmd, label, args);
			return true;
		}
		return false;
	}
	@EventHandler
	public void plEnable(PluginEnableEvent e){
		if(!e.getPlugin().equals(plugin)) addPlugin(e.getPlugin());
	}
	
	@EventHandler
	public void plDisable(PluginDisableEvent e){
		if(!e.getPlugin().equals(plugin)) removePlugin(e.getPlugin());
	}

	 
}
