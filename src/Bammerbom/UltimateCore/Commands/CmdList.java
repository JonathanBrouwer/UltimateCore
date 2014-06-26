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
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class CmdList{
	static Plugin plugin;
	public static Permission permission = null;
	public CmdList(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		if(Bukkit.getPluginManager().getPlugin("Vault") != null && Bukkit.getPluginManager().isPluginEnabled("Vault")){
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
		        online.append(UC.getPlayer(player).getNick());
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
				if(isAnyUserOnline(g)){
				String gn = g;
				/*if(r.getCnfg().getBoolean("Chat.Tab.Enabled") && r.getCnfg().get("Chat.Tab." + g) != null){
					gn = r.getCnfg().getString("Chat.Tab." + g).replaceAll("&", "§") + gn;
				}*/
				if(first2){
					first2=false;
				    online.append(r.default1 + StringUtil.firstUpperCase(gn) + ": ");
				}else{
					online.append("\n" + r.default1 + StringUtil.firstUpperCase(gn) +  ": ");
				}
				Boolean first = true;
				Boolean any = false;
				ArrayList<Player> remove = new ArrayList<Player>();
			    for(Player pl : plz){
			    	Player p = Bukkit.getPlayer(sender.getName());
			    	if(p == null || p.canSee(pl)){
			    	if(permission.getPrimaryGroup(pl) != null && permission.getPrimaryGroup(pl).equalsIgnoreCase(g)){
			    		if(!first){ online.append(", "); }
			    		online.append(r.default2 + UC.getPlayer(pl).getNick());
			    		i++;
			    		any = true;
			    		first = false;
			    		remove.add(pl);
			    	}
			    	}
			    }
			    plz.removeAll(remove);
			    remove.clear();
			    if(any == false){
			    	online.append(r.default2 + "none");
			    }
				}
			}
			if(!plz.isEmpty()){
			online.append("\n" + r.default1 + "No group: ");
			for(Player pl : plz){
				online.append(r.default2 + pl.getName());
			}
			}
			plz.clear();
			pls = null;
			plz = null;
			String message = r.mes("List.List").replaceAll("%Online", i + "").replaceAll("%Max", Bukkit.getMaxPlayers() + "").replaceAll("%List", online.toString());
			for(String str : message.split("\\\\n")){
				sender.sendMessage(str);
			}
		}
	}
	private static boolean isAnyUserOnline(String group){
		if(permission == null) return true;
		for(Player p : Bukkit.getOnlinePlayers()){
			if(permission.getPrimaryGroup(p).equalsIgnoreCase(group)) return true;
		}
		return false;
	}
}
