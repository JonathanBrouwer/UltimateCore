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
package bammerbom.ultimatecore.sponge.modules.fly.commands;

import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.BoundedDoubleArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.variable.utils.VariableUtil;
import bammerbom.ultimatecore.sponge.modules.fly.FlyModule;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

@CommandInfo(module = FlyModule.class, aliases = {"speed", "generalspeed"})
@CommandPermissions(level = PermissionLevel.ADMIN)
public class SpeedCommand implements HighPermCommand {

    double flymultiplier = 20;
    double walkmultiplier = 10;

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new BoundedDoubleArgument(Text.of("speed"), 0.0, null)).onlyOne().build(), Arguments.builder(new PlayerArgument(Text.of("player"))).optional().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        Double speed = args.<Double>getOne("speed").get();
        if (!args.hasAny("player")) {
            checkIfPlayer(sender);
            Player p = (Player) sender;
            if (p.get(Keys.IS_FLYING).orElse(false)) {
                p.offer(Keys.FLYING_SPEED, speed / flymultiplier);
                Messages.send(p, "fly.command.flyspeed.success.self", "%speed%", speed);
            } else {
                p.offer(Keys.WALKING_SPEED, speed / walkmultiplier);
                Messages.send(p, "fly.command.walkspeed.success.self", "%speed%", speed);
            }
            return CommandResult.success();
        } else {
            Player t = args.<Player>getOne("player").get();
            if (t.get(Keys.IS_FLYING).orElse(false)) {
                t.offer(Keys.FLYING_SPEED, speed / flymultiplier);
                Messages.send(sender, "fly.command.flyspeed.success.others.self", "%player%", VariableUtil.getNameEntity(t), "%speed%", speed);
                Messages.send(t, "fly.command.flyspeed.success.others.self", "%player%", sender, "%speed%", speed);
            } else {
                t.offer(Keys.WALKING_SPEED, speed / walkmultiplier);
                Messages.send(sender, "fly.command.walkspeed.success.others.self", "%player%", VariableUtil.getNameEntity(t), "%speed%", speed);
                Messages.send(t, "fly.command.walkspeed.success.others.self", "%player%", sender, "%speed%", speed);
            }
            return CommandResult.success();
        }
    }
}
