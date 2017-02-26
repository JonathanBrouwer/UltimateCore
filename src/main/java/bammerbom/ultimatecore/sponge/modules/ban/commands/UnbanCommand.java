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
package bammerbom.ultimatecore.sponge.modules.ban.commands;

import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.GameprofileArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.IpAdressArgument;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.modules.ban.BanModule;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.text.Text;

import java.net.InetAddress;

@CommandInfo(module = BanModule.class, aliases = {"unban", "pardon", "unbanip", "pardonip", "ipunban", "ippardon"})
@CommandPermissions(level = PermissionLevel.MOD)
public class UnbanCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{Arguments.builder(new GameprofileArgument(Text.of("player")), new IpAdressArgument(Text.of("ip"))).onlyOne().usageKey("Player/IP").build(),};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        GameProfile profile = args.<GameProfile>getOne("player").orElse(null);
        InetAddress address = args.<InetAddress>getOne("ip").orElse(null);

        //Unban user + Send message
        BanService bs = Sponge.getServiceManager().provide(BanService.class).get();
        if (profile != null && bs.getBanFor(profile).isPresent()) {
            bs.removeBan(bs.getBanFor(profile).get());
            Messages.send(src, "ban.command.unban.success", "%player%", profile.getName().orElse(""));
            return CommandResult.success();
        }
        if (address != null && bs.getBanFor(address).isPresent()) {
            bs.removeBan(bs.getBanFor(address).get());
            Messages.send(src, "ban.command.unban.success-ip", "%ip%", address.toString().replace("/", ""));
            return CommandResult.success();
        }

        //Not banned
        throw Messages.error(src, "ban.command.unban.notbanned");
    }
}
