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

import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.heal.HealModule;
import bammerbom.ultimatecore.sponge.modules.heal.api.HealPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CommandInfo(module = HealModule.class, aliases = {"heal"})
public class HealCommand implements HighCommand {

    @Override
    public Permission getPermission() {
        return HealPermissions.UC_HEAL_HEAL_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(HealPermissions.UC_HEAL_HEAL_BASE, HealPermissions.UC_HEAL_HEAL_OTHERS);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).optional().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, HealPermissions.UC_HEAL_HEAL_BASE);
        if (!args.hasAny("player")) {
            checkIfPlayer(sender);
            Player p = (Player) sender;

            p.offer(Keys.HEALTH, p.get(Keys.MAX_HEALTH).orElse(20.0));
            p.offer(Keys.POTION_EFFECTS, new ArrayList<>());
            p.offer(Keys.REMAINING_AIR, p.get(Keys.MAX_AIR).orElse(10));
            p.offer(Keys.FOOD_LEVEL, 20);
            p.offer(Keys.FIRE_TICKS, 0);

            sender.sendMessage(Messages.getFormatted(sender, "heal.command.heal.success"));
            return CommandResult.success();
        } else {
            checkPermission(sender, HealPermissions.UC_HEAL_HEAL_OTHERS);
            Player t = args.<Player>getOne("player").get();

            t.offer(Keys.HEALTH, t.get(Keys.MAX_HEALTH).orElse(20.0));
            t.offer(Keys.POTION_EFFECTS, new ArrayList<>());
            t.offer(Keys.REMAINING_AIR, t.get(Keys.MAX_AIR).orElse(10));
            t.offer(Keys.FOOD_LEVEL, 20);
            t.offer(Keys.FIRE_TICKS, 0);

            sender.sendMessage(Messages.getFormatted(sender, "heal.command.heal.success.self", "%player%", VariableUtil.getNameEntity(t)));
            t.sendMessage(Messages.getFormatted(t, "heal.command.heal.success.others", "%sender%", VariableUtil.getNameSource(sender)));
            return CommandResult.success();
        }
    }

}
