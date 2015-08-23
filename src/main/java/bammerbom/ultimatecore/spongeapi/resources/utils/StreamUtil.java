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
package bammerbom.ultimatecore.spongeapi.resources.utils;

import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

public class StreamUtil {

    public static UUID readUUID(DataInputStream stream) throws IOException {
        return new UUID(stream.readLong(), stream.readLong());
    }

    public static void writeUUID(DataOutputStream stream, UUID uuid) throws IOException {
        stream.writeLong(uuid.getMostSignificantBits());
        stream.writeLong(uuid.getLeastSignificantBits());
    }

    public static void writeIndent(BufferedWriter writer, int indent) throws IOException {
        for (int i = 0;
             i < indent;
             i++) {
            writer.write(32);
        }
    }

    public static Iterable<File> listFiles(File directory) {
        return Arrays.asList(directory.listFiles());
    }

    public static long getFileSize(File file) {
        if (!file.exists()) {
            return 0L;
        }
        if (file.isDirectory()) {
            long size = 0L;
            for (File subfile : listFiles(file)) {
                size += getFileSize(subfile);
            }
            return size;
        }
        return file.length();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<File> deleteFile(File file) {
        if (file.isDirectory()) {
            List failFiles = new ArrayList();
            deleteFileList(file, failFiles);
            return Collections.unmodifiableList(failFiles);
        }
        if (file.delete()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(Arrays.asList(file));
    }

    private static boolean deleteFileList(File file, List<File> failFiles) {
        if (file.isDirectory()) {
            boolean success = true;
            for (File subFile : listFiles(file)) {
                success &= deleteFileList(subFile, failFiles);
            }
            if (success) {
                file.delete();
            }
            return success;
        }
        if (file.delete()) {
            return true;
        }
        failFiles.add(file);
        return false;
    }

    public static FileOutputStream createOutputStream(File file) throws IOException, SecurityException {
        return createOutputStream(file, false);
    }

    public static FileOutputStream createOutputStream(File file, boolean append) throws IOException, SecurityException {
        File directory = file.getAbsoluteFile().getParentFile();
        if ((!directory.exists()) && (!directory.mkdirs())) {
            throw new IOException("Failed to create the parent directory of the file");
        }
        if ((!file.exists()) && (!file.createNewFile())) {
            throw new IOException("Failed to create the new file to write to");
        }
        return new FileOutputStream(file, append);
    }

    public static boolean tryCopyFile(File sourceLocation, File targetLocation) {
        try {
            copyFile(sourceLocation, targetLocation);
            return true;
        } catch (IOException ex) {
            ErrorLogger.log(ex, "Failed to copy file.");
        }
        return false;
    }

    public static void copyFile(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdirs();
            }
            for (File subFile : listFiles(sourceLocation)) {
                String subFileName = subFile.getName();
                copyFile(new File(sourceLocation, subFileName), new File(targetLocation, subFileName));
            }
        } else {
            FileInputStream input = null;
            FileOutputStream output = null;
            FileChannel inputChannel = null;
            FileChannel outputChannel = null;
            try {
                input = new FileInputStream(sourceLocation);
                inputChannel = input.getChannel();
                output = createOutputStream(targetLocation);
                outputChannel = output.getChannel();

                long transfered = 0L;
                long bytes = inputChannel.size();
                while (transfered < bytes) {
                    transfered += outputChannel.transferFrom(inputChannel, 0L, inputChannel.size());
                    outputChannel.position(transfered);
                }
            } finally {
                if (inputChannel != null) {
                    inputChannel.close();
                } else if (input != null) {
                    input.close();
                }

                if (outputChannel != null) {
                    outputChannel.close();
                } else if (output != null) {
                    output.close();
                }
            }
        }
    }

    public static File getFileIgnoreCase(File parent, String child) {
        if (child == null || child.isEmpty()) {
            return parent;
        }
        File childFile = new File(parent, child);
        if ((!childFile.exists()) && (parent.exists())) {
            int firstFolderIdx = child.indexOf(File.separator);
            if (firstFolderIdx != -1) {
                File newParent = getFileIgnoreCase(parent, child.substring(0, firstFolderIdx));
                String newChild = child.substring(firstFolderIdx + 1);
                return getFileIgnoreCase(newParent, newChild);
            }

            for (String childName : parent.list()) {
                if (childName.equalsIgnoreCase(child)) {
                    return new File(parent, childName);
                }
            }
        }

        return childFile;
    }
}
