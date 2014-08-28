package Bammerbom.UltimateCore.Commands;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import Bammerbom.UltimateCore.UltimateCommand;
import Bammerbom.UltimateCore.r;

public class CmdUUID implements UltimateCommand{

	@Override
	public String getName() {
		return "uuid";
	}

	@Override
	public void run(CommandSender sender, String label, String[] args) {
		if(!r.perm(sender, "uc.uuid", false, true)) return;
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.default1 + "/uuid " + r.default2 + "<Player>");
			return;
		}
		OfflinePlayer p = r.searchOfflinePlayer(args[0]);
		if(p == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		sender.sendMessage(r.default1 + "UUID of " + p.getName() + ": " + r.default2 + p.getUniqueId());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
		return null;
	}
	
}
