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

import bammerbom.ultimatecore.sponge.api.command.impl.UCCommandService;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.ModuleService;
import bammerbom.ultimatecore.sponge.api.sign.SignService;
import bammerbom.ultimatecore.sponge.api.user.UserService;
import bammerbom.ultimatecore.sponge.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.utils.Messages;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.util.Optional;
import java.util.logging.Logger;

@Plugin(id = "ultimatecore", name = "UltimateCore", version = "${project.version}", description = "All you need to set up a server and more!")
public class UltimateCore {

    @ConfigDir(sharedRoot = false)
    public static File dir;
    private static UltimateCore instance = null;
    @Inject
    private Logger logger;
    private ModuleService moduleService;
    private UCCommandService commandService;
    private SignService signService;
    private UserService userService;

    private UltimateCore() {
    }

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
            //Load core messages
            Messages.reloadMessages();
            //Load services
            moduleService = new ModuleService();
            commandService = new UCCommandService();
            signService = new SignService();
            userService = new UserService();

            //Load modules
            Sponge.getServiceManager().setProvider(this, ModuleService.class, moduleService);
            File modfolder = new File(getDataFolder().toURI() + "/modules");
            modfolder.mkdirs();
            for (File f : modfolder.listFiles()) {
                if (f.getName().endsWith(".jar") || f.getName().endsWith(".ucmodule")) {
                    Optional<Module> module = moduleService.load(f);
                    if (module.isPresent()) {
                        Messages.log("Registered module: " + module.get().getIdentifier());
                    } else {
                        Messages.log("Module failed to load: " + f.getName());
                    }
                }
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(TextColors.GREEN + "Pre-initialized UltimateCore! (" + time + "ms)");

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to pre-initialize UltimateCore");
        }
    }

    @Listener
    public void onInit(GameInitializationEvent ev) {
        try {
            Long time = System.currentTimeMillis();
            //Initialize modules
            for (Module module : moduleService.getRegisteredModules()) {
                module.onInit(ev);
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(TextColors.GREEN + "Initialized UltimateCore! (" + time + "ms)");
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to initialize UltimateCore");
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
            Messages.log(TextColors.GREEN + "Post-initialized UltimateCore! (" + time + "ms)");
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to post-initialize UltimateCore");
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
            Messages.log(TextColors.GREEN + "Stopped UltimateCore! (" + time + "ms)");
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to stop UltimateCore");
        }
    }

    public File getDataFolder() {
        return dir;
    }

    //TODO move these methods???
    public ModuleService getModuleService() {
        return moduleService;
    }

    public UCCommandService getCommandService() {
        return commandService;
    }

    public SignService getSignService() {
        return signService;
    }

    public UserService getUserService() {
        return userService;
    }
}