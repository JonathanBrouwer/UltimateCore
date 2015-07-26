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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MinecraftServerUtil {
    private static final JSONParser parser = new JSONParser();
    public static ArrayList<MinecraftServer> offline = new ArrayList<>();
    public static ArrayList<MinecraftServer> unknown = new ArrayList<>();
    public static ArrayList<MinecraftServer> problems = new ArrayList<>();
    public static ArrayList<MinecraftServer> online = new ArrayList<>();

    public static void runcheck() {
        //
        offline.clear();
        unknown.clear();
        problems.clear();
        online.clear();
        for (MinecraftServer serv : MinecraftServer.values()) {
            Status status = getStatus(serv);

            if (status.equals(Status.ONLINE)) {
                online.add(serv);
            } else if (status.equals(Status.EXPERIENCE)) {
                problems.add(serv);
            } else if (status.equals(Status.OFFLINE)) {
                offline.add(serv);
            } else if (status.equals(Status.UNKNOWN)) {
                unknown.add(serv);
            }
        }
    }

    public static Status getStatus(MinecraftServer service) {
        String status;

        try {
            URL url = new URL("http://status.mojang.com/check?service=" + service.getURL());
            BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
            Object object = parser.parse(input);
            JSONObject jsonObject = (JSONObject) object;

            status = (String) jsonObject.get(service.getURL());
        } catch (Exception e) {
            return Status.UNKNOWN;
        }

        return status(status);
    }

    private static Status status(String status) {

        switch (status.toLowerCase()) {
            case "green":
                return Status.ONLINE;

            case "yellow":
                return Status.EXPERIENCE;

            case "red":
                return Status.OFFLINE;

            default:
                return Status.UNKNOWN;
        }
    }

    public static String getTip(Status status, MinecraftServer server) {
        switch (server) {
            //Skin
            case SKIN:
                switch (status) {
                    case ONLINE:
                        return "minecraftserversTipSkinOnline";
                    case EXPERIENCE:
                        return "minecraftserversTipSkinUnstable";
                    case OFFLINE:
                        return "minecraftserversTipSkinOffline";
                }
                break;
            //Login
            case AUTH:
            case AUTHSERVER:
                switch (status) {
                    case ONLINE:
                        return "minecraftserversTipAuthOnline";
                    case EXPERIENCE:
                        return "minecraftserversTipAuthUnstable";
                    case OFFLINE:
                        return "minecraftserversTipAuthOffline";
                }
                break;
            //Session
            case SESSION:
            case MOJANGSESSION:
                switch (status) {
                    case ONLINE:
                        return "minecraftserversTipSessionOnline";
                    case EXPERIENCE:
                        return "minecraftserversTipSessionUnstable";
                    case OFFLINE:
                        return "minecraftserversTipSessionOffline";
                }
                break;
        }
        return null;
    }

    public enum MinecraftServer {

        //Minecraft
        WEBSITE("minecraft.net"),
        SKIN("skins.minecraft.net"),
        SESSION("session.minecraft.net"),
        //Mojang
        ACCOUNT("account.mojang.com"),
        AUTH("auth.mojang.com"),
        AUTHSERVER("authserver.mojang.com"),
        MOJANGSESSION("sessionserver.mojang.com");

        private final String url;

        MinecraftServer(String url) {
            this.url = url;
        }

        private String getURL() {
            return url;
        }
    }

    public enum Status {

        ONLINE("No problems detected!"),
        EXPERIENCE("May be experiencing issues"),
        OFFLINE("Experiencing problems!"),
        UNKNOWN("Couldn't connect to Mojang!");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

    }
}
