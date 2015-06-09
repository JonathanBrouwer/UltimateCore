package bammerbom.ultimatecore.spongeapi;

import com.google.common.base.Optional;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.ArgumentParseException;
import org.spongepowered.api.util.command.args.CommandArgs;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.CommandElement;
import org.spongepowered.api.util.command.dispatcher.SimpleDispatcher;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class UltimateCommandElement extends CommandElement {

    private final SimpleDispatcher dispatcher = new SimpleDispatcher(SimpleDispatcher.FIRST_DISAMBIGUATOR);
    private UltimateCommand cmd;
    private String label;

    protected UltimateCommandElement(@Nullable Text key) {
        super(key);
        cmd = null;
        label = null;
    }

    public UltimateCommandElement(@Nullable Text key, UltimateCommand cmd_, String alias) {
        this(key);
        cmd = cmd_;
        label = alias;
    }

    @Override
    public Text getUsage(CommandSource scr) {
        return Texts.of(cmd.getUsage());
    }

    @Nullable
    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        final String key = args.next();
        final Optional<CommandMapping> mapping = this.dispatcher.get(key, source);
        if (!mapping.isPresent()) {
            throw args.createError(Texts.of("Input command " + key + " was not a valid subcommand!"));
        }

        return mapping.get();
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs arg, CommandContext context) {
        String[] args = UltimateCommands.convertsArgs(context);
        List<String> rtrn = cmd.onTabComplete(src, args, label, args[args.length - 1], args.length - 1);
        if (rtrn == null) {
            rtrn = new ArrayList<>();
            for (Player p : r.getOnlinePlayers()) {
                rtrn.add(p.getName());
            }
        }
        return rtrn;
    }
}