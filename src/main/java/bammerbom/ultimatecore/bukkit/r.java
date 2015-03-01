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
package bammerbom.ultimatecore.bukkit;

import bammerbom.ultimatecore.bukkit.UltimateUpdater.UpdateResult;
import bammerbom.ultimatecore.bukkit.UltimateUpdater.UpdateType;
import bammerbom.ultimatecore.bukkit.configuration.Config;
import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import com.massivecraft.factions.entity.MPlayer;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import java.io.*;
import java.util.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class r {

    //Colors
    public static ChatColor negative = ChatColor.RED;
    public static ChatColor neutral = ChatColor.AQUA;
    public static ChatColor positive = ChatColor.DARK_AQUA;

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
    //Config end
    //Vault
    private static Vault vault;
    private static Object prom;

    static {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            vault = new r().new Vault();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            prom = com.comphenix.protocol.ProtocolLibrary.getProtocolManager();
        }
    }

    public static UltimateUpdater getUpdater() {
        return updater;
    }

    public static Object getProtocolManager() {
        return prom;
    }

    public static void runUpdater() {
        if (!r.getCnfg().getBoolean("Updater.check")) {
            return;
        }
        Boolean dl = r.getCnfg().getBoolean("Updater.download");
        updater = new UltimateUpdater(r.getUC(), 66979, UltimateCore.getPluginFile(), dl ? UpdateType.DEFAULT : UpdateType.NO_DOWNLOAD, true);
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
            metrics = new UltimateMetrics(getUC());
            metrics.start();
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
                r.log(ChatColor.GOLD + "---------------------------------------------------");
                r.log(ChatColor.AQUA + "Config file failed to load, creating a new file...");
                r.log(ChatColor.AQUA + "Corrupt file saved as " + ChatColor.YELLOW + filename);
                r.log(ChatColor.GOLD + "----------------------------------------------------");
            } else {
                r.log(ChatColor.GOLD + "-------------------------------------------------");
                r.log(ChatColor.AQUA + "Config file doesn't exist, creating a new file...");
                r.log(ChatColor.GOLD + "-------------------------------------------------");
            }
            UltimateFileLoader.Enable();
        }
        return new Config(new File(r.getUC().getDataFolder(), "config.yml"));
    }

    public static Vault getVault() {
        return vault;
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
        vault = null;
    }

    public static boolean isPlayer(CommandSender cs) {
        if (cs instanceof Player) {
            return true;
        }
        r.sendMes(cs, "notPlayer");
        return false;
    }

    public static boolean perm(CommandSender cs, String perm, Boolean def, Boolean message) {
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
        if (p.isOp()) {
            r.debug("Checked " + p.getName() + " for " + perm + ", returned true. (1)");
            return true;
        }
        if (r.getVault() != null && r.getVault().getPermission() != null && !r.getVault().getPermission().getName().equals("SuperPerms")) {
            r.debug("Checked " + p.getName() + " for " + perm + ", returned " + r.getVault().getPermission().has(p, perm) + ". (2)");
            return r.getVault().getPermission().has(p, perm);
        } else {
            if (def == true) {
                r.debug("Checked " + p.getName() + " for " + perm + ", returned true. (3)");
                return true;
            }
            r.debug("Checked " + p.getName() + " for " + perm + ", returned " + p.hasPermission(perm) + ". (4)");
            return p.hasPermission(perm);
        }
    }

    public static boolean checkArgs(Object[] args, Integer numb) {
        try {
            args[numb].equals("Ritja");
            return true;
        } catch (Exception e) {
            return false;
        }
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
            enC.load(r.getUC().getResource("Messages/EN.properties"));
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
                cuC.load(r.getUC().getResource("Messages/" + FilenameUtils.getBaseName(UltimateFileLoader.LANGf.getName()) + ".properties"));
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

    public static String mes(String padMessage, Object... repl) {
        if (cu.map.containsKey(padMessage)) {
            String a = r.positive + ChatColor.translateAlternateColorCodes('&', cu.getProperty(padMessage)
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
            String b = r.positive + ChatColor.translateAlternateColorCodes('&', en.getProperty(padMessage)
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

    public static void sendMes(CommandSender cs, String padMessage, Object... repl) {
        String mes = mes(padMessage, repl);
        cs.sendMessage(mes);
    }

    public static void log(Object message) {
        String logo = ChatColor.translateAlternateColorCodes('&', "&9[&bUC&9]&r");
        if (message == null) {
            r.log("null");
            return;
        }
        Bukkit.getConsoleSender().sendMessage(logo + " " + ChatColor.YELLOW + message.toString());
    }

    public static void debug(Object message) {
        if (!debug) {
            return;
        }
        log(ChatColor.WHITE + message.toString());
        //
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(Boolean value) {
        debug = value;
    }

    @SuppressWarnings("deprecation")
    public static Player[] getOnlinePlayers() {
        List<Player> plz = (List<Player>) Bukkit.getOnlinePlayers();
        return plz.toArray(new Player[plz.size()]);
    }

    public static List<Player> getOnlinePlayersL() {
        return (List<Player>) Bukkit.getOnlinePlayers();
    }

    public static OfflinePlayer[] getOfflinePlayers() {
        List<OfflinePlayer> plz = Arrays.asList(Bukkit.getOfflinePlayers());
        return plz.toArray(new OfflinePlayer[plz.size()]);
    }

    public static List<OfflinePlayer> getOfflinePlayersL() {
        return Arrays.asList(Bukkit.getOfflinePlayers());
    }

    public static Player searchPlayer(String s) {
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
        return found;
    }

    @SuppressWarnings("deprecation")
    public static OfflinePlayer searchOfflinePlayer(String s) {
        return Bukkit.getOfflinePlayer(s);
    }

    public static Player searchPlayer(UUID u) {
        return Bukkit.getPlayer(u);
    }

    public static OfflinePlayer searchOfflinePlayer(UUID u) {
        return Bukkit.getOfflinePlayer(u);
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

    public static ChatColor getRandomChatColor() {
        ArrayList<ChatColor> values = new ArrayList<>();
        for (ChatColor c : ChatColor.values()) {
            if (!c.isFormat()) {
                values.add(c);
            }
        }
        return values.get(ra.nextInt(values.size()));
    }

    public static String getTown(Player p) {
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
    }

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

    public class Vault {

        private Permission permission = null;
        private Chat chat = null;
        private Economy economy = null;

        public Vault() {
            if (Bukkit.getPluginManager().getPlugin("Vault") != null && Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                //Permissions
                RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
                if (permissionProvider != null) {
                    permission = permissionProvider.getProvider();
                }
                //Chat
                RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
                if (chatProvider != null) {
                    chat = chatProvider.getProvider();
                }
                RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
                if (economyProvider != null) {
                    economy = economyProvider.getProvider();
                }
            }
        }

        public Permission getPermission() {
            return permission;
        }

        public Chat getChat() {
            return chat;
        }

        public Economy getEconomy() {
            return economy;
        }

    }
}
