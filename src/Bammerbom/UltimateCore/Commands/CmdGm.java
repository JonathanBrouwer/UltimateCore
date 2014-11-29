package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdGm{
	static Plugin plugin;
	public CmdGm(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(args.length == 1){
			if(!(r.isPlayer(sender))){
				return;
			}
			if(sender instanceof Player){
				Player p = (Player) sender;
			if("0".equalsIgnoreCase(args[0]) || "survival".equalsIgnoreCase(args[0]) || "s".equalsIgnoreCase(args[0])){
				if(r.perm(sender, "uc.gm", false, false) == false && r.perm(sender, "uc.gm.self.survival", false, false) == false && r.perm(sender, "uc.gm.self", false, false) == false){
					sender.sendMessage(r.mes("NoPermissions"));
					return;
				}
				sender.sendMessage(r.mes("Gamemode.setSelf").replaceAll("%Gamemode", "survival"));
				p.setGameMode(GameMode.SURVIVAL);
				
			}
			else if("1".equalsIgnoreCase(args[0]) || "creative".equalsIgnoreCase(args[0]) || "c".equalsIgnoreCase(args[0])){
				if(r.perm(sender, "uc.gm", false, false) == false && r.perm(sender, "uc.gm.self.creative", false, false) == false && r.perm(sender, "uc.gm.self", false, false) == false){
					sender.sendMessage(r.mes("NoPermissions"));
					return;
				}
				sender.sendMessage(r.mes("Gamemode.setSelf").replaceAll("%Gamemode", "creative"));
				p.setGameMode(GameMode.CREATIVE);
			
		    }
			else if("2".equalsIgnoreCase(args[0]) || "adventure".equalsIgnoreCase(args[0]) || "a".equalsIgnoreCase(args[0])){
				if(r.perm(sender, "uc.gm", false, false) == false && r.perm(sender, "uc.gm.self.adventure", false, false) == false && r.perm(sender, "uc.gm.self", false, false) == false){
					sender.sendMessage(r.mes("NoPermissions"));
					return;
				}
				sender.sendMessage(r.mes("Gamemode.setSelf").replaceAll("%Gamemode", "adventure"));
				p.setGameMode(GameMode.ADVENTURE);
            }
			else if("3".equalsIgnoreCase(args[0]) || "spectate".equalsIgnoreCase(args[0]) || "spectator".equalsIgnoreCase(args[0])){
				if(r.perm(sender, "uc.gm", false, false) == false && r.perm(sender, "uc.gm.self.spectate", false, false) == false && r.perm(sender, "uc.gm.self", false, false) == false){
					sender.sendMessage(r.mes("NoPermissions"));
					return;
				}
				sender.sendMessage(r.mes("Gamemode.setSelf").replaceAll("%Gamemode", "spectator"));
				p.setGameMode(GameMode.SPECTATOR);
            }
			else{
				if(r.perm(sender, "uc.gm", false, true)){
				sender.sendMessage(r.mes("Gamemode.Usage"));
				}
			}}
    	}
		else if(args.length == 2){
			Player target = r.searchPlayer(args[1]);
			if(target == null){
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1]));
				return;
			}
			if("0".equalsIgnoreCase(args[0]) || "survival".equalsIgnoreCase(args[0]) || "s".equalsIgnoreCase(args[0])){
				if(r.perm(sender, "uc.gm", false, false) == false && r.perm(sender, "uc.gm.others.survival", false, false) == false && r.perm(sender, "uc.gm.others", false, false) == false){
					sender.sendMessage(r.mes("NoPermissions"));
					return;
				}
				sender.sendMessage(r.mes("Gamemode.selfMessage").replaceAll("%Gamemode", "survival").replaceAll("%Player", target.getName()));
				target.sendMessage(r.mes("Gamemode.otherMessage").replaceAll("%Gamemode", "survival"));
				target.setGameMode(GameMode.SURVIVAL);
				
			}
			else if("1".equalsIgnoreCase(args[0]) || "creative".equalsIgnoreCase(args[0]) || "c".equalsIgnoreCase(args[0])){
				if(r.perm(sender, "uc.gm", false, false) == false && r.perm(sender, "uc.gm.others.creative", false, false) == false && r.perm(sender, "uc.gm.others", false, false) == false){
					sender.sendMessage(r.mes("NoPermissions"));
					return;
				}
				sender.sendMessage(r.mes("Gamemode.selfMessage").replaceAll("%Gamemode", "creative").replaceAll("%Player", target.getName()));
				target.sendMessage(r.mes("Gamemode.otherMessage").replaceAll("%Gamemode", "creative"));
				target.setGameMode(GameMode.CREATIVE);
			
		    }
			else if("2".equalsIgnoreCase(args[0]) || "adventure".equalsIgnoreCase(args[0]) || "a".equalsIgnoreCase(args[0])){
				if(r.perm(sender, "uc.gm", false, false) == false && r.perm(sender, "uc.gm.others.creative", false, false) == false && r.perm(sender, "uc.gm.others", false, false) == false){
					sender.sendMessage(r.mes("NoPermissions"));
					return;
				}
				sender.sendMessage(r.mes("Gamemode.selfMessage").replaceAll("%Gamemode", "adventure").replaceAll("%Player", target.getName()));
				target.sendMessage(r.mes("Gamemode.otherMessage").replaceAll("%Gamemode", "adventure"));
				target.setGameMode(GameMode.ADVENTURE);
			}
			else if("3".equalsIgnoreCase(args[0]) || "spectate".equalsIgnoreCase(args[0]) || "spectator".equalsIgnoreCase(args[0])){
				if(r.perm(sender, "uc.gm", false, false) == false && r.perm(sender, "uc.gm.others.spectator", false, false) == false && r.perm(sender, "uc.gm.others", false, false) == false){
					sender.sendMessage(r.mes("NoPermissions"));
					return;
				}
				sender.sendMessage(r.mes("Gamemode.selfMessage").replaceAll("%Gamemode", "adventure").replaceAll("%Player", target.getName()));
				target.sendMessage(r.mes("Gamemode.otherMessage").replaceAll("%Gamemode", "adventure"));
				target.setGameMode(GameMode.SPECTATOR);
            }else{
            	sender.sendMessage(r.mes("Gamemode.Usage"));
            }
		}else{
			sender.sendMessage(r.mes("Gamemode.Usage"));
		}
	}
}
