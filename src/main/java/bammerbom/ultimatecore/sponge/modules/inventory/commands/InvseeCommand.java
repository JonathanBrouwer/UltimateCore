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
package bammerbom.ultimatecore.sponge.modules.inventory.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.UserArgument;
import bammerbom.ultimatecore.sponge.api.permission.PermissionInfo;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.inventory.InventoryModule;
import bammerbom.ultimatecore.sponge.modules.inventory.api.InventoryKeys;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.Map;

@CommandInfo(module = InventoryModule.class, aliases = {"invsee", "inventory", "inv", "openinventory", "openinv"})
@CommandPermissions(level = PermissionLevel.ADMIN)
public class InvseeCommand implements HighPermCommand {

    @Override
    public Map<String, PermissionInfo> registerPermissionSuffixes() {
        HashMap<String, PermissionInfo> perms = new HashMap<>();
        perms.put("modify", new PermissionInfo(Text.of("Allows you to modify a player's inventory."), PermissionLevel.ADMIN));
        perms.put("offline", new PermissionInfo(Text.of("Allows you to target an offline player."), PermissionLevel.ADMIN));
        return perms;
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new UserArgument(Text.of("player"))).onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        checkIfPlayer(src);
        Player p = (Player) src;

        User user = args.<User>getOne("player").get();
        if (!user.isOnline()) checkPermSuffix(src, "offline");

        UltimateUser uu = UltimateCore.get().getUserService().getUser(user);
        uu.offer(InventoryKeys.INVSEE_TARGET, user.getUniqueId());

        p.openInventory(user.getInventory());
        return CommandResult.success();
    }
}
