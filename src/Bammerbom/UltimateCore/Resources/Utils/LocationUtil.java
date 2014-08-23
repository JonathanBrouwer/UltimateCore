package Bammerbom.UltimateCore.Resources.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class LocationUtil {
    private static final Set<Material> HOLLOW_MATERIALS = new HashSet<Material>();
	private static final HashSet<Material> TRANSPARENT_MATERIALS = new HashSet<Material>();
	public static final Vector3D[] VOLUME;
    public static Location convertStringToLocation(String s) {
        String[] split = s.split("\\|");
        return new Location(
        		Bukkit.getWorld(split[0])
        		, Double.parseDouble(split[1])
        		, Double.parseDouble(split[2])
        		, Double.parseDouble(split[3])
        		, Float.parseFloat(split[4])
        		, Float.parseFloat(split[5]));
    }
    public static String convertLocationToString(Location loc) {
        return loc.getWorld().getName() + "|" + loc.getX() + "|" + loc.getY() + "|" + loc.getZ() + "|" + loc.getPitch() + "|" + loc.getYaw();
    }
    public static Location searchSafeLocation(Location loc){
        if ((loc == null) || (loc.getWorld() == null)){
          return null;
        }
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int)Math.round(loc.getY());
        int z = loc.getBlockZ();
        int origX = x;
        int origY = y;
        int origZ = z;
        while (isBlockAboveAir(world, x, y, z))
        {
          y--;
          if (y < 0)
          {
            y = origY;
          }
        }

        if (isBlockUnsafe(world, x, y, z))
        {
          x = Math.round(loc.getX()) == origX ? x - 1 : x + 1;
          z = Math.round(loc.getZ()) == origZ ? z - 1 : z + 1;
        }
        int i = 0;
        while (isBlockUnsafe(world, x, y, z))
        {
          i++;
          if (i >= VOLUME.length)
          {
            x = origX;
            y = origY + 3;
            z = origZ;
            break;
          }
          x = origX + VOLUME[i].x;
          y = origY + VOLUME[i].y;
          z = origZ + VOLUME[i].z;
        }
        while (isBlockUnsafe(world, x, y, z))
        {
          y++;
          if (y >= world.getMaxHeight())
          {
            x++;
          }
        }

        while (isBlockUnsafe(world, x, y, z))
        {
          y--;
          if (y <= 1)
          {
            x++;
            y = world.getHighestBlockYAt(x, z);
            if (x - 48 > loc.getBlockX())
            {
              return null;
            }
          }
        }
        return new Location(world, x + 0.5D, y, z + 0.5D, loc.getYaw(), loc.getPitch());
    }
    @SuppressWarnings("deprecation")
	public static Location getTarget(LivingEntity entity){
    	HashSet<Byte> TMb = new HashSet<Byte>();
    	for(Material mat : TRANSPARENT_MATERIALS){
    		TMb.add((byte) mat.getId());
    	}
    	Block block = entity.getTargetBlock(TMb, 300);
    	if (block == null){
    	    return null;
    	}
    	return block.getLocation();
    }
    public static void teleport(Player p, Entity l, TeleportCause c){
    	teleport(p, l.getLocation(), c);
    }
    public static void teleportUnsafe(Player p, Entity l, TeleportCause c){
    	teleportUnsafe(p, l.getLocation(), c);
    }
    public static void teleport(Player p, Location l, TeleportCause c){
    	if(!p.getAllowFlight()){
    		l = searchSafeLocation(l) != null ? searchSafeLocation(l) : l;
    	}else{
    		p.setFlying(true);
    	}
    	if(p.isInsideVehicle()) p.leaveVehicle();
    	p.teleport(l, c);
    }
    public static void teleportUnsafe(Player p, Location l, TeleportCause c){
    	if(p.isInsideVehicle()) p.leaveVehicle();
    	p.teleport(l, c);
    }
    
    //
    private static boolean isBlockAboveAir(World world, int x, int y, int z)
    {
      if (y > world.getMaxHeight())
      {
        return true;
      }
      return HOLLOW_MATERIALS.contains(world.getBlockAt(x, y - 1, z).getType());
    }

    private static boolean isBlockUnsafe(World world, int x, int y, int z)
    {
      if (isBlockDamaging(world, x, y, z))
      {
        return true;
      }
      return isBlockAboveAir(world, x, y, z);
    }

    private static boolean isBlockDamaging(World world, int x, int y, int z)
    {
      Block below = world.getBlockAt(x, y - 1, z);
      if ((below.getType() == Material.LAVA) || (below.getType() == Material.STATIONARY_LAVA))
      {
        return true;
      }
      if (below.getType() == Material.FIRE)
      {
        return true;
      }
      if (below.getType() == Material.BED_BLOCK)
      {
        return true;
      }
      if ((!HOLLOW_MATERIALS.contains(world.getBlockAt(x, y, z).getType())) || (!HOLLOW_MATERIALS.contains(world.getBlockAt(x, y + 1, z).getType())))
      {
        return true;
      }
      return false;
    }
    static
    {
      HOLLOW_MATERIALS.add(Material.AIR);
      HOLLOW_MATERIALS.add(Material.SAPLING);
      HOLLOW_MATERIALS.add(Material.POWERED_RAIL);
      HOLLOW_MATERIALS.add(Material.DETECTOR_RAIL);
      HOLLOW_MATERIALS.add(Material.LONG_GRASS);
      HOLLOW_MATERIALS.add(Material.DEAD_BUSH);
      HOLLOW_MATERIALS.add(Material.YELLOW_FLOWER);
      HOLLOW_MATERIALS.add(Material.RED_ROSE);
      HOLLOW_MATERIALS.add(Material.BROWN_MUSHROOM);
      HOLLOW_MATERIALS.add(Material.RED_MUSHROOM);
      HOLLOW_MATERIALS.add(Material.TORCH);
      HOLLOW_MATERIALS.add(Material.REDSTONE_WIRE);
      HOLLOW_MATERIALS.add(Material.SEEDS);
      HOLLOW_MATERIALS.add(Material.SIGN_POST);
      HOLLOW_MATERIALS.add(Material.WOODEN_DOOR);
      HOLLOW_MATERIALS.add(Material.LADDER);
      HOLLOW_MATERIALS.add(Material.RAILS);
      HOLLOW_MATERIALS.add(Material.WALL_SIGN);
      HOLLOW_MATERIALS.add(Material.LEVER);
      HOLLOW_MATERIALS.add(Material.STONE_PLATE);
      HOLLOW_MATERIALS.add(Material.IRON_DOOR_BLOCK);
      HOLLOW_MATERIALS.add(Material.WOOD_PLATE);
      HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_OFF);
      HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_ON);
      HOLLOW_MATERIALS.add(Material.STONE_BUTTON);
      HOLLOW_MATERIALS.add(Material.SNOW);
      HOLLOW_MATERIALS.add(Material.SUGAR_CANE_BLOCK);
      HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_OFF);
      HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_ON);
      HOLLOW_MATERIALS.add(Material.PUMPKIN_STEM);
      HOLLOW_MATERIALS.add(Material.MELON_STEM);
      HOLLOW_MATERIALS.add(Material.VINE);
      HOLLOW_MATERIALS.add(Material.FENCE_GATE);
      HOLLOW_MATERIALS.add(Material.WATER_LILY);
      HOLLOW_MATERIALS.add(Material.NETHER_WARTS);
      try
      {
        HOLLOW_MATERIALS.add(Material.CARPET);
      }
      catch (NoSuchFieldError e)
      {
      }
      for (Material mat : HOLLOW_MATERIALS)
      {
        TRANSPARENT_MATERIALS.add(mat);
      }
      TRANSPARENT_MATERIALS.add(Material.WATER);
      TRANSPARENT_MATERIALS.add(Material.STATIONARY_WATER);
      List<Vector3D> pos = new ArrayList<Vector3D>();
      for (int x = -3; x <= 3; x++)
      {
        for (int y = -3; y <= 3; y++)
        {
          for (int z = -3; z <= 3; z++)
          {
            pos.add(new Vector3D(x, y, z));
          }
        }
      }
      Collections.sort(pos, new Comparator<Vector3D>()
      {
        public int compare(Vector3D a, Vector3D b)
        {
          return a.x * a.x + a.y * a.y + a.z * a.z - (b.x * b.x + b.y * b.y + b.z * b.z);
        }
      });
      VOLUME = (Vector3D[])pos.toArray(new Vector3D[0]);
    }
    private static class Vector3D
    {
      public int x;
      public int y;
      public int z;

      public Vector3D(int x, int y, int z)
      {
        this.x = x;
        this.y = y;
        this.z = z;
      }
    }
}
