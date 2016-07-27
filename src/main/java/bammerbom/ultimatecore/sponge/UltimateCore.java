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
package bammerbom.ultimatecore.sponge;

import bammerbom.ultimatecore.sponge.api.command.CommandService;
import bammerbom.ultimatecore.sponge.impl.command.UCCommandService;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.ModuleService;
import bammerbom.ultimatecore.sponge.impl.module.UCModuleService;
import bammerbom.ultimatecore.sponge.api.sign.SignService;
import bammerbom.ultimatecore.sponge.impl.sign.UCSignService;
import bammerbom.ultimatecore.sponge.api.user.UserService;
import bammerbom.ultimatecore.sponge.impl.user.UCUserService;
import bammerbom.ultimatecore.sponge.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.ServerID;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

@Plugin(id = "ultimatecore", name = "UltimateCore", version = "$version$", description = "All you need to set up a server and more!", url = "http://ultimatecore.org/index", authors =
        {"Bammerbom"})
public class UltimateCore {

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path dir;
    private static UltimateCore instance = null;
    @Inject
    private Logger logger;
    private ModuleService moduleService;
    private CommandService commandService;
    private SignService signService;
    private UserService userService;

    public static UltimateCore getInstance() {
        if (instance == null) {
            instance = new UltimateCore();
        }
        return instance;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent ev) {
        try {
            Long time = System.currentTimeMillis();
            //Save instance
            instance = this;
            //Load services
            moduleService = new UCModuleService();
            commandService = new UCCommandService();
            signService = new UCSignService();
            userService = new UCUserService();

            //Load modules
            Sponge.getServiceManager().setProvider(this, ModuleService.class, moduleService);
            File modfolder = new File(getDataFolder().toUri().getPath() + "modules");
            modfolder.mkdirs();
            for (File f : modfolder.listFiles()) {
                if (f.getName().endsWith(".jar") || f.getName().endsWith(".ucmodule")) {
                    Optional<Module> module = moduleService.load(f);
                    if (module.isPresent()) {
                        Messages.log(Text.of("Registered module: ", module.get().getIdentifier()));
                    } else {
                        Messages.log(Text.of("Module failed to load: " + f.getName()));
                    }
                }
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(Text.of(TextColors.GREEN, "Pre-initialized UltimateCore! (" + time + "ms)"));

        } catch (Exception ex) {
            ex.printStackTrace();
            //ErrorLogger.log(ex, "Failed to pre-initialize UltimateCore");
        }
    }

    @Listener
    public void onInit(GameInitializationEvent ev) {
        try {
            Long time = System.currentTimeMillis();
            //Initilaze utils
            //TODO here or in pre-init?
            ServerID.start();
            Messages.reloadMessages();
            //Initialize modules
            for (Module module : moduleService.getRegisteredModules()) {
                module.onInit(ev);
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(Text.of(TextColors.GREEN ,"Initialized UltimateCore! (" + time + "ms)"));
        } catch (Exception ex) {
            ex.printStackTrace();
            //ErrorLogger.log(ex, "Failed to initialize UltimateCore");
        }
    }

    @Listener
    public void onPostInit(GamePostInitializationEvent ev) {
        try {
            Long time = System.currentTimeMillis();
            //Post-initialize modules
            for (Module module : moduleService.getRegisteredModules()) {
                module.onPostInit(ev);
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(Text.of(TextColors.GREEN, "Post-initialized UltimateCore! (" + time + "ms)"));
        } catch (Exception ex) {
            ex.printStackTrace();
            //ErrorLogger.log(ex, "Failed to post-initialize UltimateCore");
        }
    }

    @Listener
    public void onGameStop(GameStoppingEvent ev) {
        try {
            Long time = System.currentTimeMillis();
            //Stop modules
            for (Module module : moduleService.getRegisteredModules()) {
                module.onStop(ev);
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(Text.of(TextColors.GREEN, "Stopped UltimateCore! (" + time + "ms)"));
        } catch (Exception ex) {
            ex.printStackTrace();
            //ErrorLogger.log(ex, "Failed to stop UltimateCore");
        }
    }

    public Path getDataFolder() {
        return dir;
    }

    //TODO move these methods???
    public ModuleService getModuleService() {
        return moduleService;
    }

    public CommandService getCommandService() {
        return commandService;
    }

    public SignService getSignService() {
        return signService;
    }

    public UserService getUserService() {
        return userService;
    }
}