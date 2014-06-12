package Bammerbom.UltimateCore.Commands;

import java.util.ArrayList;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class CmdList{
	static Plugin plugin;
	public static Permission permission = null;
	public CmdList(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		if(Bukkit.getPluginManager().getPlugin("Vault") != null){
		setupPermissions();
		}
	}
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	public static void handle(CommandSender sender, String[] args) {
		if(!r.perm(sender, "uc.list", false, true)){
			return;
		}
		if(permission == null){
		    StringBuilder online = new StringBuilder();

		    Player[] players = Bukkit.getOnlinePlayers();
		    
		    Integer i = 0;
		    
		    for (Player player : players)
		    {
		      if ((!(sender instanceof Player)) || (((Player)sender).canSee(player)))
		      {
		        if (online.length() > 0) {
		          online.append(", ");
		        }
		        i++;
		        online.append(player.getDisplayName());
		      }
		    }
			String message = r.mes("List.List").replaceAll("%Online", i + "").replaceAll("%Max", Bukkit.getMaxPlayers() + "").replaceAll("%List", online.toString());
			for(String str : message.split("\\\\n")){
				sender.sendMessage(str);
			}
		}else{
			StringBuilder online = new StringBuilder();
			Player[] pls = Bukkit.getOnlinePlayers();
			ArrayList<Player> plz = new ArrayList<Player>();
			for(Player pl : pls){
				plz.add(pl);
			}
			Boolean first2 = true;
			Integer i = 0;
			for(String g : permission.getGroups()){
				if(first2){
					first2=false;
				    online.append(r.default1 + StringUtil.firstUpperCase(g) + ": ");
				}else{
					online.append("\n" + r.default1 + StringUtil.firstUpperCase(g) + ": ");
				}
				Boolean first = true;
				Boolean any = false;
			    for(Player pl : plz){
			    	Player p = Bukkit.getPlayer(sender.getName());
			    	if(p == null || p.canSee(pl)){
			    	if(permission.getPrimaryGroup(pl).equalsIgnoreCase(g)){
			    		if(!first){ online.append(", "); first = false; }
			    		online.append(r.default2 + pl.getName());
			    		i++;
			    		any = true;
			    	}
			    	}
			    }
			    if(any == false){
			    	online.append(r.default2 + "none");
			    }
			}
			String message = r.mes("List.List").replaceAll("%Online", i + "").replaceAll("%Max", Bukkit.getMaxPlayers() + "").replaceAll("%List", online.toString());
			for(String str : message.split("\\\\n")){
				sender.sendMessage(str);
			}
		}
	}
}
