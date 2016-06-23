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
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class CmdFireball implements UltimateCommand {

    @Override
    public String getName() {
        return "fireball";
    }

    @Override
    public String getPermission() {
        return "uc.fireball";
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
//
//    @Override
//    public List<String> getAliases() {
//        return Arrays.asList("fireskull");
//    }
//
//    @Override
//    public void run(final CommandSource cs, String label, String[] args) {
//        if (!r.isPlayer(cs)) {
//            return;
//        }
//        Player p = (Player) cs;
//        if (!r.perm(p, "uc.fireball", false, true)) {
//            return;
//        }
//        Class type = Fireball.class;
//        if (args.length > 0) {
//            if (args[0].equalsIgnoreCase("small")) {
//                type = SmallFireball.class;
//            } else if (args[0].equalsIgnoreCase("arrow")) {
//                type = Arrow.class;
//            } else if (args[0].equalsIgnoreCase("skull")) {
//                type = WitherSkull.class;
//            } else if (args[0].equalsIgnoreCase("egg")) {
//                type = Egg.class;
//            } else if (args[0].equalsIgnoreCase("snowball")) {
//                type = Snowball.class;
//            } else if (args[0].equalsIgnoreCase("expbottle")) {
//                type = ThrownExpBottle.class;
//            } else if (args[0].equalsIgnoreCase("large")) {
//                type = LargeFireball.class;
//            }
//        }
//        Vector direction = p.getEyeLocation().getDirection().multiply(2);
//        Projectile projectile = (Projectile) p.getWorld().spawn(p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), type);
//        projectile.setShooter(p);
//        projectile.setVelocity(direction);
//    }
//
//    @Override
//    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
//        return null;
//    }
}
