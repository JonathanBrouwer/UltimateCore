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
import bammerbom.ultimatecore.sponge.api.event.module.ModuleInitializeEvent;
import bammerbom.ultimatecore.sponge.api.event.module.ModulePostInitializeEvent;
import bammerbom.ultimatecore.sponge.api.event.module.ModuleStoppingEvent;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.ModuleService;
import bammerbom.ultimatecore.sponge.api.permission.PermissionService;
import bammerbom.ultimatecore.sponge.api.sign.SignService;
import bammerbom.ultimatecore.sponge.api.user.UserService;
import bammerbom.ultimatecore.sponge.config.CommandsConfig;
import bammerbom.ultimatecore.sponge.config.GeneralConfig;
import bammerbom.ultimatecore.sponge.config.ModulesConfig;
import bammerbom.ultimatecore.sponge.config.serializers.ExtendedLocationSerializer;
import bammerbom.ultimatecore.sponge.defaultmodule.DefaultModule;
import bammerbom.ultimatecore.sponge.impl.command.UCCommandService;
import bammerbom.ultimatecore.sponge.impl.module.UCModuleService;
import bammerbom.ultimatecore.sponge.impl.permission.UCPermissionService;
import bammerbom.ultimatecore.sponge.impl.sign.UCSignService;
import bammerbom.ultimatecore.sponge.impl.user.UCUserService;
import bammerbom.ultimatecore.sponge.utils.ExtendedLocation;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.ServerID;
import bammerbom.ultimatecore.sponge.utils.Stats;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ServiceManager;

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

    @Listener(order = Order.LATE)
    public void onPreInit(GamePreInitializationEvent ev) {
        try {
            Long time = System.currentTimeMillis();
            //Save instance
            instance = this;
            //Load utils
            ServerID.start();
            Messages.reloadEnglishMessages();
            TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(ExtendedLocation.class), new ExtendedLocationSerializer());
            GeneralConfig.reload();
            CommandsConfig.preload();
            ModulesConfig.preload();
            Messages.reloadCustomMessages();
            //Load services
            moduleService = new UCModuleService();
            commandService = new UCCommandService();
            signService = new UCSignService();
            userService = new UCUserService();
            permissionService = new UCPermissionService();

            //Load modules
            Sponge.getServiceManager().setProvider(this, ModuleService.class, moduleService);
            moduleService.registerModule(new DefaultModule());
            File modfolder = new File("ultimatecore/modules");
            modfolder.mkdirs();
            for (File f : modfolder.listFiles()) {
                if (f.getName().endsWith(".jar") || f.getName().endsWith(".ucmodule")) {
                    Optional<Module> module = moduleService.load(f);
                    if (module.isPresent()) {
                        if (moduleService.registerModule(module.get())) {
                            Messages.log(Messages.getFormatted("core.load.module.registered", "%module%", module.get().getIdentifier()));
                        } else {
                            Messages.log(Messages.getFormatted("core.load.module.blocked", "%module%", module.get().getIdentifier()));
                        }
                    } else {
                        Messages.log(Messages.getFormatted("core.load.module.failed", "%module%", f.getName()));
                    }
                }
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(Messages.getFormatted("core.load.preinit", "%ms%", time));
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
            //Load configuration
            //TODO here or in separate class for config loading?

            //Initialize modules
            for (Module module : moduleService.getRegisteredModules()) {
                ModuleInitializeEvent event = new ModuleInitializeEvent(module, ev, Cause.builder().owner(this).build());
                Sponge.getEventManager().post(event);
                module.onInit(ev);
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(Messages.getFormatted("core.load.init", "%ms%", time));
        } catch (Exception ex) {
            ex.printStackTrace();
            //ErrorLogger.log(ex, "Failed to initialize UltimateCore");
        }
    }

    @Listener
    public void onPostInit(GamePostInitializationEvent ev) {
        try {
            Long time = System.currentTimeMillis();
            //All commands should be registered by now
            CommandsConfig.postload();
            ModulesConfig.postload();
            //Send stats
            Stats.start();
            //Post-initialize modules
            for (Module module : moduleService.getRegisteredModules()) {
                ModulePostInitializeEvent event = new ModulePostInitializeEvent(module, ev, Cause.builder().owner(this).build());
                Sponge.getEventManager().post(event);
                module.onPostInit(ev);
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(Messages.getFormatted("core.load.postinit", "%ms%", time));
        } catch (Exception ex) {
            ex.printStackTrace();
            //ErrorLogger.log(ex, "Failed to post-initialize UltimateCore");
        }
    }

    @Listener
    public void onStopping(GameStoppingEvent ev) {
        try {
            Long time = System.currentTimeMillis();
            //Stop modules
            for (Module module : moduleService.getRegisteredModules()) {
                ModuleStoppingEvent event = new ModuleStoppingEvent(module, ev, Cause.builder().owner(this).build());
                Sponge.getEventManager().post(event);
                module.onStop(ev);
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(Messages.getFormatted("core.load.stop", "%ms%", time));
        } catch (Exception ex) {
            ex.printStackTrace();
            //ErrorLogger.log(ex, "Failed to stop UltimateCore");
        }
    }

    public Path getConfigFolder() {
        return dir;
    }

    public Path getDataFolder() {
        return new File("ultimatecore").toPath();
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