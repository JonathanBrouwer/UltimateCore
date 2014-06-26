package Bammerbom.UltimateCore.Events;

//import net.minecraft.server.v1_7_R1.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class EventNoRespawnScreen implements Listener{
	static Plugin plugin;
	public EventNoRespawnScreen(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void ondeath(final PlayerDeathEvent e){
		if(!r.getCnfg().getBoolean("AutoRespawn")) return;
		if(!r.perm(e.getEntity(), "uc.fastrespawn", true, false)) return;
		//
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run() {
		        Player p = e.getEntity();
		        try {
		            Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
		            Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".PacketPlayInClientCommand").newInstance();
		            Class<?> enumClass = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EnumClientCommand");
		 
		            for (Object ob : enumClass.getEnumConstants()) {
		                if (ob.toString().equals("PERFORM_RESPAWN")) {
		                    packet = packet.getClass().getConstructor(enumClass).newInstance(ob);
		                }
		            }
		 
		            Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
		            con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
		        } catch (Throwable t) {
		            t.printStackTrace();
		        }
			}}, 2L);
	}
}
