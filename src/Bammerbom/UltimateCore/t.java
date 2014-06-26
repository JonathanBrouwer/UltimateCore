package Bammerbom.UltimateCore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.TimedRegisteredListener;
import org.spigotmc.CustomTimingsHandler;

public class t implements Listener{
	Plugin plugin;
	public t(Plugin instance){
		plugin = instance;
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}
	public void enable(){
		
	}
	  public static long timingStart = 0L;
	@SuppressWarnings("rawtypes")
	@EventHandler
	public void interact(PlayerInteractEvent e){
		if(!e.getPlayer().getItemInHand().getType().equals(Material.STONE)) return;
		Player sender = e.getPlayer(); 
		if (!Bukkit.getPluginManager().useTimings())
	      {
	        sender.sendMessage("Please enable timings by typing /timings on");
	        return;
	      }
	      long sampleTime = System.nanoTime() - timingStart;

	      int index = 0;
	      int pluginIdx = 0;
	      File timingFolder = new File("timings");
	      timingFolder.mkdirs();
	      File timings = new File(timingFolder, "timings.txt");
	      File names = null;
	      boolean paste = false;
	      boolean separate = false;
	      ByteArrayOutputStream bout = paste ? new ByteArrayOutputStream() : null;
	      while (timings.exists()) timings = new File(timingFolder, "timings" + ++index + ".txt");
	      PrintStream fileTimings = null;
	      PrintStream fileNames = null;
	      try {
	        fileTimings = paste ? new PrintStream(bout) : new PrintStream(timings);
	        if (separate) {
	          names = new File(timingFolder, "names" + index + ".txt");
	          fileNames = new PrintStream(names);
	        }
	        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
	          pluginIdx++;
	          long totalTime = 0L;
	          if (separate) {
	            fileNames.println(pluginIdx + " " + plugin.getDescription().getFullName());
	            fileTimings.println("Plugin " + pluginIdx);
	          } else {
	            fileTimings.println(plugin.getDescription().getFullName());
	          }for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin)) {
	            if ((listener instanceof TimedRegisteredListener)) {
	              TimedRegisteredListener trl = (TimedRegisteredListener)listener;
	              long time = trl.getTotalTime();
	              int count = trl.getCount();
	              if (count != 0) {
	                long avg = time / count;
	                totalTime += time;
	                Class eventClass = trl.getEventClass();
	                if ((count > 0) && (eventClass != null)){
	                	String name = trl.getClass().getSimpleName();
	                  fileTimings.println("    " + name + (trl.hasMultiple() ? " (and sub-classes)" : "") + " Time: " + time + " Count: " + count + " Avg: " + avg + " Violations: " + trl.violations);
	                }
	              }
	            }
	          }
	          fileTimings.println("    Total time " + totalTime + " (" + totalTime / 1000000000L + "s)");
	        }

	        CustomTimingsHandler.printTimings(fileTimings);
	        fileTimings.println("Sample time " + sampleTime + " (" + sampleTime / 1000000000.0D + "s)");

	        sender.sendMessage("Timings written to " + timings.getPath());
	        sender.sendMessage("Paste contents of file into form at http://aikar.co/timings.php to read results.");
	        if (separate) sender.sendMessage("Names written to " + names.getPath()); 
	      } catch (IOException ex) {
	    	  ex.printStackTrace();
	      }
	      finally { if (fileTimings != null) {
	          fileTimings.close();
	        }
	        if (fileNames != null) {
	          fileNames.close();
	        }
	      }
	}
}
