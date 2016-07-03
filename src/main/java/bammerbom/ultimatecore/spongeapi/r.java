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

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.configuration.Config;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.TextColorUtil;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

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
    //Methods
    static UltimateCore uc = UltimateCore.getInstance();
    static boolean debug = false;
    static Config cnfg = null;
    //Config end

    public static void start() {
        if (r.getCnfg().contains("Debug")) {
            setDebug(r.getCnfg().getBoolean("Debug"));
        }
    }

    public static UltimateUpdater getUpdater() {
        return updater;
    }

    public static void runUpdater() {
        if (!r.getCnfg().getBoolean("Updater.check")) {
            return;
        }
        Boolean dl = r.getCnfg().getBoolean("Updater.download");
        File file = Sponge.getPluginManager().getPlugin("ultimatecore").get().getSource().get().toFile();
        updater = new UltimateUpdater(66979, file, dl ? UltimateUpdater.UpdateType.DEFAULT : UltimateUpdater.UpdateType.NO_DOWNLOAD, true);
        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                updater.waitForThread();
                try {
                    if (updater != null && updater.getResult() != null && updater.getResult().equals(UltimateUpdater.UpdateResult.UPDATE_AVAILABLE)) {
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

    //Metrics end

    public static void runMetrics() {
        if (!r.getCnfg().getBoolean("Metrics")) {
            return;
        }
        try {
            metrics = new UltimateMetrics();
            metrics.start();
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to start metrics.");
        }
    }

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
        if (cnfg == null) {
            cnfg = new Config(new File(r.getUC().getDataFolder(), "config.yml"));
        }
        return cnfg;
    }

    public static UltimateCore getUC() {
        if (uc == null) {
            uc = UltimateCore.getInstance();
        }
        return uc;
    }

    protected static void removeUC() {
        uc = null;
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

    public static String getDisplayName(Object cs) {
        if (cs instanceof Player) {
            return UC.getPlayer((Player) cs).getDisplayName();
        } else if (cs instanceof User) {
            return ((User) cs).getName();
        } else if (cs instanceof CommandSource) {
            return ((CommandSource) cs).getName();
        } else {
            return cs.toString();
        }
    }

    public static boolean perm(CommandSource cs, String perm, Boolean message) {
        Boolean hasperm = perm(cs, perm);
        if (hasperm == false && message == true) {
            r.sendMes(cs, "noPermissions");
        }
        return hasperm;
    }

    private static boolean perm(CommandSource cs, String perm) {
        return cs.hasPermission(perm);
        //        if (!Sponge.getServiceManager().provide(PermissionService.class).isPresent()) {
        //            return;
        //        }
        //        p.has
        //        PermissionService ps = Sponge.getServiceManager().provide(PermissionService.class).get();
        //        if (p.get(Keys.LEVEL)) {
        //            r.debug("Checked " + p.getName() + " for " + perm + ", returned true. (1)");
        //            return true;
        //        }
        //        if (r.getVault() != null && r.getVault().getPermission() != null && !r.getVault().getPermission().getName().equals("SuperPerms")) {
        //            r.debug("Checked " + p.getName() + " for " + perm + ", returned " + r.getVault().getPermission().has(p, perm) + ". (2)");
        //            return r.getVault().getPermission().has(p, perm);
        //        } else {
        //            if (def == true) {
        //                r.debug("Checked " + p.getName() + " for " + perm + ", returned true. (3)");
        //                return true;
        //            }
        //            r.debug("Checked " + p.getName() + " for " + perm + ", returned " + p.hasPermission(perm) + ". (4)");
        //            return p.hasPermission(perm);
        //        }
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
            enC.load(r.getResource("Messages/EN.properties"));
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
                cuC.load(r.getResource("Messages/" + FilenameUtils.getBaseName(UltimateFileLoader.LANGf.getName()) + ".properties"));
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

    public static void setColors() {
        String c1 = r.getCnfg().getString("Chat.Default");
        String c2 = r.getCnfg().getString("Chat.Value");
        String c3 = r.getCnfg().getString("Chat.Error");

        positive = TextColorUtil.getColorByChar(c1.charAt(0)).orElse(TextColors.DARK_AQUA);
        neutral = TextColorUtil.getColorByChar(c2.charAt(0)).orElse(TextColors.AQUA);
        negative = TextColorUtil.getColorByChar(c3.charAt(0)).orElse(TextColors.RED);
    }

    public static Text mes(String padMessage, Object... repl) {
        if (cu.map.containsKey(padMessage)) {
            String a = r.positive + TextColorUtil.translateAlternate(cu.getProperty(padMessage).replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "")
                    .replace("\\\\n", "\n"));
            String repA = null;
            for (Object s : repl) {
                if (repA == null) {
                    repA = s.toString();
                } else {
                    a = a.replace(repA, s.toString());
                    repA = null;
                }
            }
            return Text.of(a);
        }
        if (en.map.containsKey(padMessage)) {
            String b = r.positive + TextColorUtil.translateAlternate(en.getProperty(padMessage).replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "")
                    .replace("\\\\n", "\n"));
            String repB = null;
            for (Object s : repl) {
                if (repB == null) {
                    repB = s.toString();
                } else {
                    b = b.replace(repB, s.toString());
                    repB = null;
                }
            }
            return Text.of(b);
        }
        r.log(r.negative + "Failed to find " + padMessage + " in Messages file.");
        return Text.of();
    }

    public static void sendMes(CommandSource cs, String padMessage, Object... repl) {
        cs.sendMessage(mes(padMessage, repl));
    }

    public static void log(Object message) {
        String logo = TextColorUtil.translateAlternate("&9[&bUC&9]&r");
        if (message == null) {
            r.log("null");
            return;
        }
        Sponge.getServer().getConsole().sendMessage(Text.of(logo + " " + TextColors.YELLOW + message.toString()));
    }

    public static void debug(Object message) {
        if (!debug) {
            return;
        }
        String logo = TextColorUtil.translateAlternate("&9[&bUC DEBUG&9]&r");
        if (message == null) {
            r.debug("null");
            return;
        }
        Sponge.getServer().getConsole().sendMessage(Text.of(logo + " " + TextColors.WHITE + message.toString()));
        //
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(Boolean value) {
        debug = value;
    }

    public static Player[] getOnlinePlayers() {
        List<Player> plz = (List<Player>) Sponge.getServer().getOnlinePlayers();
        return plz.toArray(new Player[plz.size()]);
    }

    public static GameProfile[] getGameProfiles() {
        Collection<GameProfile> plz = Sponge.getServer().getGameProfileManager().getCache().getProfiles();
        return plz.toArray(new GameProfile[plz.size()]);
    }

    public static Optional<Player> searchPlayer(String s) {
        Player found = null;
        String lowerName = s.toLowerCase();
        {
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
        }
        return Optional.ofNullable(found);
    }

    public static Optional<GameProfile> searchGameProfile(String s) {
        try {
            return Optional.of(Sponge.getServer().getGameProfileManager().get(s).get());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public static Optional<Player> searchPlayer(UUID u) {
        return Sponge.getServer().getPlayer(u);
    }

    public static Optional<GameProfile> searchGameProfile(UUID u) {
        try {
            return Optional.of(Sponge.getServer().getGameProfileManager().get(u).get());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    //
    public static boolean isInt(String check) {
        try {
            Integer.parseInt(check);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isUUID(String check) {
        try {
            UUID.fromString(check);
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

    public static List<Entity> getNearbyEntities(Location loc, double range) {
        List<Entity> rtrn = new ArrayList<>();
        for (Entity en : loc.getExtent().getEntities()) {
            if (LocationUtil.getDistance(en.getLocation(), loc) > range) {
                continue;
            }
            rtrn.add(en);
        }
        return rtrn;
    }

    public static List<Entity> getNearbyEntities(Entity en, double range) {
        return getNearbyEntities(en.getLocation(), range);
    }

    public static List<Player> getNearbyPlayers(Location loc, double range) {
        List<Player> rtrn = new ArrayList<>();
        for (Player en : r.getOnlinePlayers()) {
            if (!en.getLocation().getExtent().equals(loc.getExtent())) {
                continue;
            }
            if (LocationUtil.getDistance(en.getLocation(), loc) > range) {
                continue;
            }
            rtrn.add(en);
        }
        return rtrn;
    }

    public static List<Player> getNearbyPlayers(Entity en, double range) {
        return getNearbyPlayers(en.getLocation(), range);
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
        values.addAll(Sponge.getGame().getRegistry().getAllOf(CatalogTypes.TEXT_COLOR));
        return values.get(ra.nextInt(values.size()));
    }

    public static double round(double value, int places) {
        if (places < 0) {
            return value;
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static BigDecimal round(BigDecimal bd, int places) {
        if (places < 0) {
            return bd;
        }

        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd;
    }

    public static void saveResource(String resourcePath, boolean replace) {
        if ((resourcePath == null) || (resourcePath.equals(""))) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found");
        }
        File outFile = new File(getUC().getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(getUC().getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        try {
            if ((!outFile.exists()) || (replace)) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte['?'];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                r.log("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            r.log("Could not save " + outFile.getName() + " to " + outFile);
            ex.printStackTrace();
        }
    }

    public static InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }
        try {
            URL url = r.getUC().getClass().getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            }
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException localIOException) {
        }
        return null;
    }

    public static class ExtendedProperties {

        private static final String keyValueSeparators = "=: \t\r\n\f";
        private static final String strictKeyValueSeparators = "=:";
        private static final String specialSaveChars = "=: \t\r\n\f#!";
        private static final String whiteSpaceChars = " \t\r\n\f";
        protected Map<String, String> map;
        protected String enc;
        private List<String> order;

        public ExtendedProperties() {
            this("ISO-8859-1");
        }

        public ExtendedProperties(String enc) {
            this.enc = enc;
            map = new HashMap<>();
            order = new ArrayList<>();
        }

        private static void writeln(BufferedWriter bw, String s) throws IOException {
            bw.write(s);
            bw.newLine();
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
                            line = loppedLine + nextLine;
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

            for (int x = 0; x < len; ) {
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
            for (Iterator<String> iter = order.iterator(); iter.hasNext(); ) {
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
            for (Iterator<String> iter = newKeys.iterator(); iter.hasNext(); ) {
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
            for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
                String key = i.next();
                h.put(key, map.get(key));
            }
            return h.keySet().iterator();
        }
    }
}
