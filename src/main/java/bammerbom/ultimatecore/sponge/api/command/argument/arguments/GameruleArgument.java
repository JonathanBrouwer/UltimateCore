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
package bammerbom.ultimatecore.sponge.api.command.argument.arguments;

import bammerbom.ultimatecore.sponge.api.command.argument.UCommandElement;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.gamerule.DefaultGameRules;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class GameruleArgument extends UCommandElement {
    public GameruleArgument(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        return args.next();
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return Arrays.asList(
                DefaultGameRules.COMMAND_BLOCK_OUTPUT,
                DefaultGameRules.DISABLE_ELYTRA_MOVEMENT_CHECK,
                DefaultGameRules.DO_DAYLIGHT_CYCLE,
                DefaultGameRules.DO_ENTITY_DROPS,
                DefaultGameRules.DO_FIRE_TICK,
                DefaultGameRules.DO_MOB_LOOT,
                DefaultGameRules.DO_MOB_SPAWNING,
                DefaultGameRules.DO_TILE_DROPS,
                DefaultGameRules.DO_WEATHER_CYCLE,
                DefaultGameRules.KEEP_INVENTORY,
                DefaultGameRules.LOG_ADMIN_COMMANDS,
                DefaultGameRules.MAX_ENTITY_CRAMMING,
                DefaultGameRules.MOB_GRIEFING,
                DefaultGameRules.NATURAL_REGENERATION,
                DefaultGameRules.RANDOM_TICK_SPEED,
                DefaultGameRules.REDUCED_DEBUG_INFO,
                DefaultGameRules.SEND_COMMAND_FEEDBACK,
                DefaultGameRules.SHOW_DEATH_MESSAGES,
                DefaultGameRules.SPAWN_RADIUS,
                DefaultGameRules.SPECTATORS_GENERATE_CHUNKS
        );
    }
}
