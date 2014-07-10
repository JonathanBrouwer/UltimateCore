package Bammerbom.UltimateCore.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UCworld;
import Bammerbom.UltimateCore.API.UCworld.WorldFlag;
import Bammerbom.UltimateCore.Resources.MobType;
import Bammerbom.UltimateCore.Resources.MobType.Enemies;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class CmdWorld implements Listener{
	static Plugin plugin;
	public CmdWorld(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void loadws(){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.DFworlds);
		if(data.get("worldlist") == null){ return; }
		for(String str : data.getStringList("worldlist")){
		    Bukkit.createWorld(new WorldCreator(str));
		}
	}
	public static void create(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.world", false, false) && !r.perm(sender, "uc.world.create", false, false)){
			sender.sendMessage(r.mes("NoPermissions"));
			return;
		}
		if(r.checkArgs(args, 0) == false){
			usage(sender);
			return;
		}
		//Usage = /a world create <Worldname> [WorldType]
		if(r.checkArgs(args, 1) == true){
			if(Bukkit.getWorld(args[1]) != null){
				sender.sendMessage(r.mes("World.AlreadyExist"));
				return;
			}
			if(!args[1].replaceAll("[a-zA-Z0-9]", "").replaceAll("_", "").equalsIgnoreCase("")){
				sender.sendMessage(r.mes("World.NotAlpha"));
				return;
			}
			WorldCreator settings = new WorldCreator(args[1]);
			Integer na = 2;
			for (int i = 0; i < args.length + 3; i++){

		if(r.checkArgs(args, na) == true){
			if(args[na].equalsIgnoreCase("flat") || args[na].equalsIgnoreCase("flatland")){
				settings.type(WorldType.FLAT);
			}else if(args[na].equalsIgnoreCase("large") || args[na].replaceAll("_", "").equalsIgnoreCase("largebiomes")){
				settings.type(WorldType.LARGE_BIOMES);
			}else if(args[na].equalsIgnoreCase("amplified")){
				settings.type(WorldType.AMPLIFIED);
			}else if(args[na].equalsIgnoreCase("normal")){
			}else if(args[na].equalsIgnoreCase("nether")){
				settings.environment(Environment.NETHER);
			}else if(args[na].equalsIgnoreCase("end")){
				settings.environment(Environment.THE_END);
			}else if(args[na].equalsIgnoreCase("nostructures")){
				settings.generateStructures(false);
			}else if(r.isNumber(args[na])){
				settings.seed(Long.parseLong(args[na]));
			}else if(args[na].startsWith("s:")){
				if(StringUtil.isAlphaNumeric(args[na])){
				String seed1 = args[na].replaceFirst("s:", "");
				settings.seed(Long.valueOf(seed1.hashCode()));
				}
			}else if(args[na].startsWith("g:")){
				String generator = args[na].replaceFirst("g:", "");
				settings.generator(generator);
			}
			na++;
		}
		
		}
			sender.sendMessage(r.mes("World.Create.Creating").replaceAll("%world", settings.name()));
			Bukkit.createWorld(settings);
			
			try{
				UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.DFworlds);
				List<String> worlds = data.getStringList("worldlist");
				if(worlds == null){ worlds = new ArrayList<String>(); }
				worlds.add(settings.name());
				data.set("worldlist", worlds);
				data.set("worlds." + settings.name(), settings.type().toString());
			data.save(UltimateFileLoader.DFworlds);
			}catch(NullPointerException e){
				UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.DFworlds);

				HashMap<String, String> worlds = new HashMap<String, String>();
				worlds.put(settings.name(), settings.environment().name());
				data.set("worlds", worlds);
				data.save(UltimateFileLoader.DFworlds);
			}
			
			sender.sendMessage(r.mes("World.Create.Created").replaceAll("%world", settings.name()));
		}else{
			sender.sendMessage(r.mes("World.Create.Usage"));
		}
		
	}
	public static void importw(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.world", false, false) && !r.perm(sender, "uc.world.import", false, false)){
			sender.sendMessage(r.mes("NoPermissions"));
			return;
		}
		if(r.checkArgs(args, 0) == false){
			usage(sender);
			return;
		}
		//
		if(r.checkArgs(args, 1) == true){
			if(!new File(args[1]).exists()){
				sender.sendMessage(r.mes("World.NotFound").replaceAll("%world", args[0]));
				return;
			}
			WorldCreator settings = new WorldCreator(args[1]);
			for (int i = 0; i < 10; i++){
		
		Integer na = 2;
		if(r.checkArgs(args, na) == true){
			if(args[na].equalsIgnoreCase("normal")){
			}else if(args[na].equalsIgnoreCase("nether")){
				settings.environment(Environment.NETHER);
			}else if(args[na].equalsIgnoreCase("end")){
				settings.environment(Environment.THE_END);
			}			
			
		}
			}
			sender.sendMessage(r.mes("World.Import.Importing").replaceAll("%world", settings.name()));
		Bukkit.createWorld(settings);
	
		try{
			UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.DFworlds);
			List<String> worlds = data.getStringList("worldlist");
			if(worlds == null){ worlds = new ArrayList<String>(); }
			worlds.add(settings.name());
			data.set("worldlist", worlds);
			data.set("worlds." + settings.name(), settings.type().toString());
			data.save(UltimateFileLoader.DFworlds);
			}catch(NullPointerException e){
				UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.DFworlds);
				data.set("worlds." + settings.name(), settings.type().toString());
				data.save(UltimateFileLoader.DFworlds);
			}
		sender.sendMessage(r.mes("World.Import.Imported").replaceAll("%world", settings.name()));
		}else{
			sender.sendMessage(r.mes("World.Import.Usage"));
		}
	}
	public static void list(CommandSender sender,  String[] args){
		if(!r.perm(sender, "uc.world", false, false) && !r.perm(sender, "uc.world.list", false, false)){
			sender.sendMessage(r.mes("NoPermissions"));
			return;
		}
		if(r.checkArgs(args, 0) == false){
			usage(sender);
			return;
		}
		//
	    List<World> worlds1 = Bukkit.getWorlds();
	    ArrayList<String> worlds = new ArrayList<String>();
	    for(World w : worlds1){
	    	worlds.add(w.getName());
	    }
		String iets = "";
		Integer hoeveel = 0;
		try{
			Integer amount = worlds.toArray().length;
			for(int i = 0; i < amount; i++){
				iets = iets + worlds.get(hoeveel) + ", ";
				hoeveel++;
				
			}
			iets = iets.substring(0, iets.length()-1);
			iets = iets.substring(0, iets.length()-1);
		}
		catch(IndexOutOfBoundsException ex){
		}
		sender.sendMessage(r.mes("World.List").replaceAll("%worlds", iets));
	}
	public static void remove(CommandSender sender,  String[] args){
		if(!r.perm(sender, "uc.world", false, false) && !r.perm(sender, "uc.world.remove", false, false)){
			sender.sendMessage(r.mes("NoPermissions"));
			return;
		}
		if(r.checkArgs(args, 0) == false){
			usage(sender);
			return;
		}
		//
		if(r.checkArgs(args, 1) == true){
			World world = Bukkit.getWorld(args[1]);
			if(world == null){
				sender.sendMessage(r.mes("World.NotFound").replaceAll("%world", args[1]));
				return;
			}
			for(Player pl : Bukkit.getOnlinePlayers()){
				if(pl.getWorld().equals(world)){
					World w2 = Bukkit.getWorlds().get(0);
					pl.teleport(w2.getSpawnLocation(), TeleportCause.PLUGIN);
				}
			}
			Bukkit.getServer().unloadWorld(world, true);
			WorldCreator settings = new WorldCreator(args[1]);
			try{
				UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.DFworlds);
				data.set("worlds." + world.getName(), null);
				List<String> worldlist = data.getStringList("worldlist");
				if(worldlist == null){ worldlist = new ArrayList<String>(); }
				worldlist.remove(settings.name());
				data.set("worldlist", worldlist);
				data.save(UltimateFileLoader.DFworlds);
				}catch(NullPointerException e){
					UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.DFworlds);
					HashMap<String, String> worlds = new HashMap<String, String>();
					worlds.remove(settings.name());
					data.set("worlds", worlds);
					List<String> worldlist = data.getStringList("worldlist");
					if(worldlist == null){ worldlist = new ArrayList<String>(); }
					worldlist.remove(settings.name());
					data.set("worldlist", worldlist);
					data.save(UltimateFileLoader.DFworlds);
				}
			sender.sendMessage(r.mes("World.Remove").replaceAll("%world", world.getName()));
		}else{
			sender.sendMessage(r.default1 + "/world" + r.default2 + " remove [World]");
		}
	}
	public static void reset(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.world", false, false) && !r.perm(sender, "uc.world.reset", false, false)){
			sender.sendMessage(r.mes("NoPermissions"));
			return;
		}
		if(r.checkArgs(args, 1) == false){
			usage(sender);
			return;
		}
		if(r.checkArgs(args, 1) == true){
			World world = Bukkit.getWorld(args[1]);
			if(world == null){
				sender.sendMessage(r.mes("World.NotFound").replaceAll("%world", args[1]));
				return;
			}
			/*Boolean found = false;
				for(World w : Bukkit.getWorlds()){
					if(w.getEnvironment().equals(Environment.NORMAL)){
						if(!w.equals(world)){
							for(Player p : world.getPlayers()){
								p.teleport(w.getSpawnLocation());
							}
							found = true;
							break;
						}
					}
				}
				*/
			if(world.getPlayers().size() > 0){
				sender.sendMessage(r.default1 + "Can't reset world with players inside.");
				return;
			}
			String i = "";
			while(new File(world.getWorldFolder().getName() + "_OLD" + i).exists()){
				if(i.equalsIgnoreCase("")){
					i = "1";
				}else{
					Integer a = Integer.parseInt(i);
					a++;
					i = a + "";
				}
			}
			File f = new File(world.getWorldFolder().getName() + "_OLD" + i);
			new File(world.getWorldFolder().getAbsolutePath()).renameTo(f);
			sender.sendMessage(r.default1 + "Resetting world: " + world.getName());
			resetAll(world);
			sender.sendMessage(r.default1 + "Reset complete!");
		}
		
	}
	private static void resetAll(World world) {
		world.save();
		Bukkit.unloadWorld(world, true);
		File dir = world.getWorldFolder();
		for(File f : dir.listFiles()){
			if(!f.getName().contains("player")){
				f.delete();
			}
		}
		WorldCreator creator = new WorldCreator(world.getName());
		creator.seed(world.getSeed());
		creator.environment(world.getEnvironment());
		creator.generator(world.getGenerator());
		creator.generateStructures(world.canGenerateStructures());
		creator.type(world.getWorldType());
		World world2 = Bukkit.createWorld(creator);
		world2.save();
		
    }
	public static void tp(CommandSender sender,  String[] args){
		if(!r.perm(sender, "uc.world", false, false) && !r.perm(sender, "uc.world.tp", false, false)){
			sender.sendMessage(r.mes("NoPermissions"));
			return;
		}
		if(r.checkArgs(args, 1) == false){
			usage(sender);
			return;
		}
		//
		if(r.checkArgs(args, 1) == true){
			World world = Bukkit.getWorld(args[1]);
			if(world == null){
				sender.sendMessage(r.mes("World.NotFound").replaceAll("%world", args[1]));
				return;
			}
			if(!r.isPlayer(sender)){
				return;
			}
			final Player p = (Player) sender;
			final Location loc = world.getSpawnLocation();
			p.teleport(loc, TeleportCause.COMMAND);
		}
	}
	public static void flag(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.world", false, false) && !r.perm(sender, "uc.world.flag", false, false)){
			sender.sendMessage(r.mes("NoPermissions"));
			return;
		}
		if(r.checkArgs(args, 3) == false){
			usage(sender);
			return;
		}
		//world flag [world] [flag] [value]
		//true = allow, false = deny
		//monster, animal, pvp
		
		if(r.checkArgs(args, 3) == true){
			UCworld world = new UCworld(Bukkit.getWorld(args[1]));
			String flag = args[2];
			String value = args[3];
			if(flag.equalsIgnoreCase("monster") || flag.equalsIgnoreCase("monsterspawn")){
				if(value.equalsIgnoreCase("deny")){
				for(Entity en : world.getWorld().getEntities()){
					if(en instanceof Monster){
					  en.remove();
					}
				}
				    world.setFlagDenied(WorldFlag.MONSTER);
				    sender.sendMessage(r.default1 + "Monster for " + world.getWorld().getName() + " set to " + value);
				}else if(value.equalsIgnoreCase("allow")){
					world.setFlagAllowed(WorldFlag.MONSTER);
				    sender.sendMessage(r.default1 + "Flag monster for " + world.getWorld().getName() + " set to " + value);
				}else{
					sender.sendMessage(r.default2 + "/world " + r.default2 + "flag <World> <Flag> <Allow/Deny>");
					sender.sendMessage(r.default1 + "Flags: " + r.default2 + StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
				}
			}else if(flag.equalsIgnoreCase("animal") || flag.equalsIgnoreCase("animalspawn")){
				if(value.equalsIgnoreCase("deny")){
					for(Entity en : world.getWorld().getEntities()){
						if(en instanceof Animals){
						  en.remove();
						}
					}
					world.setFlagDenied(WorldFlag.ANIMAL);
				    sender.sendMessage(r.default1 + "Flag animal for " + world.getWorld().getName() + " set to " + value);
				}else if(value.equalsIgnoreCase("allow")){
					world.setFlagAllowed(WorldFlag.ANIMAL);
				    sender.sendMessage(r.default1 + "Flag animal for " + world.getWorld().getName() + " set to " + value);
				}else{
					sender.sendMessage(r.default2 + "/world " + r.default2 + "flag <World> <Flag> <Allow/Deny>");
					sender.sendMessage(r.default1 + "Flags: " + r.default2 + StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
				}
			}else if(flag.equalsIgnoreCase("pvp")){
				if(value.equalsIgnoreCase("deny")){
					world.setFlagDenied(WorldFlag.PVP);
				    sender.sendMessage(r.default1 + "Flag pvp for " + world.getWorld().getName() + " set to " + value);
				}else if(value.equalsIgnoreCase("allow")){
					world.setFlagAllowed(WorldFlag.PVP);
				    sender.sendMessage(r.default1 + "Flag pvp for " + world.getWorld().getName() + " set to " + value);
				}else{
					sender.sendMessage(r.default2 + "/world " + r.default2 + "flag <World> <Flag> <Allow/Deny>");
					sender.sendMessage(r.default1 + "Flags: " + r.default2 + StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
				}
			}else{
				sender.sendMessage(r.default2 + "/world " + r.default2 + "flag <World> <Flag> <Allow/Deny>");
				sender.sendMessage(r.default1 + "Flags: " + r.default2 + StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
			}
		}else{
			sender.sendMessage(r.default2 + "/world " + r.default2 + "flag <World> <Flag> <Allow/Deny>");
			sender.sendMessage(r.default1 + "Flags: " + r.default2 + StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void mobSpawn(CreatureSpawnEvent e){
		if(e.getSpawnReason().equals(SpawnReason.SPAWNER_EGG) || e.getSpawnReason().equals(SpawnReason.CUSTOM)){
			return;
		}
		
		if(e.getEntity() instanceof Monster || MobType.fromBukkitType(e.getEntityType()).type.equals(Enemies.ENEMY) || e.getEntityType().equals(EntityType.GHAST) || e.getEntityType().equals(EntityType.SLIME)){
			UCworld w = new UCworld(e.getLocation().getWorld());
			if(w.isFlagDenied(WorldFlag.MONSTER)){
			  e.setCancelled(true);
			}else{
			}
		}
		if(e.getEntity() instanceof Animals || (MobType.fromBukkitType(e.getEntityType()).type.equals(Enemies.FRIENDLY)) || MobType.fromBukkitType(e.getEntityType()).type.equals(Enemies.NEUTRAL) || e.getEntityType().equals(EntityType.SQUID)){
			UCworld w = new UCworld(e.getLocation().getWorld());
			if(w.isFlagDenied(WorldFlag.ANIMAL)){
			  e.setCancelled(true);
			}else{
			}
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void pvp(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
		    UCworld w = new UCworld(e.getEntity().getWorld());
		    if(w.isFlagDenied(WorldFlag.PVP)) e.setCancelled(true);
		}
	}
	public static void usage(CommandSender sender){
		sender.sendMessage(r.default1 + "/world commands:");
		sender.sendMessage(r.default2 + "/world " + r.default2 + "create <Name> [Type...] [Type...]");
		sender.sendMessage(r.default1 + "Types:" + r.default2 + " flat, largebiomes, amplified, normal, nether, end, nostructures, [SEED]");
		sender.sendMessage(r.default2 + "/world " + r.default2 + "import <Name> [Normal/Nether/End]");
		sender.sendMessage(r.default2 + "/world " + r.default2 + "remove <Name>");
		sender.sendMessage(r.default2 + "/world " + r.default2 + "list");
		sender.sendMessage(r.default2 + "/world " + r.default2 + "tp <World>");
		sender.sendMessage(r.default2 + "/world " + r.default2 + "flag <World> <Flag> <Allow/Deny>");
		sender.sendMessage(r.default1 + "Flags: " + r.default2 + StringUtil.firstUpperCase(StringUtil.joinList(WorldFlag.values()).toLowerCase()));
		sender.sendMessage(r.default2 + "/world " + r.default2 + "reset <World>");
		return;
	}
}
