package Bammerbom.UltimateCore.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdSpeed{
	static Plugin plugin;
	public CmdSpeed(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.isPlayer(sender)){
			return;
		}
		if(!r.perm(sender, "uc.speed", false, true)){
			return;
		}
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.mes("Speed.Usage"));
			return;
		}
		if(!isFloat(args[0])){
			if(isFloat(args[1])){
				ArrayList<String> list = new ArrayList<String>();
				list.add(args[1]);
				list.add(args[0]);
				String[] arg = list.toArray(new String[1]);
				handle(sender, arg);
				return;
			}
			sender.sendMessage(r.mes("Speed.Usage"));
			return;
		}
		Player p = (Player) sender;
		Float d = Float.parseFloat(args[0]);
		if(d > 10 || d < 0){
			sender.sendMessage(r.mes("Speed.Usage"));
			return;
		}
		if(r.checkArgs(args, 1) == false){
		p.setFlySpeed(getSpeed(d, true));
		p.setWalkSpeed(getSpeed(d, false));
		p.sendMessage(r.mes("Speed.Self").replaceAll("%Speed", r.default2 + "" + args[0]));
		}else{
			Player t = Bukkit.getPlayer(args[1]);
			if(t == null){
				p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1]));
				return;
			}
			if(!r.perm(sender, "uc.speed.others", false, true)){
				return;
			}
			t.setFlySpeed(getSpeed(d, true));
			t.setWalkSpeed(getSpeed(d, false));
			p.sendMessage(r.mes("Speed.OtherSelf").replaceAll("%Player", t.getName()).replaceAll("%Speed", args[0]));
			t.sendMessage(r.mes("Speed.OtherOther").replaceAll("%Player", sender.getName()).replaceAll("%Speed", args[0]));
		}
	}
	public static boolean isFloat(String str){
		try{
			Float.parseFloat(str);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	public static Float getSpeed(Float f, Boolean fly){
		    float userSpeed;
		      userSpeed = f;
		      if (userSpeed > 10.0F)
		      {
		        userSpeed = 10.0F;
		      }
		      else if (userSpeed < 1.0E-004F)
		      {
		        userSpeed = 1.0E-004F;
		      }
		    
	        float defaultSpeed = fly ? 0.1F : 0.2F;
	        float maxSpeed = 1.0F;
	        if (userSpeed < 1.0F)
		    {
		      return defaultSpeed * userSpeed;
		    }
	        float ratio = (userSpeed - 1.0F) / 9.0F * (maxSpeed - defaultSpeed);
		    return ratio + defaultSpeed;
	}
}
