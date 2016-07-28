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
package bammerbom.ultimatecore.sponge.modules.afk.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkKeys;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkPermissions;
import bammerbom.ultimatecore.sponge.utils.CMGenerator;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class AfkCommand implements Command {

    @Override
    public Module getModule() {
        return Modules.AFK.get();
    }

    @Override
    public String getIdentifier() {
        return "afk";
    }

    @Override
    public Permission getPermission() {
        return AfkPermissions.UC_AFK;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(AfkPermissions.UC_AFK, AfkPermissions.UC_AFK_MESSAGE, AfkPermissions.UC_AFK_OTHERS, AfkPermissions.UC_AFK_OTHERS_MESSAGE);
    }

    @Override
    public Text getUsage() {
        //TODO <command>?
        return CMGenerator.usage(this, Messages.getColored("afk.command.afk.usage"));
    }

    @Override
    public Text getShortDescription() {
        return CMGenerator.shortDescription(this, Messages.getFormatted("afk.command.afk.shortdescription"));
    }

    @Override
    public Text getLongDescription() {
        return Messages.getFormatted("afk.command.afk.longdescription");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("afk", "idle", "away");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Text.of("You are not a player..."));
            return CommandResult.empty();
        }
        boolean isafk = UltimateCore.get().getUserService().getUser((Player) sender).get(AfkKeys.IS_AFK).get();
        if(UltimateCore.get().getUserService().getUser((Player) sender).offer(AfkKeys.IS_AFK, !isafk)){
            sender.sendMessage(Text.of("You are now " + (isafk ? "" : "no longer ") + "afk"));
        }else{
            sender.sendMessage(Text.of("Inserting data failed!"));
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
