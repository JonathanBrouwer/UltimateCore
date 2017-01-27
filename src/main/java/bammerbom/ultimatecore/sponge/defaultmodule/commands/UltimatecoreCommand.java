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
package bammerbom.ultimatecore.sponge.defaultmodule.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.HighSubCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandChildrenInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandParentInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.NotEnoughArgumentsException;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.config.datafiles.PlayerDataFile;
import bammerbom.ultimatecore.sponge.defaultmodule.DefaultModule;
import bammerbom.ultimatecore.sponge.defaultmodule.api.DefaultPermissions;
import bammerbom.ultimatecore.sponge.utils.Docgen;
import bammerbom.ultimatecore.sponge.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.stream.Collectors;

@CommandChildrenInfo(children = {
        UltimatecoreCommand.ClearcacheCommand.class,
        UltimatecoreCommand.ResetuserCommand.class,
        UltimatecoreCommand.ModulesCommand.class,
        UltimatecoreCommand.GendocsCommand.class,
        UltimatecoreCommand.ErrorCommand.class,
        UltimatecoreCommand.ReloadCommand.class
})
@CommandPermissions(level = PermissionLevel.OWNER)
@CommandInfo(module = DefaultModule.class, aliases = {"ultimatecore", "uc"})
public class UltimatecoreCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[0];
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, DefaultPermissions.UC_ULTIMATECORE_ULTIMATECORE_BASE);
        throw new NotEnoughArgumentsException(getUsage(sender));
    }

    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = DefaultModule.class, aliases = {"clearcache"})
    public static class ClearcacheCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[0];
        }

        @Override
        public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
            checkPermission(sender, DefaultPermissions.UC_ULTIMATECORE_ULTIMATECORE_BASE);
            UltimateCore.get().getUserService().clearcache();
            sender.sendMessage(Messages.getFormatted(sender, "default.command.ultimatecore.clearcache.success"));
            return CommandResult.success();
        }
    }

    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = DefaultModule.class, aliases = {"resetuser"})
    public static class ResetuserCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[]{
                    Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().build()
            };
        }

        @Override
        public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
            checkPermission(sender, DefaultPermissions.UC_ULTIMATECORE_ULTIMATECORE_BASE);
            Player t = args.<Player>getOne("player").get();
            UltimateUser user = UltimateCore.get().getUserService().getUser(t);
            //Clear the cache for the user
            user.datas.clear();
            PlayerDataFile file = new PlayerDataFile(user.getIdentifier());
            //Delete the user's file
            file.getFile().delete();
            sender.sendMessage(Messages.getFormatted(sender, "default.command.ultimatecore.resetuser.success", "%player%", t.getName()));
            return CommandResult.success();
        }
    }

    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = DefaultModule.class, aliases = {"modules"})
    public static class ModulesCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[0];
        }

        @Override
        public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
            checkPermission(sender, DefaultPermissions.UC_ULTIMATECORE_ULTIMATECORE_BASE);
            List<String> modules = UltimateCore.get().getModuleService().getModules().stream().map(Module::getIdentifier).filter(name -> !name.equalsIgnoreCase("default")).collect(Collectors.toList());
            sender.sendMessage(Messages.getFormatted(sender, "default.command.ultimatecore.modules.success", "%modules%", StringUtil.join(", ", modules)));
            return CommandResult.success();
        }
    }

    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = DefaultModule.class, aliases = {"gendocs"})
    public static class GendocsCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[0];
        }

        @Override
        public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
            checkPermission(sender, DefaultPermissions.UC_ULTIMATECORE_ULTIMATECORE_BASE);
            Docgen.generateDocs();
            return CommandResult.success();
        }
    }

    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = DefaultModule.class, aliases = {"error"})
    public static class ErrorCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[0];
        }

        @Override
        public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
            checkPermission(sender, DefaultPermissions.UC_ULTIMATECORE_ULTIMATECORE_BASE);
            try {
                String string = null;
                string.toLowerCase();
            } catch (Exception ex) {
                ErrorLogger.log(ex, "Generated error using /uc error");
            }
            return CommandResult.success();
        }
    }

    @CommandParentInfo(parent = UltimatecoreCommand.class)
    @CommandInfo(module = DefaultModule.class, aliases = {"reload"})
    public static class ReloadCommand implements HighSubCommand {
        @Override
        public CommandElement[] getArguments() {
            return new CommandElement[0];
        }

        @Override
        public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
            checkPermission(sender, DefaultPermissions.UC_ULTIMATECORE_ULTIMATECORE_BASE);
            UltimateCore.get().onReload(null);
            sender.sendMessage(Messages.getFormatted(sender, "default.command.ultimatecore.reload.success"));
            return CommandResult.success();
        }
    }
}
