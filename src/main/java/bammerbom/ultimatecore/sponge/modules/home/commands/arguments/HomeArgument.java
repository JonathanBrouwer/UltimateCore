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
package bammerbom.ultimatecore.sponge.modules.home.commands.arguments;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.argument.UCommandElement;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.home.api.Home;
import bammerbom.ultimatecore.sponge.modules.home.api.HomeKeys;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeArgument extends UCommandElement {
    public HomeArgument(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    public Home parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String value = args.next();
        if (!(src instanceof Player)) {
            //This shouldnt happen
            return null;
        }
        Player p = (Player) src;
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        List<Home> homes = up.get(HomeKeys.HOMES).get();
        for (Home home : homes) {
            if (value.equalsIgnoreCase(home.getName())) {
                return home;
            }
        }
        throw args.createError(Messages.getFormatted(src, "home.command.home.notfound", "%home%", value.toLowerCase()));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        if (!(src instanceof Player)) {
            //This shouldnt happen
            return new ArrayList<>();
        }
        Player p = (Player) src;
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        List<Home> homes = up.get(HomeKeys.HOMES).get();
        return homes.stream().map(home -> home.getName()).collect(Collectors.toList());
    }
}
