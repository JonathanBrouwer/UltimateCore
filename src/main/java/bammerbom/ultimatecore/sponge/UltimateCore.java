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
import bammerbom.ultimatecore.sponge.api.command.impl.UCCommandService;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.CommandsConfig;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.GeneralConfig;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.ModulesConfig;
import bammerbom.ultimatecore.sponge.api.config.serializers.ItemStackSnapshotSerializer;
import bammerbom.ultimatecore.sponge.api.config.serializers.LocationSerializer;
import bammerbom.ultimatecore.sponge.api.config.serializers.TransformSerializer;
import bammerbom.ultimatecore.sponge.api.config.serializers.Vector3dSerializer;
import bammerbom.ultimatecore.sponge.api.error.ErrorService;
import bammerbom.ultimatecore.sponge.api.error.impl.UCErrorService;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.api.language.LanguageService;
import bammerbom.ultimatecore.sponge.api.language.impl.UCLanguageService;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.ModuleService;
import bammerbom.ultimatecore.sponge.api.module.event.ModuleInitializeEvent;
import bammerbom.ultimatecore.sponge.api.module.event.ModulePostInitializeEvent;
import bammerbom.ultimatecore.sponge.api.module.event.ModuleStoppingEvent;
import bammerbom.ultimatecore.sponge.api.module.impl.UCModuleService;
import bammerbom.ultimatecore.sponge.api.permission.PermissionService;
import bammerbom.ultimatecore.sponge.api.permission.impl.UCPermissionService;
import bammerbom.ultimatecore.sponge.api.sign.SignService;
import bammerbom.ultimatecore.sponge.api.teleport.TeleportService;
import bammerbom.ultimatecore.sponge.api.teleport.impl.UCTeleportService;
import bammerbom.ultimatecore.sponge.api.tick.TickService;
import bammerbom.ultimatecore.sponge.api.tick.impl.UCTickService;
import bammerbom.ultimatecore.sponge.api.user.UserService;
import bammerbom.ultimatecore.sponge.api.user.impl.UCUserService;
import bammerbom.ultimatecore.sponge.api.variable.VariableService;
import bammerbom.ultimatecore.sponge.api.variable.impl.UCVariableService;
import bammerbom.ultimatecore.sponge.utils.ServerID;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.bstats.Metrics;
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
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.world.Location;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

@Plugin(id = "ultimatecore", name = "UltimateCore", version = "@VERSION@", description = "All you need to set up a server and more!", url = "http://ultimatecore.org/index", authors = {"Bammerbom"})
public class UltimateCore {

    private static UltimateCore instance = null;
    @Inject
    @ConfigDir(sharedRoot = false)
    public Path dir;
    //Did uc start yet?
    boolean started = false;
    //Config files
    private GeneralConfig generalConfig;
    private CommandsConfig commandsConfig;
    private ModulesConfig modulesConfig;
    @Inject
    private Logger logger;
    //Bstats
    @Inject
    private Metrics metrics;

    public static UltimateCore get() {
        if (instance == null) {
            instance = new UltimateCore();
        }
        return instance;
    }

    public static PluginContainer getContainer() {
        return Sponge.getPluginManager().fromInstance(get()).get();
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
            //Language service
            UCLanguageService languageService = new UCLanguageService();
            languageService.reloadPre();

            //Register serializers because sponge doesn't for some reason
            TypeSerializerCollection serializers = TypeSerializers.getDefaultSerializers();
//            serializers.registerType(TypeToken.of(BlockState.class), new BlockStateSerializer());
            serializers.registerType(TypeToken.of(ItemStackSnapshot.class), new ItemStackSnapshotSerializer());
            serializers.registerType(TypeToken.of(Location.class), new LocationSerializer());
            serializers.registerType(TypeToken.of(Transform.class), new TransformSerializer());
            serializers.registerType(TypeToken.of(Vector3d.class), new Vector3dSerializer());

            //Config
            this.generalConfig = new GeneralConfig();
            this.generalConfig.reload();
            this.commandsConfig = new CommandsConfig();
            this.commandsConfig.reload();
            this.modulesConfig = new ModulesConfig();
            this.modulesConfig.reload();
            languageService.reloadPost();

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
            UCErrorService errorService = new UCErrorService();

            //Register services
            ServiceManager sm = Sponge.getServiceManager();
            sm.setProvider(this, ModuleService.class, moduleService);
            sm.setProvider(this, CommandService.class, commandService);
            sm.setProvider(this, UserService.class, userService);
            sm.setProvider(this, PermissionService.class, permissionService);
            sm.setProvider(this, TeleportService.class, teleportService);
            sm.setProvider(this, TickService.class, tickService);
            sm.setProvider(this, VariableService.class, variableService);
            sm.setProvider(this, LanguageService.class, languageService);
            sm.setProvider(this, ErrorService.class, errorService);

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
        }
    }

    @Listener
    public void onInit(GameInitializationEvent ev) {
        try {
            if (Sponge.getPlatform().getType().equals(Platform.Type.CLIENT)) {
                System.out.println("[UC] You are running UC on a client. Waiting with starting the game until getServer() is available.");
                return;
            }
            this.started = true;
            Long time = System.currentTimeMillis();

            //Initialize modules
            for (Module module : getModuleService().getModules()) {
                try {
                    ModuleInitializeEvent event = new ModuleInitializeEvent(module, ev, Cause.builder().owner(getContainer()).build());
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
            this.commandsConfig.postload();
            this.modulesConfig.postload();
            //Add custom bstats charts
            this.metrics.addCustomChart(new Metrics.SimpleBarChart("modules") {
                @Override
                public HashMap<String, Integer> getValues(HashMap<String, Integer> valueMap) {
                    HashMap<String, Integer> modules = new HashMap<>();
                    UltimateCore.get().getModuleService().getModules().forEach(module -> {
                        modules.put(module.getIdentifier(), 1);
                    });
                    return modules;
                }
            });
            this.metrics.addCustomChart(new Metrics.SimplePie("language") {
                @Override
                public String getValue() {
                    return getGeneralConfig().get().getNode("language", "language").getString();
                }
            });
            this.metrics.addCustomChart(new Metrics.SimplePie("platform") {
                @Override
                public String getValue() {
                    return StringUtil.firstUpperCase(Sponge.getPlatform().getType().name());
                }
            });
            this.metrics.addCustomChart(new Metrics.SimplePie("implementation") {
                @Override
                public String getValue() {
                    return Sponge.getPlatform().getContainer(Platform.Component.IMPLEMENTATION).getName();
                }
            });
            //Post-initialize modules
            for (Module module : getModuleService().getModules()) {
                try {
                    ModulePostInitializeEvent event = new ModulePostInitializeEvent(module, ev, Cause.builder().owner(getContainer()).build());
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
        if (Sponge.getPlatform().getType().equals(Platform.Type.CLIENT) && !this.started) {
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
                    ModuleStoppingEvent event = new ModuleStoppingEvent(module, ev, Cause.builder().owner(getContainer()).build());
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
            UltimateCore.get().getCommandsConfig().reload();
            UltimateCore.get().getCommandsConfig().postload();
            UltimateCore.get().getModulesConfig().reload();
            UltimateCore.get().getModulesConfig().postload();

            for (Module mod : UltimateCore.get().getModuleService().getModules()) {
                if (mod.getConfig() != null && mod.getConfig().isPresent()) {
                    mod.getConfig().get().reload();
                    mod.onReload(event);
                }
            }

            getLanguageService().reload();

            time = System.currentTimeMillis() - time;
            Messages.log(Messages.getFormatted("core.load.reload", "%ms%", time));
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to stop UltimateCore");
        }
    }

    public Path getConfigFolder() {
        return this.dir;
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

    public LanguageService getLanguageService() {
        ServiceManager manager = Sponge.getServiceManager();
        return manager.provide(LanguageService.class).orElse(null);
    }

    public ErrorService getErrorService() {
        ServiceManager manager = Sponge.getServiceManager();
        return manager.provide(ErrorService.class).orElse(null);
    }

    public Optional<SignService> getSignService() {
        ServiceManager manager = Sponge.getServiceManager();
        return manager.provide(SignService.class);
    }

    //Get configs
    public GeneralConfig getGeneralConfig() {
        return this.generalConfig;
    }

    public CommandsConfig getCommandsConfig() {
        return this.commandsConfig;
    }

    public ModulesConfig getModulesConfig() {
        return this.modulesConfig;
    }
}