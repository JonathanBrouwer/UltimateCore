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
package bammerbom.ultimatecore.sponge.modules.serverlist.handlers;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.network.status.Favicon;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class FaviconHandler {
    static Random random = new Random();
    static File path = new File(UltimateCore.get().getDataFolder().toFile().getPath() + "/data/serverlist/");
    static File cache = new File(UltimateCore.get().getDataFolder().toFile().getPath() + "/data/serverlist/cache/");

    public static List<Favicon> getFavicons(List<String> rawlist, @Nullable Object p) {
        if (!path.exists()) {
            path.mkdirs();
        }
        List<Favicon> favicons = new ArrayList<>();
        for (String raw : rawlist) {
            String replaced = VariableUtil.replaceVariables(Text.of(raw), null).toPlain();
            try {
                if (replaced.startsWith("file:")) {
                    File file = new File(path, replaced.split(":", 2)[1]);
                    favicons.add(Sponge.getRegistry().loadFavicon(file.toPath()));

                }
                if (replaced.startsWith("dir:")) {
                    File dir = new File(path, replaced.split(":", 2)[1]);
                    for (File file : dir.listFiles()) {
                        favicons.add(Sponge.getRegistry().loadFavicon(file.toPath()));
                    }
                }
                if (replaced.startsWith("url:")) {
                    favicons.add(Sponge.getRegistry().loadFavicon(new URL(replaced.split(":", 2)[1])));
                }
            } catch (Exception ex) {
                Messages.log("Failed to load favicon from string: " + replaced);
                Messages.log("Error: " + ex.getMessage());
            }
        }
        return favicons;
    }

    private static Favicon fromUrl(URL url) throws Exception {
        return null;
    }

    public static Optional<Favicon> randomFavicon(List<String> rawlist, @Nullable Object p) {
        List<Favicon> favicons = getFavicons(rawlist, p);
        if (favicons.isEmpty()) return Optional.empty();
        return Optional.of(favicons.get(random.nextInt(favicons.size())));
    }
}
