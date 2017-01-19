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
package bammerbom.ultimatecore.sponge.api.command.arguments;

import bammerbom.ultimatecore.sponge.api.command.UCommandElement;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BlocktypesArgument extends UCommandElement {
    public BlocktypesArgument(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    public Set<BlockType> parseValue(CommandSource sender, CommandArgs args) throws ArgumentParseException {
        Set<BlockType> types = new HashSet<>();
        while (args.hasNext()) {
            String value = args.next();
            Optional<BlockType> type = Sponge.getRegistry().getType(CatalogTypes.BLOCK_TYPE, value);
            if (!type.isPresent()) {
                throw args.createError(Messages.getFormatted(sender, "item.blocknotfound", "%type%", value));
            }
            types.add(type.get());
        }
        return types;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return Sponge.getRegistry().getAllOf(CatalogTypes.BLOCK_TYPE).stream().map(CatalogType::getId).collect(Collectors.toList());
    }
}
