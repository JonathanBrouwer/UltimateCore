package Bammerbom.UltimateCore.Resources;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

@SuppressWarnings({ "rawtypes", "unchecked" })
public enum MobType
{
  CHICKEN("Chicken", Enemies.FRIENDLY, EntityType.CHICKEN), 
  COW("Cow", Enemies.FRIENDLY, EntityType.COW), 
  CREEPER("Creeper", Enemies.ENEMY, EntityType.CREEPER), 
  GHAST("Ghast", Enemies.ENEMY, EntityType.GHAST), 
  GIANT("Giant", Enemies.ENEMY, EntityType.GIANT), 
  HORSE("Horse", Enemies.FRIENDLY, EntityType.HORSE), 
  PIG("Pig", Enemies.FRIENDLY, EntityType.PIG), 
  PIGZOMB("PigZombie", Enemies.NEUTRAL, EntityType.PIG_ZOMBIE), 
  SHEEP("Sheep", Enemies.FRIENDLY, "", EntityType.SHEEP), 
  SKELETON("Skeleton", Enemies.ENEMY, EntityType.SKELETON), 
  SLIME("Slime", Enemies.ENEMY, EntityType.SLIME), 
  SPIDER("Spider", Enemies.ENEMY, EntityType.SPIDER), 
  SQUID("Squid", Enemies.FRIENDLY, EntityType.SQUID), 
  ZOMBIE("Zombie", Enemies.ENEMY, EntityType.ZOMBIE), 
  WOLF("Wolf", Enemies.NEUTRAL, "", EntityType.WOLF), 
  WITHERSKELETON("Witherskeleton", Enemies.ENEMY, EntityType.SKELETON),
  CAVESPIDER("CaveSpider", Enemies.ENEMY, EntityType.CAVE_SPIDER), 
  ENDERMAN("Enderman", Enemies.ENEMY, "", EntityType.ENDERMAN), 
  SILVERFISH("Silverfish", Enemies.ENEMY, "", EntityType.SILVERFISH), 
  ENDERDRAGON("EnderDragon", Enemies.ENEMY, EntityType.ENDER_DRAGON), 
  VILLAGER("Villager", Enemies.FRIENDLY, EntityType.VILLAGER), 
  BLAZE("Blaze", Enemies.ENEMY, EntityType.BLAZE), 
  MUSHROOMCOW("MushroomCow", Enemies.FRIENDLY, EntityType.MUSHROOM_COW), 
  MAGMACUBE("MagmaCube", Enemies.ENEMY, EntityType.MAGMA_CUBE), 
  SNOWMAN("Snowman", Enemies.FRIENDLY, "", EntityType.SNOWMAN), 
  OCELOT("Ocelot", Enemies.NEUTRAL, EntityType.OCELOT), 
  IRONGOLEM("IronGolem", Enemies.NEUTRAL, EntityType.IRON_GOLEM), 
  WITHER("Wither", Enemies.ENEMY, EntityType.WITHER), 
  BAT("Bat", Enemies.FRIENDLY, EntityType.BAT), 
  WITCH("Witch", Enemies.ENEMY, EntityType.WITCH), 
  BOAT("Boat", Enemies.NEUTRAL, EntityType.BOAT), 
  MINECART("Minecart", Enemies.NEUTRAL, EntityType.MINECART), 
  MINECART_CHEST("ChestMinecart", Enemies.NEUTRAL, EntityType.MINECART_CHEST), 
  MINECART_FURNACE("FurnaceMinecart", Enemies.NEUTRAL, EntityType.MINECART_FURNACE), 
  MINECART_TNT("TNTMinecart", Enemies.NEUTRAL, EntityType.MINECART_TNT), 
  MINECART_HOPPER("HopperMinecart", Enemies.NEUTRAL, EntityType.MINECART_HOPPER), 
  MINECART_MOB_SPAWNER("SpawnerMinecart", Enemies.NEUTRAL, EntityType.MINECART_MOB_SPAWNER), 
  ENDERCRYSTAL("EnderCrystal", Enemies.NEUTRAL, EntityType.ENDER_CRYSTAL), 
  EXPERIENCEORB("ExperienceOrb", Enemies.NEUTRAL, EntityType.EXPERIENCE_ORB);

  public static final Logger logger;
  public String suffix = "s";
  public final String name;
  public final Enemies type;
  private final EntityType bukkitType;
  private static final Map<String, MobType> hashMap;
  private static final Map<EntityType, MobType> bukkitMap;

  private MobType(String n, Enemies en, String s, EntityType type) { this.suffix = s;
    this.name = n;
    this.type = en;
    this.bukkitType = type;
  }

  private MobType(String n, Enemies en, EntityType type)
  {
    this.name = n;
    this.type = en;
    this.bukkitType = type;
  }

  public static Set<String> getMobList()
  {
    return Collections.unmodifiableSet(hashMap.keySet());
  }

  public Entity spawn(World world, Server server, Location loc) throws MobType.MobException
  {
    Entity entity = world.spawn(loc, this.bukkitType.getEntityClass());
    if (entity == null)
    {
      throw new MobException();
    }
    return entity;
  }

  public EntityType getType()
  {
    return this.bukkitType;
  }

  public static MobType fromName(String name)
  {
    return (MobType)hashMap.get(name.toLowerCase(Locale.ENGLISH));
  }

  public static MobType fromBukkitType(EntityType type)
  {
    return (MobType)bukkitMap.get(type);
  }

  static
  {
    logger = Logger.getLogger("Minecraft");

    hashMap = new HashMap();
    bukkitMap = new HashMap();

    for (MobType mob : values())
    {
      hashMap.put(mob.name.toLowerCase(Locale.ENGLISH), mob);
      bukkitMap.put(mob.bukkitType, mob);
    }
  }

  public static class MobException extends Exception
  {
    private static final long serialVersionUID = 1L;
  }

  public static enum Enemies
  {
    FRIENDLY("friendly"), 
    NEUTRAL("neutral"), 
    ENEMY("enemy");

    protected final String type;

    private Enemies(String type) { this.type = type; }

  }
}