package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import Bammerbom.UltimateCore.UltimateCore;
import Bammerbom.UltimateCore.r;

public class EventUnknownCommand implements Listener{
	
	static UltimateCore plugin;
	public EventUnknownCommand(UltimateCore loader){
		plugin = loader;
		plugin.getServer().getPluginManager().registerEvents(this, loader);
	}
	public boolean isCmdRegistered(String cmd){
		return Bukkit.getPluginCommand(cmd.replaceFirst("/", "")) != null || Bukkit.getServer().getHelpMap().getHelpTopic(
				(cmd.contains(":") 
				? cmd.split(":")[0].equalsIgnoreCase("/bukkit") ? "/" + cmd.split(":")[1] : "////asasasas" 
						: cmd)) != null;
	}
	@EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
      String cmd = event.getMessage();
      if(cmd == null || cmd.equalsIgnoreCase("")){
    	  event.getPlayer().sendMessage(r.mes("UnknownCommandMessage"));
    	  event.setCancelled(true);
          return;
      }
      cmd = cmd.split(" ")[0];

      if (!isCmdRegistered(cmd) || event.isCancelled()) {
    	  event.getPlayer().sendMessage(r.mes("UnknownCommandMessage"));
    	  event.setCancelled(true);
      }
      
  }
}
