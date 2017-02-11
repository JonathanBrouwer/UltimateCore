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
package bammerbom.ultimatecore.sponge.modules.world.commands.world;

import bammerbom.ultimatecore.sponge.api.command.HighSubCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandParentInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.*;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.modules.world.WorldModule;
import bammerbom.ultimatecore.sponge.modules.world.commands.WorldCommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.*;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

@CommandInfo(module = WorldModule.class, aliases = {"create", "add"})
@CommandParentInfo(parent = WorldCommand.class)
public class CreateWorldCommand implements HighSubCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new StringArgument(Text.of("name"))).onlyOne().build(),
                Arguments.builder(GenericArguments.flags()
                        .valueFlag(new DimensionArgument(Text.of("dimension")), "d", "-dim", "-dimension")
                        .valueFlag(new GeneratorTypeArgument(Text.of("generator")), "g", "-gen", "-generator")
                        .valueFlag(new WorldGeneratorModifierArgument(Text.of("wgm")), "-wgm", "-wg", "-worldgenerator", "-worldgeneratormodifier")
                        .valueFlag(GenericArguments.longNum(Text.of("seed")), "s", "-seed")
                        .valueFlag(new GamemodeArgument(Text.of("gamemode")), "-gm", "-gamemode")
                        .valueFlag(new DifficultyArgument(Text.of("difficulty")), "-dif", "-difficulty")
                        .valueFlag(new BooleanArgument(Text.of("n")), "n", "-nostructures")
                        .valueFlag(new BooleanArgument(Text.of("l")), "l", "-loadonstartup")
                        .valueFlag(new BooleanArgument(Text.of("k")), "k", "-keepspawnloaded")
                        .valueFlag(new BooleanArgument(Text.of("c")), "c", "-allowcommands")
                        .valueFlag(new BooleanArgument(Text.of("b")), "b", "-bonuschest")
                        .buildWith(GenericArguments.none())).build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        String name = (String) args.getOne("name").get();
        Optional<DimensionType> dimension = args.getOne("dimension");
        Optional<GeneratorType> generator = args.getOne("generator");
        Collection<WorldGeneratorModifier> wgm = args.getAll("wgm");
        Optional<Long> seed = args.getOne("seed");
        Optional<GameMode> gm = args.getOne("gamemode");
        Optional<Difficulty> diff = args.getOne("difficulty");

        boolean nostructures = args.hasAny("n");
        boolean load = args.<Boolean>getOne("l").orElse(true);
        boolean keepspawnloaded = args.<Boolean>getOne("k").orElse(true);
        boolean allowcommands = args.<Boolean>getOne("c").orElse(true);
        boolean bonuschest = args.<Boolean>getOne("b").orElse(true);

        Path path = Sponge.getGame().getGameDirectory();
        if (Files.exists(path.resolve(name.toLowerCase())) || Files.exists(path.resolve(name)) || Sponge.getServer().getAllWorldProperties().stream().anyMatch(x -> x.getWorldName().equalsIgnoreCase(name))) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "world.exists", "%name%", name));
        }

        Messages.send(sender, "world.command.world.create.starting", "%name%", name);

        WorldArchetype.Builder archetype = WorldArchetype.builder().enabled(true);
        dimension.ifPresent(archetype::dimension);
        generator.ifPresent(archetype::generator);
        archetype.generatorModifiers(wgm.toArray(new WorldGeneratorModifier[wgm.size()]));
        seed.ifPresent(archetype::seed);
        gm.ifPresent(archetype::gameMode);
        diff.ifPresent(archetype::difficulty);
        archetype.usesMapFeatures(!nostructures);
        archetype.loadsOnStartup(load);
        archetype.keepsSpawnLoaded(keepspawnloaded);
        archetype.commandsAllowed(allowcommands);
        archetype.generateBonusChest(bonuschest);

        WorldProperties props;
        try {
            props = Sponge.getServer().createWorldProperties(name.toLowerCase(), archetype.build(name.toLowerCase(), name));
        } catch (IOException e) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "world.command.world.create.fileerror", "%error%", e.getMessage()));
        }
        Sponge.getServer().saveWorldProperties(props);
        World world = Sponge.getServer().loadWorld(props).orElseThrow(() -> new ErrorMessageException(Messages.getFormatted(sender, "world.command.world.create.loaderror")));
        Messages.send(sender, "world.command.world.create.success", "%name%", world.getName());
        return CommandResult.success();
    }
}
