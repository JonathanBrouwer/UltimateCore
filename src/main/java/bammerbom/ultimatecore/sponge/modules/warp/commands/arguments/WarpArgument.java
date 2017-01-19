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
package bammerbom.ultimatecore.sponge.modules.warp.commands.arguments;

import bammerbom.ultimatecore.sponge.api.command.UCommandElement;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.modules.warp.api.Warp;
import bammerbom.ultimatecore.sponge.modules.warp.api.WarpKeys;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class WarpArgument extends UCommandElement {
    public WarpArgument(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    public Warp parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String value = args.next();

        List<Warp> warps = GlobalData.get(WarpKeys.WARPS).get();
        for (Warp warp : warps) {
            if (value.equalsIgnoreCase(warp.getName())) {
                return warp;
            }
        }
        throw args.createError(Messages.getFormatted(src, "warp.notfound", "%warp%", value.toLowerCase()));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        List<Warp> warps = GlobalData.get(WarpKeys.WARPS).get();
        return warps.stream().map(warp -> warp.getName()).collect(Collectors.toList());
    }
}
