package Bammerbom.UltimateCore.Events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.Resources.Utils.ParticleUtil;

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
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void bleed(final EntityDamageEvent e){
		if(e.isCancelled() || e.getEntity().isDead()) return;
		if(!(e.getEntity() instanceof LivingEntity)) return;
		if(plugin.getConfig().getBoolean("Bleed.Enabled") == false) return;
		if(e.getEntity() instanceof Player || plugin.getConfig().getBoolean("Bleed.PlayersOnly") == false){
			if(e.getEntity() instanceof Player){
				final Player p = (Player) e.getEntity();
				if(UC.getPlayer(p).isGod()) return;
				if(inCoolDown.contains(p.getName())) return;
				inCoolDown.add(p.getName());
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run(){
						inCoolDown.remove(p.getName());
					}
				}, 5L);
				if(((Player) e.getEntity()).getGameMode().equals(GameMode.CREATIVE)) return;
				if(UC.getPlayer((Player) e.getEntity()).isGod()) return;
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
			ParticleUtil.displayBlockDust(e.getEntity().getLocation().add(0.0, 1.0, 0.0), Material.REDSTONE_BLOCK.getId(), Byte.parseByte("0"), 0.5F, 0.5F, 0.5F, 0, 30);
		}//55
	}
}
