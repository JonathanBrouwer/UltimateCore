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

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.config.GeneralConfig;
import com.goebl.david.Response;
import com.goebl.david.Webb;
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
        String error = getStackTrace(t);

        //FILE
        final String time = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(Calendar.getInstance().getTime());
        //CONSOLE
        Sponge.getServer().getConsole().sendMessage(Text.of(" "));
        Messages.log(Text.of(TextColors.DARK_RED, "========================================================="));
        Messages.log(Text.of(TextColors.RED, "UltimateCore has run into an error "));
        Messages.log(Text.of(TextColors.RED, "Please report your error on "));
        Messages.log(Text.of(TextColors.YELLOW, "https://github.com/Bammerbom/UltimateCore/issues"));
        Messages.log(Text.of(TextColors.RED, "Include the file: "));
        Messages.log(Text.of(TextColors.YELLOW, "config/ultimatecore/errors/" + time + ".txt "));
        Messages.log(Text.of(TextColors.DARK_RED, "========================================================="));
        Messages.log(Text.of(TextColors.RED, "Stacktrace: "));
        t.printStackTrace();
        Messages.log(Text.of(TextColors.DARK_RED, "========================================================="));

        Sponge.getServer().getConsole().sendMessage(Text.of(" "));
        //SEND TO UC
        if (!GeneralConfig.get().getNode("errors", "enabled").getBoolean()) {
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
            writer.append("Stacktrace: \n" + getStackTrace(t) + "\n");
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
                    writer.append("Stacktrace: \n" + getStackTrace(t) + "\n");
                    writer.append("=======================================");
                    countdown = System.currentTimeMillis();

                    while (System.currentTimeMillis() < countdown + 10000) {
                        try {
                            Thread.currentThread().wait(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }

                    //File
                    String msg = writer.toString();
                    File dir = new File(UltimateCore.get().getDataFolder() + "/Errors");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    File file = new File(UltimateCore.get().getDataFolder() + "/Errors", time + ".txt");
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
                    Response<String> rtrn = webb.post("http://ultimatecore.org/postrequest/error_report.php").param("server_id", ServerID.getUUID().toString()).param("error_log", msg)
                            .asString();

                    if (rtrn.getBody() != null && rtrn.getBody().equalsIgnoreCase("true")) {
                        Messages.log("SEND ERROR SUCCESSFULLY");
                    } else {
                        Messages.log("SENDING ERROR FAILED (" + rtrn.getStatusCode() + " / " + rtrn.getResponseMessage() + " / " + rtrn.getBody() + ")");
                    }

                    countdown = null;
                    writer = null;
                }
            });
            tr.start();
        }
    }

    private static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
