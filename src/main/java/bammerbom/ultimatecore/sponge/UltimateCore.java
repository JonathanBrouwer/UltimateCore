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
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.ModuleService;
import bammerbom.ultimatecore.sponge.api.permission.PermissionService;
import bammerbom.ultimatecore.sponge.api.sign.SignService;
import bammerbom.ultimatecore.sponge.api.user.UserService;
import bammerbom.ultimatecore.sponge.impl.command.UCCommandService;
import bammerbom.ultimatecore.sponge.impl.module.UCModuleService;
import bammerbom.ultimatecore.sponge.impl.permission.UCPermissionService;
import bammerbom.ultimatecore.sponge.impl.sign.UCSignService;
import bammerbom.ultimatecore.sponge.impl.user.UCUserService;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.ServerID;
import bammerbom.ultimatecore.sponge.utils.Stats;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

@Plugin(id = "ultimatecore", name = "UltimateCore", version = "@VERSION@", description = "All you need to set up a server and more!", url = "http://ultimatecore.org/index", authors =
        {"Bammerbom"})
public class UltimateCore {

    private static UltimateCore instance = null;
    @Inject
    @ConfigDir(sharedRoot = false)
    public Path dir;
    @Inject
    private Logger logger;
    private ModuleService moduleService;
    private CommandService commandService;
    private SignService signService;
    private UserService userService;
    private PermissionService permissionService;

    public static UltimateCore get() {
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
            permissionService = new UCPermissionService();

            //Load modules
            Sponge.getServiceManager().setProvider(this, ModuleService.class, moduleService);
            File modfolder = new File("ultimatecore/modules");
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
            //Register services
            ServiceManager sm = Sponge.getServiceManager();
            sm.setProvider(this, ModuleService.class, moduleService);
            sm.setProvider(this, CommandService.class, commandService);
            sm.setProvider(this, SignService.class, signService);
            sm.setProvider(this, UserService.class, userService);
            sm.setProvider(this, PermissionService.class, permissionService);
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
            Messages.log(Text.of(TextColors.GREEN, "Initialized UltimateCore! (" + time + "ms)"));
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
            //Send stats
            Stats.start();
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

    public PermissionService getPermissionService() {
        return permissionService;
    }
}