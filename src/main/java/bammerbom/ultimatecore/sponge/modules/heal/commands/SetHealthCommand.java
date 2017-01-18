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

import bammerbom.ultimatecore.sponge.api.command.Arguments;
import bammerbom.ultimatecore.sponge.api.command.RegisterCommand;
import bammerbom.ultimatecore.sponge.api.command.SmartCommand;
import bammerbom.ultimatecore.sponge.api.command.arguments.BoundedDoubleArgument;
import bammerbom.ultimatecore.sponge.api.command.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.heal.HealModule;
import bammerbom.ultimatecore.sponge.modules.heal.api.HealPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@RegisterCommand(module = HealModule.class, aliases = {"sethealth", "setlives"})
public class SethealthCommand implements SmartCommand {

    @Override
    public Permission getPermission() {
        return HealPermissions.UC_HEAL_SETHEALTH_BASE;

    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(HealPermissions.UC_HEAL_SETHEALTH_BASE, HealPermissions.UC_HEAL_SETHEALTH_OTHERS);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new BoundedDoubleArgument(Text.of("health"), 0.0, null)).onlyOne().build(), Arguments.builder(new PlayerArgument(Text.of("player"))).optional().onlyOne().build(),
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, HealPermissions.UC_HEAL_SETHEALTH_BASE);
        if (!args.hasAny("player")) {
            checkIfPlayer(sender);
            Player p = (Player) sender;

            Double health = args.<Double>getOne("health").get();
            if (health > p.get(Keys.MAX_HEALTH).orElse(20.0)) {
                p.offer(Keys.MAX_HEALTH, health);
            }
            p.offer(Keys.HEALTH, health);

            sender.sendMessage(Messages.getFormatted(sender, "heal.command.sethealth.success", "%health%", health));
            return CommandResult.success();
        } else {
            checkPermission(sender, HealPermissions.UC_HEAL_SETHEALTH_OTHERS);
            Player t = args.<Player>getOne("player").get();

            Double health = args.<Double>getOne("health").get();
            if (health > t.get(Keys.MAX_HEALTH).orElse(20.0)) {
                t.offer(Keys.MAX_HEALTH, health);
            }
            t.offer(Keys.HEALTH, health);

            sender.sendMessage(Messages.getFormatted(sender, "heal.command.sethealth.success.self", "%target%", t.getName(), "%health%", health));
            t.sendMessage(Messages.getFormatted(t, "heal.command.sethealth.success.others", "%player%", sender.getName(), "%health%", health));
            return CommandResult.success();
        }
    }
}
