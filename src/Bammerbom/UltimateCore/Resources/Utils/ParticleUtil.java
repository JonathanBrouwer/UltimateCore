package Bammerbom.UltimateCore.Resources.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.Resources.Utils.ReflectionHandler.PackageType;
import Bammerbom.UltimateCore.Resources.Utils.ReflectionHandler.PacketType;
import Bammerbom.UltimateCore.Resources.Utils.ReflectionHandler.SubPackageType;
 
/**
 * ParticleEffect Library v1.4
 * 
 * This library was created by @DarkBlade12 based on content related to particles of @microgeek (names and packet values), it allows you to display all Minecraft particle effects on a Bukkit server
 * 
 * You are welcome to use it, modify it and redistribute it under the following conditions:
 * 1. Don't claim this class as your own
 * 2. Don't remove this text
 * 
 * (Would be nice if you provide credit to me)
 * 
 * @author DarkBlade12
 */
public enum ParticleUtil {
	/**
	 * @appearance Huge explosions
	 * @displayed by TNT and creepers
	 */
	HUGE_EXPLOSION("hugeexplosion"),
	/**
	 * @appearance Smaller explosions
	 * @displayed by TNT and creepers
	 */
	LARGE_EXPLODE("largeexplode"),
	/**
	 * @appearance Little white sparkling stars
	 * @displayed by Fireworks
	 */
	FIREWORKS_SPARK("fireworksSpark"),
	/**
	 * @appearance Bubbles
	 * @displayed in water
	 */
	BUBBLE("bubble"),
	/**
	 * @appearance Unknown
	 */
	SUSPEND("suspend"),
	/**
	 * @appearance Little gray dots
	 * @displayed in the Void and water
	 */
	DEPTH_SUSPEND("depthSuspend"),
	/**
	 * @appearance Little gray dots
	 * @displayed by Mycelium
	 */
	TOWN_AURA("townaura"),
	/**
	 * @appearance Light brown crosses
	 * @displayed by critical hits
	 */
	CRIT("crit"),
	/**
	 * @appearance Cyan stars
	 * @displayed by hits with an enchanted weapon
	 */
	MAGIC_CRIT("magicCrit"),
	/**
	 * @appearance Little black/gray clouds
	 * @displayed by torches, primed TNT and end portals
	 */
	SMOKE("smoke"),
	/**
	 * @appearance Colored swirls
	 * @displayed by potion effects
	 */
	MOB_SPELL("mobSpell"),
	/**
	 * @appearance Transparent colored swirls
	 * @displayed by beacon effect
	 */
	MOB_SPELL_AMBIENT("mobSpellAmbient"),
	/**
	 * @appearance Colored swirls
	 * @displayed by splash potions
	 */
	SPELL("spell"),
	/**
	 * @appearance Colored crosses
	 * @displayed by instant splash potions (instant health/instant damage)
	 */
	INSTANT_SPELL("instantSpell"),
	/**
	 * @appearance Colored crosses
	 * @displayed by witches
	 */
	WITCH_MAGIC("witchMagic"),
	/**
	 * @appearance Colored notes
	 * @displayed by note blocks
	 */
	NOTE("note"),
	/**
	 * @appearance Little purple clouds
	 * @displayed by nether portals, endermen, ender pearls, eyes of ender and ender chests
	 */
	PORTAL("portal"),
	/**
	 * @appearance: White letters
	 * @displayed by enchantment tables that are near bookshelves
	 */
	ENCHANTMENT_TABLE("enchantmenttable"),
	/**
	 * @appearance White clouds
	 */
	EXPLODE("explode"),
	/**
	 * @appearance Little flames
	 * @displayed by torches, furnaces, magma cubes and monster spawners
	 */
	FLAME("flame"),
	/**
	 * @appearance Little orange blobs
	 * @displayed by lava
	 */
	LAVA("lava"),
	/**
	 * @appearance Gray transparent squares
	 */
	FOOTSTEP("footstep"),
	/**
	 * @appearance Blue drops
	 * @displayed by water, rain and shaking wolves
	 */
	SPLASH("splash"),
	/**
	 * @appearance Blue droplets
	 * @displayed on water when fishing
	 */
	WAKE("wake"),
	/**
	 * @appearance Black/Gray clouds
	 * @displayed by fire, minecarts with furance and blazes
	 */
	LARGE_SMOKE("largesmoke"),
	/**
	 * @appearance Large white clouds
	 * @displayed on mob death
	 */
	CLOUD("cloud"),
	/**
	 * @appearance Little colored clouds
	 * @displayed by active redstone wires and redstone torches
	 */
	RED_DUST("reddust"),
	/**
	 * @appearance Little white parts
	 * @displayed by cracking snowballs and eggs
	 */
	SNOWBALL_POOF("snowballpoof"),
	/**
	 * @appearance Blue drips
	 * @displayed by blocks below a water source
	 */
	DRIP_WATER("dripWater"),
	/**
	 * @appearance Orange drips
	 * @displayed by blocks below a lava source
	 */
	DRIP_LAVA("dripLava"),
	/**
	 * @appearance White clouds
	 */
	SNOW_SHOVEL("snowshovel"),
	/**
	 * @appearance Little green parts
	 * @displayed by slimes
	 */
	SLIME("slime"),
	/**
	 * @appearance Red hearts
	 * @displayed when breeding
	 */
	HEART("heart"),
	/**
	 * @appearance Dark gray cracked hearts
	 * @displayed when attacking a villager in a village
	 */
	ANGRY_VILLAGER("angryVillager"),
	/**
	 * @appearance Green stars
	 * @displayed by bone meal and when trading with a villager
	 */
	HAPPY_VILLAGER("happyVillager");
 
	private static final Map<String, ParticleUtil> NAME_MAP = new HashMap<String, ParticleUtil>();
	private static final double MAX_RANGE = 16;
	private static Constructor<?> packetPlayOutWorldParticles;
	private static Method getHandle;
	private static Field playerConnection;
	private static Method sendPacket;
	private final String name;
 
	static {
		for (ParticleUtil p : values())
			NAME_MAP.put(p.name, p);
		try {
			packetPlayOutWorldParticles = ReflectionHandler.getConstructor(PacketType.PLAY_OUT_WORLD_PARTICLES.getPacket(), String.class, float.class, float.class, float.class, float.class, float.class,
					float.class, float.class, int.class);
			getHandle = ReflectionHandler.getMethod("CraftPlayer", SubPackageType.ENTITY, "getHandle");
			playerConnection = ReflectionHandler.getField("EntityPlayer", PackageType.MINECRAFT_SERVER, "playerConnection");
			sendPacket = ReflectionHandler.getMethod(playerConnection.getType(), "sendPacket", ReflectionHandler.getClass("Packet", PackageType.MINECRAFT_SERVER));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	/**
	 * @param name Name of this particle effect
	 */
	private ParticleUtil(String name) {
		this.name = name;
	}
 
	/**
	 * @return The name of this particle effect
	 */
	public String getName() {
		return this.name;
	}
 
	/**
	 * Gets a particle effect from name
	 * 
	 * @param name Name of the particle effect
	 * @return The particle effect
	 */
	public static ParticleUtil fromName(String name) {
		if (name != null)
			for (Entry<String, ParticleUtil> e : NAME_MAP.entrySet())
				if (e.getKey().equalsIgnoreCase(name))
					return e.getValue();
		return null;
	}
 
	/**
	 * Gets a list of players in a certain range
	 * 
	 * @param center Center location
	 * @param range Range
	 * @return The list of players in the specified range
	 */
	private static List<Player> getPlayers(Location center, double range) {
		List<Player> players = new ArrayList<Player>();
		String name = center.getWorld().getName();
		double squared = range * range;
		for (Player p : UC.getOnlinePlayers())
			if (p.getWorld().getName().equals(name) && p.getLocation().distanceSquared(center) <= squared)
				players.add(p);
		return players;
	}
 
	/**
	 * Instantiates a new @PacketPlayOutWorldParticles object through reflection
	 * 
	 * @param center Center location of the effect
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @return The packet object
	 * @throws #PacketInstantiationException if the amount is lower than 1 or if the @PacketPlayOutWorldParticles has changed its name or constructor parameters
	 */
	private static Object instantiatePacket(String name, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		if (amount < 1)
			throw new PacketInstantiationException("Amount cannot be lower than 1");
		try {
			return packetPlayOutWorldParticles.newInstance(name, (float) center.getX(), (float) center.getY(), (float) center.getZ(), offsetX, offsetY, offsetZ, speed, amount);
		} catch (Exception e) {
			throw new PacketInstantiationException("Packet instantiation failed", e);
		}
	}
 
	/**
	 * Instantiates a new @PacketPlayOutWorldParticles object through reflection especially for the "iconcrack" effect
	 * 
	 * @param id Id of the icon
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @return The packet object
	 * @throws #PacketInstantiationException if the amount is lower than 1 or if the @PacketPlayOutWorldParticles has changed its name or constructor parameters
	 * @see #instantiatePacket
	 */
	private static Object instantiateIconCrackPacket(int id, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		return instantiatePacket("iconcrack_" + id, center, offsetX, offsetY, offsetZ, speed, amount);
	}
 
	/**
	 * Instantiates a new @PacketPlayOutWorldParticles object through reflection especially for the "blockcrack" effect
	 * 
	 * @param id Id of the block
	 * @param data Data value
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param amount Amount of particles
	 * @return The packet object
	 * @throws #PacketInstantiationException if the amount is lower than 1 or if the @PacketPlayOutWorldParticles has changed its name or constructor parameters
	 * @see #instantiatePacket
	 */
	private static Object instantiateBlockCrackPacket(int id, byte data, Location center, float offsetX, float offsetY, float offsetZ, int amount) {
		return instantiatePacket("blockcrack_" + id + "_" + data, center, offsetX, offsetY, offsetZ, 0, amount);
	}
 
	/**
	 * Instantiates a new @PacketPlayOutWorldParticles object through reflection especially for the "blockdust" effect
	 * 
	 * @param id Id of the block
	 * @param data Data value
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @return The packet object
	 * @throws #PacketInstantiationException if the amount is lower than 1 or if the name or the constructor of @PacketPlayOutWorldParticles have changed
	 * @see #instantiatePacket
	 */
	private static Object instantiateBlockDustPacket(int id, byte data, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		return instantiatePacket("blockdust_" + id + "_" + data, center, offsetX, offsetY, offsetZ, speed, amount);
	}
 
	/**
	 * Sends a packet through reflection to a player
	 * 
	 * @param p Receiver of the packet
	 * @param packet Packet that is sent
	 * @throws #PacketSendingException if the packet is null or some methods which are accessed through reflection have changed
	 */
	private static void sendPacket(Player p, Object packet) {
		try {
			sendPacket.invoke(playerConnection.get(getHandle.invoke(p)), packet);
		} catch (Exception e) {
			throw new PacketSendingException("Failed to send a packet to player '" + p.getName() + "'", e);
		}
	}
 
	/**
	 * Sends a packet through reflection to a collection of players
	 * 
	 * @param players Receivers of the packet
	 * @param packet Packet that is sent
	 * @throws #PacketSendingException if the sending to a single player fails
	 * @see #sendPacket
	 */
	private static void sendPacket(Collection<Player> players, Object packet) {
		for (Player p : players)
			sendPacket(p, packet);
	}
 
	/**
	 * Displays a particle effect which is only visible for the specified players
	 * 
	 * @param center Center location of the effect
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @param players Receivers of the effect
	 * @see #sendPacket
	 * @see #instantiatePacket
	 */
	public void display(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
		sendPacket(Arrays.asList(players), instantiatePacket(name, center, offsetX, offsetY, offsetZ, speed, amount));
	}
 
	/**
	 * Displays a particle effect which is only visible for all players within a certain range in the world of @param center
	 * 
	 * @param center Center location of the effect
	 * @param range Range of the visibility
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @param players Receivers of the effect
	 * @throws @IllegalArgumentException if the range is higher than 20
	 * @see #sendPacket
	 * @see #instantiatePacket
	 */
	public void display(Location center, double range, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		if (range > MAX_RANGE)
			throw new IllegalArgumentException("Range cannot exceed the maximum value of 16");
		sendPacket(getPlayers(center, range), instantiatePacket(name, center, offsetX, offsetY, offsetZ, speed, amount));
	}
 
	/**
	 * Displays a particle effect which is only visible for all players within a range of 20 in the world of @param center
	 * 
	 * @param center Center location of the effect
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @param players Receivers of the effect
	 * @see #display(Location, double, float, float, float, float, int)
	 */
	public void display(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		display(center, MAX_RANGE, offsetX, offsetY, offsetZ, speed, amount);
	}
 
	/**
	 * Displays an icon crack (item break) particle effect which is only visible for the specified players
	 * 
	 * @param center Center location of the effect
	 * @param id Id of the icon
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @param players Receivers of the effect
	 * @see #sendPacket
	 * @see #instantiateIconCrackPacket
	 */
	public static void displayIconCrack(Location center, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
		sendPacket(Arrays.asList(players), instantiateIconCrackPacket(id, center, offsetX, offsetY, offsetZ, speed, amount));
	}
 
	/**
	 * Displays an icon crack (item break) particle effect which is only visible for all players within a certain range in the world of @param center
	 * 
	 * @param center Center location of the effect
	 * @param range Range of the visibility
	 * @param id Id of the icon
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @throws @IllegalArgumentException if the range is higher than 20
	 * @see #sendPacket
	 * @see #instantiateIconCrackPacket
	 */
	public static void displayIconCrack(Location center, double range, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		if (range > MAX_RANGE)
			throw new IllegalArgumentException("Range has to be lower/equal the maximum of 16");
		sendPacket(getPlayers(center, range), instantiateIconCrackPacket(id, center, offsetX, offsetY, offsetZ, speed, amount));
	}
 
	/**
	 * Displays an icon crack (item break) effect which is visible for all players whitin the maximum range of 20 blocks in the world of @param center
	 * 
	 * @param center Center location of the effect
	 * @param id Id of the icon
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @see #displayIconCrack(Location, double, int, float, float, float, float, int)
	 */
	public static void displayIconCrack(Location center, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		displayIconCrack(center, MAX_RANGE, id, offsetX, offsetY, offsetZ, speed, amount);
	}
 
	/**
	 * Displays a block crack (block break) particle effect which is only visible for the specified players
	 * 
	 * @param center Center location of the effect
	 * @param id Id of the block
	 * @param data Data value
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param amount Amount of particles
	 * @param players Receivers of the effect
	 * @see #sendPacket
	 * @see #instantiateBlockCrackPacket
	 */
	public static void displayBlockCrack(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount, Player... players) {
		sendPacket(Arrays.asList(players), instantiateBlockCrackPacket(id, data, center, offsetX, offsetY, offsetZ, amount));
	}
 
	/**
	 * Displays a block crack (block break) particle effect which is only visible for all players within a certain range in the world of @param center
	 * 
	 * @param center Center location of the effect
	 * @param range Range of the visibility
	 * @param id Id of the block
	 * @param data Data value
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param amount Amount of particles
	 * @throws @IllegalArgumentException if the range is higher than 20
	 * @see #sendPacket
	 * @see #instantiateBlockCrackPacket
	 */
	public static void displayBlockCrack(Location center, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount) {
		if (range > MAX_RANGE)
			throw new IllegalArgumentException("Range has to be lower/equal the maximum of 16");
		sendPacket(getPlayers(center, range), instantiateBlockCrackPacket(id, data, center, offsetX, offsetY, offsetZ, amount));
	}
 
	/**
	 * Displays a block crack (block break) effect which is visible for all players whitin the maximum range of 20 blocks in the world of @param center
	 * 
	 * @param center Center location of the effect
	 * @param id Id of the block
	 * @param data Data value
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param amount Amount of particles
	 * @see #displayBlockCrack(Location, double, int, byte, float, float, float, int)
	 */
	public static void displayBlockCrack(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount) {
		displayBlockCrack(center, MAX_RANGE, id, data, offsetX, offsetY, offsetZ, amount);
	}
 
	/**
	 * Displays a block dust particle effect which is only visible for the specified players
	 * 
	 * @param center Center location of the effect
	 * @param id Id of the block
	 * @param data Data value
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @param players Receivers of the effect
	 * @see #sendPacket
	 * @see #instantiateBlockDustPacket
	 */
	public static void displayBlockDust(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
		sendPacket(Arrays.asList(players), instantiateBlockDustPacket(id, data, center, offsetX, offsetY, offsetZ, speed, amount));
	}
 
	/**
	 * Displays a block dust particle effect which is only visible for all players within a certain range in the world of @param center
	 * 
	 * @param center Center location of the effect
	 * @param range Range of the visibility
	 * @param id Id of the block
	 * @param data Data value
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @throws @IllegalArgumentException if the range is higher than 20
	 * @see #sendPacket
	 * @see #instantiateBlockDustPacket
	 */
	public static void displayBlockDust(Location center, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		if (range > MAX_RANGE)
			throw new IllegalArgumentException("Range has to be lower/equal the maximum of 16");
		sendPacket(getPlayers(center, range), instantiateBlockDustPacket(id, data, center, offsetX, offsetY, offsetZ, speed, amount));
	}
 
	/**
	 * Displays a block dust effect which is visible for all players whitin the maximum range of 20 blocks in the world of @param center
	 * 
	 * @param center Center location of the effect
	 * @param id Id of the block
	 * @param data Data value
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @see #displayBlockDust(Location, double, int, byte, float, float, float, float, int)
	 */
	public static void displayBlockDust(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		displayBlockDust(center, MAX_RANGE, id, data, offsetX, offsetY, offsetZ, speed, amount);
	}
 
	/**
	 * Represents a runtime exception that can be thrown upon packet instantiation
	 */
	private static final class PacketInstantiationException extends RuntimeException {
		private static final long serialVersionUID = 3203085387160737484L;
 
		/**
		 * @param message Message that will be logged
		 */
		public PacketInstantiationException(String message) {
			super(message);
		}
 
		/**
		 * @param message Message that will be logged
		 * @param cause Cause of the exception
		 */
		public PacketInstantiationException(String message, Throwable cause) {
			super(message, cause);
		}
	}
 
	/**
	 * Represents a runtime exception that can be thrown upon packet sending
	 */
	private static final class PacketSendingException extends RuntimeException {
		private static final long serialVersionUID = 3203085387160737484L;
 
		/**
		 * @param message Message that will be logged
		 * @param cause Cause of the exception
		 */
		public PacketSendingException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}

/**
 * ReflectionHandler v1.0
 * 
 * This class makes dealing with reflection much easier, especially when working with Bukkit
 * 
 * You are welcome to use it, modify it and redistribute it under the following conditions:
 * 1. Don't claim this class as your own
 * 2. Don't remove this text
 * 
 * (Would be nice if you provide credit to me)
 * 
 * @author DarkBlade12
 */
final class ReflectionHandler {
	private ReflectionHandler() {}
 
	public static Class<?> getClass(String name, PackageType type) throws Exception {
		return Class.forName(type + "." + name);
	}
 
	public static Class<?> getClass(String name, SubPackageType type) throws Exception {
		return Class.forName(type + "." + name);
	}
 
	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) {
		Class<?>[] p = DataType.convertToPrimitive(parameterTypes);
		for (Constructor<?> c : clazz.getConstructors())
			if (DataType.equalsArray(DataType.convertToPrimitive(c.getParameterTypes()), p))
				return c;
		return null;
	}
 
	public static Constructor<?> getConstructor(String className, PackageType type, Class<?>... parameterTypes) throws Exception {
		return getConstructor(getClass(className, type), parameterTypes);
	}
 
	public static Constructor<?> getConstructor(String className, SubPackageType type, Class<?>... parameterTypes) throws Exception {
		return getConstructor(getClass(className, type), parameterTypes);
	}
 
	public static Object newInstance(Class<?> clazz, Object... args) throws Exception {
		return getConstructor(clazz, DataType.convertToPrimitive(args)).newInstance(args);
	}
 
	public static Object newInstance(String className, PackageType type, Object... args) throws Exception {
		return newInstance(getClass(className, type), args);
	}
 
	public static Object newInstance(String className, SubPackageType type, Object... args) throws Exception {
		return newInstance(getClass(className, type), args);
	}
 
	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		Class<?>[] p = DataType.convertToPrimitive(parameterTypes);
		for (Method m : clazz.getMethods())
			if (m.getName().equals(name) && DataType.equalsArray(DataType.convertToPrimitive(m.getParameterTypes()), p))
				return m;
		return null;
	}
 
	public static Method getMethod(String className, PackageType type, String name, Class<?>... parameterTypes) throws Exception {
		return getMethod(getClass(className, type), name, parameterTypes);
	}
 
	public static Method getMethod(String className, SubPackageType type, String name, Class<?>... parameterTypes) throws Exception {
		return getMethod(getClass(className, type), name, parameterTypes);
	}
 
	public static Object invokeMethod(String name, Object instance, Object... args) throws Exception {
		return getMethod(instance.getClass(), name, DataType.convertToPrimitive(args)).invoke(instance, args);
	}
 
	public static Object invokeMethod(Class<?> clazz, String name, Object instance, Object... args) throws Exception {
		return getMethod(clazz, name, DataType.convertToPrimitive(args)).invoke(instance, args);
	}
 
	public static Object invokeMethod(String className, PackageType type, String name, Object instance, Object... args) throws Exception {
		return invokeMethod(getClass(className, type), name, instance, args);
	}
 
	public static Object invokeMethod(String className, SubPackageType type, String name, Object instance, Object... args) throws Exception {
		return invokeMethod(getClass(className, type), name, instance, args);
	}
 
	public static Field getField(Class<?> clazz, String name) throws Exception {
		Field f = clazz.getField(name);
		f.setAccessible(true);
		return f;
	}
 
	public static Field getField(String className, PackageType type, String name) throws Exception {
		return getField(getClass(className, type), name);
	}
 
	public static Field getField(String className, SubPackageType type, String name) throws Exception {
		return getField(getClass(className, type), name);
	}
 
	public static Field getDeclaredField(Class<?> clazz, String name) throws Exception {
		Field f = clazz.getDeclaredField(name);
		f.setAccessible(true);
		return f;
	}
 
	public static Field getDeclaredField(String className, PackageType type, String name) throws Exception {
		return getDeclaredField(getClass(className, type), name);
	}
 
	public static Field getDeclaredField(String className, SubPackageType type, String name) throws Exception {
		return getDeclaredField(getClass(className, type), name);
	}
 
	public static Object getValue(Object instance, String fieldName) throws Exception {
		return getField(instance.getClass(), fieldName).get(instance);
	}
 
	public static Object getValue(Class<?> clazz, Object instance, String fieldName) throws Exception {
		return getField(clazz, fieldName).get(instance);
	}
 
	public static Object getValue(String className, PackageType type, Object instance, String fieldName) throws Exception {
		return getValue(getClass(className, type), instance, fieldName);
	}
 
	public static Object getValue(String className, SubPackageType type, Object instance, String fieldName) throws Exception {
		return getValue(getClass(className, type), instance, fieldName);
	}
 
	public static Object getDeclaredValue(Object instance, String fieldName) throws Exception {
		return getDeclaredField(instance.getClass(), fieldName).get(instance);
	}
 
	public static Object getDeclaredValue(Class<?> clazz, Object instance, String fieldName) throws Exception {
		return getDeclaredField(clazz, fieldName).get(instance);
	}
 
	public static Object getDeclaredValue(String className, PackageType type, Object instance, String fieldName) throws Exception {
		return getDeclaredValue(getClass(className, type), instance, fieldName);
	}
 
	public static Object getDeclaredValue(String className, SubPackageType type, Object instance, String fieldName) throws Exception {
		return getDeclaredValue(getClass(className, type), instance, fieldName);
	}
 
	public static void setValue(Object instance, String fieldName, Object fieldValue) throws Exception {
		Field f = getField(instance.getClass(), fieldName);
		f.set(instance, fieldValue);
	}
 
	public static void setValue(Object instance, FieldPair pair) throws Exception {
		setValue(instance, pair.getName(), pair.getValue());
	}
 
	public static void setValue(Class<?> clazz, Object instance, String fieldName, Object fieldValue) throws Exception {
		Field f = getField(clazz, fieldName);
		f.set(instance, fieldValue);
	}
 
	public static void setValue(Class<?> clazz, Object instance, FieldPair pair) throws Exception {
		setValue(clazz, instance, pair.getName(), pair.getValue());
	}
 
	public static void setValue(String className, PackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		setValue(getClass(className, type), instance, fieldName, fieldValue);
	}
 
	public static void setValue(String className, PackageType type, Object instance, FieldPair pair) throws Exception {
		setValue(className, type, instance, pair.getName(), pair.getValue());
	}
 
	public static void setValue(String className, SubPackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		setValue(getClass(className, type), instance, fieldName, fieldValue);
	}
 
	public static void setValue(String className, SubPackageType type, Object instance, FieldPair pair) throws Exception {
		setValue(className, type, instance, pair.getName(), pair.getValue());
	}
 
	public static void setValues(Object instance, FieldPair... pairs) throws Exception {
		for (FieldPair pair : pairs)
			setValue(instance, pair);
	}
 
	public static void setValues(Class<?> clazz, Object instance, FieldPair... pairs) throws Exception {
		for (FieldPair pair : pairs)
			setValue(clazz, instance, pair);
	}
 
	public static void setValues(String className, PackageType type, Object instance, FieldPair... pairs) throws Exception {
		setValues(getClass(className, type), instance, pairs);
	}
 
	public static void setValues(String className, SubPackageType type, Object instance, FieldPair... pairs) throws Exception {
		setValues(getClass(className, type), instance, pairs);
	}
 
	public static void setDeclaredValue(Object instance, String fieldName, Object fieldValue) throws Exception {
		Field f = getDeclaredField(instance.getClass(), fieldName);
		f.set(instance, fieldValue);
	}
 
	public static void setDeclaredValue(Object instance, FieldPair pair) throws Exception {
		setDeclaredValue(instance, pair.getName(), pair.getValue());
	}
 
	public static void setDeclaredValue(Class<?> clazz, Object instance, String fieldName, Object fieldValue) throws Exception {
		Field f = getDeclaredField(clazz, fieldName);
		f.set(instance, fieldValue);
	}
 
	public static void setDeclaredValue(Class<?> clazz, Object instance, FieldPair pair) throws Exception {
		setDeclaredValue(clazz, instance, pair.getName(), pair.getValue());
	}
 
	public static void setDeclaredValue(String className, PackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		setDeclaredValue(getClass(className, type), instance, fieldName, fieldValue);
	}
 
	public static void setDeclaredValue(String className, PackageType type, Object instance, FieldPair pair) throws Exception {
		setDeclaredValue(className, type, instance, pair.getName(), pair.getValue());
	}
 
	public static void setDeclaredValue(String className, SubPackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		setDeclaredValue(getClass(className, type), instance, fieldName, fieldValue);
	}
 
	public static void setDeclaredValue(String className, SubPackageType type, Object instance, FieldPair pair) throws Exception {
		setDeclaredValue(className, type, instance, pair.getName(), pair.getValue());
	}
 
	public static void setDeclaredValues(Object instance, FieldPair... pairs) throws Exception {
		for (FieldPair pair : pairs)
			setDeclaredValue(instance, pair);
	}
 
	public static void setDeclaredValues(Class<?> clazz, Object instance, FieldPair... pairs) throws Exception {
		for (FieldPair pair : pairs)
			setDeclaredValue(clazz, instance, pair);
	}
 
	public static void setDeclaredValues(String className, PackageType type, Object instance, FieldPair... pairs) throws Exception {
		setDeclaredValues(getClass(className, type), instance, pairs);
	}
 
	public static void setDeclaredValues(String className, SubPackageType type, Object instance, FieldPair... pairs) throws Exception {
		setDeclaredValues(getClass(className, type), instance, pairs);
	}
 
	/**
	 * This class is part of the ReflectionHandler and follows the same usage conditions
	 * 
	 * @author DarkBlade12
	 */
	public enum DataType {
		BYTE(byte.class, Byte.class),
		SHORT(short.class, Short.class),
		INTEGER(int.class, Integer.class),
		LONG(long.class, Long.class),
		CHARACTER(char.class, Character.class),
		FLOAT(float.class, Float.class),
		DOUBLE(double.class, Double.class),
		BOOLEAN(boolean.class, Boolean.class);
 
		private static final Map<Class<?>, DataType> CLASS_MAP = new HashMap<Class<?>, DataType>();
		private final Class<?> primitive;
		private final Class<?> reference;
 
		static {
			for (DataType t : values()) {
				CLASS_MAP.put(t.primitive, t);
				CLASS_MAP.put(t.reference, t);
			}
		}
 
		private DataType(Class<?> primitive, Class<?> reference) {
			this.primitive = primitive;
			this.reference = reference;
		}
 
		public Class<?> getPrimitive() {
			return this.primitive;
		}
 
		public Class<?> getReference() {
			return this.reference;
		}
 
		public static DataType fromClass(Class<?> c) {
			return CLASS_MAP.get(c);
		}
 
		public static Class<?> getPrimitive(Class<?> c) {
			DataType t = fromClass(c);
			return t == null ? c : t.getPrimitive();
		}
 
		public static Class<?> getReference(Class<?> c) {
			DataType t = fromClass(c);
			return t == null ? c : t.getReference();
		}
 
		public static Class<?>[] convertToPrimitive(Class<?>[] classes) {
			int length = classes == null ? 0 : classes.length;
			Class<?>[] types = new Class<?>[length];
			for (int i = 0; i < length; i++)
				types[i] = getPrimitive(classes[i]);
			return types;
		}
 
		public static Class<?>[] convertToPrimitive(Object[] objects) {
			int length = objects == null ? 0 : objects.length;
			Class<?>[] types = new Class<?>[length];
			for (int i = 0; i < length; i++)
				types[i] = getPrimitive(objects[i].getClass());
			return types;
		}
 
		public static boolean equalsArray(Class<?>[] a1, Class<?>[] a2) {
			if (a1 == null || a2 == null || a1.length != a2.length)
				return false;
			for (int i = 0; i < a1.length; i++)
				if (!a1[i].equals(a2[i]) && !a1[i].isAssignableFrom(a2[i]))
					return false;
			return true;
		}
	}
 
	/**
	 * This class is part of the ReflectionHandler and follows the same usage conditions
	 * 
	 * @author DarkBlade12
	 */
	public final class FieldPair {
		private final String name;
		private final Object value;
 
		public FieldPair(String name, Object value) {
			this.name = name;
			this.value = value;
		}
 
		public String getName() {
			return this.name;
		}
 
		public Object getValue() {
			return this.value;
		}
	}
 
	/**
	 * This class is part of the ReflectionHandler and follows the same usage conditions
	 * 
	 * @author DarkBlade12
	 */
	public enum PackageType {
		MINECRAFT_SERVER("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().substring(23)),
		CRAFTBUKKIT(Bukkit.getServer().getClass().getPackage().getName());
 
		private final String name;
 
		private PackageType(String name) {
			this.name = name;
		}
 
		public String getName() {
			return this.name;
		}
 
		@Override
		public String toString() {
			return name;
		}
	}
 
	/**
	 * This class is part of the ReflectionHandler and follows the same usage conditions
	 * 
	 * @author DarkBlade12
	 */
	public enum SubPackageType {
		BLOCK,
		CHUNKIO,
		COMMAND,
		CONVERSATIONS,
		ENCHANTMENS,
		ENTITY,
		EVENT,
		GENERATOR,
		HELP,
		INVENTORY,
		MAP,
		METADATA,
		POTION,
		PROJECTILES,
		SCHEDULER,
		SCOREBOARD,
		UPDATER,
		UTIL;
 
		private final String name;
 
		private SubPackageType() {
			name = PackageType.CRAFTBUKKIT + "." + name().toLowerCase();
		}
 
		public String getName() {
			return this.name;
		}
 
		@Override
		public String toString() {
			return name;
		}
	}
 
	/**
	 * This class is part of the ReflectionHandler and follows the same usage conditions
	 * 
	 * @author DarkBlade12
	 */
	public enum PacketType {
		HANDSHAKING_IN_SET_PROTOCOL("PacketHandshakingInSetProtocol"),
		LOGIN_IN_ENCRYPTION_BEGIN("PacketLoginInEncryptionBegin"),
		LOGIN_IN_START("PacketLoginInStart"),
		LOGIN_OUT_DISCONNECT("PacketLoginOutDisconnect"),
		LOGIN_OUT_ENCRYPTION_BEGIN("PacketLoginOutEncryptionBegin"),
		LOGIN_OUT_SUCCESS("PacketLoginOutSuccess"),
		PLAY_IN_ABILITIES("PacketPlayInAbilities"),
		PLAY_IN_ARM_ANIMATION("PacketPlayInArmAnimation"),
		PLAY_IN_BLOCK_DIG("PacketPlayInBlockDig"),
		PLAY_IN_BLOCK_PLACE("PacketPlayInBlockPlace"),
		PLAY_IN_CHAT("PacketPlayInChat"),
		PLAY_IN_CLIENT_COMMAND("PacketPlayInClientCommand"),
		PLAY_IN_CLOSE_WINDOW("PacketPlayInCloseWindow"),
		PLAY_IN_CUSTOM_PAYLOAD("PacketPlayInCustomPayload"),
		PLAY_IN_ENCHANT_ITEM("PacketPlayInEnchantItem"),
		PLAY_IN_ENTITY_ACTION("PacketPlayInEntityAction"),
		PLAY_IN_FLYING("PacketPlayInFlying"),
		PLAY_IN_HELD_ITEM_SLOT("PacketPlayInHeldItemSlot"),
		PLAY_IN_KEEP_ALIVE("PacketPlayInKeepAlive"),
		PLAY_IN_LOOK("PacketPlayInLook"),
		PLAY_IN_POSITION("PacketPlayInPosition"),
		PLAY_IN_POSITION_LOOK("PacketPlayInPositionLook"),
		PLAY_IN_SET_CREATIVE_SLOT("PacketPlayInSetCreativeSlot "),
		PLAY_IN_SETTINGS("PacketPlayInSettings"),
		PLAY_IN_STEER_VEHICLE("PacketPlayInSteerVehicle"),
		PLAY_IN_TAB_COMPLETE("PacketPlayInTabComplete"),
		PLAY_IN_TRANSACTION("PacketPlayInTransaction"),
		PLAY_IN_UPDATE_SIGN("PacketPlayInUpdateSign"),
		PLAY_IN_USE_ENTITY("PacketPlayInUseEntity"),
		PLAY_IN_WINDOW_CLICK("PacketPlayInWindowClick"),
		PLAY_OUT_ABILITIES("PacketPlayOutAbilities"),
		PLAY_OUT_ANIMATION("PacketPlayOutAnimation"),
		PLAY_OUT_ATTACH_ENTITY("PacketPlayOutAttachEntity"),
		PLAY_OUT_BED("PacketPlayOutBed"),
		PLAY_OUT_BLOCK_ACTION("PacketPlayOutBlockAction"),
		PLAY_OUT_BLOCK_BREAK_ANIMATION("PacketPlayOutBlockBreakAnimation"),
		PLAY_OUT_BLOCK_CHANGE("PacketPlayOutBlockChange"),
		PLAY_OUT_CHAT("PacketPlayOutChat"),
		PLAY_OUT_CLOSE_WINDOW("PacketPlayOutCloseWindow"),
		PLAY_OUT_COLLECT("PacketPlayOutCollect"),
		PLAY_OUT_CRAFT_PROGRESS_BAR("PacketPlayOutCraftProgressBar"),
		PLAY_OUT_CUSTOM_PAYLOAD("PacketPlayOutCustomPayload"),
		PLAY_OUT_ENTITY("PacketPlayOutEntity"),
		PLAY_OUT_ENTITY_DESTROY("PacketPlayOutEntityDestroy"),
		PLAY_OUT_ENTITY_EFFECT("PacketPlayOutEntityEffect"),
		PLAY_OUT_ENTITY_EQUIPMENT("PacketPlayOutEntityEquipment"),
		PLAY_OUT_ENTITY_HEAD_ROTATION("PacketPlayOutEntityHeadRotation"),
		PLAY_OUT_ENTITY_LOOK("PacketPlayOutEntityLook"),
		PLAY_OUT_ENTITY_METADATA("PacketPlayOutEntityMetadata"),
		PLAY_OUT_ENTITY_STATUS("PacketPlayOutEntityStatus"),
		PLAY_OUT_ENTITY_TELEPORT("PacketPlayOutEntityTeleport"),
		PLAY_OUT_ENTITY_VELOCITY("PacketPlayOutEntityVelocity"),
		PLAY_OUT_EXPERIENCE("PacketPlayOutExperience"),
		PLAY_OUT_EXPLOSION("PacketPlayOutExplosion"),
		PLAY_OUT_GAME_STATE_CHANGE("PacketPlayOutGameStateChange"),
		PLAY_OUT_HELD_ITEM_SLOT("PacketPlayOutHeldItemSlot"),
		PLAY_OUT_KEEP_ALIVE("PacketPlayOutKeepAlive"),
		PLAY_OUT_KICK_DISCONNECT("PacketPlayOutKickDisconnect"),
		PLAY_OUT_LOGIN("PacketPlayOutLogin"),
		PLAY_OUT_MAP("PacketPlayOutMap"),
		PLAY_OUT_MAP_CHUNK("PacketPlayOutMapChunk"),
		PLAY_OUT_MAP_CHUNK_BULK("PacketPlayOutMapChunkBulk"),
		PLAY_OUT_MULTI_BLOCK_CHANGE("PacketPlayOutMultiBlockChange"),
		PLAY_OUT_NAMED_ENTITY_SPAWN("PacketPlayOutNamedEntitySpawn"),
		PLAY_OUT_NAMED_SOUND_EFFECT("PacketPlayOutNamedSoundEffect"),
		PLAY_OUT_OPEN_SIGN_EDITOR("PacketPlayOutOpenSignEditor"),
		PLAY_OUT_OPEN_WINDOW("PacketPlayOutOpenWindow"),
		PLAY_OUT_PLAYER_INFO("PacketPlayOutPlayerInfo"),
		PLAY_OUT_POSITION("PacketPlayOutPosition"),
		PLAY_OUT_REL_ENTITY_MOVE("PacketPlayOutRelEntityMove"),
		PLAY_OUT_REL_ENTITY_MOVE_LOOK("PacketPlayOutRelEntityMoveLook"),
		PLAY_OUT_REMOVE_ENTITY_EFFECT("PacketPlayOutRemoveEntityEffect"),
		PLAY_OUT_RESPAWN("PacketPlayOutRespawn"),
		PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE("PacketPlayOutScoreboardDisplayObjective"),
		PLAY_OUT_SCOREBOARD_OBJECTIVE("PacketPlayOutScoreboardObjective"),
		PLAY_OUT_SCOREBOARD_SCORE("PacketPlayOutScoreboardScore"),
		PLAY_OUT_SCOREBOARD_TEAM("PacketPlayOutScoreboardTeam"),
		PLAY_OUT_SET_SLOT("PacketPlayOutSetSlot"),
		PLAY_OUT_SPAWN_ENTITY("PacketPlayOutSpawnEntity"),
		PLAY_OUT_SPAWN_ENTITY_EXPERIENCE_ORB("PacketPlayOutSpawnEntityExperienceOrb"),
		PLAY_OUT_SPAWN_ENTITY_LIVING("PacketPlayOutSpawnEntityLiving"),
		PLAY_OUT_SPAWN_ENTITY_PAINTING("PacketPlayOutSpawnEntityPainting"),
		PLAY_OUT_SPAWN_ENTITY_WEATHER("PacketPlayOutSpawnEntityWeather"),
		PLAY_OUT_SPAWN_POSITION("PacketPlayOutSpawnPosition"),
		PLAY_OUT_STATISTIC("PacketPlayOutStatistic"),
		PLAY_OUT_TAB_COMPLETE("PacketPlayOutTabComplete"),
		PLAY_OUT_TILE_ENTITY_DATA("PacketPlayOutTileEntityData"),
		PLAY_OUT_TRANSACTION("PacketPlayOutTransaction"),
		PLAY_OUT_UPDATE_ATTRIBUTES("PacketPlayOutUpdateAttributes"),
		PLAY_OUT_UPDATE_HEALTH("PacketPlayOutUpdateHealth"),
		PLAY_OUT_UPDATE_SIGN("PacketPlayOutUpdateSign"),
		PLAY_OUT_UPDATE_TIME("PacketPlayOutUpdateTime"),
		PLAY_OUT_WINDOW_ITEMS("PacketPlayOutWindowItems"),
		PLAY_OUT_WORLD_EVENT("PacketPlayOutWorldEvent"),
		PLAY_OUT_WORLD_PARTICLES("PacketPlayOutWorldParticles"),
		STATUS_IN_PING("PacketStatusInPing"),
		STATUS_IN_START("PacketStatusInStart"),
		STATUS_OUT_PONG("PacketStatusOutPong"),
		STATUS_OUT_SERVER_INFO("PacketStatusOutServerInfo");
 
		private final String name;
		private Class<?> packet;
 
		private PacketType(String name) {
			this.name = name;
		}
 
		public String getName() {
			return this.name;
		}
 
		public Class<?> getPacket() throws Exception {
			return packet == null ? packet = ReflectionHandler.getClass(name, PackageType.MINECRAFT_SERVER) : packet;
		}
	}
}