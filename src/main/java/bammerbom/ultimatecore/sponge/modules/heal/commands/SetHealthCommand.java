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
package bammerbom.ultimatecore.sponge.modules.heal.commands;

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.heal.api.HealPermissions;
import bammerbom.ultimatecore.sponge.utils.CMGenerator;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class SetHealthCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.HEAL.get();
    }

    @Override
    public String getIdentifier() {
        return "sethealth";
    }

    @Override
    public Permission getPermission() {
        return HealPermissions.UC_SETHEALTH;

    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(HealPermissions.UC_SETHEALTH, HealPermissions.UC_SETHEALTH_OTHERS);
    }

    @Override
    public Text getUsage() {
        return CMGenerator.usage(this, Messages.getFormatted("heal.command.sethealth.usage"));
    }

    @Override
    public Text getShortDescription() {
        return CMGenerator.shortDescription(this, Messages.getFormatted("heal.command.sethealth.shortdescription"));
    }

    @Override
    public Text getLongDescription() {
        return CMGenerator.longDescription(this, Messages.getFormatted("heal.command.sethealth.longdescription"));

    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("sethealth", "setlives");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {

        if (!sender.hasPermission(HealPermissions.UC_SETHEALTH.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        if (args.length == 0) {
            if (sender instanceof Player) {
                sender.sendMessage(Messages.getFormatted("heal.command.sethealth.usage"));
                return CommandResult.empty();
            } else {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
        }
        Player t;
        Double health;
        if (args.length == 1) {
            try {
                Player p = (Player) sender;
                health = Double.parseDouble(args[0]);
                p.offer(Keys.HEALTH, health);
                sender.sendMessage(Messages.getFormatted("heal.command.sethealth.success", "%health%", health));

                return CommandResult.success();
            } catch (Exception ex) {
                sender.sendMessage(getUsage());

                return CommandResult.empty();
            }
        } else if (Sponge.getServer().getPlayer(args[1]).isPresent()) {
            t = Sponge.getServer().getPlayer(args[1]).get();

            try {
                health = Double.parseDouble(args[0]);
                t.offer(Keys.HEALTH, health);
                sender.sendMessage(Messages.getFormatted("heal.command.sethealth.success.self", "%target%", t.getName(), "%health%", health));
                t.sendMessage(Messages.getFormatted("heal.command.sethealth.success.others", "%player%", sender.getName(), "%health%", health));

                return CommandResult.success();
            } catch (Exception ex) {
                sender.sendMessage(getUsage());

                return CommandResult.empty();
            }

        } else {
            sender.sendMessage(Messages.getFormatted("core.playernotfound", "%player%", args[1]));

            return CommandResult.empty();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
