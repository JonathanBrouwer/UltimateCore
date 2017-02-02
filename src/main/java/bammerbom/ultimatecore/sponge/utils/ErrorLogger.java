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
import bammerbom.ultimatecore.sponge.api.config.utils.FileUtil;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class ErrorLogger {

    static StringWriter writer = null;
    static Long countdown = null;

    public static void log(final Throwable t, final String ucmessage) {
        //Special exceptions
        String exception = getStackTrace(t);

        //FILE
        final String time = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(Calendar.getInstance().getTime());
        HashMap<String, Object> stats = Stats.collect();
        stats.put("time", time + " / " + System.currentTimeMillis());

        File file = new File(UltimateCore.get().getDataFolder().toFile().getPath() + "/errors/", time + ".txt");
        List<String> lines = new ArrayList<>();
        stats.forEach((key, value) -> {
            lines.add("# " + key + ": " + value);
        });
        Collections.sort(lines);

        //Add custom information at the bottom
        lines.add("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        lines.add("# UC message: " + ucmessage);
        lines.add("# EX message: " + t.getMessage());
        lines.add("# Exception: " + exception);

        try {
            FileUtil.writeLines(file, lines);
        } catch (IOException e) {
            Messages.log("Failed to write file for error.");
        }

        //CONSOLE
        Sponge.getServer().getConsole().sendMessage(Text.of(" "));
        Messages.log(Text.of(TextColors.DARK_RED, "========================================================="));
        Messages.log(Text.of(TextColors.RED, "UltimateCore has run into an error "));
        Messages.log(Text.of(TextColors.RED, "Please report your error on "));
        Messages.log(Text.of(TextColors.YELLOW, "https://github.com/Bammerbom/UltimateCore/issues"));
        Messages.log(Text.of(TextColors.RED, "Include the file: "));
        Messages.log(Text.of(TextColors.YELLOW, "server-dir/ultimatecore/errors/" + time + ".txt "));
        Messages.log(Text.of(TextColors.DARK_RED, "========================================================="));
        Messages.log(Text.of(TextColors.RED, "Stacktrace: \n", Text.of(TextColors.YELLOW, exception)));
        Messages.log(Text.of(TextColors.DARK_RED, "========================================================="));

        //SEND TO UC
        if (!UltimateCore.get().getGeneralConfig().get().getNode("errors", "enabled").getBoolean()) {
            return;
        }


//        //
//        Webb webb = Webb.create();
//        Response<String> rtrn = webb.post("http://ultimatecore.org/postrequest/error.php").param("server_id", ServerID.getUUID().toString()).param("error_log", msg)
//                .asString();
//
//        if (rtrn.getBody() != null && rtrn.getBody().equalsIgnoreCase("true")) {
//            Messages.log("SEND ERROR SUCCESSFULLY");
//        } else {
//            Messages.log("SENDING ERROR FAILED (" + rtrn.getStatusCode() + " / " + rtrn.getResponseMessage() + " / " + rtrn.getBody() + ")");
//        }

    }

    private static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
