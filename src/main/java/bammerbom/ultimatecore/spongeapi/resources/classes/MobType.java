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
package bammerbom.ultimatecore.spongeapi.resources.classes;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.SkeletonTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.monster.Guardian;
import org.spongepowered.api.entity.living.monster.Skeleton;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

public enum MobType {

    ARMORSTAND("ArmorStand", Enemies.NEUTRAL, EntityTypes.ARMOR_STAND),
    CHICKEN("Chicken", Enemies.FRIENDLY, EntityTypes.CHICKEN),
    COW("Cow", Enemies.FRIENDLY, EntityTypes.COW),
    CREEPER("Creeper", Enemies.ENEMY, EntityTypes.CREEPER),
    GHAST("Ghast", Enemies.ENEMY, EntityTypes.GHAST),
    GIANT("Giant", Enemies.ENEMY, EntityTypes.GIANT),
    HORSE("Horse", Enemies.FRIENDLY, EntityTypes.HORSE),
    PIG("Pig", Enemies.FRIENDLY, EntityTypes.PIG),
    PIGZOMBIE("PigZombie", Enemies.NEUTRAL, EntityTypes.PIG_ZOMBIE),
    SHEEP("Sheep", Enemies.FRIENDLY, "", EntityTypes.SHEEP),
    SKELETON("Skeleton", Enemies.ENEMY, EntityTypes.SKELETON),
    SLIME("Slime", Enemies.ENEMY, EntityTypes.SLIME),
    SPIDER("Spider", Enemies.ENEMY, EntityTypes.SPIDER),
    SQUID("Squid", Enemies.FRIENDLY, EntityTypes.SQUID),
    ZOMBIE("Zombie", Enemies.ENEMY, EntityTypes.ZOMBIE),
    WOLF("Wolf", Enemies.NEUTRAL, "", EntityTypes.WOLF),
    WITHERSKELETON("WitherSkeleton", Enemies.ENEMY, EntityTypes.SKELETON),
    CAVESPIDER("CaveSpider", Enemies.ENEMY, EntityTypes.CAVE_SPIDER),
    ENDERMAN("Enderman", Enemies.ENEMY, "", EntityTypes.ENDERMAN),
    SILVERFISH("Silverfish", Enemies.ENEMY, "", EntityTypes.SILVERFISH),
    ENDERDRAGON("EnderDragon", Enemies.ENEMY, EntityTypes.ENDER_DRAGON),
    VILLAGER("Villager", Enemies.FRIENDLY, EntityTypes.VILLAGER),
    BLAZE("Blaze", Enemies.ENEMY, EntityTypes.BLAZE),
    MUSHROOMCOW("MushroomCow", Enemies.FRIENDLY, EntityTypes.MUSHROOM_COW),
    MAGMACUBE("MagmaCube", Enemies.ENEMY, EntityTypes.MAGMA_CUBE),
    SNOWMAN("Snowman", Enemies.FRIENDLY, "", EntityTypes.SNOWMAN),
    OCELOT("Ocelot", Enemies.NEUTRAL, EntityTypes.OCELOT),
    IRONGOLEM("IronGolem", Enemies.NEUTRAL, EntityTypes.IRON_GOLEM),
    WITHER("Wither", Enemies.ENEMY, EntityTypes.WITHER),
    BAT("Bat", Enemies.FRIENDLY, EntityTypes.BAT),
    WITCH("Witch", Enemies.ENEMY, EntityTypes.WITCH),
    BOAT("Boat", Enemies.NEUTRAL, EntityTypes.BOAT),
    MINECART("Minecart", Enemies.NEUTRAL, EntityTypes.RIDEABLE_MINECART),
    MINECART_CHEST("ChestMinecart", Enemies.NEUTRAL, EntityTypes.CHESTED_MINECART),
    MINECART_FURNACE("FurnaceMinecart", Enemies.NEUTRAL, EntityTypes.FURNACE_MINECART),
    MINECART_TNT("TNTMinecart", Enemies.NEUTRAL, EntityTypes.TNT_MINECART),
    MINECART_HOPPER("HopperMinecart", Enemies.NEUTRAL, EntityTypes.HOPPER_MINECART),
    MINECART_MOB_SPAWNER("SpawnerMinecart", Enemies.NEUTRAL, EntityTypes.MOB_SPAWNER_MINECART),
    ENDERCRYSTAL("EnderCrystal", Enemies.NEUTRAL, EntityTypes.ENDER_CRYSTAL),
    EXPERIENCEORB("ExperienceOrb", Enemies.NEUTRAL, EntityTypes.EXPERIENCE_ORB),
    ENDERMITE("Endermite", Enemies.ENEMY, EntityTypes.ENDERMITE),
    GUARDIAN("Guardian", Enemies.ENEMY, EntityTypes.GUARDIAN),
    ELDERGUARDIAN("ElderGuardian", Enemies.ENEMY, EntityTypes.GUARDIAN),
    RABBIT("Rabbit", Enemies.FRIENDLY, EntityTypes.RABBIT),
    SHULKER("Shulker", Enemies.ENEMY, EntityTypes.SHULKER),
    POLAR_BEAR("PolarBear", Enemies.NEUTRAL, EntityTypes.POLAR_BEAR);

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

    public Entity spawn(World world, Location loc) throws MobType.MobException {
        Entity entity = world.createEntity(this.bukkitType, loc.getPosition()).get();
        if (entity == null) {
            throw new MobException();
        }
        if (name.equalsIgnoreCase("WitherSkeleton")) {
            Skeleton sk = (Skeleton) entity;
            sk.offer(Keys.SKELETON_TYPE, SkeletonTypes.WITHER);
        }
        if (name.equalsIgnoreCase("ElderGuardian")) {
            Guardian g = (Guardian) entity;
            g.offer(Keys.ELDER_GUARDIAN, true);
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
