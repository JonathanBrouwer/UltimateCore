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
package bammerbom.ultimatecore.spongeapi;

import static bammerbom.ultimatecore.spongeapi.UltimateCore.logger;
import bammerbom.ultimatecore.spongeapi.UltimateUpdater.UpdateResult;
import bammerbom.ultimatecore.spongeapi.UltimateUpdater.UpdateType;
import bammerbom.ultimatecore.spongeapi.configuration.Config;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import com.google.common.base.Optional;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.player.User;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.context.Context;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.command.CommandSource;

public class r {

    //Colors
    public static TextColor negative = TextColors.RED;
    public static TextColor neutral = TextColors.AQUA;
    public static TextColor positive = TextColors.DARK_AQUA;

    //Updater
    public static UltimateUpdater updater;
    //Updater end
    //Metrics
    public static UltimateMetrics metrics;
    public static ExtendedProperties en = null;
    public static ExtendedProperties cu = null;
    public static Random ra = new Random();
    //Vault end
    static boolean debug = false;
    //Config end

    static {
    }

    public static UltimateUpdater getUpdater() {
        return updater;
    }

    public static Game getGame() {
        return UltimateCore.game;
    }

    public static GameRegistry getRegistry() {
        return getGame().getRegistry();
    }

    public static void runUpdater() {
        if (!r.getCnfg().getBoolean("Updater.check")) {
            return;
        }
        Boolean dl = r.getCnfg().getBoolean("Updater.download");
        updater = new UltimateUpdater(r.getUC(), 66979, r.getUC().getDataFolder(), dl ? UpdateType.DEFAULT : UpdateType.NO_DOWNLOAD, true);
        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                updater.waitForThread();
                try {
                    if (updater != null && updater.getResult() != null && updater.getResult().equals(UpdateResult.UPDATE_AVAILABLE)) {
                        r.log("There is an update available for UltimateCore.");
                        r.log("Use /uc update to update UltimateCore.");
                    }
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "Update check failed.");
                }
            }
        });
        thr.setName("UltimateUpdater");
        thr.start();
    }

    public static void runMetrics() {
        if (!r.getCnfg().getBoolean("Metrics")) {
            return;
        }
        try {
            //metrics = new UltimateMetrics(getUC()); //TODO
            //metrics.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Metrics end
    //Config
    public static Config getCnfg() {
        if (!new File(r.getUC().getDataFolder(), "config.yml").exists()) {
            if (new File(r.getUC().getDataFolder(), "config_CORRUPT.yml").exists()) {
                String filename = "config_CORRUPT.yml";
                Integer i = 1;
                while (new File(r.getUC().getDataFolder(), filename).exists()) {
                    i++;
                    filename = "config_CORRUPT" + i + ".yml";
                }
                i--;
                if (!(i == 0)) {
                    filename = "config_CORRUPT" + i + ".yml";
                }
                r.log(TextColors.GOLD + "---------------------------------------------------");
                r.log(TextColors.AQUA + "Config file failed to load, creating a new file...");
                r.log(TextColors.AQUA + "Corrupt file saved as " + TextColors.YELLOW + filename);
                r.log(TextColors.GOLD + "----------------------------------------------------");
            } else {
                r.log(TextColors.GOLD + "-------------------------------------------------");
                r.log(TextColors.AQUA + "Config file doesn't exist, creating a new file...");
                r.log(TextColors.GOLD + "-------------------------------------------------");
            }
            UltimateFileLoader.Enable();
        }
        return new Config(new File(r.getUC().getDataFolder(), "config.yml"));
    }

    public static UltimateCore getUC() {
        return UltimateCore.getInstance();
    }

    protected static void removeUC() {
        updater = null;
        metrics = null;
    }

    public static boolean isPlayer(CommandSource cs) {
        if (cs instanceof Player) {
            return true;
        }
        r.sendMes(cs, "notPlayer");
        return false;
    }

    public static boolean perm(CommandSource cs, String perm, Boolean def, Boolean message) {
        if (!(cs instanceof Player)) {
            return true;
        }
        Player pl = (Player) cs;
        Boolean hasperm = perm(pl, perm, def);
        if (hasperm == false && message == true) {
            r.sendMes(cs, "noPermissions");
        }
        return hasperm;
    }

    private static boolean perm(Player p, String perm, Boolean def) {
        if (false) { //TODO is op
            r.debug("Checked " + p.getName() + " for " + perm + ", returned true. (1)");
            return true;
        }
        Optional<PermissionService> op = r.getGame().getServiceManager().provide(PermissionService.class);
        if (op.isPresent() && def) {
            op.get().getDefaultData().setPermission(new HashSet<Context>(), perm, Tristate.TRUE); //TODO Contexts?
        }
        r.debug("Checked " + p.getName() + " for " + perm + ", returned " + p.hasPermission(perm) + ". (4)");
        return p.hasPermission(perm);
    }

    public static boolean checkArgs(Object[] args, Integer numb) {
        return args.length >= numb + 1;
    }

    public static String getFinalArg(String[] args, int start) {
        StringBuilder bldr = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i != start) {
                bldr.append(" ");
            }
            bldr.append(args[i]);
        }
        return bldr.toString();
    }

    public static void enableMES() {
        try {
            en = new ExtendedProperties("UTF-8");
            FileInputStream inen = new FileInputStream(UltimateFileLoader.ENf);
            en.load(inen);
            inen.close();
            ExtendedProperties enC = new ExtendedProperties("UTF-8");
            enC.load(r.getUC().getClass().getClassLoader().getResourceAsStream("Messages/EN.properties"));
            Boolean a = false;
            for (String s : enC.map.keySet()) {
                if (!en.map.containsKey(s)) {
                    en.setProperty(s, enC.getProperty(s));
                    a = true;
                }
            }
            if (a) {
                en.save(new FileOutputStream(UltimateFileLoader.ENf), "UltimateCore messages file.");
            }
            //
            try {
                cu = new ExtendedProperties("UTF-8");
                FileInputStream incu = new FileInputStream(UltimateFileLoader.LANGf);
                cu.load(incu);
                incu.close();
                ExtendedProperties cuC = new ExtendedProperties("UTF-8");
                cuC.load(r.getUC().getClass().getClassLoader().getResourceAsStream("Messages/" + FilenameUtils.getBaseName(UltimateFileLoader.LANGf.getName()) + ".properties"));
                Boolean b = false;
                for (String s : cuC.map.keySet()) {
                    if (!cu.map.containsKey(s)) {
                        cu.setProperty(s, cuC.getProperty(s));
                        b = true;
                    }
                }
                if (b) {
                    en.save(new FileOutputStream(UltimateFileLoader.ENf), "UltimateCore messages file.");
                }
            } catch (NullPointerException | FileNotFoundException ex) {
                //IGNORE: CUSTOM MESSAGES FILE
            }
        } catch (IOException ex) {
            ErrorLogger.log(ex, "Failed to load language files.");
        }
    }

    public static String translateAlternateColorCodes(String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if ((b[i] == '&') && ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[(i + 1)]) > -1)) {
                b[i] = '§';
                b[(i + 1)] = Character.toLowerCase(b[(i + 1)]);
            }
        }
        return new String(b);
    }

    public static String mes(String padMessage, Object... repl) {
        if (cu.map.containsKey(padMessage)) {
            String a = r.positive + r.translateAlternateColorCodes(cu.getProperty(padMessage)
                    .replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "").replace("\\\\n", "\n"));
            String repA = null;
            for (Object s : repl) {
                if (repA == null) {
                    repA = s.toString();
                } else {
                    a = a.replace(repA, s.toString());
                    repA = null;
                }
            }
            return a;
        }
        if (en.map.containsKey(padMessage)) {
            String b = r.positive + r.translateAlternateColorCodes(en.getProperty(padMessage)
                    .replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "").replace("\\\\n", "\n"));
            String repB = null;
            for (Object s : repl) {
                if (repB == null) {
                    repB = s.toString();
                } else {
                    b = b.replace(repB, s.toString());
                    repB = null;
                }
            }
            return b;
        }
        r.log(r.negative + "Failed to find " + padMessage + " in Messages file.");
        return "";
    }

    public static void sendMes(CommandSource cs, String padMessage, Object... repl) {
        String mes = mes(padMessage, repl);
        cs.sendMessage(mes);
    }

    public static void log(Object message) {
        String logo = r.translateAlternateColorCodes("&9[&bUC&9]&r");
        if (message == null) {
            r.log("null");
            return;
        }
        logger.info(logo + " " + TextColors.YELLOW + message.toString());
    }

    public static void debug(Object message) {
        if (!debug) {
            return;
        }
        log(TextColors.WHITE + message.toString());
        //
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(Boolean value) {
        debug = value;
    }

    public static Collection<Player> getOnlinePlayers() {
        if (!r.getGame().getServer().isPresent()) {
            return new ArrayList<>();
        }
        return r.getGame().getServer().get().getOnlinePlayers();

    }

    public static Collection<User> getOfflinePlayers() {
        return null;
        /*
         if (!r.getGame().getServer().isPresent()) {
         return new ArrayList<>();
         }
         return r.getGame().getServer().get().
         */
    } //TODO OFFLINE PLAYERS

    public static Player searchPlayer(String s) {
        Player found = null;
        String lowerName = s.toLowerCase();
        int delta = 2147483647;
        for (Player player : getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(lowerName)) {
                int curDelta = player.getName().length() - lowerName.length();
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                }
                if (curDelta == 0) {
                    break;
                }
            }
        }
        return found;
    }

    public static User searchOfflinePlayer(String s) {
        return null;
        //return Bukkit.getOfflinePlayer(s);
    } //TODO OFFLINE PLAYERS

    public static Player searchPlayer(UUID u) {
        if (!r.getGame().getServer().isPresent()) {
            return null;
        }
        if (!r.getGame().getServer().get().getPlayer(u).isPresent()) {
            return null;
        }
        return r.getGame().getServer().get().getPlayer(u).get();
    }

    public static User searchOfflinePlayer(UUID u) {
        return null;
    }
    /*
     if (!r.getGame().getServer().isPresent()) {
     return null;
     }
     if (!r.getGame().getServer().get().getPlayer(u).isPresent()) {
     return null;
     }
     return r.getGame().getServer().get().getPlayer(u).get();
     }*/ //TODO OFFLINE PLAYERS
    //

    public static boolean isInt(String check) {
        try {
            Integer.parseInt(check);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    //
    public static Integer normalize(Integer a, Integer b, Integer c) {
        if (a < b) {
            a = b;
        }
        if (a > c) {
            a = c;
        }
        return a;
    }

    public static Double normalize(Double a, Double b, Double c) {
        if (a < b) {
            a = b;
        }
        if (a > c) {
            a = c;
        }
        return a;
    }

    public static TextColor getRandomTextColor() {
        ArrayList<TextColor> values = new ArrayList<>();
        for (TextColor c : TextColors.getValues()) {
            if (!c.isReset()) {
                values.add(c);
            }
        }
        return values.get(ra.nextInt(values.size()));
    }

    public static String stripColor(String input) {
        if (input == null) {
            return null;
        }
        return Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]").matcher(input).replaceAll("");
    }

    /*public static String getTown(Player p) {
     if (!Bukkit.getPluginManager().isPluginEnabled("Towny")) {
     return null;
     }
     Towny t = (Towny) Bukkit.getServer().getPluginManager().getPlugin("Towny");
     if (t == null) {
     return null;
     }
     Resident r;
     try {
     r = TownyUniverse.getDataSource().getResident(p.getName());
     } catch (Exception ex) {
     return null;
     }
     try {
     return r.getTown().getName();
     } catch (Exception ex) {
     return null;
     }
     }

     public static String getFaction(Player p) {
     if (!Bukkit.getPluginManager().isPluginEnabled("Factions")) {
     return null;
     }
     try {
     return MPlayer.get(p).getFaction().getName();
     } catch (Exception ex) {
     return null;
     }
     }*/
    public static class ExtendedProperties {

        private static final String keyValueSeparators = "=: \t\r\n\f";
        private static final String strictKeyValueSeparators = "=:";
        private static final String specialSaveChars = "=: \t\r\n\f#!";
        private static final String whiteSpaceChars = " \t\r\n\f";

        private static void writeln(BufferedWriter bw, String s) throws IOException {
            bw.write(s);
            bw.newLine();
        }
        protected Map<String, String> map;
        private List<String> order;
        protected String enc;

        public ExtendedProperties() {
            this("ISO-8859-1");
        }

        public ExtendedProperties(String enc) {
            this.enc = enc;
            map = new HashMap<>();
            order = new ArrayList<>();
        }

        public synchronized void load(InputStream inStream) throws IOException {
            BufferedReader in;
            try {
                in = new BufferedReader(new InputStreamReader(inStream, enc));
            } catch (Exception ex) {
                //SYSTEM DOESNT SUPPORT ANSI AS UTF-8
                in = new BufferedReader(new InputStreamReader(inStream));
            }
            while (true) {
                // Get next line
                String line = in.readLine();
                if (line == null) {
                    return;
                }

                if (line.length() > 0) {

                    // Find start of key
                    int len = line.length();
                    int keyStart;
                    for (keyStart = 0; keyStart < len; keyStart++) {
                        if (whiteSpaceChars.indexOf(line.charAt(keyStart)) == -1) {
                            break;
                        }
                    }

                    // Blank lines are ignored
                    if (keyStart == len) {
                        continue;
                    }

                    // Continue lines that end in slashes if they are not comments
                    char firstChar = line.charAt(keyStart);
                    if ((firstChar == '#') || (firstChar == '!')) {
                        // add comment to order list
                        order.add(line.substring(keyStart));
                    } else {
                        while (continueLine(line)) {
                            String nextLine = in.readLine();
                            if (nextLine == null) {
                                nextLine = "";
                            }
                            String loppedLine = line.substring(0, len - 1);
                            // Advance beyond whitespace on new line
                            int startIndex;
                            for (startIndex = 0; startIndex < nextLine.length(); startIndex++) {
                                if (whiteSpaceChars.indexOf(nextLine.charAt(startIndex)) == -1) {
                                    break;
                                }
                            }
                            nextLine = nextLine.substring(startIndex, nextLine.length());
                            line = new String(loppedLine + nextLine);
                            len = line.length();
                        }

                        // Find separation between key and value
                        int separatorIndex;
                        for (separatorIndex = keyStart; separatorIndex < len; separatorIndex++) {
                            char currentChar = line.charAt(separatorIndex);
                            if (currentChar == '\\') {
                                separatorIndex++;
                            } else if (keyValueSeparators.indexOf(currentChar) != -1) {
                                break;
                            }
                        }

                        // Skip over whitespace after key if any
                        int valueIndex;
                        for (valueIndex = separatorIndex; valueIndex < len; valueIndex++) {
                            if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1) {
                                break;
                            }
                        }

                        // Skip over one non whitespace key value separators if any
                        if (valueIndex < len) {
                            if (strictKeyValueSeparators.indexOf(line.charAt(valueIndex)) != -1) {
                                valueIndex++;
                            }
                        }

                        // Skip over white space after other separators if any
                        while (valueIndex < len) {
                            if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1) {
                                break;
                            }
                            valueIndex++;
                        }
                        String key = line.substring(keyStart, separatorIndex);
                        String value = (separatorIndex < len) ? line.substring(valueIndex, len) : "";

                        // Convert then store key and value
                        key = loadConvert(key);
                        value = loadConvert(value);
                        map.put(key, value);
                        // add key value to order list
                        order.add(key);
                    }
                }
            }
        }

        /*
         * Returns true if the given line is a line that must be appended to the next line
         */
        private boolean continueLine(String line) {
            int slashCount = 0;
            int index = line.length() - 1;
            while ((index >= 0) && (line.charAt(index--) == '\\')) {
                slashCount++;
            }
            return (slashCount % 2 == 1);
        }

        /*
         * Converts encoded &#92;uxxxx to unicode chars
         * and changes special saved chars to their original forms
         */
        private String loadConvert(String theString) {
            char aChar;
            int len = theString.length();
            StringBuilder outBuffer = new StringBuilder(len);

            for (int x = 0; x < len;) {
                aChar = theString.charAt(x++);
                if (aChar == '\\') {
                    aChar = theString.charAt(x++);
                    if (aChar == 'u') {
                        // Read the xxxx
                        int value = 0;
                        for (int i = 0; i < 4; i++) {
                            aChar = theString.charAt(x++);
                            switch (aChar) {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    value = (value << 4) + aChar - '0';
                                    break;
                                case 'a':
                                case 'b':
                                case 'c':
                                case 'd':
                                case 'e':
                                case 'f':
                                    value = (value << 4) + 10 + aChar - 'a';
                                    break;
                                case 'A':
                                case 'B':
                                case 'C':
                                case 'D':
                                case 'E':
                                case 'F':
                                    value = (value << 4) + 10 + aChar - 'A';
                                    break;
                                default:
                                    throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                            }
                        }
                        outBuffer.append((char) value);
                    } else {
                        if (aChar == 't') {
                            aChar = '\t';
                        } else if (aChar == 'r') {
                            aChar = '\r';
                        } else if (aChar == 'n') {
                            aChar = '\n';
                        } else if (aChar == 'f') {
                            aChar = '\f';
                        }
                        outBuffer.append(aChar);
                    }
                } else {
                    outBuffer.append(aChar);
                }
            }
            return outBuffer.toString();
        }

        /*
         * writes out any of the characters in specialSaveChars with a preceding slash
         */
        private String saveConvert(String theString, boolean escapeSpace) {
            int len = theString.length();
            StringBuilder outBuffer = new StringBuilder(len * 2);

            for (int x = 0; x < len; x++) {
                char aChar = theString.charAt(x);
                switch (aChar) {
                    case ' ':
                        if (x == 0 || escapeSpace) {
                            outBuffer.append('\\');
                        }

                        outBuffer.append(' ');
                        break;
                    case '\\':
                        outBuffer.append('\\');
                        outBuffer.append('\\');
                        break;
                    case '\t':
                        outBuffer.append('\\');
                        outBuffer.append('t');
                        break;
                    case '\n':
                        outBuffer.append('\\');
                        outBuffer.append('n');
                        break;
                    case '\r':
                        outBuffer.append('\\');
                        outBuffer.append('r');
                        break;
                    case '\f':
                        outBuffer.append('\\');
                        outBuffer.append('f');
                        break;
                    default:
                        if (specialSaveChars.indexOf(aChar) != -1) {
                            outBuffer.append('\\');
                        }
                        outBuffer.append(aChar);
                }
            }
            return outBuffer.toString();
        }

        public synchronized void save(OutputStream out, String header) throws IOException {
            BufferedWriter awriter;
            awriter = new BufferedWriter(new OutputStreamWriter(out, enc));
            if (header != null) {
                writeln(awriter, "#" + header);
            }
            writeln(awriter, "#" + new Date().toString());

            Set<String> newKeys = new HashSet<>(map.keySet());
            for (Iterator<String> iter = order.iterator(); iter.hasNext();) {
                String str = iter.next();
                if ((str.charAt(0) == '#') || (str.charAt(0) == '!')) {
                    writeln(awriter, str);
                } else {
                    if (newKeys.contains(str)) {
                        String key = saveConvert(str, true);
                        String val = saveConvert(map.get(key), false);
                        writeln(awriter, key + "=" + val);
                        newKeys.remove(str);
                    }
                }
            }
            for (Iterator<String> iter = newKeys.iterator(); iter.hasNext();) {
                String key = saveConvert(iter.next(), true);
                String val = saveConvert(map.get(key), false);
                writeln(awriter, key + "=" + val);
            }

            awriter.flush();
        }

        public String getProperty(String key) {
            return map.get(key);
        }

        public String getProperty(String key, String defaultValue) {
            String val = getProperty(key);
            return (val == null) ? defaultValue : val;
        }

        public synchronized String setProperty(String key, String value) {
            return map.put(key, value);
        }

        public Iterator<String> propertyNames() {
            Map<String, String> h = new HashMap<>();
            for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) {
                String key = i.next();
                h.put(key, map.get(key));
            }
            return h.keySet().iterator();
        }
    }

}
