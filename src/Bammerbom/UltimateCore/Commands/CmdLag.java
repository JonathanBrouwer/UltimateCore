 package Bammerbom.UltimateCore.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdLag{
	private static float tps = 20F;
	private static long lastPoll = System.currentTimeMillis() - 3000L;
	static Plugin plugin;
	public CmdLag(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
		{
			@Override
			public void run() {
			    long now = System.currentTimeMillis();
			    Float timeSpent = ((now - lastPoll) * 1.0F) / 1000.0F;
			    float tpsn = ((float)timeSpent * 20.0F);
			    if(tpsn > 20.0F) tpsn = 20.0F;
			    tps = tpsn;
			    lastPoll = now;
			}
		}, 20, 20);
	}
	public static void handle(CommandSender sender, String label, String[] args){
		if(!r.perm(sender, "uc.lag", true, true)){
			return;
		}
		Long mb = 1024L*1024L;
		ArrayList<String> msgs = new ArrayList<String>();
		//Long totalram1 = Runtime.getRuntime().maxMemory();
		Long maxram = Runtime.getRuntime().maxMemory() / mb;
		Long totalram = Runtime.getRuntime().totalMemory() / mb;
		//Long freeram1 = Runtime.getRuntime().freeMemory();
		Long freeram2 = Runtime.getRuntime().freeMemory() / mb;
		//Long usedram1 = totalram1 - freeram1;
		Long usedram2 = maxram - freeram2;
		Double percentage1 = ((usedram2 * 1.0) / (maxram * 1.0)) * 100.0;
		Float percentage2 = Math.round(percentage1 * 10) / 10F;   
	    msgs.add(r.default1 + "Tps: " + r.default2 + (Math.round(tps * 10.0) / 10.0));
	    msgs.add(r.default1 + "Memory used: " + r.default2 + usedram2 + "/" + totalram + "/" + maxram + r.default1 + " MB (Used/Total/Max) (" + r.default2 + percentage2 + "%" + r.default1 + ")");
	    for(World w : Bukkit.getWorlds()){
			int tiles = 0;
			for(Chunk c : w.getLoadedChunks()){
				tiles += c.getTileEntities().length;
			}
			msgs.add(r.default1 + "World '" + r.default2 + w.getName() + r.default1 + "': " + r.default2 + w.getLoadedChunks().length + r.default1 + " chunks, " + r.default2 + w.getEntities().size() + r.default1 + " entities, " + r.default2 + tiles);
		}
		for(String string : msgs){
	    	sender.sendMessage(string);
	    }
	}
	
	//
	
}
