/*
 * This file is part of UltimateCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) Bammerbom
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bammerbom.ultimatecore.spongeapi_old.resources.utils;

import bammerbom.ultimatecore.spongeapi_old.resources.utils.ReflectionUtils.PackageType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <b>ParticleEffect Library</b>
 * <p/>
 * This library was created by @DarkBlade12 and allows you to display all Minecraft particle
 * effects on a Bukkit server
 * <p/>
 * You are welcome to use it, modify it and redistribute it under the following conditions:
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 * <p/>
 * Special thanks:
 * <ul>
 * <li>@microgeek (original idea, names and packet parameters)
 * <li>@ShadyPotato (1.8 names, ids and packet parameters)
 * <li>@RingOfStorms (particle behavior)
 * <li>@Cybermaxke (particle behavior)
 * </ul>
 * <p/>
 * <i>It would be nice if you provide credit to me if you use this class in a published project</i>
 *
 * @author DarkBlade12
 * @version 1.7
 */
public enum ParticleUtil {

    /**
     * A particle effect which is displayed by exploding tnt and creepers:
     * <ul>
     * <li>It looks like a white cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    EXPLOSION_NORMAL("explode", 0, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by exploding ghast fireballs and wither skulls:
     * <ul>
     * <li>It looks like a gray ball which is fading away
     * <li>The speed value slightly influences the size of this particle effect
     * </ul>
     */
    EXPLOSION_LARGE("largeexplode", 1, -1),
    /**
     * A particle effect which is displayed by exploding tnt and creepers:
     * <ul>
     * <li>It looks like a crowd of gray balls which are fading away
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    EXPLOSION_HUGE("hugeexplosion", 2, -1),
    /**
     * A particle effect which is displayed by launching fireworks:
     * <ul>
     * <li>It looks like a white star which is sparkling
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    FIREWORKS_SPARK("fireworksSpark", 3, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by swimming entities and arrows in water:
     * <ul>
     * <li>It looks like a bubble
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    WATER_BUBBLE("bubble", 4, -1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_WATER),
    /**
     * A particle effect which is displayed by swimming entities and shaking wolves:
     * <ul>
     * <li>It looks like a blue drop
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    WATER_SPLASH("splash", 5, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed on water when fishing:
     * <ul>
     * <li>It looks like a blue droplet
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    WATER_WAKE("wake", 6, 7, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by water:
     * <ul>
     * <li>It looks like a tiny blue square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    SUSPENDED("suspended", 7, -1, ParticleProperty.REQUIRES_WATER),
    /**
     * A particle effect which is displayed by air when close to bedrock and the in the void:
     * <ul>
     * <li>It looks like a tiny gray square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    SUSPENDED_DEPTH("depthSuspend", 8, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed when landing a critical hit and by arrows:
     * <ul>
     * <li>It looks like a light brown cross
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    CRIT("crit", 9, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed when landing a hit with an enchanted weapon:
     * <ul>
     * <li>It looks like a cyan star
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    CRIT_MAGIC("magicCrit", 10, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by primed tnt, torches, droppers, dispensers, end
     * portals, brewing stands and monster spawners:
     * <ul>
     * <li>It looks like a little gray cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    SMOKE_NORMAL("smoke", 11, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by fire, minecarts with furnace and blazes:
     * <ul>
     * <li>It looks like a large gray cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    SMOKE_LARGE("largesmoke", 12, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed when splash potions or bottles o' enchanting hit
     * something:
     * <ul>
     * <li>It looks like a white swirl
     * <li>The speed value causes the particle to only move upwards when set to 0
     * <li>Only the motion on the y-axis can be controlled, the motion on the x- and z-axis are
     * multiplied by 0.1 when setting the values to 0
     * </ul>
     */
    SPELL("spell", 13, -1),
    /**
     * A particle effect which is displayed when instant splash potions hit something:
     * <ul>
     * <li>It looks like a white cross
     * <li>The speed value causes the particle to only move upwards when set to 0
     * <li>Only the motion on the y-axis can be controlled, the motion on the x- and z-axis are
     * multiplied by 0.1 when setting the values to 0
     * </ul>
     */
    SPELL_INSTANT("instantSpell", 14, -1),
    /**
     * A particle effect which is displayed by entities with active potion effects:
     * <ul>
     * <li>It looks like a colored swirl
     * <li>The speed value causes the particle to be colored black when set to 0
     * <li>The particle color gets lighter when increasing the speed and darker when decreasing the
     * speed
     * </ul>
     */
    SPELL_MOB("mobSpell", 15, -1, ParticleProperty.COLORABLE),
    /**
     * A particle effect which is displayed by entities with active potion effects applied through
     * a beacon:
     * <ul>
     * <li>It looks like a transparent colored swirl
     * <li>The speed value causes the particle to be always colored black when set to 0
     * <li>The particle color gets lighter when increasing the speed and darker when decreasing the
     * speed
     * </ul>
     */
    SPELL_MOB_AMBIENT("mobSpellAmbient", 16, -1, ParticleProperty.COLORABLE),
    /**
     * A particle effect which is displayed by witches:
     * <ul>
     * <li>It looks like a purple cross
     * <li>The speed value causes the particle to only move upwards when set to 0
     * <li>Only the motion on the y-axis can be controlled, the motion on the x- and z-axis are
     * multiplied by 0.1 when setting the values to 0
     * </ul>
     */
    SPELL_WITCH("witchMagic", 17, -1),
    /**
     * A particle effect which is displayed by blocks beneath a water source:
     * <ul>
     * <li>It looks like a blue drip
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    DRIP_WATER("dripWater", 18, -1),
    /**
     * A particle effect which is displayed by blocks beneath a lava source:
     * <ul>
     * <li>It looks like an orange drip
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    DRIP_LAVA("dripLava", 19, -1),
    /**
     * A particle effect which is displayed when attacking a villager in a village:
     * <ul>
     * <li>It looks like a cracked gray heart
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    VILLAGER_ANGRY("angryVillager", 20, -1),
    /**
     * A particle effect which is displayed when using bone meal and trading with a villager in a
     * village:
     * <ul>
     * <li>It looks like a green star
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    VILLAGER_HAPPY("happyVillager", 21, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by mycelium:
     * <ul>
     * <li>It looks like a tiny gray square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    TOWN_AURA("townaura", 22, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by note blocks:
     * <ul>
     * <li>It looks like a colored note
     * <li>The speed value causes the particle to be colored green when set to 0
     * </ul>
     */
    NOTE("note", 23, -1, ParticleProperty.COLORABLE),
    /**
     * A particle effect which is displayed by nether portals, endermen, ender pearls, eyes of
     * ender, ender chests and dragon eggs:
     * <ul>
     * <li>It looks like a purple cloud
     * <li>The speed value influences the spread of this particle effect
     * </ul>
     */
    PORTAL("portal", 24, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by enchantment tables which are nearby bookshelves:
     * <ul>
     * <li>It looks like a cryptic white letter
     * <li>The speed value influences the spread of this particle effect
     * </ul>
     */
    ENCHANTMENT_TABLE("enchantmenttable", 25, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by torches, active furnaces, magma cubes and monster
     * spawners:
     * <ul>
     * <li>It looks like a tiny flame
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    FLAME("flame", 26, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by lava:
     * <ul>
     * <li>It looks like a spark
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    LAVA("lava", 27, -1),
    /**
     * A particle effect which is currently unused:
     * <ul>
     * <li>It looks like a transparent gray square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    FOOTSTEP("footstep", 28, -1),
    /**
     * A particle effect which is displayed when a mob dies:
     * <ul>
     * <li>It looks like a large white cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    CLOUD("cloud", 29, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by redstone ore, powered redstone, redstone torches and
     * redstone repeaters:
     * <ul>
     * <li>It looks like a tiny colored cloud
     * <li>The speed value causes the particle to be colored red when set to 0
     * </ul>
     */
    REDSTONE("reddust", 30, -1, ParticleProperty.COLORABLE),
    /**
     * A particle effect which is displayed when snowballs hit a block:
     * <ul>
     * <li>It looks like a little piece with the snowball texture
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    SNOWBALL("snowballpoof", 31, -1),
    /**
     * A particle effect which is currently unused:
     * <ul>
     * <li>It looks like a tiny white cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    SNOW_SHOVEL("snowshovel", 32, -1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by slimes:
     * <ul>
     * <li>It looks like a tiny part of the slimeball icon
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    SLIME("slime", 33, -1),
    /**
     * A particle effect which is displayed when breeding and taming animals:
     * <ul>
     * <li>It looks like a red heart
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    HEART("heart", 34, -1),
    /**
     * A particle effect which is displayed by barriers:
     * <ul>
     * <li>It looks like a red box with a slash through it
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    BARRIER("barrier", 35, 8),
    /**
     * A particle effect which is displayed when breaking a tool or eggs hit a block:
     * <ul>
     * <li>It looks like a little piece with an item texture
     * </ul>
     */
    ITEM_CRACK("iconcrack", 36, -1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
    /**
     * A particle effect which is displayed when breaking blocks or sprinting:
     * <ul>
     * <li>It looks like a little piece with a block texture
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    BLOCK_CRACK("blockcrack", 37, -1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
    /**
     * A particle effect which is displayed when falling:
     * <ul>
     * <li>It looks like a little piece with a block texture
     * </ul>
     */
    BLOCK_DUST("blockdust", 38, 7, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
    /**
     * A particle effect which is displayed when rain hits the ground:
     * <ul>
     * <li>It looks like a blue droplet
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    WATER_DROP("droplet", 39, 8),
    /**
     * A particle effect which is currently unused:
     * <ul>
     * <li>It has no visual effect
     * </ul>
     */
    ITEM_TAKE("take", 40, 8),
    /**
     * A particle effect which is displayed by elder guardians:
     * <ul>
     * <li>It looks like the shape of the elder guardian
     * <li>The speed value has no influence on this particle effect
     * <li>The offset values have no influence on this particle effect
     * </ul>
     */
    MOB_APPEARANCE("mobappearance", 41, 8);

    private static final Map<String, ParticleUtil> NAME_MAP = new HashMap<>();
    private static final Map<Integer, ParticleUtil> ID_MAP = new HashMap<>();

    // Initialize map for quick name and id lookup
    static {
        for (ParticleUtil effect : values()) {
            NAME_MAP.put(effect.name, effect);
            ID_MAP.put(effect.id, effect);
        }
    }

    private final String name;
    private final int id;
    private final int requiredVersion;
    private final List<ParticleProperty> properties;

    /**
     * Construct a new particle effect
     *
     * @param name            Name of this particle effect
     * @param id              Id of this particle effect
     * @param requiredVersion Version which is required (1.x)
     * @param properties      Properties of this particle effect
     */
    private ParticleUtil(String name, int id, int requiredVersion, ParticleProperty... properties) {
        this.name = name;
        this.id = id;
        this.requiredVersion = requiredVersion;
        this.properties = Arrays.asList(properties);
    }

    /**
     * Returns the particle effect with the given name
     *
     * @param name Name of the particle effect
     * @return The particle effect
     */
    public static ParticleUtil fromName(String name) {
        for (Entry<String, ParticleUtil> entry : NAME_MAP.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase(name)) {
                continue;
            }
            return entry.getValue();
        }
        return null;
    }

    /**
     * Returns the particle effect with the given id
     *
     * @param id Id of the particle effect
     * @return The particle effect
     */
    public static ParticleUtil fromId(int id) {
        for (Entry<Integer, ParticleUtil> entry : ID_MAP.entrySet()) {
            if (entry.getKey() != id) {
                continue;
            }
            return entry.getValue();
        }
        return null;
    }

    /**
     * Determine if water is at a certain location
     *
     * @param location Location to check
     * @return Whether water is at this location or not
     */
    private static boolean isWater(Location location) {
        Material material = location.getBlock().getType();
        return material == Material.WATER || material == Material.STATIONARY_WATER;
    }

    /**
     * Determine if the distance between @param location and one of the players exceeds 256
     *
     * @param location Location to check
     * @return Whether the distance exceeds 256 or not
     */
    private static boolean isLongDistance(Location location, List<Player> players) {
        for (Player player : players) {
            if (!player.getWorld().equals(location.getWorld())) {
                return true;
            }
            if (player.getLocation().distanceSquared(location) < 65536) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * Determine if the data type for a particle effect is correct
     *
     * @param effect Particle effect
     * @param data   Particle data
     * @return Whether the data type is correct or not
     */
    private static boolean isDataCorrect(ParticleUtil effect, ParticleData data) {
        return ((effect == BLOCK_CRACK || effect == BLOCK_DUST) && data instanceof BlockData) || (effect == ITEM_CRACK && data instanceof ItemData);
    }

    /**
     * Determine if the color type for a particle effect is correct
     *
     * @param effect Particle effect
     * @param color  Particle color
     * @return Whether the color type is correct or not
     */
    private static boolean isColorCorrect(ParticleUtil effect, ParticleColor color) {
        return ((effect == SPELL_MOB || effect == SPELL_MOB_AMBIENT || effect == REDSTONE) && color instanceof OrdinaryColor) || (effect == NOTE && color instanceof NoteColor);
    }

    /**
     * Returns the name of this particle effect
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the id of this particle effect
     *
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the required version for this particle effect (1.x)
     *
     * @return The required version
     */
    public int getRequiredVersion() {
        return requiredVersion;
    }

    /**
     * Determine if this particle effect has a specific property
     *
     * @return Whether it has the property or not
     */
    public boolean hasProperty(ParticleProperty property) {
        return properties.contains(property);
    }

    /**
     * Determine if this particle effect is supported by your current server version
     *
     * @return Whether the particle effect is supported or not
     */
    public boolean isSupported() {
        if (requiredVersion == -1) {
            return true;
        }
        return ParticlePacket.getVersion() >= requiredVersion;
    }

    /**
     * Displays a particle effect which is only visible for all players within a certain range in
     * the world of @param center
     *
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param center  Center location of the effect
     * @param range   Range of the visibility
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleDataException    If the particle effect requires additional data
     * @throws IllegalArgumentException If the particle effect requires water and none is at the
     *                                  center location
     * @see ParticlePacket
     * @see ParticlePacket#sendTo(Location, double)
     */
    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect requires additional data");
        }
        if (hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(center)) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, range > 256, null).sendTo(center, range);
    }

    /**
     * Displays a particle effect which is only visible for the specified players
     *
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param center  Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleDataException    If the particle effect requires additional data
     * @throws IllegalArgumentException If the particle effect requires water and none is at the
     *                                  center location
     * @see ParticlePacket
     * @see ParticlePacket#sendTo(Location, List)
     */
    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect requires additional data");
        }
        if (hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(center)) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, isLongDistance(center, players), null).sendTo(center, players);
    }

    /**
     * Displays a particle effect which is only visible for the specified players
     *
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param center  Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleDataException    If the particle effect requires additional data
     * @throws IllegalArgumentException If the particle effect requires water and none is at the
     *                                  center location
     * @see #display(float, float, float, float, int, Location, List)
     */
    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, Player... players) throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        display(offsetX, offsetY, offsetZ, speed, amount, center, Arrays.asList(players));
    }

    /**
     * Displays a single particle which flies into a determined direction and is only visible for
     * all players within a certain range in the world of
     *
     * @param direction Direction of the particle
     * @param speed     Display speed of the particle
     * @param center    Center location of the effect
     * @param range     Range of the visibility
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleDataException    If the particle effect requires additional data
     * @throws IllegalArgumentException If the particle effect is not directional or if it requires
     *                                  water and none is at the center location
     * @see ParticlePacket#ParticlePacket(ParticleEffect, Vector, float, boolean, ParticleData)
     * @see ParticlePacket#sendTo(Location, double)
     */
    public void display(Vector direction, float speed, Location center, double range) throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect requires additional data");
        }
        if (!hasProperty(ParticleProperty.DIRECTIONAL)) {
            throw new IllegalArgumentException("This particle effect is not directional");
        }
        if (hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(center)) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticlePacket(this, direction, speed, range > 256, null).sendTo(center, range);
    }

    /**
     * Displays a single particle which flies into a determined direction and is only visible for
     * the specified players
     *
     * @param direction Direction of the particle
     * @param speed     Display speed of the particle
     * @param center    Center location of the effect
     * @param players   Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleDataException    If the particle effect requires additional data
     * @throws IllegalArgumentException If the particle effect is not directional or if it requires
     *                                  water and none is at the center location
     * @see ParticlePacket#ParticlePacket(ParticleEffect, Vector, float, boolean, ParticleData)
     * @see ParticlePacket#sendTo(Location, List)
     */
    public void display(Vector direction, float speed, Location center, List<Player> players) throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect requires additional data");
        }
        if (!hasProperty(ParticleProperty.DIRECTIONAL)) {
            throw new IllegalArgumentException("This particle effect is not directional");
        }
        if (hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(center)) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticlePacket(this, direction, speed, isLongDistance(center, players), null).sendTo(center, players);
    }

    /**
     * Displays a single particle which flies into a determined direction and is only visible for
     * the specified players
     *
     * @param direction Direction of the particle
     * @param speed     Display speed of the particle
     * @param center    Center location of the effect
     * @param players   Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleDataException    If the particle effect requires additional data
     * @throws IllegalArgumentException If the particle effect is not directional or if it requires
     *                                  water and none is at the center location
     * @see #display(Vector, float, Location, List)
     */
    public void display(Vector direction, float speed, Location center, Player... players) throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        display(direction, speed, center, Arrays.asList(players));
    }

    /**
     * Displays a single particle which is colored and only visible for all players within a
     * certain range in the world of @param center
     *
     * @param color  Color of the particle
     * @param center Center location of the effect
     * @param range  Range of the visibility
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleColorException   If the particle effect is not colorable or the color type is
     *                                  incorrect
     * @see ParticlePacket#ParticlePacket(ParticleEffect, ParticleColor, boolean)
     * @see ParticlePacket#sendTo(Location, double)
     */
    public void display(ParticleColor color, Location center, double range) throws ParticleVersionException, ParticleColorException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (!hasProperty(ParticleProperty.COLORABLE)) {
            throw new ParticleColorException("This particle effect is not colorable");
        }
        if (!isColorCorrect(this, color)) {
            throw new ParticleColorException("The particle color type is incorrect");
        }
        new ParticlePacket(this, color, range > 256).sendTo(center, range);
    }

    /**
     * Displays a single particle which is colored and only visible for the specified players
     *
     * @param color   Color of the particle
     * @param center  Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleColorException   If the particle effect is not colorable or the color type is
     *                                  incorrect
     * @see ParticlePacket#ParticlePacket(ParticleEffect, ParticleColor, boolean)
     * @see ParticlePacket#sendTo(Location, List)
     */
    public void display(ParticleColor color, Location center, List<Player> players) throws ParticleVersionException, ParticleColorException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (!hasProperty(ParticleProperty.COLORABLE)) {
            throw new ParticleColorException("This particle effect is not colorable");
        }
        if (!isColorCorrect(this, color)) {
            throw new ParticleColorException("The particle color type is incorrect");
        }
        new ParticlePacket(this, color, isLongDistance(center, players)).sendTo(center, players);
    }

    /**
     * Displays a single particle which is colored and only visible for the specified players
     *
     * @param color   Color of the particle
     * @param center  Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleColorException   If the particle effect is not colorable or the color type is
     *                                  incorrect
     * @see #display(ParticleColor, Location, List)
     */
    public void display(ParticleColor color, Location center, Player... players) throws ParticleVersionException, ParticleColorException {
        display(color, center, Arrays.asList(players));
    }

    /**
     * Displays a particle effect which requires additional data and is only visible for all
     * players within a certain range in the world of @param center
     *
     * @param data    Data of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param center  Center location of the effect
     * @param range   Range of the visibility
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleDataException    If the particle effect does not require additional data or if
     *                                  the data type is incorrect
     * @see ParticlePacket
     * @see ParticlePacket#sendTo(Location, double)
     */
    public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) throws ParticleVersionException, ParticleDataException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect does not require additional data");
        }
        if (!isDataCorrect(this, data)) {
            throw new ParticleDataException("The particle data type is incorrect");
        }
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, range > 256, data).sendTo(center, range);
    }

    /**
     * Displays a particle effect which requires additional data and is only visible for the
     * specified players
     *
     * @param data    Data of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param center  Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleDataException    If the particle effect does not require additional data or if
     *                                  the data type is incorrect
     * @see ParticlePacket
     * @see ParticlePacket#sendTo(Location, List)
     */
    public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) throws ParticleVersionException, ParticleDataException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect does not require additional data");
        }
        if (!isDataCorrect(this, data)) {
            throw new ParticleDataException("The particle data type is incorrect");
        }
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, isLongDistance(center, players), data).sendTo(center, players);
    }

    /**
     * Displays a particle effect which requires additional data and is only visible for the
     * specified players
     *
     * @param data    Data of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param center  Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleDataException    If the particle effect does not require additional data or if
     *                                  the data type is incorrect
     * @see #display(ParticleData, float, float, float, float, int, Location, List)
     */
    public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, Player... players) throws ParticleVersionException, ParticleDataException {
        display(data, offsetX, offsetY, offsetZ, speed, amount, center, Arrays.asList(players));
    }

    /**
     * Displays a single particle which requires additional data that flies into a determined
     * direction and is only visible for all players within a certain range in the world of @param
     * center
     *
     * @param data      Data of the effect
     * @param direction Direction of the particle
     * @param speed     Display speed of the particles
     * @param center    Center location of the effect
     * @param range     Range of the visibility
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleDataException    If the particle effect does not require additional data or if
     *                                  the data type is incorrect
     * @see ParticlePacket
     * @see ParticlePacket#sendTo(Location, double)
     */
    public void display(ParticleData data, Vector direction, float speed, Location center, double range) throws ParticleVersionException, ParticleDataException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect does not require additional data");
        }
        if (!isDataCorrect(this, data)) {
            throw new ParticleDataException("The particle data type is incorrect");
        }
        new ParticlePacket(this, direction, speed, range > 256, data).sendTo(center, range);
    }

    /**
     * Displays a single particle which requires additional data that flies into a determined
     * direction and is only visible for the specified players
     *
     * @param data      Data of the effect
     * @param direction Direction of the particle
     * @param speed     Display speed of the particles
     * @param center    Center location of the effect
     * @param players   Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleDataException    If the particle effect does not require additional data or if
     *                                  the data type is incorrect
     * @see ParticlePacket
     * @see ParticlePacket#sendTo(Location, List)
     */
    public void display(ParticleData data, Vector direction, float speed, Location center, List<Player> players) throws ParticleVersionException, ParticleDataException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect does not require additional data");
        }
        if (!isDataCorrect(this, data)) {
            throw new ParticleDataException("The particle data type is incorrect");
        }
        new ParticlePacket(this, direction, speed, isLongDistance(center, players), data).sendTo(center, players);
    }

    /**
     * Displays a single particle which requires additional data that flies into a determined
     * direction and is only visible for the specified players
     *
     * @param data      Data of the effect
     * @param direction Direction of the particle
     * @param speed     Display speed of the particles
     * @param center    Center location of the effect
     * @param players   Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server
     *                                  version
     * @throws ParticleDataException    If the particle effect does not require additional data or if
     *                                  the data type is incorrect
     * @see #display(ParticleData, Vector, float, Location, List)
     */
    public void display(ParticleData data, Vector direction, float speed, Location center, Player... players) throws ParticleVersionException, ParticleDataException {
        display(data, direction, speed, center, Arrays.asList(players));
    }

    /**
     * Represents the property of a particle effect
     * <p/>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
     * conditions
     *
     * @author DarkBlade12
     * @since 1.7
     */
    public static enum ParticleProperty {

        /**
         * The particle effect requires water to be displayed
         */
        REQUIRES_WATER,
        /**
         * The particle effect requires block or item data to be displayed
         */
        REQUIRES_DATA,
        /**
         * The particle effect uses the offsets as direction values
         */
        DIRECTIONAL,
        /**
         * The particle effect uses the offsets as color values
         */
        COLORABLE;
    }

    /**
     * Represents the particle data for effects like
     * {@link ParticleEffect#ITEM_CRACK}, {@link ParticleEffect#BLOCK_CRACK} and
     * {@link ParticleEffect#BLOCK_DUST}
     * <p/>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
     * conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    public static abstract class ParticleData {

        private final Material material;
        private final byte data;
        private final int[] packetData;

        /**
         * Construct a new particle data
         *
         * @param material Material of the item/block
         * @param data     Data value of the item/block
         */
        @SuppressWarnings("deprecation")
        public ParticleData(Material material, byte data) {
            this.material = material;
            this.data = data;
            this.packetData = new int[]{material.getId(), data};
        }

        /**
         * Returns the material of this data
         *
         * @return The material
         */
        public Material getMaterial() {
            return material;
        }

        /**
         * Returns the data value of this data
         *
         * @return The data value
         */
        public byte getData() {
            return data;
        }

        /**
         * Returns the data as an int array for packet construction
         *
         * @return The data for the packet
         */
        public int[] getPacketData() {
            return packetData;
        }

        /**
         * Returns the data as a string for pre 1.8 versions
         *
         * @return The data string for the packet
         */
        public String getPacketDataString() {
            return "_" + packetData[0] + "_" + packetData[1];
        }
    }

    /**
     * Represents the item data for the {@link ParticleEffect#ITEM_CRACK} effect
     * <p/>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
     * conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    public static final class ItemData extends ParticleData {

        /**
         * Construct a new item data
         *
         * @param material Material of the item
         * @param data     Data value of the item
         * @see ParticleData#ParticleData(Material, byte)
         */
        public ItemData(Material material, byte data) {
            super(material, data);
        }
    }

    /**
     * Represents the block data for the {@link ParticleEffect#BLOCK_CRACK} and
     * {@link ParticleEffect#BLOCK_DUST} effects
     * <p/>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
     * conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    public static final class BlockData extends ParticleData {

        /**
         * Construct a new block data
         *
         * @param material Material of the block
         * @param data     Data value of the block
         * @throws IllegalArgumentException If the material is not a block
         * @see ParticleData#ParticleData(Material, byte)
         */
        public BlockData(Material material, byte data) throws IllegalArgumentException {
            super(material, data);
            if (!material.isBlock()) {
                throw new IllegalArgumentException("The material is not a block");
            }
        }
    }

    /**
     * Represents the color for effects like
     * {@link ParticleEffect#SPELL_MOB}, {@link ParticleEffect#SPELL_MOB_AMBIENT}, {@link ParticleEffect#REDSTONE}
     * and {@link ParticleEffect#NOTE}
     * <p/>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
     * conditions
     *
     * @author DarkBlade12
     * @since 1.7
     */
    public static abstract class ParticleColor {

        /**
         * Returns the value for the offsetX field
         *
         * @return The offsetX value
         */
        public abstract float getValueX();

        /**
         * Returns the value for the offsetY field
         *
         * @return The offsetY value
         */
        public abstract float getValueY();

        /**
         * Returns the value for the offsetZ field
         *
         * @return The offsetZ value
         */
        public abstract float getValueZ();
    }

    /**
     * Represents the color for effects like
     * {@link ParticleEffect#SPELL_MOB}, {@link ParticleEffect#SPELL_MOB_AMBIENT} and
     * {@link ParticleEffect#NOTE}
     * <p/>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
     * conditions
     *
     * @author DarkBlade12
     * @since 1.7
     */
    public static final class OrdinaryColor extends ParticleColor {

        private final int red;
        private final int green;
        private final int blue;

        /**
         * Construct a new ordinary color
         *
         * @param red   Red value of the RGB format
         * @param green Green value of the RGB format
         * @param blue  Blue value of the RGB format
         * @throws IllegalArgumentException If one of the values is lower than 0 or higher than 255
         */
        public OrdinaryColor(int red, int green, int blue) throws IllegalArgumentException {
            if (red < 0) {
                throw new IllegalArgumentException("The red value is lower than 0");
            }
            if (red > 255) {
                throw new IllegalArgumentException("The red value is higher than 255");
            }
            this.red = red;
            if (green < 0) {
                throw new IllegalArgumentException("The green value is lower than 0");
            }
            if (green > 255) {
                throw new IllegalArgumentException("The green value is higher than 255");
            }
            this.green = green;
            if (blue < 0) {
                throw new IllegalArgumentException("The blue value is lower than 0");
            }
            if (blue > 255) {
                throw new IllegalArgumentException("The blue value is higher than 255");
            }
            this.blue = blue;
        }

        /**
         * Returns the red value of the RGB format
         *
         * @return The red value
         */
        public int getRed() {
            return red;
        }

        /**
         * Returns the green value of the RGB format
         *
         * @return The green value
         */
        public int getGreen() {
            return green;
        }

        /**
         * Returns the blue value of the RGB format
         *
         * @return The blue value
         */
        public int getBlue() {
            return blue;
        }

        /**
         * Returns the red value divided by 255
         *
         * @return The offsetX value
         */
        @Override
        public float getValueX() {
            return red / 255F;
        }

        /**
         * Returns the green value divided by 255
         *
         * @return The offsetY value
         */
        @Override
        public float getValueY() {
            return green / 255F;
        }

        /**
         * Returns the blue value divided by 255
         *
         * @return The offsetZ value
         */
        @Override
        public float getValueZ() {
            return blue / 255F;
        }
    }

    /**
     * Represents the color for the {@link ParticleEffect#NOTE} effect
     * <p/>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
     * conditions
     *
     * @author DarkBlade12
     * @since 1.7
     */
    public static final class NoteColor extends ParticleColor {

        private final int note;

        /**
         * Construct a new note color
         *
         * @param note Note id which determines color
         * @throws IllegalArgumentException If the note value is lower than 0 or higher than 255
         */
        public NoteColor(int note) throws IllegalArgumentException {
            if (note < 0) {
                throw new IllegalArgumentException("The note value is lower than 0");
            }
            if (note > 24) {
                throw new IllegalArgumentException("The note value is higher than 24");
            }
            this.note = note;
        }

        /**
         * Returns the note value divided by 24
         *
         * @return The offsetX value
         */
        @Override
        public float getValueX() {
            return note / 24F;
        }

        /**
         * Returns zero because the offsetY value is unused
         *
         * @return zero
         */
        @Override
        public float getValueY() {
            return 0;
        }

        /**
         * Returns zero because the offsetZ value is unused
         *
         * @return zero
         */
        @Override
        public float getValueZ() {
            return 0;
        }

    }

    /**
     * Represents a runtime exception that is thrown either if the displayed particle effect
     * requires data and has none or vice-versa or if the data type is incorrect
     * <p/>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
     * conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    private static final class ParticleDataException extends RuntimeException {

        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * Construct a new particle data exception
         *
         * @param message Message that will be logged
         */
        public ParticleDataException(String message) {
            super(message);
        }
    }

    /**
     * Represents a runtime exception that is thrown either if the displayed particle effect is not
     * colorable or if the particle color type is incorrect
     * <p/>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
     * conditions
     *
     * @author DarkBlade12
     * @since 1.7
     */
    private static final class ParticleColorException extends RuntimeException {

        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * Construct a new particle color exception
         *
         * @param message Message that will be logged
         */
        public ParticleColorException(String message) {
            super(message);
        }
    }

    /**
     * Represents a runtime exception that is thrown if the displayed particle effect requires a
     * newer version
     * <p/>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
     * conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    private static final class ParticleVersionException extends RuntimeException {

        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * Construct a new particle version exception
         *
         * @param message Message that will be logged
         */
        public ParticleVersionException(String message) {
            super(message);
        }
    }

    /**
     * Represents a particle effect packet with all attributes which is used for sending packets to
     * the players
     * <p/>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
     * conditions
     *
     * @author DarkBlade12
     * @since 1.5
     */
    public static final class ParticlePacket {

        private static int version;
        private static Class<?> enumParticle;
        private static Constructor<?> packetConstructor;
        private static Method getHandle;
        private static Field playerConnection;
        private static Method sendPacket;
        private static boolean initialized;
        private final ParticleUtil effect;
        private final float offsetX;
        private final float offsetY;
        private final float offsetZ;
        private final float speed;
        private final int amount;
        private final boolean longDistance;
        private final ParticleData data;
        private Object packet;

        /**
         * Construct a new particle packet
         *
         * @param effect       Particle effect
         * @param offsetX      Maximum distance particles can fly away from the center on the x-axis
         * @param offsetY      Maximum distance particles can fly away from the center on the y-axis
         * @param offsetZ      Maximum distance particles can fly away from the center on the z-axis
         * @param speed        Display speed of the particles
         * @param amount       Amount of particles
         * @param longDistance Indicates whether the maximum distance is increased from 256 to
         *                     65536
         * @param data         Data of the effect
         * @throws IllegalArgumentException If the speed or amount is lower than 0
         * @see #initialize()
         */
        public ParticlePacket(ParticleUtil effect, float offsetX, float offsetY, float offsetZ, float speed, int amount, boolean longDistance, ParticleData data) throws IllegalArgumentException {
            initialize();
            if (speed < 0) {
                throw new IllegalArgumentException("The speed is lower than 0");
            }
            if (amount < 0) {
                throw new IllegalArgumentException("The amount is lower than 0");
            }
            this.effect = effect;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.speed = speed;
            this.amount = amount;
            this.longDistance = longDistance;
            this.data = data;
        }

        /**
         * Construct a new particle packet of a single particle flying into a determined direction
         *
         * @param effect       Particle effect
         * @param direction    Direction of the particle
         * @param speed        Display speed of the particle
         * @param longDistance Indicates whether the maximum distance is increased from 256 to
         *                     65536
         * @param data         Data of the effect
         * @throws IllegalArgumentException If the speed is lower than 0
         * @see #ParticleEffect(ParticleEffect, float, float, float, float, int, boolean,
         * ParticleData)
         */
        public ParticlePacket(ParticleUtil effect, Vector direction, float speed, boolean longDistance, ParticleData data) throws IllegalArgumentException {
            this(effect, (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(), speed, 0, longDistance, data);
        }

        /**
         * Construct a new particle packet of a single colored particle
         *
         * @param effect       Particle effect
         * @param color        Color of the particle
         * @param longDistance Indicates whether the maximum distance is increased from 256 to
         *                     65536
         * @see #ParticleEffect(ParticleEffect, float, float, float, float, int, boolean,
         * ParticleData)
         */
        public ParticlePacket(ParticleUtil effect, ParticleColor color, boolean longDistance) {
            this(effect, color.getValueX(), color.getValueY(), color.getValueZ(), 1, 0, longDistance, null);
        }

        /**
         * Initializes {@link #packetConstructor}, {@link #getHandle}, {@link #playerConnection}
         * and {@link #sendPacket} and sets {@link #initialized} to <code>true</code> if it
         * succeeds
         * <p/>
         * <b>Note:</b> These fields only have to be initialized once, so it will return if
         * {@link #initialized} is already set to <code>true</code>
         *
         * @throws VersionIncompatibleException if your bukkit version is not supported by this
         *                                      library
         */
        public static void initialize() throws VersionIncompatibleException {
            if (initialized) {
                return;
            }
            try {
                version = Integer.parseInt(Character.toString(PackageType.getServerVersion().charAt(3)));
                if (version > 7) {
                    enumParticle = PackageType.MINECRAFT_SERVER.getClass("EnumParticle");
                }
                Class<?> packetClass = PackageType.MINECRAFT_SERVER.getClass(version < 7 ? "Packet63WorldParticles" : "PacketPlayOutWorldParticles");
                packetConstructor = ReflectionUtils.getConstructor(packetClass);
                getHandle = ReflectionUtils.getMethod("CraftPlayer", PackageType.CRAFTBUKKIT_ENTITY, "getHandle");
                playerConnection = ReflectionUtils.getField("EntityPlayer", PackageType.MINECRAFT_SERVER, false, "playerConnection");
                sendPacket = ReflectionUtils.getMethod(playerConnection.getType(), "sendPacket", PackageType.MINECRAFT_SERVER.getClass("Packet"));
            } catch (Exception exception) {
                throw new VersionIncompatibleException("Your current bukkit version seems to be incompatible with " + "this library", exception);
            }
            initialized = true;
        }

        /**
         * Returns the version of your server (1.x)
         *
         * @return The version number
         */
        public static int getVersion() {
            return version;
        }

        /**
         * Determine if {@link #packetConstructor}, {@link #getHandle}, {@link #playerConnection}
         * and {@link #sendPacket} are initialized
         *
         * @return Whether these fields are initialized or not
         * @see #initialize()
         */
        public static boolean isInitialized() {
            return initialized;
        }

        /**
         * Initializes {@link #packet} with all set values
         *
         * @param center Center location of the effect
         * @throws PacketInstantiationException If instantion fails due to an unknown error
         */
        private void initializePacket(Location center) throws PacketInstantiationException {
            if (packet != null) {
                return;
            }
            try {
                packet = packetConstructor.newInstance();
                if (version < 8) {
                    String name = effect.getName();
                    if (data != null) {
                        name += data.getPacketDataString();
                    }
                    ReflectionUtils.setValue(packet, true, "a", name);
                } else {
                    ReflectionUtils.setValue(packet, true, "a", enumParticle.getEnumConstants()[effect.getId()]);
                    ReflectionUtils.setValue(packet, true, "j", longDistance);
                    if (data != null) {
                        ReflectionUtils.setValue(packet, true, "k", data.getPacketData());
                    }
                }
                ReflectionUtils.setValue(packet, true, "b", (float) center.getX());
                ReflectionUtils.setValue(packet, true, "c", (float) center.getY());
                ReflectionUtils.setValue(packet, true, "d", (float) center.getZ());
                ReflectionUtils.setValue(packet, true, "e", offsetX);
                ReflectionUtils.setValue(packet, true, "f", offsetY);
                ReflectionUtils.setValue(packet, true, "g", offsetZ);
                ReflectionUtils.setValue(packet, true, "h", speed);
                ReflectionUtils.setValue(packet, true, "i", amount);
            } catch (Exception exception) {
                throw new PacketInstantiationException("Packet instantiation failed", exception);
            }
        }

        /**
         * Sends the packet to a single player and caches it
         *
         * @param center Center location of the effect
         * @param player Receiver of the packet
         * @throws PacketInstantiationException If instantion fails due to an unknown error
         * @throws PacketSendingException       If sending fails due to an unknown error
         * @see #initializePacket(Location)
         */
        public void sendTo(Location center, Player player) throws PacketInstantiationException, PacketSendingException {
            initializePacket(center);
            try {
                sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), packet);
            } catch (Exception exception) {
                throw new PacketSendingException("Failed to send the packet to player '" + player.getName() + "'", exception);
            }
        }

        /**
         * Sends the packet to all players in the list
         *
         * @param center  Center location of the effect
         * @param players Receivers of the packet
         * @throws IllegalArgumentException If the player list is empty
         * @see #sendTo(Location center, Player player)
         */
        public void sendTo(Location center, List<Player> players) throws IllegalArgumentException {
            if (players.isEmpty()) {
                throw new IllegalArgumentException("The player list is empty");
            }
            for (Player player : players) {
                sendTo(center, player);
            }
        }

        /**
         * Sends the packet to all players in a certain range
         *
         * @param center Center location of the effect
         * @param range  Range in which players will receive the packet (Maximum range for particles
         *               is usually 16, but it can differ for some types)
         * @throws IllegalArgumentException If the range is lower than 1
         * @see #sendTo(Location center, Player player)
         */
        @SuppressWarnings("deprecation")
        public void sendTo(Location center, double range) throws IllegalArgumentException {
            if (range < 1) {
                throw new IllegalArgumentException("The range is lower than 1");
            }
            String worldName = center.getWorld().getName();
            double squared = range * range;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().getName().equals(worldName) || player.getLocation().distanceSquared(center) > squared) {
                    continue;
                }
                sendTo(center, player);
            }
        }

        /**
         * Represents a runtime exception that is thrown if a bukkit version is not compatible with
         * this library
         * <p/>
         * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
         * conditions
         *
         * @author DarkBlade12
         * @since 1.5
         */
        private static final class VersionIncompatibleException extends RuntimeException {

            private static final long serialVersionUID = 3203085387160737484L;

            /**
             * Construct a new version incompatible exception
             *
             * @param message Message that will be logged
             * @param cause   Cause of the exception
             */
            public VersionIncompatibleException(String message, Throwable cause) {
                super(message, cause);
            }
        }

        /**
         * Represents a runtime exception that is thrown if packet instantiation fails
         * <p/>
         * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
         * conditions
         *
         * @author DarkBlade12
         * @since 1.4
         */
        private static final class PacketInstantiationException extends RuntimeException {

            private static final long serialVersionUID = 3203085387160737484L;

            /**
             * Construct a new packet instantiation exception
             *
             * @param message Message that will be logged
             * @param cause   Cause of the exception
             */
            public PacketInstantiationException(String message, Throwable cause) {
                super(message, cause);
            }
        }

        /**
         * Represents a runtime exception that is thrown if packet sending fails
         * <p/>
         * This class is part of the <b>ParticleEffect Library</b> and follows the same usage
         * conditions
         *
         * @author DarkBlade12
         * @since 1.4
         */
        private static final class PacketSendingException extends RuntimeException {

            private static final long serialVersionUID = 3203085387160737484L;

            /**
             * Construct a new packet sending exception
             *
             * @param message Message that will be logged
             * @param cause   Cause of the exception
             */
            public PacketSendingException(String message, Throwable cause) {
                super(message, cause);
            }
        }
    }
}

/**
 * <b>ReflectionUtils</b>
 * <p/>
 * This class provides useful methods which makes dealing with reflection much easier, especially
 * when working with Bukkit
 * <p/>
 * You are welcome to use it, modify it and redistribute it under the following conditions:
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 * <p/>
 * <i>It would be nice if you provide credit to me if you use this class in a published project</i>
 *
 * @author DarkBlade12
 * @version 1.1
 */
final class ReflectionUtils {

    // Prevent accidental construction
    private ReflectionUtils() {
    }

    /**
     * Returns the constructor of a given class with the given parameter types
     *
     * @param clazz          Target class
     * @param parameterTypes Parameter types of the desired constructor
     * @return The constructor of the target class with the specified parameter types
     * @throws NoSuchMethodException If the desired constructor with the specified parameter types
     *                               cannot be found
     * @see DataType
     * @see DataType#getPrimitive(Class[])
     * @see DataType#compare(Class[], Class[])
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (!DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes)) {
                continue;
            }
            return constructor;
        }
        throw new NoSuchMethodException("There is no such constructor in this class with the specified parameter " + "types");
    }

    /**
     * Returns the constructor of a desired class with the given parameter types
     *
     * @param className      Name of the desired target class
     * @param packageType    Package where the desired target class is located
     * @param parameterTypes Parameter types of the desired constructor
     * @return The constructor of the desired target class with the specified parameter types
     * @throws NoSuchMethodException  If the desired constructor with the specified parameter types
     *                                cannot be found
     * @throws ClassNotFoundException ClassNotFoundException If the desired target class with the
     *                                specified name and package cannot be found
     * @see #getClass(String, PackageType)
     * @see #getConstructor(Class, Class...)
     */
    public static Constructor<?> getConstructor(String className, PackageType packageType, Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
        return getConstructor(packageType.getClass(className), parameterTypes);
    }

    /**
     * Returns an instance of a class with the given arguments
     *
     * @param clazz     Target class
     * @param arguments Arguments which are used to construct an object of the target class
     * @return The instance of the target class with the specified arguments
     * @throws InstantiationException    If you cannot create an instance of the target class due to
     *                                   certain circumstances
     * @throws IllegalAccessException    If the desired constructor cannot be accessed due to certain
     *                                   circumstances
     * @throws IllegalArgumentException  If the types of the arguments do not match the parameter
     *                                   types of the constructor (this should not occur since it searches for a
     *                                   constructor with the
     *                                   types of the arguments)
     * @throws InvocationTargetException If the desired constructor cannot be invoked
     * @throws NoSuchMethodException     If the desired constructor with the specified arguments cannot
     *                                   be found
     */
    public static Object instantiateObject(Class<?> clazz, Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getConstructor(clazz, DataType.getPrimitive(arguments)).newInstance(arguments);
    }

    /**
     * Returns an instance of a desired class with the given arguments
     *
     * @param className   Name of the desired target class
     * @param packageType Package where the desired target class is located
     * @param arguments   Arguments which are used to construct an object of the desired target class
     * @return The instance of the desired target class with the specified arguments
     * @throws InstantiationException    If you cannot create an instance of the desired target class
     *                                   due to certain circumstances
     * @throws IllegalAccessException    If the desired constructor cannot be accessed due to certain
     *                                   circumstances
     * @throws IllegalArgumentException  If the types of the arguments do not match the parameter
     *                                   types of the constructor (this should not occur since it searches for a
     *                                   constructor with the
     *                                   types of the arguments)
     * @throws InvocationTargetException If the desired constructor cannot be invoked
     * @throws NoSuchMethodException     If the desired constructor with the specified arguments cannot
     *                                   be found
     * @throws ClassNotFoundException    If the desired target class with the specified name and
     *                                   package cannot be found
     * @see #getClass(String, PackageType)
     * @see #instantiateObject(Class, Object...)
     */
    public static Object instantiateObject(String className, PackageType packageType, Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        return instantiateObject(packageType.getClass(className), arguments);
    }

    /**
     * Returns a method of a class with the given parameter types
     *
     * @param clazz          Target class
     * @param methodName     Name of the desired method
     * @param parameterTypes Parameter types of the desired method
     * @return The method of the target class with the specified name and parameter types
     * @throws NoSuchMethodException If the desired method of the target class with the specified
     *                               name and parameter types cannot be found
     * @see DataType#getPrimitive(Class[])
     * @see DataType#compare(Class[], Class[])
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        for (Method method : clazz.getMethods()) {
            if (!method.getName().equals(methodName) || !DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveTypes)) {
                continue;
            }
            return method;
        }
        throw new NoSuchMethodException("There is no such method in this class with the specified name and parameter " + "types");
    }

    /**
     * Returns a method of a desired class with the given parameter types
     *
     * @param className      Name of the desired target class
     * @param packageType    Package where the desired target class is located
     * @param methodName     Name of the desired method
     * @param parameterTypes Parameter types of the desired method
     * @return The method of the desired target class with the specified name and parameter types
     * @throws NoSuchMethodException  If the desired method of the desired target class with the
     *                                specified name and parameter types cannot be found
     * @throws ClassNotFoundException If the desired target class with the specified name and
     *                                package cannot be found
     * @see #getClass(String, PackageType)
     * @see #getMethod(Class, String, Class...)
     */
    public static Method getMethod(String className, PackageType packageType, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
        return getMethod(packageType.getClass(className), methodName, parameterTypes);
    }

    /**
     * Invokes a method on an object with the given arguments
     *
     * @param instance   Target object
     * @param methodName Name of the desired method
     * @param arguments  Arguments which are used to invoke the desired method
     * @return The result of invoking the desired method on the target object
     * @throws IllegalAccessException    If the desired method cannot be accessed due to certain
     *                                   circumstances
     * @throws IllegalArgumentException  If the types of the arguments do not match the parameter
     *                                   types of the method (this should not occur since it searches for a method
     *                                   with the types of
     *                                   the arguments)
     * @throws InvocationTargetException If the desired method cannot be invoked on the target
     *                                   object
     * @throws NoSuchMethodException     If the desired method of the class of the target object with
     *                                   the specified name and arguments cannot be found
     * @see #getMethod(Class, String, Class...)
     * @see DataType#getPrimitive(Object[])
     */
    public static Object invokeMethod(Object instance, String methodName, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getMethod(instance.getClass(), methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
    }

    /**
     * Invokes a method of the target class on an object with the given arguments
     *
     * @param instance   Target object
     * @param clazz      Target class
     * @param methodName Name of the desired method
     * @param arguments  Arguments which are used to invoke the desired method
     * @return The result of invoking the desired method on the target object
     * @throws IllegalAccessException    If the desired method cannot be accessed due to certain
     *                                   circumstances
     * @throws IllegalArgumentException  If the types of the arguments do not match the parameter
     *                                   types of the method (this should not occur since it searches for a method
     *                                   with the types of
     *                                   the arguments)
     * @throws InvocationTargetException If the desired method cannot be invoked on the target
     *                                   object
     * @throws NoSuchMethodException     If the desired method of the target class with the specified
     *                                   name and arguments cannot be found
     * @see #getMethod(Class, String, Class...)
     * @see DataType#getPrimitive(Object[])
     */
    public static Object invokeMethod(Object instance, Class<?> clazz, String methodName, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getMethod(clazz, methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
    }

    /**
     * Invokes a method of a desired class on an object with the given arguments
     *
     * @param instance    Target object
     * @param className   Name of the desired target class
     * @param packageType Package where the desired target class is located
     * @param methodName  Name of the desired method
     * @param arguments   Arguments which are used to invoke the desired method
     * @return The result of invoking the desired method on the target object
     * @throws IllegalAccessException    If the desired method cannot be accessed due to certain
     *                                   circumstances
     * @throws IllegalArgumentException  If the types of the arguments do not match the parameter
     *                                   types of the method (this should not occur since it searches for a method
     *                                   with the types of
     *                                   the arguments)
     * @throws InvocationTargetException If the desired method cannot be invoked on the target
     *                                   object
     * @throws NoSuchMethodException     If the desired method of the desired target class with the
     *                                   specified name and arguments cannot be found
     * @throws ClassNotFoundException    If the desired target class with the specified name and
     *                                   package cannot be found
     * @see #getClass(String, PackageType)
     * @see #invokeMethod(Object, Class, String, Object...)
     */
    public static Object invokeMethod(Object instance, String className, PackageType packageType, String methodName, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        return invokeMethod(instance, packageType.getClass(className), methodName, arguments);
    }

    /**
     * Returns a field of the target class with the given name
     *
     * @param clazz     Target class
     * @param declared  Whether the desired field is declared or not
     * @param fieldName Name of the desired field
     * @return The field of the target class with the specified name
     * @throws NoSuchFieldException If the desired field of the given class cannot be found
     * @throws SecurityException    If the desired field cannot be made accessible
     */
    public static Field getField(Class<?> clazz, boolean declared, String fieldName) throws NoSuchFieldException, SecurityException {
        Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
        field.setAccessible(true);
        return field;
    }

    /**
     * Returns a field of a desired class with the given name
     *
     * @param className   Name of the desired target class
     * @param packageType Package where the desired target class is located
     * @param declared    Whether the desired field is declared or not
     * @param fieldName   Name of the desired field
     * @return The field of the desired target class with the specified name
     * @throws NoSuchFieldException   If the desired field of the desired class cannot be found
     * @throws SecurityException      If the desired field cannot be made accessible
     * @throws ClassNotFoundException If the desired target class with the specified name and
     *                                package cannot be found
     * @see #getField(Class, boolean, String)
     */
    public static Field getField(String className, PackageType packageType, boolean declared, String fieldName) throws NoSuchFieldException, SecurityException, ClassNotFoundException {
        return getField(packageType.getClass(className), declared, fieldName);
    }

    /**
     * Returns the value of a field of the given class of an object
     *
     * @param instance  Target object
     * @param clazz     Target class
     * @param declared  Whether the desired field is declared or not
     * @param fieldName Name of the desired field
     * @return The value of field of the target object
     * @throws IllegalArgumentException If the target object does not feature the desired field
     * @throws IllegalAccessException   If the desired field cannot be accessed
     * @throws NoSuchFieldException     If the desired field of the target class cannot be found
     * @throws SecurityException        If the desired field cannot be made accessible
     * @see #getField(Class, boolean, String)
     */
    public static Object getValue(Object instance, Class<?> clazz, boolean declared, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        return getField(clazz, declared, fieldName).get(instance);
    }

    /**
     * Returns the value of a field of a desired class of an object
     *
     * @param instance    Target object
     * @param className   Name of the desired target class
     * @param packageType Package where the desired target class is located
     * @param declared    Whether the desired field is declared or not
     * @param fieldName   Name of the desired field
     * @return The value of field of the target object
     * @throws IllegalArgumentException If the target object does not feature the desired field
     * @throws IllegalAccessException   If the desired field cannot be accessed
     * @throws NoSuchFieldException     If the desired field of the desired class cannot be found
     * @throws SecurityException        If the desired field cannot be made accessible
     * @throws ClassNotFoundException   If the desired target class with the specified name and
     *                                  package cannot be found
     * @see #getValue(Object, Class, boolean, String)
     */
    public static Object getValue(Object instance, String className, PackageType packageType, boolean declared, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
        return getValue(instance, packageType.getClass(className), declared, fieldName);
    }

    /**
     * Returns the value of a field with the given name of an object
     *
     * @param instance  Target object
     * @param declared  Whether the desired field is declared or not
     * @param fieldName Name of the desired field
     * @return The value of field of the target object
     * @throws IllegalArgumentException If the target object does not feature the desired field
     *                                  (should not occur since it searches for a field with the given name in the
     *                                  class of the
     *                                  object)
     * @throws IllegalAccessException   If the desired field cannot be accessed
     * @throws NoSuchFieldException     If the desired field of the target object cannot be found
     * @throws SecurityException        If the desired field cannot be made accessible
     * @see #getValue(Object, Class, boolean, String)
     */
    public static Object getValue(Object instance, boolean declared, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        return getValue(instance, instance.getClass(), declared, fieldName);
    }

    /**
     * Sets the value of a field of the given class of an object
     *
     * @param instance  Target object
     * @param clazz     Target class
     * @param declared  Whether the desired field is declared or not
     * @param fieldName Name of the desired field
     * @param value     New value
     * @throws IllegalArgumentException If the type of the value does not match the type of the
     *                                  desired field
     * @throws IllegalAccessException   If the desired field cannot be accessed
     * @throws NoSuchFieldException     If the desired field of the target class cannot be found
     * @throws SecurityException        If the desired field cannot be made accessible
     * @see #getField(Class, boolean, String)
     */
    public static void setValue(Object instance, Class<?> clazz, boolean declared, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        getField(clazz, declared, fieldName).set(instance, value);
    }

    /**
     * Sets the value of a field of a desired class of an object
     *
     * @param instance    Target object
     * @param className   Name of the desired target class
     * @param packageType Package where the desired target class is located
     * @param declared    Whether the desired field is declared or not
     * @param fieldName   Name of the desired field
     * @param value       New value
     * @throws IllegalArgumentException If the type of the value does not match the type of the
     *                                  desired field
     * @throws IllegalAccessException   If the desired field cannot be accessed
     * @throws NoSuchFieldException     If the desired field of the desired class cannot be found
     * @throws SecurityException        If the desired field cannot be made accessible
     * @throws ClassNotFoundException   If the desired target class with the specified name and
     *                                  package cannot be found
     * @see #setValue(Object, Class, boolean, String, Object)
     */
    public static void setValue(Object instance, String className, PackageType packageType, boolean declared, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
        setValue(instance, packageType.getClass(className), declared, fieldName, value);
    }

    /**
     * Sets the value of a field with the given name of an object
     *
     * @param instance  Target object
     * @param declared  Whether the desired field is declared or not
     * @param fieldName Name of the desired field
     * @param value     New value
     * @throws IllegalArgumentException If the type of the value does not match the type of the
     *                                  desired field
     * @throws IllegalAccessException   If the desired field cannot be accessed
     * @throws NoSuchFieldException     If the desired field of the target object cannot be found
     * @throws SecurityException        If the desired field cannot be made accessible
     * @see #setValue(Object, Class, boolean, String, Object)
     */
    public static void setValue(Object instance, boolean declared, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        setValue(instance, instance.getClass(), declared, fieldName, value);
    }

    /**
     * Represents an enumeration of dynamic packages of NMS and CraftBukkit
     * <p/>
     * This class is part of the <b>ReflectionUtils</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.0
     */
    public enum PackageType {

        MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()),
        CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion()),
        CRAFTBUKKIT_BLOCK(CRAFTBUKKIT, "block"),
        CRAFTBUKKIT_CHUNKIO(CRAFTBUKKIT, "chunkio"),
        CRAFTBUKKIT_COMMAND(CRAFTBUKKIT, "command"),
        CRAFTBUKKIT_CONVERSATIONS(CRAFTBUKKIT, "conversations"),
        CRAFTBUKKIT_ENCHANTMENS(CRAFTBUKKIT, "enchantments"),
        CRAFTBUKKIT_ENTITY(CRAFTBUKKIT, "entity"),
        CRAFTBUKKIT_EVENT(CRAFTBUKKIT, "event"),
        CRAFTBUKKIT_GENERATOR(CRAFTBUKKIT, "generator"),
        CRAFTBUKKIT_HELP(CRAFTBUKKIT, "help"),
        CRAFTBUKKIT_INVENTORY(CRAFTBUKKIT, "inventory"),
        CRAFTBUKKIT_MAP(CRAFTBUKKIT, "map"),
        CRAFTBUKKIT_METADATA(CRAFTBUKKIT, "metadata"),
        CRAFTBUKKIT_POTION(CRAFTBUKKIT, "potion"),
        CRAFTBUKKIT_PROJECTILES(CRAFTBUKKIT, "projectiles"),
        CRAFTBUKKIT_SCHEDULER(CRAFTBUKKIT, "scheduler"),
        CRAFTBUKKIT_SCOREBOARD(CRAFTBUKKIT, "scoreboard"),
        CRAFTBUKKIT_UPDATER(CRAFTBUKKIT, "updater"),
        CRAFTBUKKIT_UTIL(CRAFTBUKKIT, "util");

        private final String path;

        /**
         * Construct a new package type
         *
         * @param path Path of the package
         */
        private PackageType(String path) {
            this.path = path;
        }

        /**
         * Construct a new package type
         *
         * @param parent Parent package of the package
         * @param path   Path of the package
         */
        private PackageType(PackageType parent, String path) {
            this(parent + "." + path);
        }

        /**
         * Returns the version of your server
         *
         * @return The server version
         */
        public static String getServerVersion() {
            return Bukkit.getServer().getClass().getPackage().getName().substring(23);
        }

        /**
         * Returns the path of this package type
         *
         * @return The path
         */
        public String getPath() {
            return path;
        }

        /**
         * Returns the class with the given name
         *
         * @param className Name of the desired class
         * @return The class with the specified name
         * @throws ClassNotFoundException If the desired class with the specified name and package
         *                                cannot be found
         */
        public Class<?> getClass(String className) throws ClassNotFoundException {
            return Class.forName(this + "." + className);
        }

        // Override for convenience
        @Override
        public String toString() {
            return path;
        }
    }

    /**
     * Represents an enumeration of Java data types with corresponding classes
     * <p/>
     * This class is part of the <b>ReflectionUtils</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.0
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

        private static final Map<Class<?>, DataType> CLASS_MAP = new HashMap<>();

        // Initialize map for quick class lookup
        static {
            for (DataType type : values()) {
                CLASS_MAP.put(type.primitive, type);
                CLASS_MAP.put(type.reference, type);
            }
        }

        private final Class<?> primitive;
        private final Class<?> reference;

        /**
         * Construct a new data type
         *
         * @param primitive Primitive class of this data type
         * @param reference Reference class of this data type
         */
        private DataType(Class<?> primitive, Class<?> reference) {
            this.primitive = primitive;
            this.reference = reference;
        }

        /**
         * Returns the data type with the given primitive/reference class
         *
         * @param clazz Primitive/Reference class of the data type
         * @return The data type
         */
        public static DataType fromClass(Class<?> clazz) {
            return CLASS_MAP.get(clazz);
        }

        /**
         * Returns the primitive class of the data type with the given reference class
         *
         * @param clazz Reference class of the data type
         * @return The primitive class
         */
        public static Class<?> getPrimitive(Class<?> clazz) {
            DataType type = fromClass(clazz);
            return type == null ? clazz : type.getPrimitive();
        }

        /**
         * Returns the reference class of the data type with the given primitive class
         *
         * @param clazz Primitive class of the data type
         * @return The reference class
         */
        public static Class<?> getReference(Class<?> clazz) {
            DataType type = fromClass(clazz);
            return type == null ? clazz : type.getReference();
        }

        /**
         * Returns the primitive class array of the given class array
         *
         * @param classes Given class array
         * @return The primitive class array
         */
        public static Class<?>[] getPrimitive(Class<?>[] classes) {
            int length = classes == null ? 0 : classes.length;
            Class<?>[] types = new Class<?>[length];
            for (int index = 0;
                 index < length;
                 index++) {
                types[index] = getPrimitive(classes[index]);
            }
            return types;
        }

        /**
         * Returns the reference class array of the given class array
         *
         * @param classes Given class array
         * @return The reference class array
         */
        public static Class<?>[] getReference(Class<?>[] classes) {
            int length = classes == null ? 0 : classes.length;
            Class<?>[] types = new Class<?>[length];
            for (int index = 0;
                 index < length;
                 index++) {
                types[index] = getReference(classes[index]);
            }
            return types;
        }

        /**
         * Returns the primitive class array of the given object array
         *
         * @param object Given object array
         * @return The primitive class array
         */
        public static Class<?>[] getPrimitive(Object[] objects) {
            int length = objects == null ? 0 : objects.length;
            Class<?>[] types = new Class<?>[length];
            for (int index = 0;
                 index < length;
                 index++) {
                types[index] = getPrimitive(objects[index].getClass());
            }
            return types;
        }

        /**
         * Returns the reference class array of the given object array
         *
         * @param object Given object array
         * @return The reference class array
         */
        public static Class<?>[] getReference(Object[] objects) {
            int length = objects == null ? 0 : objects.length;
            Class<?>[] types = new Class<?>[length];
            for (int index = 0;
                 index < length;
                 index++) {
                types[index] = getReference(objects[index].getClass());
            }
            return types;
        }

        /**
         * Compares two class arrays on equivalence
         *
         * @param primary   Primary class array
         * @param secondary Class array which is compared to the primary array
         * @return Whether these arrays are equal or not
         */
        public static boolean compare(Class<?>[] primary, Class<?>[] secondary) {
            if (primary == null || secondary == null || primary.length != secondary.length) {
                return false;
            }
            for (int index = 0;
                 index < primary.length;
                 index++) {
                Class<?> primaryClass = primary[index];
                Class<?> secondaryClass = secondary[index];
                if (primaryClass.equals(secondaryClass) || primaryClass.isAssignableFrom(secondaryClass)) {
                    continue;
                }
                return false;
            }
            return true;
        }

        /**
         * Returns the primitive class of this data type
         *
         * @return The primitive class
         */
        public Class<?> getPrimitive() {
            return primitive;
        }

        /**
         * Returns the reference class of this data type
         *
         * @return The reference class
         */
        public Class<?> getReference() {
            return reference;
        }
    }
}
