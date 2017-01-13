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
package bammerbom.ultimatecore.sponge.modules.jail.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.jail.api.Jail;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailData;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailKeys;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailPermissions;
import bammerbom.ultimatecore.sponge.utils.*;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class JailCommand implements Command {
    static Random random = new Random();

    @Override
    public Module getModule() {
        return Modules.JAIL.get();
    }

    @Override
    public String getIdentifier() {
        return "jail";
    }

    @Override
    public Permission getPermission() {
        return JailPermissions.UC_JAIL_JAIL_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(JailPermissions.UC_JAIL_JAIL_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("jail");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(JailPermissions.UC_JAIL_JAIL_BASE.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        }

        //Find player
        UUID suuid = sender instanceof Player ? ((Player) sender).getUniqueId() : UUID.fromString("00000000-0000-0000-0000-000000000000");
        Player t = Selector.one(sender, args[0]).orElse(null);
        if (t == null) {
            sender.sendMessage(Messages.getFormatted("core.playernotfound", "%player%", args[0]));
            return CommandResult.empty();
        }
        UltimateUser ut = UltimateCore.get().getUserService().getUser(t);

        if ((JailPermissions.UC_JAIL_EXEMPTPOWER.getIntFor(t) > JailPermissions.UC_JAIL_POWER.getIntFor(sender)) && sender instanceof Player) {
            sender.sendMessage(Messages.getFormatted("jail.command.jail.exempt", "%player%", VariableUtil.getNameSource(t)));
            return CommandResult.empty();
        }

        //Find jail, time and reason
        List<Jail> jails = GlobalData.get(JailKeys.JAILS).get();
        Jail jail = jails.get(random.nextInt(jails.size()));
        long time = -1;
        //Current is the current argument being checked.
        //For example is the jail is not found, see if that argument is a time
        int current = 1;
        Text reason = Messages.getFormatted("jail.command.jail.defaultreason");

        //Jail
        if (args.length >= (current + 1)) {
            String jailname = args[1];
            Jail jail2 = jails.stream().filter(jail1 -> jail1.getName().equalsIgnoreCase(jailname)).findAny().orElse(null);
            if (jail2 != null) {
                jail = jail2;
                current++;
            }
        }

        //Time
        if (args.length >= (current + 1)) {
            time = TimeUtil.parseDateDiff(args[current]);
            if (time != -1) {
                current++;
            }
        }

        //Reason
        if (args.length >= (current + 1)) {
            reason = Messages.toText(StringUtil.getFinalArg(args, current));
        }

        JailData data = new JailData(t.getUniqueId(), suuid, time == -1 ? -1 : (time + System.currentTimeMillis()), System.currentTimeMillis(), reason, jail.getName());
        ut.offer(JailKeys.JAIL, data);
        sender.sendMessage(Messages.getFormatted("jail.command.jail.success", "%player%", VariableUtil.getNameSource(t), "%jail%", jail.getName(), "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", reason));
        t.sendMessage(Messages.getFormatted("jail.target.jailed", "%player%", VariableUtil.getNameSource(sender), "%jail%", jail.getName(), "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", reason));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
