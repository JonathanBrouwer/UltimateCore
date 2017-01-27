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
package bammerbom.ultimatecore.sponge.modules.vanish.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.vanish.VanishModule;
import bammerbom.ultimatecore.sponge.modules.vanish.api.VanishKeys;
import bammerbom.ultimatecore.sponge.modules.vanish.api.VanishPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@CommandInfo(module = VanishModule.class, aliases = {"vanish"})
public class VanishCommand implements HighCommand {
    @Override
    public Permission getPermission() {
        return VanishPermissions.UC_VANISH_VANISH_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(VanishPermissions.UC_VANISH_VANISH_BASE, VanishPermissions.UC_VANISH_VANISH_OTHERS);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, VanishPermissions.UC_VANISH_VANISH_BASE);
        if (!args.hasAny("player")) {
            checkIfPlayer(sender);
            Player p = (Player) sender;
            UltimateUser up = UltimateCore.get().getUserService().getUser(p);
            boolean vanish = !up.get(VanishKeys.VANISH).get();
            up.offer(VanishKeys.VANISH, vanish);
            if (vanish) {
                sender.sendMessage(Messages.getFormatted(sender, "vanish.command.vanish.self.on"));
            } else {
                sender.sendMessage(Messages.getFormatted(sender, "vanish.command.vanish.self.off"));
            }
            return CommandResult.success();
        } else {
            checkPermission(sender, VanishPermissions.UC_VANISH_VANISH_OTHERS);
            Player t = args.<Player>getOne("player").get();
            UltimateUser ut = UltimateCore.get().getUserService().getUser(t);
            boolean vanish = !ut.get(VanishKeys.VANISH).get();
            ut.offer(VanishKeys.VANISH, vanish);
            if (vanish) {
                sender.sendMessage(Messages.getFormatted(sender, "vanish.command.vanish.others.self.on", "%player%", VariableUtil.getNameEntity(t)));
                t.sendMessage(Messages.getFormatted(t, "vanish.command.vanish.others.others.on", "%player%", sender));
            } else {
                sender.sendMessage(Messages.getFormatted(sender, "vanish.command.vanish.others.self.off", "%player%", VariableUtil.getNameEntity(t)));
                t.sendMessage(Messages.getFormatted(t, "vanish.command.vanish.others.others.off", "%player%", sender));
            }
            return CommandResult.success();
        }
    }
}
