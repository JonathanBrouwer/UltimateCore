package Bammerbom.UltimateCore.Events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class EventBleed implements Listener{
	
	static Plugin plugin;
	public EventBleed(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	ArrayList<String> inCoolDown = new ArrayList<String>();
	ArrayList<Integer> iCD = new ArrayList<Integer>();
	@EventHandler(priority = EventPriority.LOWEST)
	public void bleed(final EntityDamageEvent e){
		if(e.isCancelled() || e.getEntity().isDead()) return;
		if(!(e.getEntity() instanceof LivingEntity)) return;
		if(plugin.getConfig().getBoolean("Bleed.Enabled") == false) return;
		if(e.getEntity() instanceof Player || plugin.getConfig().getBoolean("Bleed.PlayersOnly") == false){
			if(e.getEntity() instanceof Player){
				final Player p = (Player) e.getEntity();
				if(inCoolDown.contains(p.getName())) return;
				inCoolDown.add(p.getName());
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run(){
						inCoolDown.remove(p.getName());
					}
				}, 5L);
				if(((Player) e.getEntity()).getGameMode().equals(GameMode.CREATIVE)) return;
			}else{
				if(iCD.contains(e.getEntity().getEntityId())) return;
				iCD.add(e.getEntity().getEntityId());
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

					@Override
					public void run() {
						iCD.remove((Object)e.getEntity().getEntityId()) ;
						
					}
				}, 4L);
			}
			e.getEntity().getLocation().getWorld().playEffect(e.getEntity().getLocation().clone().add(0, 1, 0), Effect.STEP_SOUND, 55);
		}//55
	}
}
