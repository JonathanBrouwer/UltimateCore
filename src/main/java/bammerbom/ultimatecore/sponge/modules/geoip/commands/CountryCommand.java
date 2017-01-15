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
package bammerbom.ultimatecore.sponge.modules.geoip.commands;

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.geoip.api.GeoipPermissions;
import bammerbom.ultimatecore.sponge.modules.geoip.handlers.GeoipHandler;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.Selector;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import com.maxmind.geoip2.record.Country;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Arrays;
import java.util.List;

public class CountryCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.GEOIP.get();
    }

    @Override
    public String getIdentifier() {
        return "country";
    }

    @Override
    public Permission getPermission() {
        return GeoipPermissions.UC_GEOIP_COUNTRY_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(GeoipPermissions.UC_GEOIP_COUNTRY_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("country");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(GeoipPermissions.UC_GEOIP_COUNTRY_BASE.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }

        Player t = Selector.one(sender, args[0]).orElse(null);
        if (t == null) {
            sender.sendMessage(Messages.getFormatted(sender, "core.playernotfound", "%player%", args[0]));
            return CommandResult.empty();
        }

        Country country = GeoipHandler.getCountry(t.getConnection().getAddress().getAddress()).orElse(null);
        if (country == null) {
            sender.sendMessage(Messages.getFormatted(sender, "geoip.command.country.failed", "%player%", VariableUtil.getNameSource(t)));
            return CommandResult.empty();
        }
        sender.sendMessage(Messages.getFormatted(sender, "geoip.command.country.success", "%player%", VariableUtil.getNameSource(t), "%country%", country.getName()));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
