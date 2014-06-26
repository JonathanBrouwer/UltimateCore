package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.EntityEffect;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdClean{
	static Plugin plugin;
	public CmdClean(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.clean", false, true)){
			return;
		}
		StringBuilder s = new StringBuilder("");
		for(World w : Bukkit.getWorlds()){
			Integer c = 0;
			for(Chunk chunk : w.getLoadedChunks()){
				try{
				chunk.unload(true, true);
				}catch(Exception ex){
					return;
				}
				c++;
			}
			c = c - w.getLoadedChunks().length;
			Integer e = 0;
			Integer d = 0;
			for(Entity en : w.getEntities()){
				if(en instanceof Monster){
					en.playEffect(EntityEffect.DEATH);
					en.remove();
					e++;
				}
				if(en instanceof Item){
					Item item = (Item) en;
					if(item.getTicksLived() > 200){
					en.remove();
					d++;
					}
				}
			}
			s.append(r.default1 + "Cleaned " + r.default2 + w.getName() + r.default1 + " (" + r.default2 + c + " chunks, " + e + " monsters, " + d + " drops" + r.default1 + ")" + "\n");
		}
		sender.sendMessage(s.toString());
		
		
	}
}
