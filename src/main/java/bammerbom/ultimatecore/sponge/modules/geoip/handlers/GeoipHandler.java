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
package bammerbom.ultimatecore.sponge.modules.geoip.handlers;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.utils.Messages;
import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.record.Country;
import org.spongepowered.api.Sponge;

import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

public class GeoipHandler {

    private static DatabaseReader dbr = null;

    public static Optional<Country> getCountry(InetAddress ip) {
        if (dbr == null) return Optional.empty();
        try {
            return Optional.ofNullable(dbr.country(ip).getCountry());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public static boolean init(boolean forceReload, boolean forceRedownload) {
        if (dbr != null && !forceReload) {
            return false;
        }
        if (!Modules.GEOIP.get().getConfig().get().get().getNode("enable").getBoolean()) {
            return false;
        }

        //Download if needed
        File file = new File(UltimateCore.get().getDataFolder().toFile().getPath() + "/data/", "geoip.mmdb");
        if (!file.exists() || forceRedownload) {
            try {
                download(new URL("http://geolite.maxmind.com/download/geoip/database/GeoLite2-Country.mmdb.gz"), file);
                return true;
            } catch (MalformedURLException e) {
            }
        }

        //Load
        try {
            dbr = new DatabaseReader.Builder(new FileInputStream(file)).withCache(new CHMCache()).fileMode(Reader.FileMode.MEMORY).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean isLoaded() {
        return dbr != null;
    }

    private static void download(URL url, File file) {
        Sponge.getScheduler().createAsyncExecutor(UltimateCore.get()).execute(() -> {
            try {
                Messages.log("Downloading geoip database...");
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(10000);
                conn.connect();

                try (InputStream input = new GZIPInputStream(conn.getInputStream()); OutputStream output = new FileOutputStream(file)) {
                    byte[] buffer = new byte[2048];
                    int length = input.read(buffer);
                    while (length >= 0) {
                        output.write(buffer, 0, length);
                        length = input.read(buffer);
                    }
                    output.flush();
                }
                Messages.log("Downloaded geoip database.");
                init(true, false);
            } catch (Exception ex) {
                Messages.log("Downloading geoip database failed. " + (ex.getMessage()));
            }
        });
    }
}