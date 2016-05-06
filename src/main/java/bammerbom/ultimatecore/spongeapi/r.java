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

import bammerbom.ultimatecore.spongeapi.UltimateUpdater.UpdateResult;
import bammerbom.ultimatecore.spongeapi.UltimateUpdater.UpdateType;
import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.configuration.Config;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.permission.option.OptionSubject;
import org.spongepowered.api.service.user.UserStorage;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.TextMessageException;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.world.Location;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

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
    //Config end
    //Vault
    //private static Vault vault;
    //private static Object prom;
    static ArrayList<String> defperms = new ArrayList<>();

    public static void start() {
        /*if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            prom = com.comphenix.protocol.ProtocolLibrary.getProtocolManager();
        }*/
        Sponge.getGame().getServiceManager().potentiallyProvide(PermissionService.class).executeWhenPresent(new Predicate<PermissionService>() {
            @Override
            public boolean apply(PermissionService input) {
                final SubjectData defaultData = input.getDefaultData();
                for (String perm : defperms) {
                    //TODO set default permissions
                    defaultData.setPermission(SubjectData.GLOBAL_CONTEXT, perm, Tristate.TRUE);
                }
                return true;
            }
        });
        if (r.getCnfg().contains("Debug")) {
            setDebug(r.getCnfg().getBoolean("Debug"));
        }
    }

    /*public static Object getProtocolManager() {
        return prom;
    }*/

    public static UltimateUpdater getUpdater() {
        return updater;
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

    /*public static Vault getVault() {
        return vault;
    }*/

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
        if (uc == null) {
            uc = UltimateCore.getInstance();
        }
        return uc;
    }

    protected static void removeUC() {
        uc = null;
        updater = null;
        metrics = null;
        //vault = null;
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
        if (!hasperm && message) {
            r.sendMes(cs, "noPermissions");
        }
        return hasperm;
    }

    private static boolean perm(Player p, String perm, Boolean def) {
        if (def) {
            if (!defperms.contains(perm)) {
                defperms.add(perm);
            }
            r.debug("Checked " + p.getName() + " for " + perm + ", returned true. (3)");
            return true;
        }
        r.debug("Checked " + p.getName() + " for " + perm + ", returned " + p.hasPermission(perm) + ". (4)");
        return p.hasPermission(perm);
    }

    public static boolean checkArgs(Object[] args, Integer numb) {
        return args.length >= numb + 1;
    }

    public static String getFinalArg(String[] args, int start) {
        StringBuilder bldr = new StringBuilder();
        for (int i = start;
             i < args.length;
             i++) {
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

    public static Game getGame() {
        return UltimateCore.game;
    }

    public static GameRegistry getRegistry() {
        return getGame().getRegistry();
    }

    public static PermissionService getPermission() {
        return getGame().getServiceManager().provide(PermissionService.class).orElse(null);
    }

    public static Text.Literal getDisplayName(Object cs) {
        if (cs instanceof Player) {
            return UC.getPlayer((Player) cs).getDisplayName();
        } else if (cs instanceof User) {
            return Texts.of(((User) cs).getName());
        } else if (cs instanceof CommandSource) {
            return Texts.of(((CommandSource) cs).getName());
        } else {
            return Texts.of(cs.toString());
        }
    }

    public static TextColor charToTextColor(String str) {
        if (str.startsWith("&") || str.startsWith("§")) {
            str = str.substring(1);
        }
        switch (str.toLowerCase()) {
            case "0":
                return TextColors.BLACK;
            case "1":
                return TextColors.DARK_BLUE;
            case "2":
                return TextColors.DARK_GREEN;
            case "3":
                return TextColors.DARK_AQUA;
            case "4":
                return TextColors.DARK_RED;
            case "5":
                return TextColors.DARK_PURPLE;
            case "6":
                return TextColors.GOLD;
            case "7":
                return TextColors.GRAY;
            case "8":
                return TextColors.DARK_GRAY;
            case "9":
                return TextColors.BLUE;
            case "a":
                return TextColors.GREEN;
            case "b":
                return TextColors.AQUA;
            case "c":
                return TextColors.RED;
            case "d":
                return TextColors.LIGHT_PURPLE;
            case "e":
                return TextColors.YELLOW;
            case "f":
                return TextColors.WHITE;
            case "r":
                return TextColors.RESET;
            default:
                return null;
        }
    }

    public static Character textColorToChar(TextColor color) {
        if (color.equals(TextColors.BLACK)) {
            return '0';
        } else if (color.equals(TextColors.DARK_BLUE)) {
            return '1';
        } else if (color.equals(TextColors.DARK_GREEN)) {
            return '2';
        } else if (color.equals(TextColors.DARK_AQUA)) {
            return '3';
        } else if (color.equals(TextColors.DARK_RED)) {
            return '4';
        } else if (color.equals(TextColors.DARK_PURPLE)) {
            return '5';
        } else if (color.equals(TextColors.GOLD)) {
            return '6';
        } else if (color.equals(TextColors.GRAY)) {
            return '7';
        } else if (color.equals(TextColors.DARK_GRAY)) {
            return '8';
        } else if (color.equals(TextColors.BLUE)) {
            return '9';
        } else if (color.equals(TextColors.GREEN)) {
            return 'a';
        } else if (color.equals(TextColors.AQUA)) {
            return 'b';
        } else if (color.equals(TextColors.RED)) {
            return 'c';
        } else if (color.equals(TextColors.LIGHT_PURPLE)) {
            return 'd';
        } else if (color.equals(TextColors.YELLOW)) {
            return 'e';
        } else if (color.equals(TextColors.WHITE)) {
            return 'f';
        } else if (color.equals(TextColors.RESET)) {
            return 'r';
        }
        return null;
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0;
             i < b.length - 1;
             i++) {
            if ((b[i] == altColorChar) && ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[(i + 1)]) > -1)) {
                b[i] = '§';
                b[(i + 1)] = Character.toLowerCase(b[(i + 1)]);
            }
        }
        return new String(b);
    }

    public static Text translateAlternateColorCodes(char altColorChar, Text text) {
        try {
            return Texts.legacy(altColorChar).from(translateAlternateColorCodes(altColorChar, Texts.legacy(altColorChar).to(text)));
        } catch (TextMessageException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String stripColor(String input) {
        if (input == null) {
            return null;
        }
        return Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]").matcher(input).replaceAll("");
    }

    public static void setColors() {
        String c1 = r.getCnfg().getString("Chat.Default");
        String c2 = r.getCnfg().getString("Chat.Value");
        String c3 = r.getCnfg().getString("Chat.Error");

        positive = charToTextColor(c1);
        neutral = charToTextColor(c2);
        negative = charToTextColor(c3);
        if (positive == null) {
            r.log("Failed to find color: " + c1);
            positive = TextColors.DARK_AQUA;
        }
        if (neutral == null) {
            r.log("Failed to find color: " + c1);
            positive = TextColors.AQUA;
        }
        if (negative == null) {
            r.log("Failed to find color: " + c1);
            positive = TextColors.RED;
        }
    }

    public static String mesRaw(String padMessage, Object... repl) {
        Text.Literal text = mes(padMessage, repl);
        return text.getContent();
    }

    public static Text.Literal mes(String padMessage, Object... repl) {
        if (cu.map.containsKey(padMessage)) {
            Text.Literal a = Texts
                    .of(r.positive + translateAlternateColorCodes('&', cu.getProperty(padMessage).replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "")
                            .replace("\\\\n", "\n")));
            String repA = null;
            for (Object s : repl) {
                if (repA == null) {
                    repA = s.toString();
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put(repA, s.toString());
                    a = (Text.Literal) Texts.format(a, map);
                    repA = null;
                }
            }
            return a;
        }
        if (en.map.containsKey(padMessage)) {
            Text.Literal a = Texts
                    .of(r.positive + translateAlternateColorCodes('&', en.getProperty(padMessage).replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "")
                            .replace("\\\\n", "\n")));
            String repA = null;
            for (Object s : repl) {
                if (repA == null) {
                    repA = s.toString();
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put(repA, s.toString());
                    a = (Text.Literal) Texts.format(a, map);
                    repA = null;
                }
            }
            return a;
        }
        r.log(r.negative + "Failed to find " + padMessage + " in Messages file.");
        return Texts.of("");
    }

    public static void sendMes(CommandSource cs, String padMessage, Object... repl) {
        cs.sendMessage(mes(padMessage, repl));
    }

    public static void log(Object message) {
        String logo = translateAlternateColorCodes('&', "&9[&bUC&9]&r");
        if (message == null) {
            r.log("null");
            return;
        }
        UltimateCore.logger.info(logo + " " + TextColors.YELLOW + message.toString());
    }

    public static void debug(Object message) {
        if (!debug) {
            return;
        }
        String logo = translateAlternateColorCodes('&', "&9[&bUC DEBUG&9]&r");
        if (message == null) {
            r.debug("null");
            return;
        }
        UltimateCore.logger.info(logo + " " + TextColors.WHITE + message.toString());
        //
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(Boolean value) {
        debug = value;
    }

    public static Player[] getOnlinePlayers() {
        List<Player> plz = (List<Player>) getGame().getServer().getOnlinePlayers();
        return plz.toArray(new Player[plz.size()]);
    }

    public static List<Player> getOnlinePlayersL() {
        return (List<Player>) getGame().getServer().getOnlinePlayers();
    }

    public static User[] getOfflinePlayers() {
        List<User> users = new ArrayList<>();
        for (GameProfile f : Sponge.getGame().getServiceManager().provide(UserStorage.class).get().getAll()) {
            users.add((User) f);
        }
        return (User[]) users.toArray();
    }

    public static List<User> getOfflinePlayersL() {
        List<User> users = new ArrayList<>();
        for (GameProfile f : Sponge.getGame().getServiceManager().provide(UserStorage.class).get().getAll()) {
            users.add((User) f);
        }
        return users;
    }

    public static Player searchPlayer(String s) {
        return Sponge.getGame().getServer().getPlayer(s).orElse(null);
    }

    public static User searchOfflinePlayer(String s) {
        return Sponge.getGame().getServiceManager().provide(UserStorage.class).get().get(s).get();
    }

    public static Player searchPlayer(UUID u) {
        return Sponge.getGame().getServer().getPlayer(u).orElse(null);
    }

    public static User searchOfflinePlayer(UUID u) {
        return Sponge.getGame().getServiceManager().provide(UserStorage.class).get().get(u).get();
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

    public static double distance(Location l1, Location l2) {
        if (l1 == null || l2 == null) {
            throw new IllegalArgumentException("Cannot measure distance to a null location");
        }
        if (l1.getExtent() != l2.getExtent()) {
            throw new IllegalArgumentException("Cannot measure distance between " + l1.getExtent() + " and " + l2.getExtent());
        }
        return Math.sqrt(((l1.getX() - l2.getX()) * (l1.getX() - l2.getX())) + ((l1.getY() - l2.getY()) * (l1.getY() - l2.getY())) + ((l1.getZ() - l2.getZ()) * (l1.getZ() - l2.getZ())));
    }

    public static List<Entity> getNearbyEntities(Location loc, double range) {

        Collection<Entity> entities = loc.getExtent().getEntities();
        TreeMap<Double, Entity> rtrn = new TreeMap<>();
        for (Entity en : entities) {
            if (distance(en.getLocation(), loc) > range) {
                continue;
            }
            rtrn.put(distance(en.getLocation(), loc), en);
        }
        return new ArrayList<>(rtrn.values());
    }

    public static List<Entity> getNearbyEntities(Entity en, double range) {
        return getNearbyEntities(en.getLocation(), range);
    }

    public static List<Player> getNearbyPlayers(Location loc, double range) {
        List<Player> entities = r.getOnlinePlayersL();
        TreeMap<Double, Player> rtrn = new TreeMap<>();
        for (Player en : entities) {
            if (!en.getLocation().getExtent().equals(loc.getExtent())) {
                continue;
            }
            if (distance(en.getLocation(), loc) > range) {
                continue;
            }
            rtrn.put(distance(en.getLocation(), loc), en);
        }
        return new ArrayList<>(rtrn.values());
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

    public static double round(double value, int places) {
        if (places < 0) {
            return value;
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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
        List<TextColor> values = Arrays
                .asList(TextColors.BLACK, TextColors.DARK_BLUE, TextColors.DARK_GREEN, TextColors.DARK_AQUA, TextColors.DARK_RED, TextColors.DARK_PURPLE, TextColors.GOLD, TextColors.GRAY,
                        TextColors.DARK_GRAY, TextColors.BLUE, TextColors.GREEN, TextColors.AQUA, TextColors.RED, TextColors.LIGHT_PURPLE, TextColors.YELLOW, TextColors.WHITE);
        return values.get(ra.nextInt(values.size()));
    }

    public static String getTown(Player p) {
        /*if (!Bukkit.getPluginManager().isPluginEnabled("Towny")) {
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
        }*/ //TODO
        return null;
    }

    public static String getPrefix(Player player) {
        Subject subject = player.getContainingCollection().get(player.getIdentifier());
        if (subject instanceof OptionSubject) {
            OptionSubject optionSubject = (OptionSubject) subject;
            String prefix = optionSubject.getOption("prefix").orElse("");
            prefix.replaceAll("&", "\u00A7");
            return prefix;
        } else {
            return "";
        }
    }

    public static String getSuffix(Player player) {
        Subject subject = player.getContainingCollection().get(player.getIdentifier());
        if (subject instanceof OptionSubject) {
            OptionSubject optionSubject = (OptionSubject) subject;
            String suffix = optionSubject.getOption("suffix").orElse("");
            suffix.replaceAll("&", "\u00A7");
            return suffix;
        } else {
            return "";
        }
    }

    public static String getFaction(Player p) {
        /*if (!Bukkit.getPluginManager().isPluginEnabled("Factions")) {
            return null;
        }
        try {
            return MPlayer.get(p).getFaction().getName();
        } catch (Exception ex) {
            return null;
        }*/ //TODO
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
                    for (keyStart = 0;
                         keyStart < len;
                         keyStart++) {
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
                            for (startIndex = 0;
                                 startIndex < nextLine.length();
                                 startIndex++) {
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
                        for (separatorIndex = keyStart;
                             separatorIndex < len;
                             separatorIndex++) {
                            char currentChar = line.charAt(separatorIndex);
                            if (currentChar == '\\') {
                                separatorIndex++;
                            } else if (keyValueSeparators.indexOf(currentChar) != -1) {
                                break;
                            }
                        }

                        // Skip over whitespace after key if any
                        int valueIndex;
                        for (valueIndex = separatorIndex;
                             valueIndex < len;
                             valueIndex++) {
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

            for (int x = 0;
                 x < len; ) {
                aChar = theString.charAt(x++);
                if (aChar == '\\') {
                    aChar = theString.charAt(x++);
                    if (aChar == 'u') {
                        // Read the xxxx
                        int value = 0;
                        for (int i = 0;
                             i < 4;
                             i++) {
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

            for (int x = 0;
                 x < len;
                 x++) {
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
            for (Iterator<String> iter = order.iterator();
                 iter.hasNext(); ) {
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
            for (Iterator<String> iter = newKeys.iterator();
                 iter.hasNext(); ) {
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
            for (Iterator<String> i = map.keySet().iterator();
                 i.hasNext(); ) {
                String key = i.next();
                h.put(key, map.get(key));
            }
            return h.keySet().iterator();
        }
    }

    /*public static class Vault {

        public Vault() {
        }

        public Permission getPermission() {
            RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            return permissionProvider.getProvider();

        }

        public Chat getChat() {
            RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
            return chatProvider.getProvider();
        }

        public Economy getEconomy() {
            RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            return economyProvider.getProvider();
        }

    }*/ //TODO
}
