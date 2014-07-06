package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import Bammerbom.UltimateCore.r;

public class CmdEditSign
{

     @SuppressWarnings("deprecation")
     public static void handle(final CommandSender cs, Command cmd, String label, String[] args) {
    	if(!r.perm(cs, "uc.editsign", false, true)) return;
    	if(!r.isPlayer(cs)) return;
          Player p = (Player)cs;
          if (args.length < 1) {
              cs.sendMessage(r.mes("EditSign.Usage"));
            return;
          }
          Block b = p.getTargetBlock(null, 100);
          if ((b == null) || b.getState() == null || (!(b.getState() instanceof Sign))) {
        	  p.sendMessage((b != null && b.getState() != null) ? r.default1 + "That " + r.default2 + b.getType().name().toLowerCase() + r.default1 + " is not a sign" : r.default1 + "You are not looking at a sign.");
        	  return;
          }
          Sign s = (Sign)b.getState();
          int lineNumber;
          try
          {
            lineNumber = Integer.parseInt(args[0]);
            lineNumber--;
          } catch (NumberFormatException e) {
              cs.sendMessage(r.mes("EditSign.Usage"));
            return;
          }
          if ((lineNumber < 0) || (lineNumber > 3)) {
              cs.sendMessage(r.mes("EditSign.Usage"));
            return;
          }
          if (args.length < 2) {
            s.setLine(lineNumber, "");
            s.update();
            cs.sendMessage(r.mes("EditSign.Clear").replaceAll("%Line", (lineNumber + 1) + ""));
            return;
          }
          String text = r.getFinalArg(args, 1);
  		if(r.perm(p, "uc.sign.colored", false, false)){
  		s.setLine(0, ChatColor.translateAlternateColorCodes('&', s.getLine(0)));
  		s.setLine(1, ChatColor.translateAlternateColorCodes('&', s.getLine(1)));
  		s.setLine(2, ChatColor.translateAlternateColorCodes('&', s.getLine(2)));
  		s.setLine(3, ChatColor.translateAlternateColorCodes('&', s.getLine(3)));
  		text = ChatColor.translateAlternateColorCodes('&', text);
  		}
          s.setLine(lineNumber, text);
          s.update();
          cs.sendMessage(r.mes("EditSign.Set").replaceAll("%Line", (lineNumber + 1) + "").replaceAll("%Text", r.getFinalArg(args, 1)));
          return;
  }
     public boolean canPlace(Player p, Block b, Sign s){
    	 BlockBreakEvent e = new BlockBreakEvent(b, p);
    	 SignChangeEvent e2 = new SignChangeEvent(b, p, s.getLines());
    	 Bukkit.getPluginManager().callEvent(e);
    	 Bukkit.getPluginManager().callEvent(e2);
    	 return e.isCancelled() || e2.isCancelled();
     }
}
