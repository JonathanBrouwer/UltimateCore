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
package bammerbom.ultimatecore.sponge.modules.core.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.HighSubCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandChildrenInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandParentInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.GroupSubjectArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PermissionLevelArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.NotEnoughArgumentsException;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.datafiles.PlayerDataFile;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.core.CoreModule;
import bammerbom.ultimatecore.sponge.utils.Docgen;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Tristate;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@CommandChildrenInfo(children = {UltimatecoreCommand.ClearcacheCommand.class, UltimatecoreCommand.ResetuserCommand.class, UltimatecoreCommand.ModulesCommand.class, UltimatecoreCommand.GendocsCommand.class, UltimatecoreCommand.ErrorCommand.class, UltimatecoreCommand.ReloadCommand.class, UltimatecoreCommand.SetuppermissionsCommand.class})
@CommandPermissions(level = PermissionLevel.ADMIN)
@CommandInfo(module = CoreModule.class, aliases = {"ultimatecore", "uc"})
public class UltimatecoreCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[0];
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        throw new NotEnoughArgumentsException(getUsage(sender));
    }

    @CommandPermissions(level = PermissionLevel.OWNER)
    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = CoreModule.class, aliases = {"clearcache"})
    public static class ClearcacheCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[0];
        }

        @Override
        public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
            UltimateCore.get().getUserService().clearcache();
            Messages.send(sender, "core.command.ultimatecore.clearcache.success");
            return CommandResult.success();
        }
    }

    @CommandPermissions(level = PermissionLevel.OWNER)
    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = CoreModule.class, aliases = {"resetuser"})
    public static class ResetuserCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[]{Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().build()};
        }

        @Override
        public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
            Player t = args.<Player>getOne("player").get();
            UltimateUser user = UltimateCore.get().getUserService().getUser(t);
            //Clear the cache for the user
            user.datas.clear();
            PlayerDataFile file = new PlayerDataFile(user.getIdentifier());
            //Delete the user's file
            file.getFile().delete();
            Messages.send(sender, "core.command.ultimatecore.resetuser.success", "%player%", t);
            return CommandResult.success();
        }
    }

    @CommandPermissions(level = PermissionLevel.ADMIN)
    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = CoreModule.class, aliases = {"modules"})
    public static class ModulesCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[0];
        }

        @Override
        public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
            List<String> modules = UltimateCore.get().getModuleService().getModules().stream().map(Module::getIdentifier).filter(name -> !name.equalsIgnoreCase("default")).collect(Collectors.toList());
            Messages.send(sender, "core.command.ultimatecore.modules.success", "%modules%", StringUtil.join(", ", modules));
            return CommandResult.success();
        }
    }

    @CommandPermissions(level = PermissionLevel.NOBODY)
    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = CoreModule.class, aliases = {"gendocs"})
    public static class GendocsCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[0];
        }

        @Override
        public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
            Docgen.generateDocs();
            return CommandResult.success();
        }
    }

    @CommandPermissions(level = PermissionLevel.NOBODY)
    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = CoreModule.class, aliases = {"error"})
    public static class ErrorCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[0];
        }

        @Override
        public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
            try {
                String string = null;
                string.toLowerCase();
            } catch (Exception ex) {
                ErrorLogger.log(ex, "Generated error using /uc error");
            }
            return CommandResult.success();
        }
    }

    @CommandPermissions(level = PermissionLevel.ADMIN)
    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = CoreModule.class, aliases = {"reload"})
    public static class ReloadCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[0];
        }

        @Override
        public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
            UltimateCore.get().onReload(null);
            Messages.send(sender, "core.command.ultimatecore.reload.success");
            return CommandResult.success();
        }
    }

    @CommandPermissions(level = PermissionLevel.OWNER)
    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = CoreModule.class, aliases = {"setuppermissions", "setupperms"})
    public static class SetuppermissionsCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[]{Arguments.builder(new PermissionLevelArgument(Text.of("level"))).onlyOne().build(), Arguments.builder(new GroupSubjectArgument(Text.of("group"))).onlyOne().build()};
        }

        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            PermissionLevel level = args.<PermissionLevel>getOne("level").get();
            Subject group = args.<Subject>getOne("group").get();

            //For each permission, grant if level is equal
            for (Permission perm : UltimateCore.get().getPermissionService().getPermissions()) {
                if (perm.getLevel() == level) {
                    group.getSubjectData().setPermission(new HashSet<>(), perm.get(), Tristate.TRUE);
                }
            }

            Messages.send(src, "core.command.ultimatecore.setuppermissions.success", "%group%", group.getIdentifier(), "%level%", level.name().toLowerCase());
            return CommandResult.success();
        }
    }
}
