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
package bammerbom.ultimatecore.sponge.modules.serverlist.util;

import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import org.spongepowered.api.event.server.ClientPingServerEvent;

public class StatusProtocol {
    public static boolean setVersion(ClientPingServerEvent.Response response, String name, int protocol) {
        //Depend on SpongeCommon for this code, because SpongeApi has no api for this
        //Let's do some reflection so I don't have to depend on SpongeCommon (it's bukkit all over again)
        try {
            //Try 1 (Api 6)
            Class ssr_class = Class.forName("net.minecraft.network.ServerStatusResponse");
            Class ssr_version_class = Class.forName("net.minecraft.network.ServerStatusResponse$Version");
            Object ssr_version = ssr_version_class.getConstructor(String.class, int.class).newInstance(name, protocol);
            ssr_class.getMethod("setVersion", ssr_version_class).invoke(response, ssr_version);
            return true;
        } catch (Exception | Error ex) {
            try {
                //Try 2 (Api 5)
                Class ssr_class = Class.forName("net.minecraft.network.ServerStatusResponse");
                Class ssr_version_class = Class.forName("net.minecraft.network.ServerStatusResponse$MinecraftProtocolVersionIdentifier");
                Object ssr_version = ssr_version_class.getConstructor(String.class, int.class).newInstance(name, protocol);
                ssr_class.getMethod("setProtocolVersionInfo", ssr_version_class).invoke(response, ssr_version);
                return true;
            } catch (Exception | Error ex2) {
                Messages.log("Err 1");
                ex.printStackTrace();
                Messages.log("Err 2");
                ex2.printStackTrace();
            }
        }
        return false;
    }
}
