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
package bammerbom.ultimatecore.sponge.api.module.impl;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.ModuleService;
import bammerbom.ultimatecore.sponge.api.module.annotations.ModuleDisableByDefault;
import bammerbom.ultimatecore.sponge.api.module.annotations.ModuleIgnore;
import bammerbom.ultimatecore.sponge.api.module.event.ModuleRegisterEvent;
import com.google.common.reflect.ClassPath;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

public class UCModuleService implements ModuleService {

    private List<Module> modules = new ArrayList<>();
    private List<Module> unregisteredModules = new ArrayList<>();

    /**
     * Retrieves list of all registered modules
     *
     * @return the list
     */
    @Override
    public List<Module> getModules() {
        return this.modules;
    }

    @Override
    public List<Module> getAllModules() {
        ArrayList<Module> all = new ArrayList<>();
        all.addAll(this.modules);
        all.addAll(this.unregisteredModules);
        return all;
    }

    /**
     * Retrieves a specific module by id
     *
     * @param id the id to search for
     * @return the module, or Optional.empty() if not found
     */
    @Override
    public Optional<Module> getModule(String id) {
        for (Module mod : this.modules) {
            if (mod.getIdentifier().equalsIgnoreCase(id)) {
                return Optional.of(mod);
            }
        }
        return Optional.empty();
    }

    /**
     * Registers a new module
     *
     * @param module The module to register
     * @return Whether it was successfully registered
     */
    @Override
    public boolean registerModule(Module module) {
        try {
            if (module.getClass().getAnnotation(ModuleIgnore.class) != null) return false;

            ModuleRegisterEvent event = new ModuleRegisterEvent(module, Cause.builder().owner(UltimateCore.getContainer()).build());
            Sponge.getEventManager().post(event);
            String def = module.getClass().getAnnotation(ModuleDisableByDefault.class) == null ? "enabled" : "disabled";
            String state = UltimateCore.get().getModulesConfig().get().getNode("modules", module.getIdentifier(), "state").getString(def);
            //state != null because state is null when module is first loaded
            if (event.isCancelled() && !module.getIdentifier().equalsIgnoreCase("default") && state != null && !state.equalsIgnoreCase("force")) {
                Messages.log(Messages.getFormatted("core.load.module.blocked", "%module%", module.getIdentifier()));
                this.unregisteredModules.add(module);
                return false;
            }
            if (!module.getIdentifier().equalsIgnoreCase("default") && state != null && state.equalsIgnoreCase("disabled")) {
                Messages.log(Messages.getFormatted("core.load.module.disabled", "%module%", module.getIdentifier()));
                this.unregisteredModules.add(module);
                return false;
            }
            this.modules.add(module);
            module.onRegister();
            return true;
        } catch (Exception ex) {
            ErrorLogger.log(ex, "An error occured while registering the module '" + module.getIdentifier() + "'");
            this.unregisteredModules.add(module);
            return false;
        }
    }

    /**
     * Unregisters a module
     *
     * @param id The identifier of the module
     * @return Whether the module was found
     */
    @Override
    public boolean unregisterModule(String id) {
        return getModule(id).isPresent() && unregisterModule(getModule(id).get());
    }

    /**
     * Unregisters a module
     *
     * @param module The instance of the module
     * @return Whether the module was found
     */
    @Override
    public boolean unregisterModule(Module module) {
        if (this.modules.contains(module)) {
            this.modules.remove(module);
            return true;
        }
        return false;
    }

    /**
     * This gets an Module instance from a jar file. This does not register it.
     *
     * @param file The .jar file for the module
     * @return The module, or Optional.empty() if not found
     */
    @Override
    public Optional<Module> load(File file) {
        try {
            String id = file.getName().toLowerCase().replace(".jar", "");
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};
            ClassLoader cl = new URLClassLoader(urls, this.getClass().getClassLoader());

            List<String> lines = new ArrayList<>();
            try (InputStream inputStream = cl.getResourceAsStream("ucmodule.info"); BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
                lines = buffer.lines().collect(Collectors.toList());
                buffer.close();
                inputStream.close();
            } catch (Exception ex) {
                lines.add("main=bammerbom.ultimatecore.sponge.modules." + id + "." + id.substring(0, 1).toUpperCase() + id.substring(1) + "Module");
                lines.add("autogenerated=1");
            }

            Map<String, String> keys = new HashMap<>();
            for (String line : lines) {
                if (line.contains("=")) {
                    keys.put(line.split("=")[0], line.split("=")[1]);
                } else if (!line.startsWith("//")) {
                    Messages.log("Invalid line in ucmodule.info file of " + file.getName());
                    Messages.log(line);
                }
            }

            if (keys.containsKey("main")) {
                Object omodule = cl.loadClass(keys.get("main")).getConstructors()[0].newInstance();
                Module module = (Module) omodule;
                return Optional.of(module);
            } else {
                Messages.log("The ucmodule.info file of " + file.getName() + " doesn't contain a main class reference.");
                return Optional.empty();
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to load module from file for " + file);
            return Optional.empty();
        }
    }

    /**
     * Finds all modules in both the jar and the custom directory.
     * This won't actually load/register the modules.
     */
    @Override
    public List<Module> findModules() {
        ModuleService moduleService = UltimateCore.get().getModuleService();
        ArrayList<Module> modules = new ArrayList<>();

        //Load modules from jar
        try {
            //Get all classes
            Set<ClassPath.ClassInfo> classinfo = ClassPath.from(UltimateCore.class.getClassLoader()).getTopLevelClassesRecursive("bammerbom.ultimatecore.sponge.modules");
            Set<Class<?>> classes = new HashSet<>();
            classes.addAll(classinfo.stream().map(ClassPath.ClassInfo::load).collect(Collectors.toSet()));

            for (Class cl : classes) {
                if (!cl.getPackage().getName().startsWith("bammerbom.ultimatecore.sponge.modules")) {
                    continue;
                }
                if (Module.class.isAssignableFrom(cl)) {
                    try {
                        modules.add((Module) cl.getConstructors()[0].newInstance());
                    } catch (Exception ex) {
                        Messages.log(Messages.getFormatted("core.load.module.failed", "%module%", cl.getName()));
                        ErrorLogger.log(ex, "Module has invalid main class " + cl);
                    }
                }
            }

        } catch (Exception ex) {
            //TODO errorlogger
            ErrorLogger.log(ex, "Failed to load modules from jar");
        }

        //Load modules from file
        File modfolder = new File("ultimatecore/modules");
        modfolder.mkdirs();
        for (File f : modfolder.listFiles()) {
            if (f.getName().endsWith(".jar") || f.getName().endsWith(".ucmodule")) {
                Optional<Module> module = moduleService.load(f);
                if (module.isPresent()) {
                    modules.add(module.get());
                } else {
                    Messages.log(Messages.getFormatted("core.load.module.failed", "%module%", f.getName()));
                }
            }
        }
        //
        return modules;
    }
}
