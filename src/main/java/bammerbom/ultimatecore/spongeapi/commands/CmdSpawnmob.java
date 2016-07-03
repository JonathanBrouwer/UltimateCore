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
package bammerbom.ultimatecore.spongeapi.commands;

import bammerbom.ultimatecore.spongeapi.UltimateCommand;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.classes.MobData;
import bammerbom.ultimatecore.spongeapi.resources.classes.MobType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

class SpawnKit {

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

public class CmdSpawnmob implements UltimateCommand {

    static void utilize(String[] args, MobType mob, LivingEntity en, Player p) {
        utilize((r.getFinalArg(args, 1)), mob, en, p);
    }

    static void utilize(String args, MobType mob, LivingEntity en, Player p) {
        for (String str : args.split("[ ]")) {
            if (str.isEmpty() || r.isInt(str)) {
                continue;
            }
            if (!MobData.setData(en, str, p)) {
                r.sendMes(p, "spawnmobDataNotFound", "%Data", str);
            }
        }
    }

    @Override
    public String getName() {
        return "spawnmob";
    }

    @Override
    public String getPermission() {
        return "uc.spawnmob";
    }

    @Override
    public String getUsage() {
        return "/<command> ";
    }

    @Override
    public Text getDescription() {
        return Text.of("Description");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
    //    @Override
    //    public List<String> getAliases() {
    //        return Arrays.asList("mob", "spawnentity");
    //    }
    //
    //    @Override
    //    public void run(final CommandSource cs, String label, String[] args) {
    //        if (!r.perm(cs, "uc.spawnmob", false, true)) {
    //            return CommandResult.empty();
    //        }
    //        if (!r.isPlayer(cs)) {
    //            return CommandResult.empty();
    //        }
    //        Player p = (Player) cs;
    //        if (!r.checkArgs(args, 0)) {
    //            r.sendMes(cs, "spawnmobUsage");
    //            r.sendMes(cs, "spawnmobUsage2");
    //            r.sendMes(cs, "spawnmobUsage3");
    //            return CommandResult.empty();
    //        }
    //        if (args[0].equalsIgnoreCase("list")) {
    //            String message1 = "";
    //            String message2 = "";
    //            String message3 = "";
    //            for (MobType mob : MobType.values()) {
    //                if (mob.type.equals(MobType.Enemies.ENEMY)) {
    //                    message3 = message3 + ", " + mob.name;
    //                }
    //                if (mob.type.equals(MobType.Enemies.NEUTRAL)) {
    //                    message2 = message2 + ", " + mob.name;
    //                }
    //                if (mob.type.equals(MobType.Enemies.FRIENDLY)) {
    //                    message1 = message1 + ", " + mob.name;
    //                }
    //            }
    //            message1 = message1.replaceFirst(", ", "");
    //            message2 = message2.replaceFirst(", ", "");
    //            message3 = message3.replaceFirst(", ", "");
    //            r.sendMes(cs, "spawnmobList1", "%Friendly", message3);
    //            r.sendMes(cs, "spawnmobList2", "%Neutral", message2);
    //            r.sendMes(cs, "spawnmobList3", "%Enemy", message1);
    //            return CommandResult.empty();
    //        }
    //        if (args[0].equalsIgnoreCase("data")) {
    //            r.sendMes(cs, "spawnmobData1");
    //            r.sendMes(cs, "spawnmobData2");
    //            r.sendMes(cs, "spawnmobData3");
    //            r.sendMes(cs, "spawnmobData4");
    //            r.sendMes(cs, "spawnmobData5");
    //            r.sendMes(cs, "spawnmobData6");
    //            r.sendMes(cs, "spawnmobData7");
    //            r.sendMes(cs, "spawnmobData8");
    //            r.sendMes(cs, "spawnmobData9");
    //            return CommandResult.empty();
    //        }
    //        Location loc = p.getLocation();
    //        MobType mob = MobType.fromName(args[0]);
    //        Integer amount = 1;
    //
    //        ArrayList<MobType> smob = new ArrayList<>();
    //        if (r.checkArgs(args, 1) == true) {
    //            if (r.isInt(args[1])) {
    //                amount = Integer.parseInt(args[1]);
    //            }
    //        }
    //        if (mob == null || mob.name == null || mob.name.equals("") || mob.getType() == null) {
    //            if (!args[0].contains(",")) {
    //                r.sendMes(cs, "spawnmobNotFound", "%Mob", args[0]);
    //                return CommandResult.empty();
    //            }
    //            //Stacked
    //            ArrayList<SpawnKit> kits = new ArrayList<>();
    //            for (String string : args[0].split(",")) {
    //                MobType mo1 = MobType.fromName(string);
    //                if (mo1 == null || mo1.name == null || mo1.name.equals("") || mo1.getType() == null) {
    //                    mo1 = MobType.fromName(string.split(":")[0]);
    //                    if (mo1 == null || mo1.name == null || mo1.name.equals("") || mo1.getType() == null) {
    //                        r.sendMes(cs, "spawnmobNotFound", "%Mob", string);
    //                        return CommandResult.empty();
    //                    } else {
    //                        kits.add(new SpawnKit(mo1, string.split(":")[1]));
    //                    }
    //                } else {
    //                    kits.add(new SpawnKit(mo1, ""));
    //                }
    //            }
    //            for (int i = 0;
    //                 i < amount;
    //                 i++) {
    //                LivingEntity lastmob = null;
    //                for (SpawnKit kit : kits) {
    //                    EntityType type = kit.a().getType();
    //                    LivingEntity en = (LivingEntity) loc.getWorld().spawnEntity(loc, type);
    //                    if (kit.a().name().equals("witherskeleton")) {
    //                        Skeleton skel = (Skeleton) en;
    //                        skel.setSkeletonType(SkeletonType.WITHER);
    //                        EntityEquipment invent = skel.getEquipment();
    //                        invent.setItemInHand(new ItemStack(Material.STONE_SWORD, 1));
    //                        invent.setItemInHandDropChance(0.09F);
    //                    } else if (kit.a().name().equalsIgnoreCase("skeleton")) {
    //                        Skeleton skel = (Skeleton) en;
    //                        skel.setSkeletonType(SkeletonType.NORMAL);
    //                        skel.getEquipment().setItemInHand(new ItemStack(Material.BOW));
    //                        skel.getEquipment().setItemInHandDropChance(0.09F);
    //                    }
    //                    if (kit.a().name().equalsIgnoreCase("elderguardian")) {
    //                        Guardian g = (Guardian) en;
    //                        g.setElder(true);
    //                    }
    //                    MobData.setDefault(en);
    //                    //TODO Utilize(kit.b, mob, en, p);
    //                    if (lastmob != null) {
    //                        lastmob.setPassenger(en);
    //                    }
    //                    lastmob = en;
    //                }
    //            }
    //            //End stacked
    //            return CommandResult.empty();
    //        }
    //        //Unstacked
    //        for (int i = 0;
    //             i < amount;
    //             i++) {
    //            try {
    //                Entity en = loc.getWorld().spawnEntity(loc, mob.getType());
    //                if (args[0].equals("witherskeleton")) {
    //                    Skeleton skel = (Skeleton) en;
    //                    skel.setSkeletonType(SkeletonType.WITHER);
    //                    EntityEquipment invent = skel.getEquipment();
    //                    invent.setItemInHand(new ItemStack(Material.STONE_SWORD, 1));
    //                    invent.setItemInHandDropChance(0.09F);
    //                }
    //                if (args[0].equalsIgnoreCase("elderguardian")) {
    //                    Guardian g = (Guardian) en;
    //                    g.setElder(true);
    //                }
    //                MobData.setDefault(en);
    //                if (en instanceof LivingEntity) {
    //                    utilize(args, mob, (LivingEntity) en, p);
    //                }
    //            } catch (ClassCastException ex) {
    //                ErrorLogger.log(ex, "Mob spawning failed. (Invalid mob?)");
    //            }
    //        }
    //    }
    //
    //    @Override
    //    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
    //        List<String> rtrn = new ArrayList<>();
    //        if (curn == 0) {
    //            for (MobType t : MobType.values()) {
    //                rtrn.add(t.name().toLowerCase());
    //            }
    //            return rtrn;
    //        }
    //        return rtrn;
    //    }
}
