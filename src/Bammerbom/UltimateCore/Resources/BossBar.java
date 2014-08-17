package Bammerbom.UltimateCore.Resources;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import Bammerbom.UltimateCore.r;

public class BossBar
  implements Listener
{
  private static HashMap<UUID, FakeDragon> players = new HashMap<UUID, FakeDragon>();
  private static HashMap<UUID, Integer> timers = new HashMap<UUID, Integer>();
  private static Plugin plugin;

  public BossBar(Plugin pl)
  {
    pl.getServer().getPluginManager().registerEvents(this, pl);
    plugin = pl;
  }
  public static void disable(){
	  for (Player player : r.getOnlinePlayers()) {
	      quit(player);
	    }

	    players.clear();

	    for (Iterator<Integer> i$ = timers.values().iterator(); i$.hasNext(); ) {
	      int timerID = ((Integer)i$.next()).intValue();
	      Bukkit.getScheduler().cancelTask(timerID);
	    }

	    timers.clear();
  }
  @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
  public void PlayerLoggout(PlayerQuitEvent event)
  {
    quit(event.getPlayer());
  }

  @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
  public void onPlayerKick(PlayerKickEvent event) {
    quit(event.getPlayer());
  }

  @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    handleTeleport(event.getPlayer(), event.getTo().clone());
  }

  @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
  public void onPlayerTeleport(PlayerRespawnEvent event) {
    handleTeleport(event.getPlayer(), event.getRespawnLocation().clone());
  }

  public static void handleTeleport(final Player player, final Location loc)
  {
    if (!hasBar(player)) {
      return;
    }
    Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
    {
      public void run()
      {
        FakeDragon oldDragon = BossBar.getDragon(player, "");

        float health = oldDragon.health;
        String message = oldDragon.name;

        Util.sendPacket(player, BossBar.getDragon(player, "").getDestroyPacket());

        BossBar.players.remove(player.getUniqueId());

        FakeDragon dragon = BossBar.addDragon(player, loc, message);
        dragon.health = health;

        BossBar.sendDragon(dragon, player);
      }
    }
    , 2L);
  }

  private static void quit(Player player) {
    removeBar(player);
  }

  public static void setMessage(Player player, String message) {
    FakeDragon dragon = getDragon(player, message);

    dragon.name = cleanMessage(message);
    dragon.health = 200.0F;

    cancelTimer(player);

    sendDragon(dragon, player);
  }

  public static void setMessage(Player player, String message, float percent)
  {
    FakeDragon dragon = getDragon(player, message);

    dragon.name = cleanMessage(message);
    dragon.health = (percent / 100.0F * 200.0F);

    cancelTimer(player);

    sendDragon(dragon, player);
  }

  public static void setMessage(final Player player, String message, int seconds) {
    FakeDragon dragon = getDragon(player, message);

    dragon.name = cleanMessage(message);
    dragon.health = 200.0F;

    final int dragonHealthMinus = 200 / seconds;

    cancelTimer(player);

    timers.put(player.getUniqueId(), 
      Integer.valueOf(Bukkit.getScheduler().runTaskTimer(plugin, new BukkitRunnable()
    {
      public void run()
      {
        FakeDragon drag = BossBar.getDragon(player, "");
        drag.health -= dragonHealthMinus;

        if (drag.health <= 1.0F) {
          BossBar.removeBar(player);
          BossBar.cancelTimer(player);
        } else {
          BossBar.sendDragon(drag, player);
        }
      }
    }
    , 20L, 20L).getTaskId()));

    sendDragon(dragon, player);
  }

  public static boolean hasBar(Player player) {
    return players.get(player.getUniqueId()) != null;
  }

  public static void removeBar(Player player) {
    if (!hasBar(player)) {
      return;
    }
    Util.sendPacket(player, getDragon(player, "").getDestroyPacket());

    players.remove(player.getUniqueId());

    cancelTimer(player);
  }

  public static void setHealth(Player player, float percent) {
    if (!hasBar(player)) {
      return;
    }
    FakeDragon dragon = getDragon(player, "");
    dragon.health = (percent / 100.0F * 200.0F);

    cancelTimer(player);

    sendDragon(dragon, player);
  }

  public static float getHealth(Player player) {
    if (!hasBar(player)) {
      return -1.0F;
    }
    return getDragon(player, "").health;
  }

  public static String getMessage(Player player) {
    if (!hasBar(player)) {
      return "";
    }
    return getDragon(player, "").name;
  }

  private static String cleanMessage(String message) {
    if (message.length() > 64) {
      message = message.substring(0, 63);
    }
    return message;
  }

  private static void cancelTimer(Player player) {
    Integer timerID = (Integer)timers.remove(player.getUniqueId());

    if (timerID != null)
      Bukkit.getScheduler().cancelTask(timerID.intValue());
  }

  private static void sendDragon(FakeDragon dragon, Player player)
  {
    Util.sendPacket(player, dragon.getMetaPacket(dragon.getWatcher()));
    Util.sendPacket(player, dragon.getTeleportPacket(player.getLocation().add(0.0D, -200.0D, 0.0D)));
  }

  private static FakeDragon getDragon(Player player, String message) {
    if (hasBar(player)) {
      return (FakeDragon)players.get(player.getUniqueId());
    }
    return addDragon(player, cleanMessage(message));
  }

  private static FakeDragon addDragon(Player player, String message) {
    FakeDragon dragon = Util.newDragon(message, player.getLocation().add(0.0D, -200.0D, 0.0D));

    Util.sendPacket(player, dragon.getSpawnPacket());

    players.put(player.getUniqueId(), dragon);

    return dragon;
  }

  private static FakeDragon addDragon(Player player, Location loc, String message) {
    FakeDragon dragon = Util.newDragon(message, loc.add(0.0D, -200.0D, 0.0D));

    Util.sendPacket(player, dragon.getSpawnPacket());

    players.put(player.getUniqueId(), dragon);

    return dragon;
  }
}
abstract class FakeDragon
{
  public static final float MAX_HEALTH = 200.0F;
  private int x;
  private int y;
  private int z;
  private int pitch = 0;
  private int yaw = 0;
  private byte xvel = 0;
  private byte yvel = 0;
  private byte zvel = 0;
  public float health = 0.0F;
  private boolean visible = false;
  public String name;
  private Object world;

  public FakeDragon(String name, Location loc, int percent)
  {
    this.name = name;
    this.x = loc.getBlockX();
    this.y = loc.getBlockY();
    this.z = loc.getBlockZ();
    this.health = (percent / 100.0F * 200.0F);
    this.world = Util.getHandle(loc.getWorld());
  }

  public FakeDragon(String name, Location loc) {
    this.name = name;
    this.x = loc.getBlockX();
    this.y = loc.getBlockY();
    this.z = loc.getBlockZ();
    this.world = Util.getHandle(loc.getWorld());
  }

  public float getMaxHealth() {
    return 200.0F;
  }

  public void setHealth(int percent) {
    this.health = (percent / 100.0F * 200.0F);
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getX() {
    return this.x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return this.y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getZ() {
    return this.z;
  }

  public void setZ(int z) {
    this.z = z;
  }

  public int getPitch() {
    return this.pitch;
  }

  public void setPitch(int pitch) {
    this.pitch = pitch;
  }

  public int getYaw() {
    return this.yaw;
  }

  public void setYaw(int yaw) {
    this.yaw = yaw;
  }

  public byte getXvel() {
    return this.xvel;
  }

  public void setXvel(byte xvel) {
    this.xvel = xvel;
  }

  public byte getYvel() {
    return this.yvel;
  }

  public void setYvel(byte yvel) {
    this.yvel = yvel;
  }

  public byte getZvel() {
    return this.zvel;
  }

  public void setZvel(byte zvel) {
    this.zvel = zvel;
  }

  public boolean isVisible() {
    return this.visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public Object getWorld() {
    return this.world;
  }

  public void setWorld(Object world) {
    this.world = world;
  }

  public abstract Object getSpawnPacket();

  public abstract Object getDestroyPacket();

  public abstract Object getMetaPacket(Object paramObject);

  public abstract Object getTeleportPacket(Location paramLocation);

  public abstract Object getWatcher();
}
class v1_6 extends FakeDragon
{
  private static Integer EntityID = Integer.valueOf(6000);

  public v1_6(String name, Location loc)
  {
    super(name, loc);
  }

  @SuppressWarnings({ "deprecation", "rawtypes" })
public Object getSpawnPacket()
  {
    Class mob_class = Util.getCraftClass("Packet24MobSpawn");
    Object mobPacket = null;
    try {
      mobPacket = mob_class.newInstance();

      Field a = Util.getField(mob_class, "a");
      a.setAccessible(true);
      a.set(mobPacket, EntityID);
      Field b = Util.getField(mob_class, "b");
      b.setAccessible(true);
      b.set(mobPacket, Short.valueOf(EntityType.ENDER_DRAGON.getTypeId()));

      Field c = Util.getField(mob_class, "c");
      c.setAccessible(true);
      c.set(mobPacket, Integer.valueOf(getX()));
      Field d = Util.getField(mob_class, "d");
      d.setAccessible(true);
      d.set(mobPacket, Integer.valueOf(getY()));
      Field e = Util.getField(mob_class, "e");
      e.setAccessible(true);
      e.set(mobPacket, Integer.valueOf(getZ()));
      Field f = Util.getField(mob_class, "f");
      f.setAccessible(true);
      f.set(mobPacket, Byte.valueOf((byte)(int)(getPitch() * 256.0F / 360.0F)));
      Field g = Util.getField(mob_class, "g");
      g.setAccessible(true);
      g.set(mobPacket, Byte.valueOf((byte)0));

      Field h = Util.getField(mob_class, "h");
      h.setAccessible(true);
      h.set(mobPacket, Byte.valueOf((byte)(int)(getYaw() * 256.0F / 360.0F)));
      Field i = Util.getField(mob_class, "i");
      i.setAccessible(true);
      i.set(mobPacket, Byte.valueOf(getXvel()));
      Field j = Util.getField(mob_class, "j");
      j.setAccessible(true);
      j.set(mobPacket, Byte.valueOf(getYvel()));
      Field k = Util.getField(mob_class, "k");
      k.setAccessible(true);
      k.set(mobPacket, Byte.valueOf(getZvel()));

      Object watcher = getWatcher();
      Field t = Util.getField(mob_class, "t");
      t.setAccessible(true);
      t.set(mobPacket, watcher);
    } catch (InstantiationException e1) {
      e1.printStackTrace();
    } catch (IllegalAccessException e1) {
      e1.printStackTrace();
    }

    return mobPacket;
  }

  @SuppressWarnings("rawtypes")
public Object getDestroyPacket()
  {
    Class packet_class = Util.getCraftClass("Packet29DestroyEntity");
    Object packet = null;
    try {
      packet = packet_class.newInstance();

      Field a = Util.getField(packet_class, "a");
      a.setAccessible(true);
      a.set(packet, new int[] { EntityID.intValue() });
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    return packet;
  }

  @SuppressWarnings("rawtypes")
public Object getMetaPacket(Object watcher)
  {
    Class packet_class = Util.getCraftClass("Packet40EntityMetadata");
    Object packet = null;
    try {
      packet = packet_class.newInstance();

      Field a = Util.getField(packet_class, "a");
      a.setAccessible(true);
      a.set(packet, EntityID);

      Method watcher_c = Util.getMethod(watcher.getClass(), "c");
      Field b = Util.getField(packet_class, "b");
      b.setAccessible(true);
      b.set(packet, watcher_c.invoke(watcher, new Object[0]));
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    return packet;
  }

  @SuppressWarnings("rawtypes")
public Object getTeleportPacket(Location loc)
  {
    Class packet_class = Util.getCraftClass("Packet34EntityTeleport");
    Object packet = null;
    try {
      packet = packet_class.newInstance();

      Field a = Util.getField(packet_class, "a");
      a.setAccessible(true);
      a.set(packet, EntityID);
      Field b = Util.getField(packet_class, "b");
      b.setAccessible(true);
      b.set(packet, Integer.valueOf((int)Math.floor(loc.getX() * 32.0D)));
      Field c = Util.getField(packet_class, "c");
      c.setAccessible(true);
      c.set(packet, Integer.valueOf((int)Math.floor(loc.getY() * 32.0D)));
      Field d = Util.getField(packet_class, "d");
      d.setAccessible(true);
      d.set(packet, Integer.valueOf((int)Math.floor(loc.getZ() * 32.0D)));
      Field e = Util.getField(packet_class, "e");
      e.setAccessible(true);
      e.set(packet, Byte.valueOf((byte)(int)(loc.getYaw() * 256.0F / 360.0F)));
      Field f = Util.getField(packet_class, "f");
      f.setAccessible(true);
      f.set(packet, Byte.valueOf((byte)(int)(loc.getPitch() * 256.0F / 360.0F)));
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return packet;
  }

  @SuppressWarnings("rawtypes")
public Object getWatcher()
  {
    Class watcher_class = Util.getCraftClass("DataWatcher");
    Object watcher = null;
    try {
      watcher = watcher_class.newInstance();

      Method a = Util.getMethod(watcher_class, "a", new Class[] { Integer.TYPE, Object.class });
      a.setAccessible(true);

      a.invoke(watcher, new Object[] { Integer.valueOf(0), Byte.valueOf(isVisible() ? Byte.parseByte("0") : Byte.parseByte("32")) });
      a.invoke(watcher, new Object[] { Integer.valueOf(6), Float.valueOf(this.health) });
      a.invoke(watcher, new Object[] { Integer.valueOf(7), Integer.valueOf(0) });
      a.invoke(watcher, new Object[] { Integer.valueOf(8), Byte.valueOf(Byte.parseByte("0")) });
      a.invoke(watcher, new Object[] { Integer.valueOf(10), this.name });
      a.invoke(watcher, new Object[] { Integer.valueOf(11), Byte.valueOf(Byte.parseByte("1")) });
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    return watcher;
  }
}
class v1_7 extends FakeDragon
{
  private Object dragon;
  private int id;

  public v1_7(String name, Location loc)
  {
    super(name, loc);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
public Object getSpawnPacket()
  {
    Class Entity = Util.getCraftClass("Entity");
    Class EntityLiving = Util.getCraftClass("EntityLiving");
    Class EntityEnderDragon = Util.getCraftClass("EntityEnderDragon");
    Object packet = null;
    try {
      this.dragon = EntityEnderDragon.getConstructor(new Class[] { Util.getCraftClass("World") }).newInstance(new Object[] { getWorld() });

      Method setLocation = Util.getMethod(EntityEnderDragon, "setLocation", new Class[] { Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE });
      setLocation.invoke(this.dragon, new Object[] { Integer.valueOf(getX()), Integer.valueOf(getY()), Integer.valueOf(getZ()), Integer.valueOf(getPitch()), Integer.valueOf(getYaw()) });

      Method setInvisible = Util.getMethod(EntityEnderDragon, "setInvisible", new Class[] { Boolean.TYPE });
      setInvisible.invoke(this.dragon, new Object[] { Boolean.valueOf(isVisible()) });

      Method setCustomName = Util.getMethod(EntityEnderDragon, "setCustomName", new Class[] { String.class });
      setCustomName.invoke(this.dragon, new Object[] { this.name });

      Method setHealth = Util.getMethod(EntityEnderDragon, "setHealth", new Class[] { Float.TYPE });
      setHealth.invoke(this.dragon, new Object[] { Float.valueOf(this.health) });

      Field motX = Util.getField(Entity, "motX");
      motX.set(this.dragon, Byte.valueOf(getXvel()));

      Field motY = Util.getField(Entity, "motX");
      motY.set(this.dragon, Byte.valueOf(getYvel()));

      Field motZ = Util.getField(Entity, "motX");
      motZ.set(this.dragon, Byte.valueOf(getZvel()));

      Method getId = Util.getMethod(EntityEnderDragon, "getId", new Class[0]);
      this.id = ((Integer)getId.invoke(this.dragon, new Object[0])).intValue();

      Class PacketPlayOutSpawnEntityLiving = Util.getCraftClass("PacketPlayOutSpawnEntityLiving");

      packet = PacketPlayOutSpawnEntityLiving.getConstructor(new Class[] { EntityLiving }).newInstance(new Object[] { this.dragon });
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    return packet;
  }

  @SuppressWarnings("rawtypes")
public Object getDestroyPacket()
  {
    Class PacketPlayOutEntityDestroy = Util.getCraftClass("PacketPlayOutEntityDestroy");

    Object packet = null;
    try {
      packet = PacketPlayOutEntityDestroy.newInstance();
      Field a = PacketPlayOutEntityDestroy.getDeclaredField("a");
      a.setAccessible(true);
      a.set(packet, new int[] { this.id });
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }

    return packet;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
public Object getMetaPacket(Object watcher)
  {
    Class DataWatcher = Util.getCraftClass("DataWatcher");

    Class PacketPlayOutEntityMetadata = Util.getCraftClass("PacketPlayOutEntityMetadata");

    Object packet = null;
    try {
      packet = PacketPlayOutEntityMetadata.getConstructor(new Class[] { Integer.TYPE, DataWatcher, Boolean.TYPE }).newInstance(new Object[] { Integer.valueOf(this.id), watcher, Boolean.valueOf(true) });
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    return packet;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
public Object getTeleportPacket(Location loc)
  {
    Class PacketPlayOutEntityTeleport = Util.getCraftClass("PacketPlayOutEntityTeleport");

    Object packet = null;
    try
    {
      packet = PacketPlayOutEntityTeleport.getConstructor(new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Byte.TYPE, Byte.TYPE }).newInstance(new Object[] { Integer.valueOf(this.id), Integer.valueOf(loc.getBlockX() * 32), Integer.valueOf(loc.getBlockY() * 32), Integer.valueOf(loc.getBlockZ() * 32), Byte.valueOf((byte)((int)loc.getYaw() * 256 / 360)), Byte.valueOf((byte)((int)loc.getPitch() * 256 / 360)) });
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    return packet;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
public Object getWatcher()
  {
    Class Entity = Util.getCraftClass("Entity");
    Class DataWatcher = Util.getCraftClass("DataWatcher");

    Object watcher = null;
    try {
      watcher = DataWatcher.getConstructor(new Class[] { Entity }).newInstance(new Object[] { this.dragon });
      Method a = Util.getMethod(DataWatcher, "a", new Class[] { Integer.TYPE, Object.class });

      a.invoke(watcher, new Object[] { Integer.valueOf(0), Byte.valueOf(isVisible() ? Byte.parseByte("0") : Byte.parseByte("32")) });
      a.invoke(watcher, new Object[] { Integer.valueOf(6), Float.valueOf(this.health) });
      a.invoke(watcher, new Object[] { Integer.valueOf(7), Integer.valueOf(0) });
      a.invoke(watcher, new Object[] { Integer.valueOf(8), Byte.valueOf(Byte.parseByte("0")) });
      a.invoke(watcher, new Object[] { Integer.valueOf(10), this.name });
      a.invoke(watcher, new Object[] { Integer.valueOf(11), Byte.valueOf(Byte.parseByte("1")) });
    }
    catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    catch (SecurityException e) {
      e.printStackTrace();
    }
    catch (InstantiationException e) {
      e.printStackTrace();
    }
    catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    return watcher;
  }
}
class Util
{
  public static boolean newProtocol = false;
  static String name = Bukkit.getServer().getClass().getPackage().getName();
  static String mcVersion = name.substring(name.lastIndexOf('.') + 1);
  public static String version = mcVersion + ".";

  public static Class<?> fakeDragonClass = v1_6.class;

  static
  {
    String name = Bukkit.getServer().getClass().getPackage().getName();
    String mcVersion = name.substring(name.lastIndexOf('.') + 1);
    String[] versions = mcVersion.split("_");

    if ((versions[0].equals("v1")) && (Integer.parseInt(versions[1]) > 6)) {
      newProtocol = true;
      fakeDragonClass = v1_7.class;
    }
  }

  public static FakeDragon newDragon(String message, Location loc)
  {
    FakeDragon fakeDragon = null;
    try
    {
      fakeDragon = (FakeDragon)fakeDragonClass.getConstructor(new Class[] { String.class, Location.class }).newInstance(new Object[] { message, loc });
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    return fakeDragon;
  }

  public static void sendPacket(Player p, Object packet)
  {
    try {
      Object nmsPlayer = getHandle(p);
      Field con_field = nmsPlayer.getClass().getField("playerConnection");
      Object con = con_field.get(nmsPlayer);
      Method packet_method = getMethod(con.getClass(), "sendPacket");
      packet_method.invoke(con, new Object[] { packet });
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("rawtypes")
public static Class<?> getCraftClass(String ClassName)
  {
    String className = "net.minecraft.server." + version + ClassName;
    Class c = null;
    try {
      c = Class.forName(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return c;
  }

  public static Object getHandle(World world) {
    Object nms_entity = null;
    Method entity_getHandle = getMethod(world.getClass(), "getHandle");
    try {
      nms_entity = entity_getHandle.invoke(world, new Object[0]);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return nms_entity;
  }

  public static Object getHandle(Entity entity) {
    Object nms_entity = null;
    Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
    try {
      nms_entity = entity_getHandle.invoke(entity, new Object[0]);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return nms_entity;
  }

  public static Field getField(Class<?> cl, String field_name) {
    try {
      return cl.getDeclaredField(field_name);
    }
    catch (SecurityException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Method getMethod(Class<?> cl, String method, Class<?>[] args) {
    for (Method m : cl.getMethods()) {
      if ((m.getName().equals(method)) && (ClassListEqual(args, m.getParameterTypes()))) {
        return m;
      }
    }
    return null;
  }

  public static Method getMethod(Class<?> cl, String method, Integer args) {
    for (Method m : cl.getMethods()) {
      if ((m.getName().equals(method)) && (args.equals(new Integer(m.getParameterTypes().length)))) {
        return m;
      }
    }
    return null;
  }

  public static Method getMethod(Class<?> cl, String method) {
    for (Method m : cl.getMethods()) {
      if (m.getName().equals(method)) {
        return m;
      }
    }
    return null;
  }

  public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
    boolean equal = true;

    if (l1.length != l2.length)
      return false;
    for (int i = 0; i < l1.length; i++) {
      if (l1[i] != l2[i]) {
        equal = false;
        break;
      }
    }

    return equal;
  }
}