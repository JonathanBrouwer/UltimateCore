package bammerbom.ultimatecore.spongeapi;

import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class UltimateCommandExecutor implements CommandExecutor {

    UltimateCommand cmd;
    String label;

    public UltimateCommandExecutor(UltimateCommand ex, String alias) {
        cmd = ex;
        label = alias;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        cmd.run(source, label, UltimateCommands.convertsArgs(args));
        return CommandResult.success();
    }
}