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
package bammerbom.ultimatecore.sponge.modules.personalmessage.api;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import javax.annotation.Nullable;
import java.util.Optional;

public class PersonalmessageEvent implements MessageChannelEvent {

    private Cause cause;
    private CommandSource sender;
    private CommandSource target;
    private boolean cancelled;
    private MessageFormatter formatter;
    private MessageChannel channel;
    private MessageChannel orgchannel;
    private Text message;
    private Text orgmessage;
    private String text;

    public PersonalmessageEvent(Cause cause, CommandSource sender, CommandSource target, MessageFormatter formatter, MessageChannel channel, String text, Text message) {
        this.cause = cause;
        this.sender = sender;
        this.target = target;
        this.cancelled = false;
        this.formatter = formatter;
        this.channel = channel;
        this.orgchannel = channel;
        this.message = message;
        this.orgmessage = message;
        this.text = text;
    }

    /**
     * Get the cause for the event.
     *
     * @return The last cause
     */
    @Override
    public Cause getCause() {
        return cause;
    }

    @Override
    public MessageChannel getOriginalChannel() {
        return orgchannel;
    }

    @Override
    public Optional<MessageChannel> getChannel() {
        return Optional.ofNullable(channel);
    }

    @Override
    public void setChannel(@Nullable MessageChannel channel) {
        channel = channel;
    }

    @Override
    public Text getOriginalMessage() {
        return orgmessage;
    }

    @Override
    public boolean isMessageCancelled() {
        return cancelled;
    }

    @Override
    public void setMessageCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public MessageFormatter getFormatter() {
        return formatter;
    }

    public CommandSource getPMSender() {
        return sender;
    }

    public CommandSource getPMTarget() {
        return target;
    }

    public String getPMUnformattedMessage() {
        return text;
    }
}
