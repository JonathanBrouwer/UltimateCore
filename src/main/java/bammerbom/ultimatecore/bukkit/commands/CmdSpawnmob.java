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
package bammerbom.ultimatecore.bukkit.commands;

import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.classes.MobData;
import bammerbom.ultimatecore.bukkit.resources.classes.MobType;
import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.DyeColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CmdSpawnmob implements UltimateCommand {

    @Override
    public String getName() {
        return "spawnmob";
    }

    @Override
    public String getPermission() {
        return "uc.spawnmob";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("mob");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.spawnmob", false, true)) {
            return;
        }
        if (!r.isPlayer(cs)) {
            return;
        }
        Player p = (Player) cs;
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "spawnmobUsage");
            r.sendMes(cs, "spawnmobUsage2");
            r.sendMes(cs, "spawnmobUsage3");
            return;
        }
        if (args[0].equalsIgnoreCase("list")) {
            String message1 = "";
            String message2 = "";
            String message3 = "";
            for (MobType mob : MobType.values()) {
                if (mob.type.equals(MobType.Enemies.ENEMY)) {
                    message3 = message3 + ", " + mob.name;
                }
                if (mob.type.equals(MobType.Enemies.NEUTRAL)) {
                    message2 = message2 + ", " + mob.name;
                }
                if (mob.type.equals(MobType.Enemies.FRIENDLY)) {
                    message1 = message1 + ", " + mob.name;
                }
            }
            message1 = message1.replaceFirst(", ", "");
            message2 = message2.replaceFirst(", ", "");
            message3 = message3.replaceFirst(", ", "");
            r.sendMes(cs, "spawnmobList1", "%Friendly", message3);
            r.sendMes(cs, "spawnmobList2", "%Neutral", message2);
            r.sendMes(cs, "spawnmobList3", "%Enemy", message1);
            return;
        }
        if (args[0].equalsIgnoreCase("data")) {
            r.sendMes(cs, "spawnmobData1");
            r.sendMes(cs, "spawnmobData2");
            r.sendMes(cs, "spawnmobData3");
            r.sendMes(cs, "spawnmobData4");
            r.sendMes(cs, "spawnmobData5");
            r.sendMes(cs, "spawnmobData6");
            r.sendMes(cs, "spawnmobData7");
            r.sendMes(cs, "spawnmobData8");
            r.sendMes(cs, "spawnmobData9");
            return;
        }
        Location loc = p.getLocation();
        MobType mob = MobType.fromName(args[0]);
        Integer amount = 1;

        ArrayList<MobType> smob = new ArrayList<>();
        if (r.checkArgs(args, 1) == true) {
            if (r.isInt(args[1])) {
                amount = Integer.parseInt(args[1]);
            }
        }
        if (mob == null || mob.name == null || mob.name.equals("") || mob.getType() == null) {
            if (!args[0].contains(",")) {
                r.sendMes(cs, "spawnmobNotFound", "%Mob", args[0]);
                return;
            }
            //Stacked
            ArrayList<SpawnKit> kits = new ArrayList<>();
            for (String string : args[0].split(",")) {
                MobType mo1 = MobType.fromName(string);
                if (mo1 == null || mo1.name == null || mo1.name.equals("") || mo1.getType() == null) {
                    mo1 = MobType.fromName(string.split(":")[0]);
                    if (mo1 == null || mo1.name == null || mo1.name.equals("") || mo1.getType() == null) {
                        r.sendMes(cs, "spawnmobNotFound", "%Mob", string);
                        return;
                    } else {
                        kits.add(new SpawnKit(mo1, string.split(":")[1]));
                    }
                } else {
                    kits.add(new SpawnKit(mo1, ""));
                }
            }
            for (int i = 0; i < amount; i++) {
                LivingEntity lastmob = null;
                for (SpawnKit kit : kits) {
                    EntityType type = kit.a().getType();
                    LivingEntity en = (LivingEntity) loc.getWorld().spawnEntity(loc, type);
                    if (kit.a().name().equals("witherskeleton")) {
                        Skeleton skel = (Skeleton) en;
                        skel.setSkeletonType(SkeletonType.WITHER);
                        EntityEquipment invent = ((LivingEntity) skel).getEquipment();
                        invent.setItemInHand(new ItemStack(Material.STONE_SWORD, 1));
                        invent.setItemInHandDropChance(0.09F);
                    } else if (kit.a().name().equalsIgnoreCase("skeleton")) {
                        Skeleton skel = (Skeleton) en;
                        skel.setSkeletonType(SkeletonType.NORMAL);
                        skel.getEquipment().setItemInHand(new ItemStack(Material.BOW));
                        skel.getEquipment().setItemInHandDropChance(0.09F);
                    }
                    if (kit.a().name().equalsIgnoreCase("elderguardian")) {
                        Guardian g = (Guardian) en;
                        g.setElder(true);
                    }
                    defaultMobData(type, en);
                    //TODO Utilize(kit.b, mob, en, p);
                    if (lastmob != null) {
                        lastmob.setPassenger(en);
                    }
                    lastmob = en;
                }
            }
            //End stacked
            return;
        }
        //Unstacked
        for (int i = 0; i < amount; i++) {
            try {
                LivingEntity en = (LivingEntity) loc.getWorld().spawnEntity(loc, mob.getType());
                if (args[0].equals("witherskeleton")) {
                    Skeleton skel = (Skeleton) en;
                    skel.setSkeletonType(SkeletonType.WITHER);
                    EntityEquipment invent = ((LivingEntity) skel).getEquipment();
                    invent.setItemInHand(new ItemStack(Material.STONE_SWORD, 1));
                    invent.setItemInHandDropChance(0.09F);
                }
                defaultMobData(mob.getType(), en);
                utilize(args, mob, en, p);
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }

    static void horse(EntityType type, Entity spawned, String str) {
        if (!type.equals(EntityType.HORSE)) {
            return;
        }
        Horse horse = (Horse) spawned;
        if (str.equalsIgnoreCase("donkey")) {
            horse.setVariant(Variant.DONKEY);
        } else if (str.equalsIgnoreCase("mule")) {
            horse.setVariant(Variant.MULE);
        } else if (str.equalsIgnoreCase("skeleton")) {
            horse.setVariant(Variant.SKELETON_HORSE);
        } else if (str.equalsIgnoreCase("undead") || str.equalsIgnoreCase("zombie")) {
            horse.setVariant(Variant.UNDEAD_HORSE);
        } else if (isHorseColor(str)) {
            horse.setColor(getHorseColor(str));
        } else if (isHorseStyle(str)) {
            horse.setStyle(getHorseStyle(str));
        } else if (str.equalsIgnoreCase("saddled")) {
            horse.setTamed(true);
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
        } else if (str.equalsIgnoreCase("diamond") || str.equalsIgnoreCase("diamondarmor")) {
            horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
        } else if (str.equalsIgnoreCase("iron") || str.equalsIgnoreCase("ironarmor")) {
            horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
        } else if (str.equalsIgnoreCase("gold") || str.equalsIgnoreCase("goldarmor")) {
            horse.getInventory().setArmor(new ItemStack(Material.GOLD_BARDING));
        }
        /*Color
         * BLACK
         * BROWN
         * CHESTNUT
         * CREAMY
         * DARKBROWN
         * GRAY
         * WHITE
         */
        /*Style
         * BLACKDOTS
         * NONE
         * WHITE_LEGS
         * WHITE_DOTS
         * WHITEFIELD
         */
    }

    @SuppressWarnings("unused")
    private static class SpawnKit {

        public MobType a;
        public String b;

        public SpawnKit(MobType a, String b) {
            this.a = a;
            this.b = b;
        }

        public MobType a() {
            return a;
        }

        public void a(MobType a2) {
            a = a2;
        }

        public String b() {
            return b;
        }

        public void b(String b2) {
            b = b2;
        }
    }

    static void utilize(String[] args, MobType mob, LivingEntity en, Player p) {
        utilize((r.getFinalArg(args, 1)), mob, en, p);
    }

    static void utilize(String args, MobType mob, LivingEntity en, Player p) {
        for (String str : args.split("[: ]")) {
            if(str.isEmpty()) continue;
            MobData d = MobData.fromData(en, str);
            if (d != null) {
                try {
                    d.setData(en, p, str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                r.sendMes(p, "spawnmobDataNotFound", "%Data", str);
            }
        }
    }

    static boolean isHorseColor(String str) {
        for (Color color : Color.values()) {
            String colors = color.name().toLowerCase().replaceAll("_", "");
            String inputs = str.toLowerCase().replaceAll("_", "");
            if (colors.equals(inputs)) {
                return true;
            }
        }
        return false;
    }

    static Color getHorseColor(String str) {
        for (Color color : Color.values()) {
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
        for (Style style : Style.values()) {
            String styles = style.name().toLowerCase().replaceAll("white_", "").replaceAll("_", "");
            String inputs = str.toLowerCase().replaceAll("white_", "").replaceAll("_", "").replaceAll("whitelegs", "white");
            if (styles.equals(inputs)) {
                return true;
            }
        }
        return false;
    }

    static Style getHorseStyle(String str) {
        if (str.equalsIgnoreCase("whitelegs")) {
            return Style.WHITE;
        }
        for (Style style : Style.values()) {
            String styles = style.name().toLowerCase().replaceAll("white_", "").replaceAll("_", "");
            String inputs = str.toLowerCase().replaceAll("white_", "").replaceAll("_", "").replaceAll("whitelegs", "white");
            if (styles.equals(inputs)) {
                return style;
            }
        }
        return null;
    }

    static void defaultMobData(EntityType type, Entity spawned) {
        if (type == EntityType.SKELETON) {
            if (!((Skeleton) spawned).getSkeletonType().equals(SkeletonType.WITHER)) {
                Skeleton skel = (Skeleton) spawned;
                skel.setSkeletonType(SkeletonType.NORMAL);
                skel.getEquipment().setItemInHand(new ItemStack(Material.BOW));
                skel.getEquipment().setItemInHandDropChance(0.09F);
                return;
            }

            if (type == EntityType.PIG_ZOMBIE) {
                EntityEquipment invent = ((LivingEntity) spawned).getEquipment();
                invent.setItemInHand(new ItemStack(Material.GOLD_SWORD, 1));
                invent.setItemInHandDropChance(0.05F);
            }
        }
    }
    

    static DyeColor getDyeColor(String str) {
        for (DyeColor dye : DyeColor.values()) {
            if (dye.name().toLowerCase().replaceAll("_", "").equalsIgnoreCase(str)) {
                return dye;
            }
        }
        return null;
    }
}
