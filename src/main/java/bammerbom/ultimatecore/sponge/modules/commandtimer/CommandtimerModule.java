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
package bammerbom.ultimatecore.sponge.modules.commandtimer;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.CommandsConfig;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.RawModuleConfig;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.variable.utils.TimeUtil;
import bammerbom.ultimatecore.sponge.modules.commandtimer.api.CommandtimerKeys;
import bammerbom.ultimatecore.sponge.modules.commandtimer.api.CommandtimerPermissions;
import bammerbom.ultimatecore.sponge.modules.commandtimer.listeners.CommandtimerCancelListener;
import bammerbom.ultimatecore.sponge.modules.commandtimer.listeners.CommandtimerListener;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.Optional;

public class CommandtimerModule implements Module {
    ModuleConfig config;

    @Override
    public String getIdentifier() {
        return "commandtimer";
    }

    @Override
    public Text getDescription() {
        return Text.of("Allows you to set cooldowns and teleport warmups.");
    }

    @Override
    public Optional<? extends ModuleConfig> getConfig() {
        return Optional.of(config);
    }

    @Override
    public void onInit(GameInitializationEvent event) {
        config = new RawModuleConfig("commandtimer");
        onReload(null);
        Sponge.getEventManager().registerListeners(UltimateCore.get(), new CommandtimerListener());
        CommandtimerCancelListener.registerEvents();
    }

    @Override
    public void onPostInit(GamePostInitializationEvent event) {
        CommandsConfig config = UltimateCore.get().getCommandsConfig();
        CommentedConfigurationNode node = config.get();
        boolean modified = false;
        for (Command cmd : UltimateCore.get().getCommandService().getCommands()) {
            CommentedConfigurationNode cmdnode = node.getNode("commands", cmd.getIdentifier());
            if (cmdnode.getNode("cooldown").getValue() == null) {
                modified = true;
                cmdnode.getNode("cooldown").setComment("Time in seconds that a player has to wait between uses of the command. (0 to disable)");
                cmdnode.getNode("cooldown").setValue("0s");
            }
            if (cmdnode.getNode("warmup").getValue() == null) {
                modified = true;
                cmdnode.getNode("warmup").setComment("Time in seconds that a player has to wait before the command is executed. (0 to disable)");
                cmdnode.getNode("warmup").setValue("0s");
            }
        }
        if (modified) {
            config.save(node);
        }
        onReload(null);
        new CommandtimerPermissions();
    }

    @Override
    public void onReload(GameReloadEvent event) {
        HashMap<Command, Long> cooldowns = new HashMap<>();
        HashMap<Command, Long> warmups = new HashMap<>();

        CommandsConfig config = UltimateCore.get().getCommandsConfig();
        CommentedConfigurationNode node = config.get();

        for (Command cmd : UltimateCore.get().getCommandService().getCommands()) {
            CommentedConfigurationNode cmdnode = node.getNode("commands", cmd.getIdentifier());
            if (!cmdnode.getNode("cooldown").isVirtual()) {
                cooldowns.put(cmd, TimeUtil.parse(cmdnode.getNode("cooldown").getString()));
            }
            if (!cmdnode.getNode("warmup").isVirtual()) {
                warmups.put(cmd, TimeUtil.parse(cmdnode.getNode("warmup").getString()));
            }
        }

        GlobalData.offer(CommandtimerKeys.COOLDOWNS, cooldowns);
        GlobalData.offer(CommandtimerKeys.WARMUPS, warmups);
    }
}
