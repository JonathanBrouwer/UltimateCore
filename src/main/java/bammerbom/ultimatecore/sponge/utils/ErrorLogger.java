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
package bammerbom.ultimatecore.sponge.utils;

import bammerbom.ultimatecore.sponge.r;
import bammerbom.ultimatecore.sponge.resources.utils.ServerIDUtil;
import com.goebl.david.Response;
import com.goebl.david.Webb;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ErrorLogger {

    static StringWriter writer = null;
    static Long countdown = null;

    public static void log(final Throwable t, final String s) {
        //Special exceptions
        String error = ExceptionUtils.getFullStackTrace(t);

        //FILE
        final String time = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(Calendar.getInstance().getTime());
        //CONSOLE
        Sponge.getServer().getConsole().sendMessage(Text.of(" "));
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
        Sponge.getServer().getConsole().sendMessage(Text.of(" "));
        //SEND TO UC
        if (!r.getCnfg().getBoolean("ErrorSend")) {
            return;
        }
        if (writer != null) {
            writer.append("\n");
            writer.append("=======================================\n");
            writer.append("UltimateCore has run into an error \n");
            writer.append("Please report your error on dev.bukkit.org/bukkit-plugins/ultimate_core/create-ticket\n");
            writer.append("Sponge version: " + Sponge.getPlatform().getImplementation().getName() + " - " + Sponge.getPlatform().getImplementation().getVersion() + " - " + Sponge
                    .getPlatform().getApi().getVersion() + "\n");
            writer.append("UltimateCore version: " + Sponge.getPluginManager().getPlugin("ultimatecore").get().getVersion() + "\n");
            writer.append("Plugins loaded (" + Sponge.getPluginManager().getPlugins().size() + "): " + Sponge.getPluginManager().getPlugins() + "\n");
            writer.append("Java version: " + System.getProperty("java.version") + "\n");
            writer.append("OS info: " + System.getProperty("os.arch") + ", " + System.getProperty("os.name") + ", " + System.getProperty("os.version") + "\n");
            writer.append("Online mode: " + Sponge.getServer().getOnlineMode() + "\n");
            writer.append("Time: " + time + "\n");
            writer.append("Error message: " + t.getMessage() + "\n");
            writer.append("UltimateCore message: " + s + "\n");
            writer.append("=======================================\n");
            writer.append("Stacktrace: \n" + ExceptionUtils.getStackTrace(t) + "\n");
            writer.append("=======================================\n");
            countdown = System.currentTimeMillis();
        } else {
            Thread tr = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (writer == null) {
                        writer = new StringWriter();
                    }
                    writer.append("=======================================\n");
                    writer.append("UltimateCore has run into an error \n");
                    writer.append("Please report your error on dev.bukkit.org/bukkit-plugins/ultimate_core/create-ticket\n");
                    writer.append("Sponge version: " + Sponge.getPlatform().getImplementation().getName() + " - " + Sponge.getPlatform().getImplementation().getVersion() + " - " + Sponge
                            .getPlatform().getApi().getVersion() + "\n");
                    writer.append("UltimateCore version: " + Sponge.getPluginManager().getPlugin("ultimatecore").get().getVersion() + "\n");
                    writer.append("Plugins loaded (" + Sponge.getPluginManager().getPlugins().size() + "): " + Sponge.getPluginManager().getPlugins() + "\n");
                    writer.append("Java version: " + System.getProperty("java.version") + "\n");
                    writer.append("OS info: " + System.getProperty("os.arch") + ", " + System.getProperty("os.name") + ", " + System.getProperty("os.version") + "\n");
                    writer.append("Online mode: " + Sponge.getServer().getOnlineMode() + "\n");
                    writer.append("Time: " + time + "\n");
                    writer.append("Error message: " + t.getMessage() + "\n");
                    writer.append("UltimateCore message: " + s + "\n");
                    writer.append("=======================================\n");
                    writer.append("Stacktrace: \n" + ExceptionUtils.getStackTrace(t) + "\n");
                    writer.append("=======================================");
                    countdown = System.currentTimeMillis();

                    while (System.currentTimeMillis() < countdown + 10000) {
                        try {
                            test.wait(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }

                    //File
                    String msg = writer.toString();
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
                    out.write(msg);
                    try {
                        outFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //
                    Webb webb = Webb.create();
                    Response<String> rtrn = webb.post("http://ultimatecore.org/postrequest/error_report.php").param("server_id", ServerIDUtil.getUUID().toString()).param("error_log", msg)
                            .asString();

                    if (rtrn.getBody() != null && rtrn.getBody().equalsIgnoreCase("true")) {
                        r.log("SEND ERROR SUCCESSFULLY");
                    } else {
                        r.log("SENDING ERROR FAILED (" + rtrn.getStatusCode() + " / " + rtrn.getResponseMessage() + " / " + rtrn.getBody() + ")");
                    }

                    countdown = null;
                    writer = null;
                }
            });
            tr.start();
        }
    }
}
