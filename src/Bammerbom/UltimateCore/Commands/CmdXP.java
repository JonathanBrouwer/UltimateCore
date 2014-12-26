package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdXP {
	Plugin plugin;
	public CmdXP(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, plugin);
		}
	}
	//setxp
	public static void handle2(CommandSender sender, String[] args){
		if(!r.checkArgs(args,0)){
			sender.sendMessage(r.mes("XP.Usage"));
			return;
		}
		if(r.checkArgs(args, 0) && !r.checkArgs(args, 1)){
			if(!r.perm(sender, "uc.xp", false, false) && !r.perm(sender, "uc.xp.set", false, false)){ sender.sendMessage(r.mes("NoPermissions")); return; }
			if(!r.isPlayer(sender)) return;
			Player p = (Player) sender;
			String amount = args[0];
			Integer x = null;
			if(r.isNumber(amount.replaceAll("l", "").replaceAll("L", ""))){
				 x = Integer.parseInt(amount.replaceAll("L", "").replaceAll("l", ""));
			}else{
				sender.sendMessage(r.mes("XP.AmountNotValid").replaceAll("%Amount", args[0]));
				return;
			}
			r.normalize(x, 0, 999999);
			if(amount.endsWith("l") || amount.endsWith("L")){
				p.setLevel(x);
				p.sendMessage(r.mes("XP.Set").replaceAll("%Type", "Levels").replaceAll("%Player", p.getName()).replaceAll("%XP", x + ""));
			}else{
				setXP(p, x);
				p.sendMessage(r.mes("XP.Set").replaceAll("%Type", "XP").replaceAll("%Player", p.getName()).replaceAll("%XP", x + ""));
			}
			
		}else if(r.checkArgs(args, 1)){
			if(!r.perm(sender, "uc.xp", false, false) && !r.perm(sender, "uc.xp.set.others", false, false)){ sender.sendMessage(r.mes("NoPermissions")); return; }
			Player p = r.searchPlayer(args[1]);
			if(p == null){ sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1])); return; }
			String amount = args[0];
			Integer x = null;
			if(r.isNumber(amount.replaceAll("l", "").replaceAll("L", ""))){
				 x = Integer.parseInt(amount.replaceAll("L", "").replaceAll("l", ""));
			}else{
				sender.sendMessage(r.mes("XP.AmountNotValid").replaceAll("%Amount", args[0]));
				return;
			}
			r.normalize(x, 0, 999999);
			if(amount.endsWith("l") || amount.endsWith("L")){
				p.setLevel(x);
				sender.sendMessage(r.mes("XP.Set").replaceAll("%Type", "Levels").replaceAll("%Player", p.getName()).replaceAll("%XP", x + ""));
			}else{
				setXP(p, x);
				sender.sendMessage(r.mes("XP.Set").replaceAll("%Type", "XP").replaceAll("%Player", p.getName()).replaceAll("%XP", x + ""));
			}
		}
	}
	//setlevel
	public static void handle3(CommandSender sender, String[] args){
		if(!r.checkArgs(args,0)){
			sender.sendMessage(r.mes("XP.Usage2"));
			return;
		}
		if(r.checkArgs(args, 0) && !r.checkArgs(args, 1)){
			if(!r.perm(sender, "uc.xp", false, false) && !r.perm(sender, "uc.xp.set", false, false)){ sender.sendMessage(r.mes("NoPermissions")); return; }
			if(!r.isPlayer(sender)) return;
			Player p = (Player) sender;
			String amount = args[0];
			Integer x = null;
			if(r.isNumber(amount.replaceAll("l", "").replaceAll("L", ""))){
				 x = Integer.parseInt(amount.replaceAll("L", "").replaceAll("l", ""));
			}else{
				sender.sendMessage(r.mes("XP.AmountNotValid").replaceAll("%Amount", args[0]));
				return;
			}
			r.normalize(x, 0, 999999);
				p.setLevel(x);
				p.sendMessage(r.mes("XP.Set").replaceAll("%Type", "XP").replaceAll("%Player", p.getName()).replaceAll("%XP", x + ""));
			
		}else if(r.checkArgs(args, 1)){
			if(!r.perm(sender, "uc.xp", false, false) && !r.perm(sender, "uc.xp.set.others", false, false)){ sender.sendMessage(r.mes("NoPermissions")); return; }
			Player p = r.searchPlayer(args[1]);
			if(p == null){ sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1])); return; }
			String amount = args[0];
			Integer x = null;
			if(r.isNumber(amount.replaceAll("l", "").replaceAll("L", ""))){
				 x = Integer.parseInt(amount.replaceAll("L", "").replaceAll("l", ""));
			}else{
				sender.sendMessage(r.mes("XP.AmountNotValid").replaceAll("%Amount", args[0]));
				return;
			}
			r.normalize(x, 0, 999999);
				p.setLevel(x);
				sender.sendMessage(r.mes("XP.Set").replaceAll("%Type", "Levels").replaceAll("%Player", p.getName()).replaceAll("%XP", x + ""));
		}
	}
	//xp
	public static void handle(CommandSender sender, String[] args){
		if(!r.checkArgs(args, 0)){
			if(!r.isPlayer(sender)) return;
			Player p = (Player) sender;
			if(!r.perm(sender, "uc.xp", false, false) && !r.perm(sender, "uc.xp.show", false, false)){ sender.sendMessage(r.mes("NoPermissions")); return; }
			sender.sendMessage(r.mes("XP.Show").replaceAll("%Player", sender.getName()).replaceAll("%XP", getXP(p) + "").replaceAll("%Levels", p.getLevel() + ""));
		}else if(r.checkArgs(args, 0) && !r.checkArgs(args, 1)){
			String rawxp = args[0];
			String xp = args[0].endsWith("L") ? rawxp : rawxp.replaceAll("L", "").replaceAll("l", "");
			if(!r.isNumber(xp)){
				if(r.searchPlayer(args[0]) != null){
					Player p = r.searchPlayer(args[0]);
					if(!r.perm(sender, "uc.xp", false, false) && !r.perm(sender, "uc.xp.show.others", false, false)){ sender.sendMessage(r.mes("NoPermissions")); return; }
					sender.sendMessage(r.mes("XP.Show").replaceAll("%Player", p.getName()).replaceAll("%XP", getXP(p) + "").replaceAll("%Levels", p.getLevel() + ""));
					return;
				}else{
					sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
					return;
				}
			}
			if(!r.perm(sender, "uc.xp", false, false) && !r.perm(sender, "uc.xp.set", false, false)){ sender.sendMessage(r.mes("NoPermissions")); return; }
			Integer x = Integer.parseInt(xp);
			if(!r.isPlayer(sender)) return;
			Player p = (Player) sender;
			if(rawxp.endsWith("L") || rawxp.endsWith("l")){
				 if(p.getLevel() + x < 1){ setXP(p, 0); }else{
				p.setLevel(p.getLevel() + x);
				 }
				 if(x >= 0){
				sender.sendMessage(r.mes("XP.Give").replaceAll("%Type", "Levels").replaceAll("%Player", p.getName()).replaceAll("%XP", x + ""));
				 }else{
					 sender.sendMessage(r.mes("XP.Take").replaceAll("%Type", "Levels").replaceAll("%Player", p.getName()).replaceAll("%XP", x * -1 + ""));
				 }
			}else{
				 if(getXP(p) + x < 1){ setXP(p, 0); }else{
				setXP(p, getXP(p) + x);
				 }
				 if(x >= 0){
				sender.sendMessage(r.mes("XP.Give").replaceAll("%Type", "XP").replaceAll("%Player", p.getName()).replaceAll("%XP", x + ""));
				 sender.sendMessage(r.mes("XP.Tip").replaceAll("%Command1", "/xp " + x).replaceAll("%Command2", ""));
				 }else{
					 sender.sendMessage(r.mes("XP.Take").replaceAll("%Type", "XP").replaceAll("%Player", p.getName()).replaceAll("%XP", x * -1 + ""));
					 sender.sendMessage(r.mes("XP.Tip").replaceAll("%Command1", "/xp " + x).replaceAll("%Command2", ""));
				 }
			}
		}else if(r.checkArgs(args, 1)){
			if(!r.perm(sender, "uc.xp", false, false) && !r.perm(sender, "uc.xp.set.others", false, false)){ sender.sendMessage(r.mes("NoPermissions")); return; }
			String rawxp = args[0];
			String xp = !args[0].endsWith("L") ? rawxp : rawxp.replaceAll("L", "").replaceAll("l", "");
			if(!r.isNumber(xp)){
					sender.sendMessage(r.mes("XP.AmountNotValid").replaceAll("%Amount", args[0]));
					return;
			}
			Integer x = Integer.parseInt(xp);
			Player t = r.searchPlayer(args[1]);
			if(t == null){
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1]));
				return;
			}
			if(rawxp.endsWith("L") || rawxp.endsWith("l")){
				 if (x > Integer.MAX_VALUE) x = Integer.MAX_VALUE;
				 if(x < Integer.MIN_VALUE) x = Integer.MIN_VALUE;
				 if(t.getLevel() + x < 1){ setXP(t, 0); }else{
				 t.setLevel(t.getLevel() + x);
				 }
					 if(x >= 0){
						sender.sendMessage(r.mes("XP.Give").replaceAll("%Type", "Levels").replaceAll("%Player", t.getName()).replaceAll("%XP", x + ""));
					}else{
						sender.sendMessage(r.mes("XP.Take").replaceAll("%Type", "Levels").replaceAll("%Player", t.getName()).replaceAll("%XP", x * -1 + ""));
					}

			}else{
				 if (x > Integer.MAX_VALUE) x = Integer.MAX_VALUE;
				 if(x < Integer.MIN_VALUE) x = Integer.MIN_VALUE;

				 if(getXP(t) + x < 1){ setXP(t, 0); }else{
						setXP(t, getXP(t) + x);
				 }
				 if(x >= 0){
				sender.sendMessage(r.mes("XP.Give").replaceAll("%Type", "XP").replaceAll("%Player", t.getName()).replaceAll("%XP", x + ""));
				sender.sendMessage(r.mes("XP.Tip").replaceAll("%Command1", "/xp " + x).replaceAll("%Command2", " " + args[1]));
				 }else{
					 sender.sendMessage(r.mes("XP.Take").replaceAll("%Type", "XP").replaceAll("%Player", t.getName()).replaceAll("%XP", x * -1 + ""));
					 sender.sendMessage(r.mes("XP.Tip").replaceAll("%Command1", "/xp " + x).replaceAll("%Command2", " " + args[1]));
				 }
			}
		}
	}
	public static void setXP(Player player, int exp)
	  {
	    if (exp < 0)
	    {
	      exp = 0;
	    }
	    player.setExp(0.0F);
	    player.setLevel(0);
	    player.setTotalExperience(0);

	    int amount = exp;
	    while (amount > 0)
	    {
	      int expToLevel = getExpAtLevel(player);
	      amount -= expToLevel;
	      if (amount >= 0)
	      {
	        player.giveExp(expToLevel);
	      }
	      else
	      {
	        amount += expToLevel;
	        player.giveExp(amount);
	        amount = 0;
	      }
	    }
	  }

	  private static int getExpAtLevel(Player player)
	  {
	    return getExpAtLevel(player.getLevel());
	  }

	  public static int getExpAtLevel(int level)
	  {
	    if (level <= 15)
	    {
	      return (2 * level) + 7;
	    }
	    if (level >= 16 && level <= 30)
	    {
	      return (5 * level) - 38;
	    }
	    return (9 * level) - 158;
	  }

	  public static int getXP(Player player)
	  {
	    int exp = Math.round(getExpAtLevel(player) * player.getExp());
	    int currentLevel = player.getLevel();

	    while (currentLevel > 0)
	    {
	      currentLevel--;
	      exp += getExpAtLevel(currentLevel);
	    }
	    if (exp < 0)
	    {
	      exp = 2147483647;
	    }
	    return exp;
	  }
}