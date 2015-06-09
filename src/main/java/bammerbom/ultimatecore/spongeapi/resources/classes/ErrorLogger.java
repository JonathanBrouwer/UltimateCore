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
package bammerbom.ultimatecore.spongeapi.resources.classes;

import bammerbom.ultimatecore.spongeapi.UltimateCore;
import bammerbom.ultimatecore.spongeapi.r;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class ErrorLogger {

    public static void log(Throwable t, String s) {
        String time = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(Calendar.getInstance().getTime());
        File dir = new File(r.getUC().getDataFolder() + "/Errors");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(r.getUC().getDataFolder() + "/Errors", time + ".txt");
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
        out.println("Sponge version: " + r.getGame().getPlatform().getName() + "-" + r.getGame().getPlatform().getVersion());
        out.println("UltimateCore version: " + UltimateCore.version);
        out.println("Plugins loaded (" + r.getGame().getPluginManager().getPlugins().size() + "): " + Arrays.asList(r.getGame().getPluginManager().getPlugins()));
        out.println("Java version: " + System.getProperty("java.version"));
        out.println("OS info: " + System.getProperty("os.arch") + ", " + System.getProperty("os.name") + ", " +
                System.getProperty("os.version"));
        out.println("Online mode: " + r.getGame().getServer().getOnlineMode());
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
        r.log(" ");
        r.log(TextColors.DARK_RED + "=========================================================");
        r.log(TextColors.RED + "UltimateCore has run into an error ");
        r.log(TextColors.RED + "Please report your error on ");
        r.log(TextColors.YELLOW + "http://dev.bukkit.org/bukkit-plugins/ultimate_core/create-ticket");
        r.log(TextColors.RED + "Include the file: ");
        r.log(TextColors.YELLOW + "plugins/UltimateCore/Errors/" + time + ".txt ");
        /*r.log(TextColors.RED + "Bukkit version: " + Bukkit.getServer().getVersion());
         r.log(TextColors.RED + "UltimateCore version: " + Bukkit.getPluginManager().getPlugin("UltimateCore")
         .getDescription().getVersion());
         r.log(TextColors.RED + "Plugins loaded (" + Bukkit.getPluginManager().getPlugins().length + "): " + Arrays
         .asList(Bukkit.getPluginManager().getPlugins()));
         r.log(TextColors.RED + "Java version: " + System.getProperty("java.version"));
         r.log(TextColors.RED + "OS info: " + System.getProperty("os.arch") + ", " + System.getProperty("os.name") +
         ", " + System.getProperty("os.version"));
         r.log(TextColors.RED + "Error message: " + t.getMessage());
         r.log(TextColors.RED + "UltimateCore message: " + s);*/
        r.log(TextColors.DARK_RED + "=========================================================");
        if (t instanceof Exception) {
            r.log(TextColors.RED + "Stacktrace: ");
            t.printStackTrace();
            r.log(TextColors.DARK_RED + "=========================================================");
        }
        r.log(" ");
    }
}
