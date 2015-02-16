/*
 * This file is part of UltimateCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) Bammerbom
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bammerbom.ultimatecore.bukkit.resources.classes;

import bammerbom.ultimatecore.bukkit.r;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ErrorLogger {

    public static void log(Throwable t, String s) {
        String time = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(Calendar.getInstance().getTime());
        File dir = new File(Bukkit.getPluginManager().getPlugin("UltimateCore").getDataFolder() + "/Errors");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(Bukkit.getPluginManager().getPlugin("UltimateCore").getDataFolder() + "/Errors", time + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        out.println("UltimateCore version: " + r.getUC().getDescription().getVersion());
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
        r.log(ChatColor.YELLOW + "http://dev.bukkit.org/bukkit-plugins/ultimate_core/create-ticket");
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
        if (t instanceof Exception) {
            r.log(ChatColor.RED + "Stacktrace: ");
            t.printStackTrace();
            r.log(ChatColor.DARK_RED + "=========================================================");
        }
        Bukkit.getConsoleSender().sendMessage(" ");
    }
}
