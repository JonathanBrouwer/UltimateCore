package Bammerbom.UltimateCore.Minigames;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.GhostsUtil;
import Bammerbom.UltimateCore.Resources.Utils.InventoryUtil;

/**
* @Author Jake + Edit by Bammerbom
*/
public class MinigameManager implements Listener{

static Plugin plugin;
public MinigameManager(Plugin instance){
	plugin = instance;
	plugin.getServer().getPluginManager().registerEvents(this, plugin);
}

//A method for getting one of the Arenas out of the list by name:
public Arena getArena(String name) {
for (Arena a: Arena.arenaObjects) { //For all of the arenas in the list of objects
if (a.getName().equals(name)) { //If the name of an arena object in the list is equal to the one in the parameter...
return a; //Return that object
}
}
return null; //No objects were found, return null
}


//A method for adding players
public void addPlayers(Player player, String arenaName) {

if (getArena(arenaName) != null) { //If the arena exsists

Arena arena = getArena(arenaName); //Create an arena for using in this method

if (!arena.isFull()) { //If the arena is not full

if (!arena.isInGame()) {

//Every check is complete, arena is joinable
InventoryUtil.saveInv(player, false);
player.setHealth(((Damageable) player).getMaxHealth()); //Heal the player
player.setFireTicks(0); //Heal the player even more ^ ^ ^

//Teleport to the arena's join location
player.teleport(arena.getJoinLocation());

//Add the player to the arena list
arena.getPlayers().add(player.getName()); //Add the players name to the arena

int playersLeft = arena.getStartPlayers() - arena.getPlayers().size(); //How many players needed to start
//Send the arena's players a message
arena.sendMessage(r.default1 + player.getName() + " has joined the arena! " + arena.getPlayers().size() + "/" + arena.getStartPlayers());


if (playersLeft == 0) { //IF there are 0 players needed to start the game
	if(!arena.isInCountdown()){
startCountdown(arenaName); //Start the arena, see the method way below :)
	}
}


} else { //Specifiend arena is in game, send the player an error message
player.sendMessage(r.error + "The arena you are looking for is ingame!");

}
} else { //Specified arena is full, send the player an error message
player.sendMessage(r.error + "The arena you are looking for is currently full!");
}

} else { //The arena doesn't exsist, send the player an error message
player.sendMessage(r.error + "The arena you are looking for could not be found!");
}

}

public void addSpectators(Player player, String arenaName){
	if(getArena(arenaName) == null){
		player.sendMessage(r.default1 + "The arena you are looking for could not be found!");
		return;
	}
	InventoryUtil.saveInv(player, false);
	Arena arena = getArena(arenaName);
	for(String ps : arena.getSpectators()){
		Player p = Bukkit.getPlayer(ps);
		player.showPlayer(p);
	}
	GhostsUtil.setGhost(player, true);
	for(String ps : arena.getPlayers()){
		Player p = Bukkit.getPlayer(ps);
		p.hidePlayer(player);
	}
}

//A method for removing players
public void removePlayer(Player player, String arenaName) {

if (getArena(arenaName) != null) { //If the arena exsists

Arena arena = getArena(arenaName); //Create an arena for using in this method

if (arena.getPlayers().contains(player.getName())) { //If the arena has the player already

	InventoryUtil.restore(player);
//Every check is complete, arena is leavable
player.setHealth(((Damageable)player).getMaxHealth()); //Heal the player
player.setFireTicks(0); //Heal the player even more ^ ^ ^

//Teleport to the arena's join location
player.teleport(arena.getEndLocation());

//remove the player to the arena list
arena.getPlayers().remove(player.getName()); //Removes the players name to the arena

//Send the arena's players a message
arena.sendMessage(r.default1 + player.getName() + " has left the Arena! " + arena.getPlayers().size() + "/" + arena.getStartPlayers());

}else if(arena.getSpectators().contains(player.getName())){
	arena.getSpectators().remove(player.getName());
	for(String ps : arena.getSpectators()){
		Player p = Bukkit.getPlayer(ps);
		player.showPlayer(p);
	}
	player.setHealth(((Damageable)player).getMaxHealth()); //Heal the player
	player.setFireTicks(0); //Heal the player even more ^ ^ ^
	player.teleport(arena.getEndLocation());

} else { //Specified arena doesn't have the player, send the player an error message
player.sendMessage(r.error + "Your not in the arena your looking for!");

}


} else { //The arena doesn't exsist, send the player an error message
player.sendMessage(r.error + "The arena you are looking for could not be found!");
}
}
HashMap<String, ArrayList<Integer>> cdt = new HashMap<String, ArrayList<Integer>>();
public void startCountdown(final String arenaName){
	if(getArena(arenaName) != null){
		Arena arena = getArena(arenaName);
		arena.setCountdown(true);
		String mes1 = ChatColor.GOLD + "Starting in " + ChatColor.AQUA;
		String mes2 = ChatColor.GOLD + " seconds...";
		task(arena, mes1 + "60" + mes2, 20L);
		task(arena, mes1 + "30" + mes2, 600L);
		task(arena, mes1 + "20" + mes2, 800L);
		task(arena, mes1 + "10" + mes2, 1000L);
		task(arena, mes1 + "5" + mes2, 1100L);
		task(arena, mes1 + "4" + mes2, 1120L);
		task(arena, mes1 + "3" + mes2, 1140L);
		task(arena, mes1 + "2" + mes2, 1160L);
		task(arena, mes1 + "1" + mes2, 1180L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				startArena(arenaName);
			}
		}, 1200L);
		
	}
}
private int task(final Arena arena, final String message, Long delay){
	Integer id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
		@Override
		public void run(){
			arena.sendMessage(message);
		}
	}, delay);
	ArrayList<Integer> cur = cdt.get(arena.getName());
	cur.add(id);
	cdt.put(arena.getName(), cur);
	return id;
}
public void stopCountdown(String arenaName){
	if(getArena(arenaName) != null){
		Arena arena = getArena(arenaName);
		arena.setCountdown(false);
		ArrayList<Integer> task = cdt.get(arenaName);
		for(Integer in : task){
			BukkitScheduler sch = Bukkit.getScheduler();
			if(sch.isCurrentlyRunning(in) || sch.isQueued(in)){
				sch.cancelTask(in);
			}
		}
		cdt.remove(arenaName);
	}
}
//A method for starting an Arena:
public void startArena(String arenaName) {

if (getArena(arenaName) != null) { //If the arena exsists
Arena arena = getArena(arenaName); //Create an arena for using in this method

arena.sendMessage(ChatColor.GOLD + "The arena has BEGUN!");

//Set ingame
arena.setInGame(true);
stopCountdown(arenaName);

for (String s: arena.getPlayers()) {//Loop through every player in the arena

Bukkit.getPlayer(s).teleport(arena.getStartLocation()); //Teleports the player to the arena start location

//Do custom stuff here, like give weapons etc, but for the purpose of this tutorial, i'll do nothing

}

}

}


//A method for ending an Arena:
public void endArena(String arenaName) {

if (getArena(arenaName) != null) { //If the arena exsists

Arena arena = getArena(arenaName); //Create an arena for using in this method

//Send them a message
arena.sendMessage(ChatColor.GOLD + "The arena has ended :(");

//Set ingame
arena.setInGame(false);

for (String s: arena.getPlayers()) {//Loop through every player in the arena

//Teleport them:

Player player = Bukkit.getPlayer(s); //Create a player by the name
player.teleport(arena.getEndLocation());

player.getInventory().clear(); //Clear the players inventory
player.setHealth(((Damageable)player).getMaxHealth()); //Heal the player
player.setFireTicks(0); //Heal the player even more ^ ^ ^

//Remove them all from the list
arena.getPlayers().remove(player.getName());

}


}
}


//And our final method, loading each arena
//This will be resonsible for creating each arena from the config, and creating an object to represent it
//Call this method in your main class, onEnable
public UltimateConfiguration mc = new UltimateConfiguration(UltimateFileLoader.DFminigames);
public File mcf = UltimateFileLoader.DFminigames;


public Integer loadArenas() {

//I just create a quick Config Variable, obviously don't do this.
//Use your own config file
UltimateConfiguration fc = mc; //If you just use this code, it will erorr, its null. Read the notes above, USE YOUR OWN CONFIGURATION FILE

//Youll get an error here, FOR THE LOVE OF GAWD READ THE NOTES ABOVE!!!
if(fc.get("arenas") == null) return 0;
for (String keys: fc.getConfigurationSection("arenas").getKeys(false)) { //For each arena name in the arena file

//Now lets get all of the values, and make an Arena object for each:
//Just to help me remember: Arena myArena = new Arena("My Arena", joinLocation, startLocation, endLocation, 17)

//Arena's name is keys
String type = fc.getString("arenas." + "keys." + "type");

World world = Bukkit.getWorld("arenas." + keys + ".joinW");
double joinX = fc.getDouble("arenas." + "keys." + "joinX");
double joinY = fc.getDouble("arenas." + "keys." + "joinY");
double joinZ = fc.getDouble("arenas." + "keys." + "joinZ");
float joinYAW = Float.valueOf(fc.getString("arenas." + "keys." + "joinYAW"));
float joinPITCH = Float.valueOf(fc.getString("arenas." + "keys." + "joinPITCH"));

Location joinLocation = new Location(world, joinX, joinY, joinZ, joinYAW, joinPITCH);

World world2 = Bukkit.getWorld("arenas." + "keys." + "startW");
double startX = fc.getDouble("arenas." + "keys." + "startX");
double startY = fc.getDouble("arenas." + "keys." + "startY");
double startZ = fc.getDouble("arenas." + "keys." + "startZ");
float startYAW = Float.valueOf(fc.getString("arenas." + "keys." + "startYAW"));
float startPITCH = Float.valueOf(fc.getString("arenas." + "keys." + "startPITCH"));

Location startLocation = new Location(world2, startX, startY, startZ, startYAW, startPITCH);

World world3 = Bukkit.getWorld("arenas." + "keys." + "endW");
double endX = fc.getDouble("arenas." + "keys." + "endX");
double endY = fc.getDouble("arenas." + "keys." + "endX");
double endZ = fc.getDouble("arenas." + "keys." + "endX");
float endYAW = Float.valueOf(fc.getString("arenas." + "keys." + "endYAW"));
float endPITCH = Float.valueOf(fc.getString("arenas." + "keys." + "endPITCH"));

Location endLocation = new Location(world3, endX, endY, endZ, endYAW, endPITCH);

int maxPlayers = fc.getInt("arenas." + keys + ".maxPlayers");
int startPlayers = fc.getInt("arenas." + keys + ".startPlayers");

//Now lets create an object to represent it:
new Arena(type, keys, joinLocation, startLocation, endLocation, maxPlayers, startPlayers);
}

return fc.getConfigurationSection("arenas").getKeys(false).size();
}

//Our final method, create arena!
@SuppressWarnings("unused")
public void createArena(String type, String arenaName, Location joinLocation, Location startLocation, Location endLocation, int maxPlayers, int startPlayers) {

//Now, lets create an arena object to represent it:
Arena arena = new Arena(type, arenaName, joinLocation, startLocation, endLocation, maxPlayers, startPlayers);

//Now here is where you would save it all to a file, again, im going to create a null UltimateConfiguration, USE YOUR OWN!!!
UltimateConfiguration fc = mc; //USE YOUR OWN PUNK

fc.set("arenas." + arenaName, null); //Set its name
//Now sets the other values

String path = "arenas." + arenaName + "."; //Shortcut
//Sets the paths
fc.set(path + "type", type);

fc.set(path + "joinW", joinLocation.getWorld().getName());
fc.set(path + "joinX", joinLocation.getX());
fc.set(path + "joinY", joinLocation.getY());
fc.set(path + "joinZ", joinLocation.getZ());
fc.set(path + "joinYAW", joinLocation.getYaw());
fc.set(path + "joinPITCH", joinLocation.getPitch());

fc.set(path + "startW", startLocation.getWorld().getName());
fc.set(path + "startX", startLocation.getX());
fc.set(path + "startY", startLocation.getY());
fc.set(path + "startZ", startLocation.getZ());
fc.set(path + "startYAW", startLocation.getYaw());
fc.set(path + "startPITCH", startLocation.getPitch());

fc.set(path + "endW", endLocation.getWorld().getName());
fc.set(path + "endX", endLocation.getX());
fc.set(path + "endY", endLocation.getY());
fc.set(path + "endZ", endLocation.getZ());
fc.set(path + "endYAW", endLocation.getYaw());
fc.set(path + "endPITCH", endLocation.getPitch());

fc.set(path + "maxPlayers", maxPlayers);
fc.set(path + "startPlayers", startPlayers);

fc.save(mcf);

}
//EVENTS
@EventHandler
public void playerQuit(PlayerQuitEvent e){
	try{
	for(String arenan : this.mc.getConfigurationSection("arenas").getKeys(false)){
		Arena arena = getArena(arenan);
		if(arena.getPlayers().contains(e.getPlayer().getName()) || arena.getSpectators().contains(e.getPlayer().getName())){
			removePlayer(e.getPlayer(), arenan);
		}
	}
	}catch(Exception ex){
		return;
	}
}
}
