///*
// * This file is part of UltimateCore, licensed under the MIT License (MIT).
// *
// * Copyright (c) Bammerbom
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//package bammerbom.ultimatecore.sponge.modules.afk.config;
//
//import bammerbom.ultimatecore.sponge.api.config.config.module.SmartModuleConfig;
//import com.google.common.reflect.TypeToken;
//import ninja.leaping.configurate.objectmapping.Setting;
//import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
//
//@ConfigSerializable
//public class AfkConfig extends SmartModuleConfig {
//
//    public AfkConfig() {
//        super("afk", TypeToken.of(AfkConfig.class));
//        reload();
//    }
//
//    @Setting(comment = "Events to listen to for cancelling a player's afk status")
//    private Events events = new Events();
//
//    @ConfigSerializable
//    public static class Events {
//
//        @Setting(comment = "Called when a player moves")
//        private Move move = new Move();
//
//        @ConfigSerializable
//        public static class Move {
//            @Setting(comment = "Should move checks be enabled?")
//            private boolean enabled = true;
//
//            @Setting(comment = "Which mode should be used?\ntask: Every few seconds UltimateCore checks if the player has moved (less cpu intensive, might have some delay)\nevent: UltimateCore listens for when the player moves. (more cpu intensive, no delay)")
//            private String mode = "task";
//
//            public boolean getEnabled() {
//                return enabled;
//            }
//
//            public String getMode() {
//                return mode;
//            }
//        }
//
//        @Setting(comment = "Called when a player chats")
//        private boolean chat = true;
//
//        @Setting(comment = "Called when a player runs a command")
//        private boolean command = true;
//
//        @Setting(comment = "Called when a player clicks with any mouse button")
//        private boolean interact = true;
//
//        @Setting(comment = "Called when a player tab completes a command or chat message")
//        private boolean tabcomplete = true;
//
//        @Setting(comment = "Called when a player dies")
//        private boolean death = false;
//
//        @Setting(comment = "Called when a player respawns")
//        private boolean respawn = true;
//
//        @Setting(comment = "Called when a player performs and inventory-related action")
//        private boolean inventory = true;
//
//        public boolean getChat() {
//            return chat;
//        }
//
//        public boolean getCommand() {
//            return command;
//        }
//
//        public boolean getDeath() {
//            return death;
//        }
//
//        public boolean getInteract() {
//            return interact;
//        }
//
//        public boolean getInventory() {
//            return inventory;
//        }
//
//        public boolean getRespawn() {
//            return respawn;
//        }
//
//        public boolean getTabcomplete() {
//            return tabcomplete;
//        }
//
//        public Move getMove() {
//            return move;
//        }
//    }
//
//    @Setting
//    private Time time = new Time();
//
//    @ConfigSerializable
//    public static class Time {
//        @Setting(value = "afk-check-interval", comment = "UltimateCore checks if a player should be marked afk every x ticks\nThis also includes movement checks if events.move.mode is set to task.\nHow many ticks should be between the checks?\n20 ticks = 1 second")
//        int afkcheckInterval = 60;
//
//        @Setting(comment = "How long before someone is marked as afk? (In seconds)\nSet this to -1 to disable marking someone as afk")
//        int afktime = 60;
//
//        @Setting(comment = "How long before someone gets kicked for being afk for too long? (In seconds)\nThis is including the time before the player was marked as afk\nSet this to -1 to disable kicking")
//        int kicktime = 900;
//
//        public int getAfkcheckInterval() {
//            return afkcheckInterval;
//        }
//
//        public int getAfktime() {
//            return afktime;
//        }
//
//        public int getKicktime() {
//            return kicktime;
//        }
//    }
//
//    @Setting
//    private Title title = new Title();
//
//    @ConfigSerializable
//    public static class Title {
//        @Setting(comment = "Enable sending the player a title when the player is marked as afk?")
//        private boolean enabled = true;
//
//        @Setting(comment = "Enable sending the player a subtitle with the time remaining until the player gets kicked?")
//        private boolean subtitle = true;
//
//        @Setting(value = "subtitle-exempt", comment = "Enable sending the player a subtitle when the player has the uc.afk.exempt permission?")
//        private boolean subtitleExempt = true;
//
//        @Setting(value = "subtitle-refresh", comment = "The time between each refresh of the subtitle (In ticks)\n20 ticks = 1 second")
//        private int subtitleRefresh = 20;
//
//        @Setting(value = "subtitle-show-seconds", comment = "When this is set to false the time in the subtitle won't show seconds.\nIf you have a refresh time of higher than 1 second, it is recommended to disable showing seconds.")
//        private boolean subtitleShowSeconds = true;
//
//        public boolean getEnabled() {
//            return enabled;
//        }
//
//        public boolean getSubtitle() {
//            return subtitle;
//        }
//
//        public boolean getSubtitleExempt() {
//            return subtitleExempt;
//        }
//
//        public boolean getSubtitleShowSeconds() {
//            return subtitleShowSeconds;
//        }
//
//        public int getSubtitleRefresh() {
//            return subtitleRefresh;
//        }
//    }
//
//    public Events getEvents() {
//        return events;
//    }
//
//    public Title getTitle() {
//        return title;
//    }
//
//    public Time getTime() {
//        return time;
//    }
//}
