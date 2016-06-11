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

import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.databases.EffectDatabase;
import bammerbom.ultimatecore.bukkit.resources.utils.ItemUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.ReflectionUtil;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;

import java.util.Locale;
import java.util.Random;

public class MobData {

    static Random random = new Random();

    //Methods
    static boolean isHorseColor(String str) {
        for (Horse.Color color : Horse.Color.values()) {
            String colors = color.name().toLowerCase().replaceAll("_", "");
            String inputs = str.toLowerCase().replaceAll("_", "");
            if (colors.equals(inputs)) {
                return true;
            }
        }
        return false;
    }

    static Horse.Color getHorseColor(String str) {
        for (Horse.Color color : Horse.Color.values()) {
            String colors = color.name().toLowerCase().replaceAll("_", "");
            String inputs = str.toLowerCase().replaceAll("_", "");
            if (colors.equals(inputs)) {
                return color;
            }
        }
        return null;
    }

    static boolean isHorseStyle(String str) {
        if (str.equalsIgnoreCase("whitelegs")) {
            return true;
        }
        for (Horse.Style style : Horse.Style.values()) {
            String styles = style.name().toLowerCase().replaceAll("white_", "").replaceAll("_", "");
            String inputs = str.toLowerCase().replaceAll("white_", "").replaceAll("_", "").replaceAll("whitelegs", "white");
            if (styles.equals(inputs)) {
                return true;
            }
        }
        return false;
    }

    static Horse.Style getHorseStyle(String str) {
        if (str.equalsIgnoreCase("whitelegs")) {
            return Horse.Style.WHITE;
        }
        for (Horse.Style style : Horse.Style.values()) {
            String styles = style.name().toLowerCase().replaceAll("white_", "").replaceAll("_", "");
            String inputs = str.toLowerCase().replaceAll("white_", "").replaceAll("_", "").replaceAll("whitelegs", "white");
            if (styles.equals(inputs)) {
                return style;
            }
        }
        return null;
    }

    static void setHorseSpeed(Horse h, double speed) {
        try {
            ReflectionUtil.ReflectionObject attributes = ReflectionUtil.execute("getHandle().getAttributeInstance({1})", h, ReflectionUtil
                    .executeStatic(ReflectionUtil.NMS_PATH.equalsIgnoreCase("net.minecraft.server.1_8_R1") || ReflectionUtil.NMS_PATH
                            .equalsIgnoreCase("net.minecraft.server.1_8_R2") ? "d" : "MOVEMENT_SPEED", ReflectionUtil.ReflectionStatic.fromNMS("GenericAttributes")).fetch());
            attributes.set("f", speed);
            attributes.invoke("f");
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to apply horse speed data.");
        }
    }

    public static void setDatas(Entity en, String[] data, Player p) {
        setDatas(en, r.getFinalArg(data, 1), p);
    }

    public static void setDatas(Entity en, String data, Player p) {
        if (!data.contains(" ")) {
            setData(en, data, p);
        }
        for (String dat : data.split(" ")) {
            setData(en, dat, p);
        }
    }

    public static boolean setData(Entity en, String data, Player p) {
        if (data.equalsIgnoreCase("baby") || data.equalsIgnoreCase("small")) {
            if (en instanceof Ageable) {
                ((Ageable) en).setBaby();
                return true;
            } else if (en instanceof Zombie) {
                ((Zombie) en).setBaby(true);
                return true;
            } else if (en instanceof ArmorStand) {
                ((ArmorStand) en).setSmall(true);
                return true;
            }
        } else if (data.startsWith("age:")) {
            if (en instanceof Ageable) {
                if (r.isInt(data.split(":")[1])) {
                    ((Ageable) en).setAge(Integer.parseInt(data.split(":")[1]));
                    return true;
                }
            }
        } else if (data.equalsIgnoreCase("agelock")) {
            if (en instanceof Ageable) {
                ((Ageable) en).setAgeLock(true);
                return true;
            }
        } else if (data.equalsIgnoreCase("tame") || data.equalsIgnoreCase("tameable")) {
            if (en instanceof Tameable) {
                ((Tameable) en).setTamed(true);
                if (p != null) {
                    ((Tameable) en).setOwner(p);
                }
                return true;
            }
        } else if (data.equalsIgnoreCase("random")) {
            if (en instanceof Colorable) {
                ((Colorable) en).setColor(DyeColor.values()[random.nextInt(DyeColor.values().length)]);
                return true;
            }
        } else if (data.startsWith("color:")) {
            if (en instanceof Colorable) {
                String color = data.toUpperCase(Locale.ENGLISH);
                ((Colorable) en).setColor(DyeColor.valueOf(color.split(":")[1]));
                return true;
            }
        } else if (data.equalsIgnoreCase("zombie") || data.equalsIgnoreCase("zombievillager")) {
            if (en instanceof Zombie) {
                ((Zombie) en).setVillager(true);
                return true;
            }
        } else if (data.equalsIgnoreCase("husk") || data.equalsIgnoreCase("huskzombie")) {
            if (en instanceof Zombie) {
                ((Zombie) en).setVillager(true);
                ((Zombie) en).setVillagerProfession(Villager.Profession.HUSK);
                return true;
            }
        } else if (data.equalsIgnoreCase("wither") || data.equalsIgnoreCase("witherskeleton")) {
            if (en instanceof Skeleton) {
                ((Skeleton) en).setSkeletonType(Skeleton.SkeletonType.WITHER);
                return true;
            }
        } else if (data.equalsIgnoreCase("stray")) {
            if (en instanceof Skeleton) {
                ((Skeleton) en).setSkeletonType(Skeleton.SkeletonType.STRAY);
                return true;
            }
        } else if (data.equalsIgnoreCase("powered") || data.equalsIgnoreCase("electrified") || data.equalsIgnoreCase("charged")) {
            if (en instanceof Creeper) {
                ((Creeper) en).setPowered(true);
                return true;
            }
        } else if (data.equalsIgnoreCase("saddle") || data.equalsIgnoreCase("saddled")) {
            if (en instanceof Pig) {
                ((Pig) en).setSaddle(true);
                return true;
            } else if (en instanceof Horse) {
                ((Horse) en).setTamed(true);
                ((Horse) en).getInventory().setSaddle(new ItemStack(Material.SADDLE));
                return true;
            }
        } else if (data.equalsIgnoreCase("angry") || data.equalsIgnoreCase("rabid")) {
            if (en instanceof Wolf) {
                ((Wolf) en).setAngry(true);
                return true;
            }
        } else if (data.startsWith("size:")) {
            if (r.isInt(data.split(":")[1])) {
                if (en instanceof Slime) {
                    ((Slime) en).setSize(Integer.parseInt(data.split(":")[1]));
                    return true;
                } else if (en instanceof MagmaCube) {
                    ((MagmaCube) en).setSize(Integer.parseInt(data.split(":")[1]));
                    return true;
                }
            }
        } else if (data.equalsIgnoreCase("elder") || data.equalsIgnoreCase("elderguardian")) {
            if (en instanceof Guardian) {
                ((Guardian) en).setElder(true);
                return true;
            }
        } else if (data.startsWith("exp:") || data.startsWith("xp:") || data.startsWith("experience:") || data.startsWith("amount:")) {
            if (r.isInt(data.split(":")[1]) && en instanceof ExperienceOrb) {
                Integer amount = r.normalize(Integer.parseInt(data.split(":")[1]), 1, 2000000000);
                ((ExperienceOrb) en).setExperience(amount);
                return true;
            }
        } else if (data.startsWith("maxhealth:")) {
            if (r.isDouble(data.split(":")[1]) && en instanceof LivingEntity) {
                Double amount = r.normalize(Double.parseDouble(data.split(":")[1]), 1.0, 999999.0);
                ((LivingEntity) en).setMaxHealth(amount);
                return true;
            }
        } else if (data.startsWith("health:")) {
            if (r.isDouble(data.split(":")[1]) && en instanceof LivingEntity) {
                Double amount = r.normalize(Double.parseDouble(data.split(":")[1]), 1.0, ((LivingEntity) en).getMaxHealth());
                ((LivingEntity) en).setHealth(amount);
                return true;
            }
        } else if (data.contains(":") && EffectDatabase.getByName(data.split(":")[0]) != null) {
            if (en instanceof LivingEntity) {
                if (r.isInt(data.split(":")[1])) {
                    Integer i = Integer.parseInt(data.split(":")[1]);
                    ((LivingEntity) en).addPotionEffect(EffectDatabase.getByName(data.split(":")[0]).createEffect(999999, i));
                }
            }
        } else if (data.startsWith("name:")) {
            en.setCustomNameVisible(true);
            en.setCustomName(ChatColor.translateAlternateColorCodes('&', data.split(":")[1]).replace("_", " "));
            return true;
        } else if (data.equalsIgnoreCase("noai")) {
            if (en instanceof LivingEntity) {
                try {
                    ((LivingEntity) en).setAI(false);
                    return true;
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "Failed to apply mob data.");
                }
            }
        } else if (data.equalsIgnoreCase("invisible")) {
            if (en instanceof ArmorStand) {
                ((ArmorStand) en).setVisible(false);
            }
        } else if (ItemUtil.searchItem(data) != null) {
            if (en instanceof LivingEntity) {
                ((LivingEntity) en).getEquipment().setItemInHand(ItemUtil.searchItem(data));
                return true;
            }
        }
        if (en instanceof Horse) {
            Horse horse = (Horse) en;
            if (data.equalsIgnoreCase("donkey")) {
                horse.setVariant(Horse.Variant.DONKEY);
                return true;
            } else if (data.equalsIgnoreCase("mule")) {
                horse.setVariant(Horse.Variant.MULE);
                return true;
            } else if (data.equalsIgnoreCase("skeleton")) {
                horse.setVariant(Horse.Variant.SKELETON_HORSE);
                return true;
            } else if (data.equalsIgnoreCase("undead") || data.equalsIgnoreCase("zombie")) {
                horse.setVariant(Horse.Variant.UNDEAD_HORSE);
                return true;
            } else if (isHorseColor(data)) {
                horse.setColor(getHorseColor(data));
                return true;
            } else if (isHorseStyle(data)) {
                horse.setStyle(getHorseStyle(data));
                return true;
            } else if (data.equalsIgnoreCase("diamond") || data.equalsIgnoreCase("diamondarmor")) {
                horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
                return true;
            } else if (data.equalsIgnoreCase("iron") || data.equalsIgnoreCase("ironarmor")) {
                horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
                return true;
            } else if (data.equalsIgnoreCase("gold") || data.equalsIgnoreCase("goldarmor")) {
                horse.getInventory().setArmor(new ItemStack(Material.GOLD_BARDING));
                return true;
            } else if (data.equalsIgnoreCase("tame") || data.equalsIgnoreCase("tamed")) {
                horse.setTamed(true);
                return true;
            } else if (data.startsWith("speed:")) {
                try {
                    setHorseSpeed(horse, Double.parseDouble(data.split(":")[1]));
                    return true;
                } catch (NumberFormatException ex) {
                }
            } else if (data.startsWith("jump:") || data.startsWith("jumpstrength:")) {
                try {
                    horse.setJumpStrength(Double.parseDouble(data.split(":")[1]));
                    return true;
                } catch (NumberFormatException ex) {
                }
            }
        }
        if (en instanceof Ocelot) {
            Ocelot ocelot = (Ocelot) en;
            if (data.equalsIgnoreCase("black") || data.equalsIgnoreCase("blackcat")) {
                ocelot.setCatType(Ocelot.Type.BLACK_CAT);
                return true;
            } else if (data.equalsIgnoreCase("red") || data.equalsIgnoreCase("redcat")) {
                ocelot.setCatType(Ocelot.Type.RED_CAT);
                return true;
            } else if (data.equalsIgnoreCase("siamese") || data.equalsIgnoreCase("siamesecat")) {
                ocelot.setCatType(Ocelot.Type.SIAMESE_CAT);
                return true;
            } else if (data.equalsIgnoreCase("wild") || data.equalsIgnoreCase("wildcat") || data.equalsIgnoreCase("wildocelot")) {
                ocelot.setCatType(Ocelot.Type.WILD_OCELOT);
                return true;
            }
        }
        if (en instanceof Villager) {
            if (data.equalsIgnoreCase("farmer")) {
                ((Villager) en).setProfession(Villager.Profession.FARMER);
                return true;
            } else if (data.equalsIgnoreCase("librarian")) {
                ((Villager) en).setProfession(Villager.Profession.LIBRARIAN);
                return true;
            } else if (data.equalsIgnoreCase("priest")) {
                ((Villager) en).setProfession(Villager.Profession.PRIEST);
                return true;
            } else if (data.equalsIgnoreCase("smith") || data.equalsIgnoreCase("blacksmith")) {
                ((Villager) en).setProfession(Villager.Profession.BLACKSMITH);
                return true;
            } else if (data.equalsIgnoreCase("butcher")) {
                ((Villager) en).setProfession(Villager.Profession.BUTCHER);
                return true;
            }
        }
        return false;
    }

    public static void setDefault(Entity spawned) {
        if (spawned instanceof Ageable) {
            ((Ageable) spawned).setAdult();
        }
        if (spawned instanceof Zombie) {
            ((Zombie) spawned).setBaby(false);
        }
        if (spawned.getType() == EntityType.SKELETON) {
            if (!((Skeleton) spawned).getSkeletonType().equals(Skeleton.SkeletonType.WITHER)) {
                Skeleton skel = (Skeleton) spawned;
                skel.setSkeletonType(Skeleton.SkeletonType.NORMAL);
                skel.getEquipment().setItemInHand(new ItemStack(Material.BOW));
                skel.getEquipment().setItemInHandDropChance(0.09F);
                return;
            }

        }
        if (spawned.getType() == EntityType.PIG_ZOMBIE) {
            EntityEquipment invent = ((LivingEntity) spawned).getEquipment();
            invent.setItemInHand(new ItemStack(Material.GOLD_SWORD, 1));
            invent.setItemInHandDropChance(0.05F);
        }
    }
}
