package Bammerbom.UltimateCore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class UltimateCommand{
	
    public String getName() {
		return "";
	}
    
    public abstract void run(CommandSender sender, Command cmd, String label, String[] args);
    
    public void onTabComplete(CommandSender sender, Command cmd, String alias, String[] args){
    }
    
    
    
}
