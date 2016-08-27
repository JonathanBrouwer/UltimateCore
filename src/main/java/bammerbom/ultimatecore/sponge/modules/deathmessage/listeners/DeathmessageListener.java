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
package bammerbom.ultimatecore.sponge.modules.deathmessage.listeners;

import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.TextUtil;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TranslatableText;

public class DeathmessageListener {

    @Listener
    public void onDeath(DestructEntityEvent event) {
        if (!(event.getTargetEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getTargetEntity();

        //Get the translation, because for some reason sponge hides it in a lot of children
        Text orgmessage = event.getOriginalMessage();
        while (!(orgmessage instanceof TranslatableText) && !orgmessage.getChildren().isEmpty()) {
            for (Text child : orgmessage.getChildren()) {
                if (child instanceof TranslatableText) {
                    break;
                }
            }
            orgmessage = orgmessage.getChildren().get(0); //Safe because of while loop
        }

        if (orgmessage instanceof TranslatableText) {
            //Id
            TranslatableText org = (TranslatableText) orgmessage;
            String id_mc = org.getTranslation().getId();
            if (!id_mc.startsWith("death.")) {
                return;
            }
            String id_uc = id_mc.toLowerCase().replaceFirst("death\\.", "deathmessage.message.");

            //Item
            Text item = Text.of();
            ItemStack stack = p.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.of(ItemTypes.NONE, 1));
            item = stack.get(Keys.DISPLAY_NAME).orElse(Text.of(stack.getTranslation().get()));

            //Final message //TODO hover
            Text message = Messages.getFormatted(id_uc, "%player%", p.getName(), "%item%", item);

            //Custom args
            int i = 1;
            for (Object ob : org.getArguments()) {
                message = TextUtil.replace(message, "%arg" + i + "%", ob instanceof Text ? ((Text) ob) : Text.of(ob.toString()));
                i++;
            }

            event.setMessage(message);
        }
    }
}
