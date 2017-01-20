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
import bammerbom.ultimatecore.sponge.api.teleport.TeleportService;
import bammerbom.ultimatecore.sponge.api.tick.TickService;
import bammerbom.ultimatecore.sponge.api.user.UserService;
import bammerbom.ultimatecore.sponge.api.variable.VariableService;
import bammerbom.ultimatecore.sponge.config.CommandsConfig;
import bammerbom.ultimatecore.sponge.config.GeneralConfig;
import bammerbom.ultimatecore.sponge.config.ModulesConfig;
import bammerbom.ultimatecore.sponge.config.serializers.ItemStackSnapshotSerializer;
import bammerbom.ultimatecore.sponge.config.serializers.TransformSerializer;
import bammerbom.ultimatecore.sponge.config.serializers.Vector3dSerializer;
import bammerbom.ultimatecore.sponge.impl.command.UCCommandService;
import bammerbom.ultimatecore.sponge.impl.module.UCModuleService;
import bammerbom.ultimatecore.sponge.impl.permission.UCPermissionService;
import bammerbom.ultimatecore.sponge.impl.teleport.UCTeleportService;
import bammerbom.ultimatecore.sponge.impl.tick.UCTickService;
import bammerbom.ultimatecore.sponge.impl.user.UCUserService;
import bammerbom.ultimatecore.sponge.impl.variable.UCVariableService;
import bammerbom.ultimatecore.sponge.utils.*;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ServiceManager;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

@Plugin(id = "ultimatecore", name = "UltimateCore", version = "@VERSION@", description = "All you need to set up a server and more!", url = "http://ultimatecore.org/index", authors = {"Bammerbom"})
public class UltimateCore {

    private static UltimateCore instance = null;

    //Config files
    private GeneralConfig generalConfig;
    private CommandsConfig commandsConfig;
    private ModulesConfig modulesConfig;

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path dir;
    @Inject
    private Logger logger;

    //Did uc start yet?
    boolean started = false;

    //Bstats
    @Inject
    private Metrics metrics;

    public static UltimateCore get() {
        if (instance == null) {
            instance = new UltimateCore();
        }
        return instance;
    }

    @Listener(order = Order.LATE)
    public void onPreInit(GamePreInitializationEvent ev) {
        try {
            if (Sponge.getPlatform().getType().equals(Platform.Type.CLIENT)) {
                return;
            }
            Long time = System.currentTimeMillis();
            //Save instance
            instance = this;
            //Load utils
            ServerID.start();
            Messages.reloadEnglishMessages();

            //Register serializers because sponge doesn't for some reason
            TypeSerializerCollection serializers = TypeSerializers.getDefaultSerializers();
//            serializers.registerType(TypeToken.of(BlockState.class), new BlockStateSerializer());
            serializers.registerType(TypeToken.of(ItemStackSnapshot.class), new ItemStackSnapshotSerializer());
            serializers.registerType(TypeToken.of(Transform.class), new TransformSerializer());
            serializers.registerType(TypeToken.of(Vector3d.class), new Vector3dSerializer());

            //Config
            generalConfig = new GeneralConfig();
            generalConfig.reload();
            commandsConfig = new CommandsConfig();
            commandsConfig.preload();
            modulesConfig = new ModulesConfig();
            modulesConfig.preload();
            Messages.reloadCustomMessages();

            //Load services
            UCModuleService moduleService = new UCModuleService();
            UCCommandService commandService = new UCCommandService();
            UCUserService userService = new UCUserService();
            UCPermissionService permissionService = new UCPermissionService();
            UCTeleportService teleportService = new UCTeleportService();
            UCTickService tickService = new UCTickService();
            tickService.init();
            UCVariableService variableService = new UCVariableService();
            variableService.init();

            //Register services
            ServiceManager sm = Sponge.getServiceManager();
            sm.setProvider(this, ModuleService.class, moduleService);
            sm.setProvider(this, CommandService.class, commandService);
            sm.setProvider(this, UserService.class, userService);
            sm.setProvider(this, PermissionService.class, permissionService);
            sm.setProvider(this, TeleportService.class, teleportService);
            sm.setProvider(this, TickService.class, tickService);
            sm.setProvider(this, VariableService.class, variableService);

            //Load modules
            for (Module module : moduleService.findModules()) {
                try {
                    if (moduleService.registerModule(module)) {
                        if (!module.getIdentifier().equals("default")) {
                            Messages.log(Messages.getFormatted("core.load.module.registered", "%module%", module.getIdentifier()));
                        }
                    }
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "Failed to register module " + module.getIdentifier());
                }
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(Messages.getFormatted("core.load.preinit", "%ms%", time));
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorLogger.log(ex, "Failed to pre-initialize UltimateCore");
        }
    }

    @Listener
    public void onInit(GameInitializationEvent ev) {
        try {
            if (Sponge.getPlatform().getType().equals(Platform.Type.CLIENT)) {
                System.out.println("[UC] You are running UC on a client. Waiting with starting the game until getServer() is available.");
                return;
            }
            started = true;
            Long time = System.currentTimeMillis();

            //Initialize modules
            for (Module module : getModuleService().getModules()) {
                try {
                    ModuleInitializeEvent event = new ModuleInitializeEvent(module, ev, Cause.builder().owner(this).build());
                    Sponge.getEventManager().post(event);
                    module.onInit(ev);
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "Failed to initialize module " + module.getIdentifier());
                }
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(Messages.getFormatted("core.load.init", "%ms%", time));
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to initialize UltimateCore");
        }
    }

    @Listener
    public void onPostInit(GamePostInitializationEvent ev) {
        try {
            if (Sponge.getPlatform().getType().equals(Platform.Type.CLIENT)) {
                return;
            }
            Long time = System.currentTimeMillis();
            //All commands should be registered by now
            commandsConfig.postload();
            modulesConfig.postload();
            //Add custom bstats charts
            metrics.addCustomChart(new Metrics.AdvancedPie("modules") {
                @Override
                public HashMap<String, Integer> getValues(HashMap<String, Integer> valueMap) {
                    HashMap<String, Integer> modules = new HashMap<>();
                    UltimateCore.get().getModuleService().getModules().forEach(module -> {
                        modules.put(module.getIdentifier(), 1);
                    });
                    return modules;
                }
            });
            metrics.addCustomChart(new Metrics.SimplePie("language") {
                @Override
                public String getValue() {
                    return getGeneralConfig().get().getNode("language", "language").getString();
                }
            });
            metrics.addCustomChart(new Metrics.SimplePie("platform") {
                @Override
                public String getValue() {
                    return StringUtil.firstUpperCase(Sponge.getPlatform().getType().name());
                }
            });
            metrics.addCustomChart(new Metrics.SimplePie("implementation") {
                @Override
                public String getValue() {
                    return Sponge.getPlatform().getContainer(Platform.Component.IMPLEMENTATION).getName();
                }
            });
            //Post-initialize modules
            for (Module module : getModuleService().getModules()) {
                try {
                    ModulePostInitializeEvent event = new ModulePostInitializeEvent(module, ev, Cause.builder().owner(this).build());
                    Sponge.getEventManager().post(event);
                    module.onPostInit(ev);
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "Failed to post initialize module " + module.getIdentifier());
                }
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(Messages.getFormatted("core.load.postinit", "%ms%", time));
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to post-initialize UltimateCore");
        }
    }

    @Listener
    public void onStart(GameStartingServerEvent ev) {
        //On a client, wait until the Sponge.getServer() is available and then load UC
        if (Sponge.getPlatform().getType().equals(Platform.Type.CLIENT) && !started) {
            onPreInit(null);
            onInit(null);
            onPostInit(null);
            return;
        }
    }

    @Listener
    public void onStopping(GameStoppingEvent ev) {
        try {
            Long time = System.currentTimeMillis();
            //Stop modules
            for (Module module : getModuleService().getModules()) {
                try {
                    ModuleStoppingEvent event = new ModuleStoppingEvent(module, ev, Cause.builder().owner(this).build());
                    Sponge.getEventManager().post(event);
                    module.onStop(ev);
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "Failed to stop module " + module.getIdentifier());
                }
            }
            //
            time = System.currentTimeMillis() - time;
            Messages.log(Messages.getFormatted("core.load.stop", "%ms%", time));
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to stop UltimateCore");
        }
    }

    @Listener
    public void onStart(GameStartedServerEvent ev) {
        getCommandService().registerLateCommands();
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        try {
            Long time = System.currentTimeMillis();
            UltimateCore.get().getGeneralConfig().reload();
            UltimateCore.get().getCommandsConfig().preload();
            UltimateCore.get().getCommandsConfig().postload();
            UltimateCore.get().getModulesConfig().preload();
            UltimateCore.get().getModulesConfig().postload();

            for (Module mod : UltimateCore.get().getModuleService().getModules()) {
                if (mod.getConfig().isPresent()) {
                    mod.getConfig().get().reload();
                    mod.onReload(event);
                }
            }

            Messages.reloadMessages();

            time = System.currentTimeMillis() - time;
            Messages.log(Messages.getFormatted("core.load.reload", "%ms%", time));
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to stop UltimateCore");
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
        ServiceManager manager = Sponge.getServiceManager();
        return manager.provide(ModuleService.class).orElse(null);
    }

    public CommandService getCommandService() {
        ServiceManager manager = Sponge.getServiceManager();
        return manager.provide(CommandService.class).orElse(null);
    }

    public UserService getUserService() {
        ServiceManager manager = Sponge.getServiceManager();
        return manager.provide(UserService.class).orElse(null);
    }

    public PermissionService getPermissionService() {
        ServiceManager manager = Sponge.getServiceManager();
        return manager.provide(PermissionService.class).orElse(null);
    }

    public TeleportService getTeleportService() {
        ServiceManager manager = Sponge.getServiceManager();
        return manager.provide(TeleportService.class).orElse(null);
    }

    public TickService getTickService() {
        ServiceManager manager = Sponge.getServiceManager();
        return manager.provide(TickService.class).orElse(null);
    }

    public VariableService getVariableService() {
        ServiceManager manager = Sponge.getServiceManager();
        return manager.provide(VariableService.class).orElse(null);
    }

    public Optional<SignService> getSignService() {
        ServiceManager manager = Sponge.getServiceManager();
        return manager.provide(SignService.class);
    }

    //Get configs
    public GeneralConfig getGeneralConfig() {
        return generalConfig;
    }

    public CommandsConfig getCommandsConfig() {
        return commandsConfig;
    }

    public ModulesConfig getModulesConfig() {
        return modulesConfig;
    }
}