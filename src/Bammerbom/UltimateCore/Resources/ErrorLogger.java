package Bammerbom.UltimateCore.Resources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import net.minecraft.util.org.apache.commons.lang3.exception.ExceptionUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import Bammerbom.UltimateCore.r;

public class ErrorLogger {
	public ErrorLogger(Throwable t, String s){
		//
		String time = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(Calendar.getInstance().getTime());
		File dir = new File(Bukkit.getPluginManager().getPlugin("UltimateCore").getDataFolder() + "/Errors");
		if(!dir.exists()) dir.mkdir();
		File file = new File(Bukkit.getPluginManager().getPlugin("UltimateCore").getDataFolder() + "/Errors", time + ".txt");
		if(!file.exists())try {file.createNewFile();} catch (IOException e) {e.printStackTrace();}
		FileWriter outFile;
		try {
			outFile = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		PrintWriter out = new PrintWriter(outFile);
		 out.println("=======================================");
		 out.println("UltimateCore has run into an error ");
		 out.println("Please report your error on dev.bukkit.org/bukkit-plugins/ultimate_core/create-ticket");
		 out.println("Bukkit version: " + Bukkit.getServer().getVersion());
		 out.println("UltimateCore version: " + Bukkit.getPluginManager().getPlugin("UltimateCore").getDescription().getVersion());
		 out.println("Plugins loaded (" + Bukkit.getPluginManager().getPlugins().length + "): " + Arrays.asList(Bukkit.getPluginManager().getPlugins()));
		 out.println("Java version: " + System.getProperty("java.version"));
		 out.println("OS info: " + System.getProperty("os.arch") + ", " + System.getProperty("os.name") + ", " + System.getProperty("os.version"));
		 out.println("Time: " + time);
		 out.println("Error message: " + t.getMessage());
		 out.println("UltimateCore message: " + s);
		 out.println("=======================================");
		 out.println("Stacktrace: \n" + ExceptionUtils.getStackTrace(t));
		 out.println("=======================================");
		out.close();
		try {
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		Bukkit.getConsoleSender().sendMessage(" ");
		 r.log(ChatColor.DARK_RED + "=========================================================");
		 r.log(ChatColor.RED + "UltimateCore has run into an error ");
		 r.log(ChatColor.RED + "Please report your error on ");
		 r.log(ChatColor.YELLOW + "dev.bukkit.org/bukkit-plugins/ultimate_core/create-ticket");
		 r.log(ChatColor.RED + "Include the file: ");
		 r.log(ChatColor.YELLOW + "plugins/UltimateCore/Errors/" + time + ".txt ");
		 /*r.log(ChatColor.RED + "Bukkit version: " + Bukkit.getServer().getVersion());
		 r.log(ChatColor.RED + "UltimateCore version: " + Bukkit.getPluginManager().getPlugin("UltimateCore").getDescription().getVersion());
		 r.log(ChatColor.RED + "Plugins loaded (" + Bukkit.getPluginManager().getPlugins().length + "): " + Arrays.asList(Bukkit.getPluginManager().getPlugins()));
		 r.log(ChatColor.RED + "Java version: " + System.getProperty("java.version"));
		 r.log(ChatColor.RED + "OS info: " + System.getProperty("os.arch") + ", " + System.getProperty("os.name") + ", " + System.getProperty("os.version"));
		 r.log(ChatColor.RED + "Error message: " + t.getMessage());
		 r.log(ChatColor.RED + "UltimateCore message: " + s);*/
		 r.log(ChatColor.DARK_RED + "=========================================================");
		 if(t instanceof Exception){
		 r.log(ChatColor.RED + "Stacktrace: ");
		 ((Exception)t).printStackTrace();
		 r.log(ChatColor.DARK_RED + "=========================================================");
		 }	
		 Bukkit.getConsoleSender().sendMessage(" ");
	}
}
