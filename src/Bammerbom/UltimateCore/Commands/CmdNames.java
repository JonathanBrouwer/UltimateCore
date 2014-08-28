package Bammerbom.UltimateCore.Commands;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import Bammerbom.UltimateCore.UltimateCommand;
import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdNames implements UltimateCommand{

	@Override
	public String getName() {
		return "names";
	}

	@Override
	public void run(CommandSender sender, String label, String[] args) {
		if(!r.perm(sender, "uc.names", false, true)) return;
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.default1 + "/names " + r.default2 + "<Player>");
			return;
		}
		OfflinePlayer p = r.searchOfflinePlayer(args[0]);
		if(p == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		UltimateConfiguration conf = UltimateFileLoader.getPlayerConfig(p);
		List<String> names = conf.getStringList("names");
		sender.sendMessage(r.default1 + "All names of " + r.default2 + p.getName() + r.default1 + ": ");
		for(String name : names){
			String n = name.split(" - ")[0];
			String d = name.split(" - ")[1];
			sender.sendMessage(r.default2 + d + r.default1 + " > " + r.default2 + n);
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
		return null;
	}
	
}
