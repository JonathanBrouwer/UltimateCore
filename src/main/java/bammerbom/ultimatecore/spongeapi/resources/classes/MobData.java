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

import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.databases.EffectDatabase;
import bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.attribute.Attributes;
import org.spongepowered.api.data.manipulator.AttributeData;
import org.spongepowered.api.data.manipulator.ColoredData;
import org.spongepowered.api.data.manipulator.DisplayNameData;
import org.spongepowered.api.data.manipulator.PotionEffectData;
import org.spongepowered.api.data.manipulator.entity.*;
import org.spongepowered.api.data.type.*;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.animal.Horse;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Texts;

import java.util.Locale;
import java.util.Random;

public class MobData {

    static Random random = new Random();

    //Methods
    static boolean isHorseColor(String str) {
        for (HorseColor color : r.getRegistry().getAllOf(CatalogTypes.HORSE_COLOR)) {
            String colors = color.getId().toLowerCase().replaceAll("_", "");
            String inputs = str.toLowerCase().replaceAll("_", "");
            if (colors.equals(inputs)) {
                return true;
            }
        }
        return false;
    }

    static HorseColor getHorseColor(String str) {
        for (HorseColor color : r.getRegistry().getAllOf(CatalogTypes.HORSE_COLOR)) {
            String colors = color.getId().toLowerCase().replaceAll("_", "");
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
        for (HorseStyle style : r.getRegistry().getAllOf(CatalogTypes.HORSE_STYLE)) {
            String styles = style.getId().toLowerCase().replaceAll("white_", "").replaceAll("_", "");
            String inputs = str.toLowerCase().replaceAll("white_", "").replaceAll("_", "").replaceAll("whitelegs", "white");
            if (styles.equals(inputs)) {
                return true;
            }
        }
        return false;
    }

    static HorseStyle getHorseStyle(String str) {
        if (str.equalsIgnoreCase("whitelegs")) {
            return HorseStyles.WHITE;
        }
        for (HorseStyle style : r.getRegistry().getAllOf(CatalogTypes.HORSE_STYLE)) {
            String styles = style.getId().toLowerCase().replaceAll("white_", "").replaceAll("_", "");
            String inputs = str.toLowerCase().replaceAll("white_", "").replaceAll("_", "").replaceAll("whitelegs", "white");
            if (styles.equals(inputs)) {
                return style;
            }
        }
        return null;
    }

    static void setHorseSpeed(Horse h, double speed) {
        try {
            AttributeData d = h.getOrCreate(AttributeData.class).get();
            d.setBase(Attributes.GENERIC_MOVEMENT_SPEED, speed);
            h.offer(d);
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
            if (en.isCompatible(AgeableData.class)) {
                en.offer(en.getOrCreate(AgeableData.class).get().setBaby());
                return true;
            } else if (en instanceof ArmorStand) {
                ((ArmorStand) en).setSmall(true);
                return true;
            }
        } else if (data.startsWith("age:")) {
            if (en.isCompatible(AgeableData.class)) {
                if (r.isInt(data.split(":")[1])) {
                    en.offer(en.getOrCreate(AgeableData.class).get().setAge(Integer.parseInt(data.split(":")[1])));
                    return true;
                }
            }
        } else if (data.equalsIgnoreCase("agelock")) {
            if (en.isCompatible(AgeableData.class)) {
                en.offer(en.getOrCreate(AgeableData.class).get()) //TODO agelock
                return true;
            }
        } else if (data.equalsIgnoreCase("tame") || data.equalsIgnoreCase("tameable")) {
            if (en.isCompatible(TameableData.class)) {
                if (p != null) {
                    en.offer(en.getOrCreate(TameableData.class).get().setOwner(p));
                }
                return true;
            }
        } else if (data.equalsIgnoreCase("random")) {
            if (en.isCompatible(ColoredData.class)) {
                en.offer(en.getOrCreate(ColoredData.class).get()
                        .setColor(((DyeColor) r.getRegistry().getAllOf(CatalogTypes.DYE_COLOR).toArray()[random.nextInt(r.getRegistry().getAllOf(CatalogTypes.DYE_COLOR).size())]).getColor()));
                return true;
            }
        } else if (data.startsWith("color:")) {
            if (en.isCompatible(ColoredData.class)) {
                String color = data.toUpperCase(Locale.ENGLISH);
                en.offer(en.getOrCreate(ColoredData.class).get().setColor(r.getGame().getRegistry().getType(CatalogTypes.DYE_COLOR, color).get().getColor()));
                return true;
            }
        } else if (data.equalsIgnoreCase("zombie") || data.equalsIgnoreCase("zombievillager")) {
            if (en.isCompatible(VillagerZombieData.class)) {
                en.getOrCreate(VillagerZombieData.class);
                return true;
            }
        } else if (data.equalsIgnoreCase("wither") || data.equalsIgnoreCase("witherskeleton")) {
            if (en.isCompatible(SkeletonData.class)) {
                en.offer(en.getOrCreate(SkeletonData.class).get().setValue(SkeletonTypes.WITHER));
                return true;
            }
        } else if (data.equalsIgnoreCase("powered") || data.equalsIgnoreCase("electrified") || data.equalsIgnoreCase("charged")) {
            if (en.isCompatible(ChargedData.class)) {
                en.getOrCreate(ChargedData.class);
                return true;
            }
        } else if (data.equalsIgnoreCase("saddle") || data.equalsIgnoreCase("saddled")) {
            if (en.isCompatible(SaddleData.class)) {
                en.offer(en.getOrCreate(SaddleData.class).get().setSaddle(r.getRegistry().getItemBuilder().itemType(ItemTypes.SADDLE).build()));
                return true;
            }
        } else if (data.equalsIgnoreCase("angry") || data.equalsIgnoreCase("rabid")) {
            if (en.isCompatible(AngerableData.class)) {
                en.offer(en.getOrCreate(AngerableData.class).get().setAngerLevel(999999));
                return true;
            }
        } else if (data.startsWith("size:")) {
            if (r.isInt(data.split(":")[1])) {
                if (en.isCompatible(SlimeData.class)) {
                    en.offer(en.getOrCreate(SlimeData.class).get().setSize(Integer.parseInt(data.split(":")[1])));
                    return true;
                }
            }
        } else if (data.equalsIgnoreCase("elder") || data.equalsIgnoreCase("elderguardian")) {
            if (en.isCompatible(ElderData.class)) {
                en.getOrCreate(ElderData.class).get();
                return true;
            }
        } else if (data.startsWith("exp:") || data.startsWith("xp:") || data.startsWith("experience:") || data.startsWith("amount:")) {
            if (r.isInt(data.split(":")[1]) && en.isCompatible(ExperienceHolderData.class)) {
                Integer amount = r.normalize(Integer.parseInt(data.split(":")[1]), 1, 2000000000);
                en.offer(en.getOrCreate(ExperienceHolderData.class).get().setTotalExperience(amount));
                return true;
            }
        } else if (data.startsWith("maxhealth:")) {
            if (r.isDouble(data.split(":")[1]) && en.isCompatible(HealthData.class)) {
                Double amount = r.normalize(Double.parseDouble(data.split(":")[1]), 1.0, 999999.0);
                en.offer(en.getOrCreate(HealthData.class).get().setMaxHealth(amount));
                return true;
            }
        } else if (data.startsWith("health:")) {
            if (r.isDouble(data.split(":")[1]) && en.isCompatible(HealthData.class)) {
                Double amount = r.normalize(Double.parseDouble(data.split(":")[1]), 1.0, en.getOrCreate(HealthData.class).get().getMaxHealth());
                en.offer(en.getOrCreate(HealthData.class).get().setHealth(amount));
                return true;
            }
        } else if (data.contains(":") && EffectDatabase.getByName(data.split(":")[0]) != null) {
            if (en.isCompatible(PotionEffectData.class)) {
                if (r.isInt(data.split(":")[1])) {
                    Integer i = Integer.parseInt(data.split(":")[1]);
                    PotionEffectData ped = en.getOrCreate(PotionEffectData.class).get();
                    ped.addPotionEffect(r.getRegistry().getPotionEffectBuilder().potionType(EffectDatabase.getByName(data.split(":")[0])).duration(999999).amplifier(i).build(), true);
                    en.offer(ped);
                }
            }
        } else if (data.startsWith("name:")) {
            DisplayNameData dnd = en.getOrCreate(DisplayNameData.class).get();
            dnd.setCustomNameVisible(true);
            dnd.setDisplayName(Texts.of(r.translateAlternateColorCodes('&', data.split(":")[1]).replace("_", " ")));
            return true;
        } else if (data.equalsIgnoreCase("noai")) {
            if (en instanceof LivingEntity) {
                try {
                    ReflectionUtil.ReflectionObject cen = ReflectionUtil.execute("getHandle()", en);
                    ReflectionUtil.ReflectionObject tag = cen.invoke("getNBTTag");
                    if (tag == null) {
                        tag = ReflectionUtil.ReflectionObject.fromNMS("NBTTagCompound");
                    }
                    cen.invoke("c", tag.fetch());
                    tag.invoke("setInt", "NoAI", 1);
                    cen.invoke("f", tag.fetch());
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
        } if (en instanceof Horse) {
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
