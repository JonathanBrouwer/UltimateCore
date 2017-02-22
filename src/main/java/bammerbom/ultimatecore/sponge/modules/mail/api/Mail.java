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
package bammerbom.ultimatecore.sponge.modules.mail.api;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;
import java.util.UUID;

@ConfigSerializable
public class Mail {
    @Setting
    private UUID mailid;
    @Setting
    private UUID sender;
    @Setting
    private List<UUID> receivers;
    @Setting
    private Long date;
    @Setting
    private String message;

    public Mail() {
    }

    public Mail(UUID sender, List<UUID> receivers, Long date, String message) {
        this.mailid = UUID.randomUUID();
        this.sender = sender;
        this.receivers = receivers;
        this.date = date;
        this.message = message;
    }

    public List<UUID> getReceivers() {
        return this.receivers;
    }

    public void setReceivers(List<UUID> receivers) {
        this.receivers = receivers;
    }

    public Long getDate() {
        return this.date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getSender() {
        return this.sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public UUID getMailid() {
        return this.mailid;
    }

    public void setMailid(UUID mailid) {
        this.mailid = mailid;
    }
}
