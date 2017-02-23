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
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.RemainingStringsArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.TimeArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.datafiles.PlayerDataFile;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.variable.utils.TimeUtil;
import bammerbom.ultimatecore.sponge.modules.ban.BanModule;
import bammerbom.ultimatecore.sponge.modules.ban.api.BanPermissions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.ban.Ban;
import org.spongepowered.api.util.ban.BanTypes;

import java.net.InetAddress;
import java.time.Instant;

@CommandPermissions(level = PermissionLevel.MOD)
@CommandInfo(module = BanModule.class, aliases = {"ban", "banip", "ipban", "bantemp", "tempban"})
public class BanCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new GameprofileArgument(Text.of("player")), new IpAdressArgument(Text.of("ip"))).onlyOne().usage("<Player/IP>").build(),
                Arguments.builder(new TimeArgument(Text.of("time"))).optionalWeak().build(),
                Arguments.builder(new RemainingStringsArgument(Text.of("reason"))).optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        GameProfile profile = args.<GameProfile>getOne("player").orElse(null);
        InetAddress address = args.<InetAddress>getOne("ip").orElse(null);

        Long time = args.<Long>getOne("time").orElse(-1L);
        Text reason = args.<String>getOne("reason").map(Messages::toText).orElse(Messages.getFormatted(src, "ban.command.ban.defaultreason"));

        //Try to find user
        User user = null;
        if (profile != null) {
            user = Sponge.getServiceManager().provide(UserStorageService.class).get().get(profile).get();
        } else {
            //Try to find user from ip address
            for (GameProfile prof : Sponge.getServer().getGameProfileManager().getCache().getProfiles()) {
                PlayerDataFile config = new PlayerDataFile(prof.getUniqueId());
                CommentedConfigurationNode node = config.get();
                if (node.getNode("lastip").getString("").equalsIgnoreCase(address.toString().replace("/", ""))) {
                    user = Sponge.getServiceManager().provide(UserStorageService.class).get().get(prof).get();
                }
            }
        }

        //If user is present, check exempt
        if (user != null) {
            if ((BanPermissions.UC_BAN_EXEMPTPOWER.getIntFor(user) > BanPermissions.UC_BAN_POWER.getIntFor(src)) && src instanceof Player) {
                throw new ErrorMessageException(Messages.getFormatted(src, "ban.command.ban.exempt", "%player%", user));
            }
        }

        //Ban user
        BanService bs = Sponge.getServiceManager().provide(BanService.class).get();
        Ban.Builder bb = Ban.builder();
        if (profile != null) {
            bb = bb.type(BanTypes.PROFILE).profile(profile);
        } else {
            bb = bb.type(BanTypes.IP).address(address);
        }
        bb = bb.source(src).startDate(Instant.now());
        if (time > 0) bb = bb.expirationDate(Instant.now().plusMillis(time));
        bb = bb.reason(reason);
        bs.addBan(bb.build());

        //Kick player
        if (user != null && user.getPlayer().isPresent()) {
            if (profile != null) {
                user.getPlayer().get().kick(Messages.getFormatted(user.getPlayer().get(), "ban.banned", "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", reason));
            } else {
                user.getPlayer().get().kick(Messages.getFormatted(user.getPlayer().get(), "ban.ipbanned", "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", reason));
            }
        }

        //Send message
        if (profile != null) {
            Messages.send(src, "ban.command.ban.success", "%player%", profile.getName(), "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", reason);
        } else {
            Messages.send(src, "ban.command.ban.success-ip", "%ip%", address.toString().replace("/", ""), "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", reason);
        }
        return CommandResult.success();
    }
}
