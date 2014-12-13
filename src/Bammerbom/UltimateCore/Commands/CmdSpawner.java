package Bammerbom.UltimateCore.Commands;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.MobType;

public class CmdSpawner {
	static Plugin plugin;
	public CmdSpawner(Plugin instance){
		plugin = instance;
	}
	@SuppressWarnings("deprecation")
	public static void handle(CommandSender sender, String[] args){
		if(!r.isPlayer(sender)) return;
		if(!r.perm(sender, "uc.spawner", false,true))return;
		Player p = (Player) sender;
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.mes("Spawner.Usage"));
			return;
		}
		Block b = p.getTargetBlock(null, 10);
		if(!(b.getState() instanceof CreatureSpawner)){
		    p.sendMessage(r.mes("Spawner.NotLooking"));
			return;
		}
		CreatureSpawner c = (CreatureSpawner) b.getState();
		MobType m = MobType.fromName(args[0]);
		if(m == null || m.getType() == null || m.getType().equals(EntityType.UNKNOWN) || !m.getType().isSpawnable()){
			sender.sendMessage(r.mes("Spawner.NotFound").replaceAll("%MobType",args[0]));
			return;
		}
		c.setSpawnedType(m.getType());
		c.update();
		p.sendMessage(r.mes("Spawner.Set").replaceAll("%Name", m.name().toLowerCase()));
	}
}
