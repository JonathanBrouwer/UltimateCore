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
package bammerbom.ultimatecore.bukkit.configuration;

import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.bukkit.resources.utils.StreamUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.StringUtil;

import java.io.*;
import java.util.*;

public class Config extends YamlConfiguration implements Cloneable {

    static boolean a = false;
    private final File file;
    private final HashMap<String, String> headers;
    private final Set<String> readkeys;

    public Config(File file2) {
        file = file2;
        headers = new HashMap<>();
        readkeys = new HashSet<>();
        try {
            loadFromStream(new FileInputStream(file));
        } catch (IOException e) {
            ErrorLogger.log(e, "Failed to load yaml file.");
        }
    }

    public void loadFromStream(InputStream stream) throws IOException {
        try {
            InputStreamReader reader = new InputStreamReader(stream);
            StringBuilder builder = new StringBuilder();
            BufferedReader input = new BufferedReader(reader);
            try {
                HeaderBuilder header = new HeaderBuilder();
                NodeBuilder node = new NodeBuilder(getIndent());
                StringBuilder mainHeader = new StringBuilder();
                String line;
                while ((line = input.readLine()) != null) {
                    int indent = StringUtil.getSuccessiveCharCount(line, ' ');
                    String trimmedLine = line.substring(indent);

                    if (trimmedLine.equals("*:")) {
                        trimmedLine = "'*':";
                        line = StringUtil.getFilledString(" ", indent) + trimmedLine;
                    }

                    if (trimmedLine.startsWith("#> ")) {
                        mainHeader.append('\n').append(trimmedLine.substring("#> ".length()));
                    } else if (!header.handle(trimmedLine)) {
                        node.handle(trimmedLine, indent);

                        if (header.hasHeader()) {
                            setHeader(node.getPath(), header.getHeader());
                            header.clear();
                        }
                        builder.append(line).append('\n');
                    }
                }
                if (mainHeader.length() > 0) {
                    setHeader(mainHeader.toString());
                }
            } finally {
                input.close();
            }
            try {
                loadFromString(builder.toString());
            } catch (InvalidConfigurationException e) {
                String filename = "config_CORRUPT.yml";
                Integer i = 1;
                while (new File(r.getUC().getDataFolder(), filename).exists()) {
                    i++;
                    filename = "config_CORRUPT" + i + ".yml";
                }
                file.renameTo(new File(r.getUC().getDataFolder(), filename));
                throw new IOException("YAML file is corrupt", e);
            }
        } catch (FileNotFoundException ex) {
        }
    }

    //save
    public void save() {
        save(file);
    }

    @Override
    public void save(File fi) {
        try {
            for (String key : getKeys(true)) {
                Object value = get(key);
                if ((value instanceof String)) {
                    String text = (String) value;
                    if (text.contains("\n")) {
                        set(key, Arrays.asList(text.split("\n", -1)));
                    }
                }
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(StreamUtil.createOutputStream(fi)));
            try {
                writeHeader(true, writer, getHeader(), 0);

                HashMap<Integer, String> anchorData = new HashMap<>();

                int anchId = -1;
                int anchDepth = 0;
                int anchIndent = 0;

                StringBuilder refData = new StringBuilder();
                NodeBuilder node = new NodeBuilder(getIndent());
                for (String line : saveToString().split("\n", -1)) {
                    if (line.startsWith("#")) {
                        continue;
                    }
                    line = StringUtil.colorToAmp(line);
                    int indent = StringUtil.getSuccessiveCharCount(line, ' ');
                    line = line.substring(indent);
                    boolean wasAnchor = false;

                    if (line.equals("'*':")) {
                        line = "*:";
                    }

                    if (node.handle(line, indent)) {
                        if ((anchId >= 0) && (node.getDepth() <= anchDepth)) {
                            anchorData.put(anchId, refData.toString());
                            refData.setLength(0);
                            anchId = -1;
                        }

                        writeHeader(false, writer, getHeader(node.getPath()), indent);

                        int refStart = line.indexOf("*id", node.getName().length());
                        int refEnd = line.indexOf(' ', refStart);
                        if (refEnd == -1) {
                            refEnd = line.length();
                        }
                        if ((refStart > 0) && (refEnd > refStart)) {
                            int refId = Integer.parseInt(line.substring(refStart + 3, refEnd), -1);
                            if (refId >= 0) {
                                String data = anchorData.get(refId);
                                if (data != null) {
                                    line = StringUtil.trimEnd(line.substring(0, refStart)) + " " + data;
                                }
                            }

                        }

                        int anchStart = line.indexOf("&id", node.getName().length());
                        int anchEnd = line.indexOf(' ', anchStart);
                        if (anchEnd == -1) {
                            anchEnd = line.length();
                        }
                        if ((anchStart > 0) && (anchEnd > anchStart)) {
                            anchId = Integer.parseInt(line.substring(anchStart + 3, anchEnd), -1);
                            anchDepth = node.getDepth();
                            anchIndent = indent;
                            if (anchId >= 0) {
                                anchEnd += StringUtil.getSuccessiveCharCount(line.substring(anchEnd), ' ');

                                refData.append(line.substring(anchEnd));

                                line = StringUtil.replace(line, anchStart, anchEnd, "");
                            }
                            wasAnchor = true;
                        }
                    }
                    if ((!wasAnchor) && (anchId >= 0)) {
                        refData.append('\n').append(StringUtil.getFilledString(" ", indent - anchIndent)).append(line);
                    }

                    if (StringUtil.containsChar('\n', line)) {
                        for (String part : line.split("\n", -1)) {
                            StreamUtil.writeIndent(writer, indent);
                            writer.write(part);
                            writer.newLine();
                        }
                    } else {
                        StreamUtil.writeIndent(writer, indent);
                        writer.write(line);
                        writer.newLine();
                    }
                }
            } finally {
                writer.close();
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to save yalm file.");
        }
    }

    private void writeHeader(boolean main, BufferedWriter writer, String header, int indent) throws IOException {
        if (header != null) {
            for (String headerLine : header.split("\n", -1)) {
                StreamUtil.writeIndent(writer, indent);
                if (main) {
                    writer.write("#> ");
                    writer.write(headerLine);
                } else if (headerLine.trim().length() > 0) {
                    writer.write("# ");
                    writer.write(headerLine);
                }
                writer.newLine();
            }
        }
    }

    //file
    public File getFile() {
        return file;
    }

    //headers
    public String getHeader() {
        return this.headers.get(getPath());
    }

    public void setHeader(String header) {
        setHeader("", header);
    }

    public String getHeader(String path) {
        return this.headers.get(getPath(path));
    }

    public void removeHeader() {
        setHeader(null);
    }

    public void removeHeader(String path) {
        setHeader(path, null);
    }

    public void setHeader(String path, String header) {
        if (header == null) {
            this.headers.remove(getPath(path));
        } else {
            this.headers.put(getPath(path), header);
        }
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    //path
    public String getPath(String append) {
        String p = getPath();
        if (StringUtil.nullOrEmpty(append)) {
            return p;
        }
        if (StringUtil.nullOrEmpty(p)) {
            return append;
        }
        return p + "." + append;
    }

    public String getPath() {
        return getCurrentPath();
    }

    //copydefaults
    public void copyDefaults(boolean a) {
        options.copyDefaults(a);
    }

    public boolean copyDefaults() {
        return options.copyDefaults();
    }

    public int getIndent() {
        return options().indent();
    }

    //indent
    public void setIndent(int indent) {
        options().indent(indent);
    }

    //read
    public void setRead() {
        setRead(null);
    }

    public void setRead(String path) {
        setReadFullPath(getPath(path));
    }

    private void setReadFullPath(String path) {
        if (this.readkeys.add(path)) {
            int dotindex = path.lastIndexOf('.');
            if (dotindex > 0) {
                setReadFullPath(path.substring(0, dotindex));
            }
        }
    }

    @Override
    public List<?> getList(String path) {
        List<?> list = super.getList(path);
        return list == null ? new ArrayList<>() : list;
    }

    @Override
    public List<String> getStringList(String path) {
        List<String> list = super.getStringList(path);
        return list == null ? new ArrayList<String>() : list;
    }

    //set
    @Override
    public void set(String path, Object value) {
        if (value != null) {
            setRead(path);
            if (value.getClass().isEnum()) {
                String text = value.toString();
                if (text.equals("true")) {
                    value = true;
                } else if (text.equals("false")) {
                    value = false;
                } else {
                    value = text;
                }
            }
            if (value instanceof UUID) {
                value = value.toString();
            }
        }
        super.set(path, value);
    }

    @Override
    public void addDefault(String path, Object value) {
        if (!contains(path)) {
            set(path, value);
        }
    }
}

//headerbuilder
class HeaderBuilder {

    private final StringBuilder buffer = new StringBuilder();

    private StringBuilder add() {
        return this.buffer.append('\n');
    }

    public boolean handle(String line) {
        if (line.isEmpty()) {
            add().append(' ');
        } else if (line.startsWith("# ")) {
            add().append(line.substring(2));
        } else if (line.startsWith("#")) {
            add().append(line.substring(1));
        } else {
            return false;
        }
        return true;
    }

    public void clear() {
        this.buffer.setLength(0);
    }

    public boolean hasHeader() {
        return this.buffer.length() > 0;
    }

    public String getHeader() {
        return hasHeader() ? this.buffer.substring(1) : null;
    }
}

//nodebuilder
class NodeBuilder {

    private final int indent;
    private final LinkedList<String> nodes = new LinkedList<>();

    public NodeBuilder(int indent) {
        this.indent = indent;
    }

    public boolean handle(String line, int preceedingSpaces) {
        if (line.startsWith("#")) {
            return false;
        }
        int nodeIndex = preceedingSpaces / this.indent;
        String nodeName = StringUtil.getLastBefore(line, ":");
        if (!nodeName.isEmpty()) {
            while (this.nodes.size() >= nodeIndex + 1) {
                this.nodes.pollLast();
            }
            this.nodes.offerLast(nodeName);
            return true;
        }
        return false;
    }

    public String getName() {
        return this.nodes.peekLast();
    }

    public int getDepth() {
        return this.nodes.size();
    }

    public String getPath() {
        return StringUtil.join(".", this.nodes);
    }
}
