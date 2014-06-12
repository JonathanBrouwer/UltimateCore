package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.Plugin;

public class EventWeather implements Listener{
	static Plugin plugin;
	public EventWeather(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	static Integer last = 0;
	@EventHandler(priority = EventPriority.LOWEST)
	public void toRain(WeatherChangeEvent e){
		if(plugin.getConfig().getBoolean("Weather.Rain") == true && last == 0){
			last = 1;
			e.setCancelled(true);
			e.getWorld().setThundering(false);
			e.getWorld().setStorm(false);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){@Override public void run() {
				
				last = 0;
			
			}}, 3L);
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void toThunder(ThunderChangeEvent e){
		if(plugin.getConfig().getBoolean("Weather.Rain") == true && last == 0){
			last = 1;
			e.setCancelled(true);
			e.getWorld().setThundering(false);
			e.getWorld().setStorm(false);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){@Override public void run() {
				
				last = 0;
			
			}}, 3L);
		}
	}
	public static void setLast(Integer n){
		last = n;
	}
}
