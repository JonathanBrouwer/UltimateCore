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
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.Metrics;
import bammerbom.ultimatecore.sponge.utils.ServerID;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ServiceManager;

import java.io.File;
import java.nio.file.Path;
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
            Long time = System.currentTimeMillis();
            //Save instance
            instance = this;
            //Load utils
            ServerID.start();
            Messages.reloadEnglishMessages();
            TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(ItemStackSnapshot.class), new ItemStackSnapshotSerializer());
            TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Vector3d.class), new Vector3dSerializer());
            TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Transform.class), new TransformSerializer());

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
                if (moduleService.registerModule(module)) {
                    if (!module.getIdentifier().equals("default")) {
                        Messages.log(Messages.getFormatted("core.load.module.registered", "%module%", module.getIdentifier()));
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

            //Initialize modules
            for (Module module : getModuleService().getRegisteredModules()) {
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
            commandsConfig.postload();
            modulesConfig.postload();
            //Send stats
            //TODO wait for website
            //Stats.start();
            //Post-initialize modules
            for (Module module : getModuleService().getRegisteredModules()) {
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

        //This is some code I use to generate the temporary docs
        //Shouldnt really be runned on a normal server
//        File file = new File(UltimateCore.get().getDataFolder().toFile(), "DOCS.md");
//        StringWriter writer = new StringWriter();
//        for (Module mod : getModuleService().getRegisteredModules()) {
//            writer.write("## " + StringUtil.firstUpperCase(mod.getIdentifier()) + "\n");
//            //Commands
//            CommandService service = getCommandService();
//            List<Command> commands = service.getCommands().stream().filter(cmd -> cmd.getModule().getIdentifier().equalsIgnoreCase(mod.getIdentifier())).collect(Collectors.toList());
//            if (!commands.isEmpty()) {
//                writer.write("### Commands<br>\n");
//                for (Command cmd : commands) {
//                    writer.write("**" + cmd.getUsage().toPlain() + "**: " + cmd.getLongDescription().toPlain() + "<br>\n");
//                }
//            }
//            //Permissions
//            PermissionService service2 = getPermissionService();
//            List<Permission> perms = service2.getPermissions().stream().filter(cmd -> cmd.getModule().getIdentifier().equalsIgnoreCase(mod.getIdentifier())).collect(Collectors.toList());
//            if (!perms.isEmpty()) {
//                writer.write("### Permissions<br>\n");
//                for (Permission perm : perms) {
//                    writer.write("**" + perm.get() + "**: " + perm.getDescription().toPlain() + " (Recommended for " + perm.getLevel().name() + ")<br>\n");
//                }
//            }
//            //Permission options
//            List<PermissionOption> permops = service2.getPermissionOptions().stream().filter(cmd -> cmd.getModule().getIdentifier().equalsIgnoreCase(mod.getIdentifier())).collect(Collectors.toList());
//            if (!permops.isEmpty()) {
//                writer.write("### Permissions options<br>\n");
//                for (PermissionOption perm : permops) {
//                    writer.write("**" + perm.get() + "**: " + perm.getDescription().toPlain() + "<br>\n");
//                }
//            }
//            if (commands.isEmpty() && perms.isEmpty() && permops.isEmpty()) {
//                writer.write("This module has no commands or permissions.<br>\n");
//            }
//        }
//        try {
//            FileUtil.writeLines(file, Arrays.asList(writer.toString().split("\n")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Listener
    public void onStopping(GameStoppingEvent ev) {
        try {
            Long time = System.currentTimeMillis();
            //Stop modules
            for (Module module : getModuleService().getRegisteredModules()) {
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