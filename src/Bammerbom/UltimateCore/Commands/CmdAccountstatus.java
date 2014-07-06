package Bammerbom.UltimateCore.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import Bammerbom.UltimateCore.r;

public class CmdAccountstatus
{

     @SuppressWarnings("deprecation")
     public static void handle(final CommandSender cs, Command cmd, String label, String[] args) {
    	if(!r.perm(cs, "uc.accountstatus", false, true)) return;
    	 if(!r.checkArgs(args, 0)){
    		cs.sendMessage(r.mes("AccountStatus.Usage"));
    		return;
    	}
    	 if(!r.getCnfg().getBoolean("mcstats")) return;
      final String name;
      final OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
      if(p == null){
    	  cs.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
      }
      name = p.getName();
      Thread t = new Thread(new Runnable(){
    	  public void run(){
      URL u;
      try { u = new URL("https://minecraft.net/haspaid.jsp?user=" + URLEncoder.encode(name, "UTF-8"));
      } catch (MalformedURLException ex) {
    	  cs.sendMessage(r.mes("AccountStatus.FailedSupport"));
        return;
      } catch (UnsupportedEncodingException ex) {
    	  cs.sendMessage(r.mes("AccountStatus.FailedSupport"));
        return;
      }
      boolean isPremium;
      try {
        BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
        isPremium = br.readLine().equalsIgnoreCase("true");
      } catch (IOException ex) {
    	  cs.sendMessage(r.mes("AccountStatus.FailedConnect"));
        return;
      }
      String status = isPremium ? ChatColor.GREEN + r.word("Words.Premium") : ChatColor.DARK_RED + r.word("Words.NotPremium");
      cs.sendMessage(r.mes("AccountStatus.Succes").replaceAll("%Player", name).replaceAll("%Status", status));
    	  }});
      t.setName("UltimateCore: /accountstatus thread.");
      t.start();
      return;
  }
}