package Bammerbom.UltimateCore.Events;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.SimplePluginManager;

import Bammerbom.UltimateCore.UltimateCore;
import Bammerbom.UltimateCore.r;

public class EventUnknownCommand implements Listener{
	
	static UltimateCore plugin;
	public EventUnknownCommand(UltimateCore loader){
		plugin = loader;
		plugin.getServer().getPluginManager().registerEvents(this, loader);
	}
	public boolean isCmdRegistered(String cmd){
		return getCommandMap().getCommand(cmd.replaceFirst("/", "")) != null;
	}
	private SimpleCommandMap getCommandMap(){
		    if ((Bukkit.getPluginManager() instanceof SimplePluginManager)) {
		      Field f = null;
			try {
				f = SimplePluginManager.class
				    .getDeclaredField("commandMap");
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
				return null;
			}
		      f.setAccessible(true);
		      try {
				return (SimpleCommandMap)f.get(Bukkit.getPluginManager());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		    }
		    r.log("PluginManager invalid!");
		    return null;
		  }
	@EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.isCancelled()) return;
      String cmd = event.getMessage();
      if(cmd == null || cmd.equalsIgnoreCase("")){
    	  event.getPlayer().sendMessage(r.mes("UnknownCommandMessage"));
    	  event.setCancelled(true);
          return;
      }
      cmd = cmd.split(" ")[0];

      if (!isCmdRegistered(cmd)) {
    	  event.getPlayer().sendMessage(r.mes("UnknownCommandMessage"));
    	  event.setCancelled(true);
      }
      
  }
}
