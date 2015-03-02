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
package bammerbom.ultimatecore.bukkit.resources.databases;

import bammerbom.ultimatecore.bukkit.UltimateCore;
import bammerbom.ultimatecore.bukkit.r;
import java.io.*;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ItemDatabase {

    private final transient static Map<String, Integer> items = new HashMap<>();
    private final transient static Map<ItemData, List<String>> names = new HashMap<>();
    private final transient static Map<ItemData, String> primaryName = new HashMap<>();
    private final transient static Map<String, Short> durabilities = new HashMap<>();
    static private UltimateCore plugin;

    public static void disable() {
        items.clear();
        names.clear();
        primaryName.clear();
        durabilities.clear();
        plugin = null;
    }

    public static void enable() {
        plugin = r.getUC();
        InputStream resource = plugin.getResource("Data/items.csv");
        BufferedReader r = new BufferedReader(new InputStreamReader(resource));
        ArrayList<String> lines = new ArrayList<>();
        String lineC;
        try {
            while ((lineC = r.readLine()) != null) {
                lines.add(lineC);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        durabilities.clear();
        items.clear();
        names.clear();
        primaryName.clear();
        for (String line : lines) {
            line = line.trim().toLowerCase(Locale.ENGLISH);
            if ((line.length() <= 0) || (line.charAt(0) != '#')) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    int numeric = Integer.parseInt(parts[1]);
                    short data = (parts.length > 2) && (!parts[2].equals("0")) ? Short.parseShort(parts[2]) : 0;
                    String itemName = parts[0].toLowerCase(Locale.ENGLISH);

                    durabilities.put(itemName, data);
                    items.put(itemName, numeric);
                    if (itemName.contains("_")) {
                        items.put(itemName.replace("_", ""), Integer.valueOf(numeric));
                    }

                    ItemData itemData = new ItemData(numeric, data);
                    if (names.containsKey(itemData)) {
                        List<String> nameList = names.get(itemData);
                        nameList.add(itemName);
                        if (itemName.contains("_")) {
                            nameList.add(itemName.replace("_", ""));
                        }
                        Collections.sort(nameList, new LengthCompare());
                    } else {
                        List<String> nameList = new ArrayList<>();
                        nameList.add(itemName);
                        names.put(itemData, nameList);
                        primaryName.put(itemData, itemName);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static ItemStack get(String id) {
        int itemid = 0;
        String itemname;
        short metaData = 0;
        Matcher parts = Pattern.compile("((.*)[:+',;.](\\d+))").matcher(id);
        if (parts.matches()) {
            itemname = parts.group(2);
            metaData = Short.parseShort(parts.group(3));
        } else {
            itemname = id;
        }
        if (r.isInt(itemname)) {
            itemid = Integer.parseInt(itemname);
        } else if (r.isInt(id)) {
            itemid = Integer.parseInt(id);
        } else {
            itemname = itemname.toLowerCase(Locale.ENGLISH);
        }
        if (itemid < 1) {
            if (items.containsKey(itemname)) {
                itemid = items.get(itemname).intValue();
                if ((durabilities.containsKey(itemname)) && (metaData == 0)) {
                    metaData = durabilities.get(itemname).shortValue();
                }
            } else if (Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH)) != null) {
                Material bMaterial = Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH));
                itemid = bMaterial.getId();
            } else {
                try {
                    Material bMaterial = Bukkit.getUnsafe().getMaterialFromInternalName(itemname.toLowerCase(Locale.ENGLISH));
                    itemid = bMaterial.getId();
                } catch (Throwable throwable) {
                    return null;
                }
            }
        }
        if (itemid < 1) {
            return null;
        }
        Material mat = Material.getMaterial(itemid);
        if (mat == null) {
            return null;
        }
        ItemStack retval = new ItemStack(mat);
        retval.setAmount(mat.getMaxStackSize());
        retval.setDurability(metaData);
        return retval;
    }

    public static ItemStack getItem(String str) {
        if (str.contains(":") && !r.isInt(str.split(":")[1])) {
            str = str.split(":")[1];
        }

        if (Material.matchMaterial(str) != null) {
            Material mat = Material.matchMaterial(str);
            ItemStack stack = new ItemStack(mat);
            return stack;
        }
        return get(str);
    }

}

class ManagedFile {

    public static void copyResourceAscii(String resourceName, File file) throws IOException {
        InputStreamReader reader = new InputStreamReader(ManagedFile.class.getResourceAsStream(resourceName));
        try {
            MessageDigest digest = getDigest();
            DigestOutputStream digestStream = new DigestOutputStream(new FileOutputStream(file), digest);
            try {
                OutputStreamWriter writer = new OutputStreamWriter(digestStream);
                try {
                    char[] buffer = new char[8192];
                    while (true) {
                        int length = reader.read(buffer);
                        if (length < 0) {
                            break;
                        }
                        writer.write(buffer, 0, length);
                    }

                    writer.write("\n");
                    writer.flush();
                    BigInteger hashInt = new BigInteger(1, digest.digest());
                    digestStream.on(false);
                    digestStream.write(35);
                    digestStream.write(hashInt.toString(16).getBytes());
                } finally {
                    writer.close();
                }

            } finally {
            }

        } finally {
            reader.close();
        }
    }

    public static boolean checkForVersion(File file, String version) throws IOException {
        if (file.length() < 33L) {
            return false;
        }
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        try {
            byte[] buffer = new byte[(int) file.length()];
            int position = 0;
            do {
                int length = bis.read(buffer, position, Math.min((int) file.length() - position, 8192));
                if (length < 0) {
                    break;
                }
                position += length;
            } while (position < file.length());
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            if (bais.skip(file.length() - 33L) != file.length() - 33L) {
                return false;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(bais));
            try {
                String hash = reader.readLine();
                if ((hash != null) && (hash.matches("#[a-f0-9]{32}"))) {
                    hash = hash.substring(1);
                    bais.reset();
                    String versionline = reader.readLine();
                    if ((versionline != null) && (versionline.matches("#version: .+"))) {
                        String versioncheck = versionline.substring(10);
                        if (!versioncheck.equalsIgnoreCase(version)) {
                            bais.reset();
                            MessageDigest digest = getDigest();
                            DigestInputStream digestStream = new DigestInputStream(bais, digest);
                            try {
                                byte[] bytes = new byte[(int) file.length() - 33];
                                digestStream.read(bytes);
                                BigInteger correct = new BigInteger(hash, 16);
                                BigInteger test = new BigInteger(1, digest.digest());
                                if (correct.equals(test)) {
                                    return true;
                                }

                            } finally {
                            }
                        }
                    }

                }

            } finally {
            }

        } finally {
            bis.close();
        }
        return false;
    }

    public static MessageDigest getDigest() throws IOException {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new IOException(ex);
        }
    }

    private final transient File file;

    public ManagedFile(String filename, Plugin instance) {
        this.file = new File(instance.getDataFolder(), filename);

        if (this.file.exists()) {
            try {
                if ((checkForVersion(this.file, instance.getDescription().getVersion())) && (!this.file.delete())) {
                    throw new IOException("Could not delete file " + this.file.toString());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (!this.file.exists()) {
            try {
                copyResourceAscii("/" + filename, this.file);
            } catch (IOException ex) {
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<String> getLines() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.file));
            try {
                List lines = new ArrayList();
                String line;
                while (true) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }

                    lines.add(line);
                }

                return lines;
            } finally {
                reader.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

}

class ItemData {

    private final int itemNo;
    private final short itemData;

    ItemData(int itemNo, short itemData) {
        this.itemNo = itemNo;
        this.itemData = itemData;
    }

    public int getItemNo() {
        return this.itemNo;
    }

    public short getItemData() {
        return this.itemData;
    }

    @Override
    public int hashCode() {
        return 31 * this.itemNo ^ this.itemData;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof ItemData)) {
            return false;
        }
        ItemData pairo = (ItemData) o;
        return (this.itemNo == pairo.getItemNo()) && (this.itemData == pairo.getItemData());
    }
}

class LengthCompare
        implements Comparator<String> {

    public LengthCompare() {
    }

    @Override
    public int compare(String s1, String s2) {
        return s1.length() - s2.length();
    }
}
