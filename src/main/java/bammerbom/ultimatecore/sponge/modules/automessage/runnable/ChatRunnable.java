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
package bammerbom.ultimatecore.sponge.modules.automessage.runnable;

import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

import java.util.ArrayList;
import java.util.List;

public class ChatRunnable implements Runnable {
    //The order set in the config
    static String order = "randomorder";
    //Contains all messages
    static List<String> messages = new ArrayList<>();
    //Contains all messages excluding the already send messages
    static List<String> tempmessages = new ArrayList<>();

    public void init() {
        CommentedConfigurationNode config = Modules.AUTOMESSAGE.get().getConfig().get().get();
        messages.clear();
        //Set order
        order = config.getNode("chat", "order").getString();
        //Set messages
        Object mes = config.getNode("chat", "messages").getValue();
        if (mes instanceof List) {
            List<? extends Object> list = (List<? extends Object>) mes;
            if (list.get(0) instanceof List) {
                //A list of a list of Objects (use toString())
                List<List> castedlist = (List<List>) list;
                for (List messagelist : castedlist) {
                    messages.add(StringUtil.join("\n", messagelist));
                }
            } else {
                //A list of Objects (use toString())
                for (Object message : list) {
                    messages.add(message.toString());
                }
            }
        } else {
            messages.add(mes.toString());
            return;
        }
    }

    @Override
    public void run() {

    }
}
