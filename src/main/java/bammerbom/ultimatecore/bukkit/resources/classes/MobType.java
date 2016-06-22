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
package bammerbom.ultimatecore.bukkit.resources.classes;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;

import java.util.*;

public enum MobType {

    ARMORSTAND("ArmorStand", Enemies.NEUTRAL, EntityType.ARMOR_STAND),
    CHICKEN("Chicken", Enemies.FRIENDLY, EntityType.CHICKEN),
    COW("Cow", Enemies.FRIENDLY, EntityType.COW),
    CREEPER("Creeper", Enemies.ENEMY, EntityType.CREEPER),
    GHAST("Ghast", Enemies.ENEMY, EntityType.GHAST),
    GIANT("Giant", Enemies.ENEMY, EntityType.GIANT),
    HORSE("Horse", Enemies.FRIENDLY, EntityType.HORSE),
    PIG("Pig", Enemies.FRIENDLY, EntityType.PIG),
    PIGZOMBIE("PigZombie", Enemies.NEUTRAL, EntityType.PIG_ZOMBIE),
    SHEEP("Sheep", Enemies.FRIENDLY, "", EntityType.SHEEP),
    SKELETON("Skeleton", Enemies.ENEMY, EntityType.SKELETON),
    SLIME("Slime", Enemies.ENEMY, EntityType.SLIME),
    SPIDER("Spider", Enemies.ENEMY, EntityType.SPIDER),
    SQUID("Squid", Enemies.FRIENDLY, EntityType.SQUID),
    ZOMBIE("Zombie", Enemies.ENEMY, EntityType.ZOMBIE),
    WOLF("Wolf", Enemies.NEUTRAL, "", EntityType.WOLF),
    WITHERSKELETON("WitherSkeleton", Enemies.ENEMY, EntityType.SKELETON),
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
    EXPERIENCEORB("ExperienceOrb", Enemies.NEUTRAL, EntityType.EXPERIENCE_ORB),
    ENDERMITE("Endermite", Enemies.ENEMY, EntityType.ENDERMITE),
    GUARDIAN("Guardian", Enemies.ENEMY, EntityType.GUARDIAN),
    ELDERGUARDIAN("ElderGuardian", Enemies.ENEMY, EntityType.GUARDIAN),
    RABBIT("Rabbit", Enemies.FRIENDLY, EntityType.RABBIT),
    SHULKER("Shulker", Enemies.ENEMY, EntityType.SHULKER),
    POLAR_BEAR("PolarBear", Enemies.NEUTRAL, EntityType.POLAR_BEAR);

    private static final Map<String, MobType> hashMap;
    private static final Map<EntityType, MobType> bukkitMap;

    static {
        hashMap = new HashMap<>();
        bukkitMap = new HashMap<>();

        for (MobType mob : values()) {
            hashMap.put(mob.name.toLowerCase(Locale.ENGLISH), mob);
            bukkitMap.put(mob.bukkitType, mob);
        }
        hashMap.put("zombiepigman", MobType.PIGZOMBIE);
        hashMap.put("zombiepig", MobType.PIGZOMBIE);
    }

    public final String name;
    public final Enemies type;
    public final String suffix;
    private final EntityType bukkitType;

    MobType(String n, Enemies en, String s, EntityType type) {
        this.suffix = s;
        this.name = n;
        this.type = en;
        this.bukkitType = type;
    }

    MobType(String n, Enemies en, EntityType type) {
        this.name = n;
        this.type = en;
        this.bukkitType = type;
        this.suffix = "s";
    }

    public static Set<String> getMobList() {
        return Collections.unmodifiableSet(hashMap.keySet());
    }

    public static MobType fromName(String name) {
        return hashMap.get(name.toLowerCase(Locale.ENGLISH));
    }

    public static MobType fromBukkitType(EntityType type) {
        if (!bukkitMap.containsKey(type)) {
            return null;
        }
        return bukkitMap.get(type);
    }

    public Entity spawn(World world, Server server, Location loc) throws MobType.MobException {
        Entity entity = world.spawn(loc, this.bukkitType.getEntityClass());
        if (entity == null) {
            throw new MobException();
        }
        if (name.equalsIgnoreCase("WitherSkeleton")) {
            Skeleton sk = (Skeleton) entity;
            sk.setSkeletonType(SkeletonType.WITHER);
        }
        if (name.equalsIgnoreCase("ElderGuardian")) {
            Guardian g = (Guardian) entity;
            g.setElder(true);
        }
        return entity;
    }

    public EntityType getType() {
        return this.bukkitType;
    }

    public enum Enemies {

        FRIENDLY("friendly"),
        NEUTRAL("neutral"),
        ENEMY("enemy");

        protected final String type;

        Enemies(String type) {
            this.type = type;
        }

    }

    public static class MobException extends Exception {

        private static final long serialVersionUID = 1L;
    }
}
