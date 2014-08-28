package Bammerbom.UltimateCore;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract interface UltimateCommand{

	public abstract String getName();

    public abstract void run(CommandSender sender, String label, String[] args);
    
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn);
    
}
