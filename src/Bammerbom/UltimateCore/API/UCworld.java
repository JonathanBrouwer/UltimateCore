package Bammerbom.UltimateCore.API;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import Bammerbom.UltimateCore.Resources.FireworkEffectPlayer;

public class UCworld{
	World base;
	public UCworld(World w){
		base = w;
	}
	public UCworld(String world) {
		try{
			if(world == null || Bukkit.getWorld(world) == null) throw new NullPointerException("World not found");
			base = new UCworld(Bukkit.getWorld(world)).getWorld();
		}catch(NullPointerException ex){
			throw new NullPointerException("World not found");
		}
	}
	public World getWorld(){
		return base;
	}
	public void playFirework(Location l, FireworkEffect ef){
		FireworkEffectPlayer.play(l, ef);
	}
	public static enum WorldFlag{
		MONSTER, ANIMAL, PVP
	}
	public File getDataFile(){
		return new File(Bukkit.getPluginManager().getPlugin("UltimateCore").getDataFolder() + File.separator + "Data", "worlds.yml");
	}
	public boolean isFlagDenied(WorldFlag f){
		File file = getDataFile();
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
		if(!conf.contains("worldflags." + getWorld().getName() + "." + f.toString().toLowerCase())) return false;
		return !conf.getBoolean("worldflags." + getWorld().getName() + "." + f.toString().toLowerCase());
	}
	public boolean isFlagAllowed(WorldFlag f){
		return !isFlagDenied(f);
	}
	public void setFlagAllowed(WorldFlag f){
		File file = getDataFile();
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
		conf.set("worldflags." + getWorld().getName() + "." + f.toString().toLowerCase(), true);
		try {
			conf.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setFlagDenied(WorldFlag f){
		File file = getDataFile();
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
		conf.set("worldflags." + getWorld().getName() + "." + f.toString().toLowerCase(), false);
		try {
			conf.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
