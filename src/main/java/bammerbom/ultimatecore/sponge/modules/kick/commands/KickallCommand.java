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
package bammerbom.ultimatecore.sponge.modules.kick.commands;

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.kick.api.KickPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KickallCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.KICK.get();
    }

    @Override
    public String getIdentifier() {
        return "kickall";
    }

    @Override
    public Permission getPermission() {
        return KickPermissions.UC_KICK_KICKALL_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(KickPermissions.UC_KICK_KICKALL_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("kickall", "kickeveryone");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(KickPermissions.UC_KICK_KICKALL_BASE.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }
        Text reason;
        if (args.length > 0) {
            reason = Messages.toText(StringUtil.getFinalArg(args, 0));
        } else {
            reason = Messages.getFormatted("kick.command.kickall.defaultreason");
        }
        for (Player p : Sponge.getServer().getOnlinePlayers()) {
            if (p.getName().equals(sender.getName())) {
                continue;
            }
            p.kick(Messages.getFormatted("kick.command.kickall.message", "%kicker%", sender.getName(), "%reason%", reason));
        }
        sender.sendMessage(Messages.getFormatted(sender, "kick.command.kickall.success"));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
