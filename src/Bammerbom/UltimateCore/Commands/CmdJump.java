package Bammerbom.UltimateCore.Commands;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdJump {
	static Plugin plugin;
	public static final Set<Integer> HOLLOW_MATERIALS = new HashSet<Integer>();
	private static final HashSet<Byte> TRANSPARENT_MATERIALS = new HashSet<Byte>();
	@SuppressWarnings("deprecation")
	public CmdJump(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		// The player can stand inside these materials
			HOLLOW_MATERIALS.add(Material.AIR.getId());
			HOLLOW_MATERIALS.add(Material.SAPLING.getId());
			HOLLOW_MATERIALS.add(Material.POWERED_RAIL.getId());
			HOLLOW_MATERIALS.add(Material.DETECTOR_RAIL.getId());
			HOLLOW_MATERIALS.add(Material.LONG_GRASS.getId());
			HOLLOW_MATERIALS.add(Material.DEAD_BUSH.getId());
			HOLLOW_MATERIALS.add(Material.YELLOW_FLOWER.getId());
			HOLLOW_MATERIALS.add(Material.RED_ROSE.getId());
			HOLLOW_MATERIALS.add(Material.BROWN_MUSHROOM.getId());
			HOLLOW_MATERIALS.add(Material.RED_MUSHROOM.getId());
			HOLLOW_MATERIALS.add(Material.TORCH.getId());
			HOLLOW_MATERIALS.add(Material.REDSTONE_WIRE.getId());
			HOLLOW_MATERIALS.add(Material.SEEDS.getId());
			HOLLOW_MATERIALS.add(Material.SIGN_POST.getId());
			HOLLOW_MATERIALS.add(Material.WOODEN_DOOR.getId());
			HOLLOW_MATERIALS.add(Material.LADDER.getId());
			HOLLOW_MATERIALS.add(Material.RAILS.getId());
			HOLLOW_MATERIALS.add(Material.WALL_SIGN.getId());
			HOLLOW_MATERIALS.add(Material.LEVER.getId());
			HOLLOW_MATERIALS.add(Material.STONE_PLATE.getId());
			HOLLOW_MATERIALS.add(Material.IRON_DOOR_BLOCK.getId());
			HOLLOW_MATERIALS.add(Material.WOOD_PLATE.getId());
			HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_OFF.getId());
			HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_ON.getId());
			HOLLOW_MATERIALS.add(Material.STONE_BUTTON.getId());
			HOLLOW_MATERIALS.add(Material.SNOW.getId());
			HOLLOW_MATERIALS.add(Material.SUGAR_CANE_BLOCK.getId());
			HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_OFF.getId());
			HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_ON.getId());
			HOLLOW_MATERIALS.add(Material.PUMPKIN_STEM.getId());
			HOLLOW_MATERIALS.add(Material.MELON_STEM.getId());
			HOLLOW_MATERIALS.add(Material.VINE.getId());
			HOLLOW_MATERIALS.add(Material.FENCE_GATE.getId());
			HOLLOW_MATERIALS.add(Material.WATER_LILY.getId());
			HOLLOW_MATERIALS.add(Material.NETHER_WARTS.getId());

			try //1.6
			{
				HOLLOW_MATERIALS.add(Material.CARPET.getId());
			}
			catch (java.lang.NoSuchFieldError e)
			{
			}

			for (Integer integer : HOLLOW_MATERIALS)
			{
				TRANSPARENT_MATERIALS.add(integer.byteValue());
			}
			TRANSPARENT_MATERIALS.add((byte)Material.WATER.getId());
			TRANSPARENT_MATERIALS.add((byte)Material.STATIONARY_WATER.getId());
		
	}
	public static void handle(CommandSender sender, String[] args){
		if(!(r.isPlayer(sender))){
			return;
		}
		if(!r.perm(sender, "uc.jump", false, true)){
			return;
		}
		Player p = (Player)sender;
		Location loc;
		final Location cloc = p.getLocation();
		loc = getTarget(p);
		loc.setYaw(cloc.getYaw());
		loc.setPitch(cloc.getPitch());
		loc.setY(loc.getY() + 1);
		p.teleport(loc, TeleportCause.COMMAND);
	}
	@SuppressWarnings("deprecation")
	public static Location getTarget(final LivingEntity entity)
	{

		final Block block = entity.getTargetBlock(null, 300);
		if (block == null)
		{
			return null;
		}
		return block.getLocation();
	}
}