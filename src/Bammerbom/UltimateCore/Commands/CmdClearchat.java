package Bammerbom.UltimateCore.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;

public class CmdClearchat{

     public static void handle(final CommandSender cs, Command cmd, String label, String[] args) {
    	if(!r.perm(cs, "uc.clearchat", false, true)) return;
        for(Player p : UC.getOnlinePlayers()){
        	for (int i = 0; i < 100; i++) {
        		p.sendMessage("");
        	}
        }
  }
}