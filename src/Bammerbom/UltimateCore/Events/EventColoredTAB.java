package Bammerbom.UltimateCore.Events;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;

public class EventColoredTAB implements Listener{
	static Plugin plugin;
    public static Permission permission = null;
	public EventColoredTAB(Plugin instance){
		
		plugin = instance;
		if(!r.getCnfg().contains("Chat.Tab.Enabled")) return;
		if(!r.getCnfg().getBoolean("Chat.Tab.Enabled")) return;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		if(Bukkit.getPluginManager().isPluginEnabled("Vault")){
			setupPermissions();
			if(permission == null) return;
		for(Player p : r.getOnlinePlayers()){
		    String group = permission.getPrimaryGroup(p);
		    if(group == null || group.equalsIgnoreCase("")) return;
		    String prefix = r.getCnfg().getString("Chat.Tab." + group);
		    if(prefix == null || prefix.equalsIgnoreCase("")) return;
		    String name = prefix + UC.getPlayer(p).getNick();
		    name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
		    if(name.length() > 15) name = name.substring(0, 14);
		    p.setPlayerListName(name);
		}	
		}else{
			return;
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			public void run(){
				if(permission == null) return;
				for(Player p : r.getOnlinePlayers()){
				    String group = permission.getPrimaryGroup(p);
				    if(group == null || group.equalsIgnoreCase("")) return;
				    String prefix = r.getCnfg().getString("Chat.Tab." + group);
				    if(prefix == null || prefix.equalsIgnoreCase("")) return;
				    String name = prefix + UC.getPlayer(p).getNick();
				    name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
				    if(name.length() > 15) name = name.substring(0, 14);
				    p.setPlayerListName(name);
				}	
			}
		}, 100L, 100L);
		
	}
	  private boolean setupPermissions()
	    {
	        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
	        if (permissionProvider != null) {
	            permission = permissionProvider.getProvider();
	        }
	        return (permission != null);
	    }
	  @EventHandler
	  public void onJoin(PlayerJoinEvent e){
		  String group = permission.getPrimaryGroup(e.getPlayer());
		    if(group == null || group.equalsIgnoreCase("")) return;
		    String prefix = r.getCnfg().getString("Chat.Tab." + group);
		    if(prefix == null || prefix.equalsIgnoreCase("")) return;
		    String name = prefix + UC.getPlayer(e.getPlayer()).getNick();
		    name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
		    e.getPlayer().setPlayerListName(name);
	  }
	
}
