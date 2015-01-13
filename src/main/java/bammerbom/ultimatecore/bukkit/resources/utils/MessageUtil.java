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
package bammerbom.ultimatecore.bukkit.resources.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.Statistic.Type;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Represents an object that can be serialized to a JSON writer instance.
 */
interface JsonRepresentedObject {
    /**
     * Writes the JSON representation of this object to the specified writer.
     *
     * @param writer The JSON writer which will receive the object.
     * @throws IOException If an error occurs writing to the stream.
     */
    public void writeJson(JsonWriter writer) throws IOException;

}

/**
 * Represents a formattable message. Such messages can use elements such as
 * colors, formatting codes, hover and click data, and other features provided
 * by the vanilla Minecraft
 * <a href="http://minecraft.gamepedia.com/Tellraw#Raw_JSON_Text">JSON message
 * formatter</a>. This class allows plugins to emulate the functionality of the
 * vanilla Minecraft
 * <a href="http://minecraft.gamepedia.com/Commands#tellraw">tellraw
 * command</a>.
 * <p>
 * This class follows the builder pattern, allowing for method chaining. It is
 * set up such that invocations of property-setting methods will affect the
 * current editing component, and a call to {@link #then()} or
 * {@link #then(Object)} will append a new editing component to the end of the
 * message, optionally initializing it with text. Further property-setting
 * method calls will affect that editing component.
 * </p>
 */
public class MessageUtil implements JsonRepresentedObject, Cloneable, Iterable<MessagePart>, ConfigurationSerializable {

    static {
        ConfigurationSerialization.registerClass(MessageUtil.class);
    }
    private static Constructor<?> nmsPacketPlayOutChatConstructor;
    // The ChatSerializer's instance of Gson
    private static Object nmsChatSerializerGsonInstance;
    private static Method fromJsonMethod;
    private static JsonParser _stringParser = new JsonParser();
    private List<MessagePart> messageParts;
    private String jsonString;
    private boolean dirty;

    /**
     * Creates a JSON message with text.
     *
     * @param firstPartText The existing text in the message.
     */
    public MessageUtil(final String firstPartText) {
        this(TextualComponent.rawText(firstPartText));
    }

    public MessageUtil(final TextualComponent firstPartText) {
        messageParts = new ArrayList<MessagePart>();
        messageParts.add(new MessagePart(firstPartText));
        jsonString = null;
        dirty = false;

        if (nmsPacketPlayOutChatConstructor == null) {
            try {
                nmsPacketPlayOutChatConstructor = Reflection.getNMSClass("PacketPlayOutChat").getDeclaredConstructor(Reflection.getNMSClass("IChatBaseComponent"));
                nmsPacketPlayOutChatConstructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Could not find Minecraft method or constructor.", e);
            } catch (SecurityException e) {
                Bukkit.getLogger().log(Level.WARNING, "Could not access constructor.", e);
            }
        }
    }

    /**
     * Creates a JSON message without text.
     */
    public MessageUtil() {
        this((TextualComponent) null);
    }

    /**
     * Deserializes a JSON-represented message from a mapping of key-value
     * pairs. This is called by the Bukkit serialization API. It is not intended
     * for direct public API consumption.
     *
     * @param serialized The key-value mapping which represents a fancy message.
     */
    public static MessageUtil deserialize(Map<String, Object> serialized) {
        MessageUtil msg = new MessageUtil();
        msg.messageParts = (List<MessagePart>) serialized.get("messageParts");
        msg.jsonString = serialized.containsKey("JSON") ? serialized.get("JSON").toString() : null;
        msg.dirty = !serialized.containsKey("JSON");
        return msg;
    }

    /**
     * Deserializes a fancy message from its JSON representation. This JSON
     * representation is of the format of that returned by
     * {@link #toJSONString()}, and is compatible with vanilla inputs.
     *
     * @param json The JSON string which represents a fancy message.
     * @return A {@code FancyMessage} representing the parameterized JSON
     * message.
     */
    public static MessageUtil deserialize(String json) {
        JsonObject serialized = _stringParser.parse(json).getAsJsonObject();
        JsonArray extra = serialized.getAsJsonArray("extra"); // Get the extra component
        MessageUtil returnVal = new MessageUtil();
        returnVal.messageParts.clear();
        for (JsonElement mPrt : extra) {
            MessagePart component = new MessagePart();
            JsonObject messagePart = mPrt.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : messagePart.entrySet()) {
                // Deserialize text
                if (TextualComponent.isTextKey(entry.getKey())) {
                    // The map mimics the YAML serialization, which has a "key" field and one or more "value" fields
                    Map<String, Object> serializedMapForm = new HashMap<String, Object>(); // Must be object due to Bukkit serializer API compliance
                    serializedMapForm.put("key", entry.getKey());
                    if (entry.getValue().isJsonPrimitive()) {
                        // Assume string
                        serializedMapForm.put("value", entry.getValue().getAsString());
                    } else {
                        // Composite object, but we assume each element is a string
                        for (Map.Entry<String, JsonElement> compositeNestedElement : entry.getValue().getAsJsonObject().entrySet()) {
                            serializedMapForm.put("value." + compositeNestedElement.getKey(), compositeNestedElement.getValue().getAsString());
                        }
                    }
                    component.text = TextualComponent.deserialize(serializedMapForm);
                } else if (MessagePart.stylesToNames.inverse().containsKey(entry.getKey())) {
                    if (entry.getValue().getAsBoolean()) {
                        component.styles.add(MessagePart.stylesToNames.inverse().get(entry.getKey()));
                    }
                } else if (entry.getKey().equals("color")) {
                    component.color = ChatColor.valueOf(entry.getValue().getAsString().toUpperCase());
                } else if (entry.getKey().equals("clickEvent")) {
                    JsonObject object = entry.getValue().getAsJsonObject();
                    component.clickActionName = object.get("action").getAsString();
                    component.clickActionData = object.get("value").getAsString();
                } else if (entry.getKey().equals("hoverEvent")) {
                    JsonObject object = entry.getValue().getAsJsonObject();
                    component.hoverActionName = object.get("action").getAsString();
                    if (object.get("value").isJsonPrimitive()) {
                        // Assume string
                        component.hoverActionData = new JsonString(object.get("value").getAsString());
                    } else {
                        // Assume composite type
                        // The only composite type we currently store is another FancyMessage
                        // Therefore, recursion time!
                        component.hoverActionData = deserialize(object.get("value").toString() /* This should properly serialize the JSON object as a JSON string */);
                    }
                }
            }
            returnVal.messageParts.add(component);
        }
        return returnVal;
    }

    @Override
    public MessageUtil clone() throws CloneNotSupportedException {
        MessageUtil instance = (MessageUtil) super.clone();
        instance.messageParts = new ArrayList<MessagePart>(messageParts.size());
        for (int i = 0; i < messageParts.size(); i++) {
            instance.messageParts.add(i, messageParts.get(i).clone());
        }
        instance.dirty = false;
        instance.jsonString = null;
        return instance;
    }

    /**
     * Sets the text of the current editing component to a value.
     *
     * @param text The new text of the current editing component.
     * @return This builder instance.
     * @throws IllegalStateException If the text for the current editing
     * component has already been set.
     */
    public MessageUtil text(String text) {
        MessagePart latest = latest();
        if (latest.hasText()) {
            throw new IllegalStateException("text for this message part is already set");
        }
        latest.text = TextualComponent.rawText(text);
        dirty = true;
        return this;
    }

    public MessageUtil text(TextualComponent text) {
        MessagePart latest = latest();
        if (latest.hasText()) {
            throw new IllegalStateException("text for this message part is already set");
        }
        latest.text = text;
        dirty = true;
        return this;
    }

    /**
     * Sets the color of the current editing component to a value.
     *
     * @param color The new color of the current editing component.
     * @return This builder instance.
     * @throws IllegalArgumentException If the specified enumeration value does
     * not represent a color.
     */
    public MessageUtil color(final ChatColor color) {
        if (!color.isColor()) {
            throw new IllegalArgumentException(color.name() + " is not a color");
        }
        latest().color = color;
        dirty = true;
        return this;
    }

    /**
     * Sets the stylization of the current editing component.
     *
     * @param styles The array of styles to apply to the editing component.
     * @return This builder instance.
     * @throws IllegalArgumentException If any of the enumeration values in the
     * array do not represent formatters.
     */
    public MessageUtil style(ChatColor... styles) {
        for (final ChatColor style : styles) {
            if (!style.isFormat()) {
                throw new IllegalArgumentException(style.name() + " is not a style");
            }
        }
        latest().styles.addAll(Arrays.asList(styles));
        dirty = true;
        return this;
    }

    /**
     * Set the behavior of the current editing component to instruct the client
     * to open a file on the client side filesystem when the currently edited
     * part of the {@code FancyMessage} is clicked.
     *
     * @param path The path of the file on the client filesystem.
     * @return This builder instance.
     */
    public MessageUtil file(final String path) {
        onClick("open_file", path);
        return this;
    }

    /**
     * Set the behavior of the current editing component to instruct the client
     * to open a webpage in the client's web browser when the currently edited
     * part of the {@code FancyMessage} is clicked.
     *
     * @param url The URL of the page to open when the link is clicked.
     * @return This builder instance.
     */
    public MessageUtil link(final String url) {
        onClick("open_url", url);
        return this;
    }

    /**
     * Set the behavior of the current editing component to instruct the client
     * to replace the chat input box content with the specified string when the
     * currently edited part of the {@code FancyMessage} is clicked. The client
     * will not immediately send the command to the server to be executed unless
     * the client player submits the command/chat message, usually with the
     * enter key.
     *
     * @param command The text to display in the chat bar of the client.
     * @return This builder instance.
     */
    public MessageUtil suggest(final String command) {
        onClick("suggest_command", command);
        return this;
    }

    /**
     * Set the behavior of the current editing component to instruct the client
     * to send the specified string to the server as a chat message when the
     * currently edited part of the {@code FancyMessage} is clicked. The client
     * <b>will</b> immediately send the command to the server to be executed
     * when the editing component is clicked.
     *
     * @param command The text to display in the chat bar of the client.
     * @return This builder instance.
     */
    public MessageUtil command(final String command) {
        onClick("run_command", command);
        return this;
    }

    /**
     * Set the behavior of the current editing component to display information
     * about an achievement when the client hovers over the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param name The name of the achievement to display, excluding the
     * "achievement." prefix.
     * @return This builder instance.
     */
    public MessageUtil achievementTooltip(final String name) {
        onHover("show_achievement", new JsonString("achievement." + (name.contains(".") ? name.split("\\.")[1] : name)));
        return this;
    }

    /**
     * Set the behavior of the current editing component to display information
     * about an achievement when the client hovers over the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param which The achievement to display.
     * @return This builder instance.
     */
    public MessageUtil achievementTooltip(final Achievement which) {
        try {
            Object achievement = Reflection.getMethod(Reflection.getOBCClass("CraftStatistic"), "getNMSAchievement", Achievement.class).invoke(null, which);
            return achievementTooltip(((String) ReflectionUtil.execute("name", achievement).fetch()));
        } catch (IllegalAccessException e) {
            Bukkit.getLogger().log(Level.WARNING, "Could not access method.", e);
            return this;
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().log(Level.WARNING, "Argument could not be passed.", e);
            return this;
        } catch (InvocationTargetException e) {
            Bukkit.getLogger().log(Level.WARNING, "A error has occured durring invoking of method.", e);
            return this;
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    /**
     * Set the behavior of the current editing component to display information
     * about a parameterless statistic when the client hovers over the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param which The statistic to display.
     * @return This builder instance.
     * @throws IllegalArgumentException If the statistic requires a parameter
     * which was not supplied.
     */
    public MessageUtil statisticTooltip(final Statistic which) {
        Type type = which.getType();
        if (type != Type.UNTYPED) {
            throw new IllegalArgumentException("That statistic requires an additional " + type + " parameter!");
        }
        try {
            Object statistic = Reflection.getMethod(Reflection.getOBCClass("CraftStatistic"), "getNMSStatistic", Statistic.class).invoke(null, which);
            return achievementTooltip((String) Reflection.getField(Reflection.getNMSClass("Statistic"), "name").get(statistic));
        } catch (IllegalAccessException e) {
            Bukkit.getLogger().log(Level.WARNING, "Could not access method.", e);
            return this;
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().log(Level.WARNING, "Argument could not be passed.", e);
            return this;
        } catch (InvocationTargetException e) {
            Bukkit.getLogger().log(Level.WARNING, "A error has occured durring invoking of method.", e);
            return this;
        }
    }

    /**
     * Set the behavior of the current editing component to display information
     * about a statistic parameter with a material when the client hovers over
     * the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param which The statistic to display.
     * @param item The sole material parameter to the statistic.
     * @return This builder instance.
     * @throws IllegalArgumentException If the statistic requires a parameter
     * which was not supplied, or was supplied a parameter that was not
     * required.
     */
    public MessageUtil statisticTooltip(final Statistic which, Material item) {
        Type type = which.getType();
        if (type == Type.UNTYPED) {
            throw new IllegalArgumentException("That statistic needs no additional parameter!");
        }
        if ((type == Type.BLOCK && item.isBlock()) || type == Type.ENTITY) {
            throw new IllegalArgumentException("Wrong parameter type for that statistic - needs " + type + "!");
        }
        try {
            Object statistic = Reflection.getMethod(Reflection.getOBCClass("CraftStatistic"), "getMaterialStatistic", Statistic.class, Material.class).invoke(null, which, item);
            return achievementTooltip((String) Reflection.getField(Reflection.getNMSClass("Statistic"), "name").get(statistic));
        } catch (IllegalAccessException e) {
            Bukkit.getLogger().log(Level.WARNING, "Could not access method.", e);
            return this;
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().log(Level.WARNING, "Argument could not be passed.", e);
            return this;
        } catch (InvocationTargetException e) {
            Bukkit.getLogger().log(Level.WARNING, "A error has occured durring invoking of method.", e);
            return this;
        }
    }

    /**
     * Set the behavior of the current editing component to display information
     * about a statistic parameter with an entity type when the client hovers
     * over the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param which The statistic to display.
     * @param entity The sole entity type parameter to the statistic.
     * @return This builder instance.
     * @throws IllegalArgumentException If the statistic requires a parameter
     * which was not supplied, or was supplied a parameter that was not
     * required.
     */
    public MessageUtil statisticTooltip(final Statistic which, EntityType entity) {
        Type type = which.getType();
        if (type == Type.UNTYPED) {
            throw new IllegalArgumentException("That statistic needs no additional parameter!");
        }
        if (type != Type.ENTITY) {
            throw new IllegalArgumentException("Wrong parameter type for that statistic - needs " + type + "!");
        }
        try {
            Object statistic = Reflection.getMethod(Reflection.getOBCClass("CraftStatistic"), "getEntityStatistic", Statistic.class, EntityType.class).invoke(null, which, entity);
            return achievementTooltip((String) Reflection.getField(Reflection.getNMSClass("Statistic"), "name").get(statistic));
        } catch (IllegalAccessException e) {
            Bukkit.getLogger().log(Level.WARNING, "Could not access method.", e);
            return this;
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().log(Level.WARNING, "Argument could not be passed.", e);
            return this;
        } catch (InvocationTargetException e) {
            Bukkit.getLogger().log(Level.WARNING, "A error has occured durring invoking of method.", e);
            return this;
        }
    }

    /**
     * Set the behavior of the current editing component to display information
     * about an item when the client hovers over the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param itemJSON A string representing the JSON-serialized NBT data tag of
     * an {@link ItemStack}.
     * @return This builder instance.
     */
    public MessageUtil itemTooltip(final String itemJSON) {
        onHover("show_item", new JsonString(itemJSON)); // Seems a bit hacky, considering we have a JSON object as a parameter
        return this;
    }

    /**
     * Set the behavior of the current editing component to display information
     * about an item when the client hovers over the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param itemStack The stack for which to display information.
     * @return This builder instance.
     */
    public MessageUtil itemTooltip(final ItemStack itemStack) {
        try {
            Object nmsItem = Reflection.getMethod(Reflection.getOBCClass("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class).invoke(null, itemStack);
            return itemTooltip(Reflection.getMethod(Reflection.getNMSClass("ItemStack"), "save", Reflection.getNMSClass("NBTTagCompound")).invoke(nmsItem, Reflection.getNMSClass("NBTTagCompound").newInstance()).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    /**
     * Set the behavior of the current editing component to display raw text
     * when the client hovers over the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param text The text, which supports newlines, which will be displayed to
     * the client upon hovering.
     * @return This builder instance.
     */
    public MessageUtil tooltip(final String text) {
        onHover("show_text", new JsonString(text));
        return this;
    }

    /**
     * Set the behavior of the current editing component to display raw text
     * when the client hovers over the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param lines The lines of text which will be displayed to the client upon
     * hovering. The iteration order of this object will be the order in which
     * the lines of the tooltip are created.
     * @return This builder instance.
     */
    public MessageUtil tooltip(final Iterable<String> lines) {
        tooltip(ArrayWrapper.toArray(lines, String.class));
        return this;
    }

    /**
     * Set the behavior of the current editing component to display raw text
     * when the client hovers over the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param lines The lines of text which will be displayed to the client upon
     * hovering.
     * @return This builder instance.
     */
    public MessageUtil tooltip(final String... lines) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            builder.append(lines[i]);
            if (i != lines.length - 1) {
                builder.append('\n');
            }
        }
        tooltip(builder.toString());
        return this;
    }

    /**
     * Set the behavior of the current editing component to display formatted
     * text when the client hovers over the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param text The formatted text which will be displayed to the client upon
     * hovering.
     * @return This builder instance.
     */
    public MessageUtil formattedTooltip(MessageUtil text) {
        for (MessagePart component : text.messageParts) {
            if (component.clickActionData != null && component.clickActionName != null) {
                throw new IllegalArgumentException("The tooltip text cannot have click data.");
            } else if (component.hoverActionData != null && component.hoverActionName != null) {
                throw new IllegalArgumentException("The tooltip text cannot have a tooltip.");
            }
        }
        onHover("show_text", text);
        return this;
    }

    /**
     * Set the behavior of the current editing component to display the
     * specified lines of formatted text when the client hovers over the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param lines The lines of formatted text which will be displayed to the
     * client upon hovering.
     * @return This builder instance.
     */
    public MessageUtil formattedTooltip(MessageUtil... lines) {
        if (lines.length < 1) {
            onHover(null, null); // Clear tooltip
            return this;
        }

        MessageUtil result = new MessageUtil();
        result.messageParts.clear(); // Remove the one existing text component that exists by default, which destabilizes the object

        for (int i = 0; i < lines.length; i++) {
            try {
                for (MessagePart component : lines[i]) {
                    if (component.clickActionData != null && component.clickActionName != null) {
                        throw new IllegalArgumentException("The tooltip text cannot have click data.");
                    } else if (component.hoverActionData != null && component.hoverActionName != null) {
                        throw new IllegalArgumentException("The tooltip text cannot have a tooltip.");
                    }
                    if (component.hasText()) {
                        result.messageParts.add(component.clone());
                    }
                }
                if (i != lines.length - 1) {
                    result.messageParts.add(new MessagePart(TextualComponent.rawText("\n")));
                }
            } catch (CloneNotSupportedException e) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to clone object", e);
                return this;
            }
        }
        return formattedTooltip(result.messageParts.isEmpty() ? null : result); // Throws NPE if size is 0, intended
    }

    /**
     * Set the behavior of the current editing component to display the
     * specified lines of formatted text when the client hovers over the text.
     * <p>
     * Tooltips do not inherit display characteristics, such as color and
     * styles, from the message component on which they are applied.</p>
     *
     * @param lines The lines of text which will be displayed to the client upon
     * hovering. The iteration order of this object will be the order in which
     * the lines of the tooltip are created.
     * @return This builder instance.
     */
    public MessageUtil formattedTooltip(final Iterable<MessageUtil> lines) {
        return formattedTooltip(ArrayWrapper.toArray(lines, MessageUtil.class));
    }

    /**
     * Terminate construction of the current editing component, and begin
     * construction of a new message component. After a successful call to this
     * method, all setter methods will refer to a new message component, created
     * as a result of the call to this method.
     *
     * @param text The text which will populate the new message component.
     * @return This builder instance.
     */
    public MessageUtil then(final String text) {
        return then(TextualComponent.rawText(text));
    }

    /**
     * Terminate construction of the current editing component, and begin
     * construction of a new message component. After a successful call to this
     * method, all setter methods will refer to a new message component, created
     * as a result of the call to this method.
     *
     * @param text The text which will populate the new message component.
     * @return This builder instance.
     */
    public MessageUtil then(final TextualComponent text) {
        if (!latest().hasText()) {
            throw new IllegalStateException("previous message part has no text");
        }
        messageParts.add(new MessagePart(text));
        dirty = true;
        return this;
    }

    /**
     * Terminate construction of the current editing component, and begin
     * construction of a new message component. After a successful call to this
     * method, all setter methods will refer to a new message component, created
     * as a result of the call to this method.
     *
     * @return This builder instance.
     */
    public MessageUtil then() {
        if (!latest().hasText()) {
            throw new IllegalStateException("previous message part has no text");
        }
        messageParts.add(new MessagePart());
        dirty = true;
        return this;
    }

    @Override
    public void writeJson(JsonWriter writer) throws IOException {
        if (messageParts.size() == 1) {
            latest().writeJson(writer);
        } else {
            writer.beginObject().name("text").value("").name("extra").beginArray();
            for (final MessagePart part : this) {
                part.writeJson(writer);
            }
            writer.endArray().endObject();
        }
    }

    /**
     * Serialize this fancy message, converting it into syntactically-valid JSON
     * using a {@link JsonWriter}. This JSON should be compatible with vanilla
     * formatter commands such as {@code /tellraw}.
     *
     * @return The JSON string representing this object.
     */
    public String toJSONString() {
        if (!dirty && jsonString != null) {
            return jsonString;
        }
        StringWriter string = new StringWriter();
        JsonWriter json = new JsonWriter(string);
        try {
            writeJson(json);
            json.close();
        } catch (IOException e) {
            throw new RuntimeException("invalid message");
        }
        jsonString = string.toString();
        dirty = false;
        return jsonString;
    }

    /**
     * Sends this message to a player. The player will receive the fully-fledged
     * formatted display of this message.
     *
     * @param player The player who will receive the message.
     */
    public void send(Player player) {
        send(player, toJSONString());
    }

    private void send(CommandSender sender, String jsonString) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(toOldMessageFormat());
            return;
        }
        Player player = (Player) sender;
        try {
            Object handle = Reflection.getHandle(player);
            Object connection = Reflection.getField(handle.getClass(), "playerConnection").get(handle);
            Reflection.getMethod(connection.getClass(), "sendPacket", Reflection.getNMSClass("Packet")).invoke(connection, createChatPacket(jsonString));
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().log(Level.WARNING, "Argument could not be passed.", e);
        } catch (IllegalAccessException e) {
            Bukkit.getLogger().log(Level.WARNING, "Could not access method.", e);
        } catch (InstantiationException e) {
            Bukkit.getLogger().log(Level.WARNING, "Underlying class is abstract.", e);
        } catch (InvocationTargetException e) {
            Bukkit.getLogger().log(Level.WARNING, "A error has occured durring invoking of method.", e);
        } catch (NoSuchMethodException e) {
            Bukkit.getLogger().log(Level.WARNING, "Could not find method.", e);
        }
    }

    private Object createChatPacket(String json) throws IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        if (nmsChatSerializerGsonInstance == null) {
            // Find the field and its value, completely bypassing obfuscation
            for (Field declaredField : Reflection.getNMSClass("ChatSerializer").getDeclaredFields()) {
                if (Modifier.isFinal(declaredField.getModifiers()) && Modifier.isStatic(declaredField.getModifiers()) && declaredField.getType().getName().endsWith("Gson")) {
                    // We've found our field
                    declaredField.setAccessible(true);
                    nmsChatSerializerGsonInstance = declaredField.get(null);
                    fromJsonMethod = nmsChatSerializerGsonInstance.getClass().getMethod("fromJson", String.class, Class.class);
                    break;
                }
            }
        }

        // Since the method is so simple, and all the obfuscated methods have the same name, it's easier to reimplement 'IChatBaseComponent a(String)' than to reflectively call it
        // Of course, the implementation may change, but fuzzy matches might break with signature changes
        Object serializedChatComponent = fromJsonMethod.invoke(nmsChatSerializerGsonInstance, json, Reflection.getNMSClass("IChatBaseComponent"));

        return nmsPacketPlayOutChatConstructor.newInstance(serializedChatComponent);
    }

    /**
     * Sends this message to a command sender. If the sender is a player, they
     * will receive the fully-fledged formatted display of this message.
     * Otherwise, they will receive a version of this message with less
     * formatting.
     *
     * @param sender The command sender who will receive the message.
     * @see #toOldMessageFormat()
     */
    public void send(CommandSender sender) {
        send(sender, toJSONString());
    }

    /**
     * Sends this message to multiple command senders.
     *
     * @param senders The command senders who will receive the message.
     * @see #send(CommandSender)
     */
    public void send(final Iterable<? extends CommandSender> senders) {
        String string = toJSONString();
        for (final CommandSender sender : senders) {
            send(sender, string);
        }
    }

    /**
     * Convert this message to a human-readable string with limited formatting.
     * This method is used to send this message to clients without JSON
     * formatting support.
     * <p>
     * Serialization of this message by using this message will include (in this
     * order for each message part):
     * <ol>
     * <li>The color of each message part.</li>
     * <li>The applicable stylizations for each message part.</li>
     * <li>The core text of the message part.</li>
     * </ol>
     * The primary omissions are tooltips and clickable actions. Consequently,
     * this method should be used only as a last resort.
     * </p>
     * <p>
     * Color and formatting can be removed from the returned string by using
     * {@link ChatColor#stripColor(String)}.</p>
     *
     * @return A human-readable string representing limited formatting in
     * addition to the core text of this message.
     */
    public String toOldMessageFormat() {
        StringBuilder result = new StringBuilder();
        for (MessagePart part : this) {
            result.append(part.color == null ? "" : part.color);
            for (ChatColor formatSpecifier : part.styles) {
                result.append(formatSpecifier);
            }
            result.append(part.text);
        }
        return result.toString();
    }

    private MessagePart latest() {
        return messageParts.get(messageParts.size() - 1);
    }

    private void onClick(final String name, final String data) {
        final MessagePart latest = latest();
        latest.clickActionName = name;
        latest.clickActionData = data;
        dirty = true;
    }

    private void onHover(final String name, final JsonRepresentedObject data) {
        final MessagePart latest = latest();
        latest.hoverActionName = name;
        latest.hoverActionData = data;
        dirty = true;
    }

    // Doc copied from interface
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("messageParts", messageParts);
//		map.put("JSON", toJSONString());
        return map;
    }

    /**
     * <b>Internally called method. Not for API consumption.</b>
     */
    public Iterator<MessagePart> iterator() {
        return messageParts.iterator();
    }
}

/**
 * Represents a JSON string value. Writes by this object will not write name
 * values nor begin/end objects in the JSON stream. All writes merely write the
 * represented string value.
 */
@Immutable
final class JsonString implements JsonRepresentedObject, ConfigurationSerializable {

    private String _value;

    public JsonString(String value) {
        _value = value;
    }

    public static JsonString deserialize(Map<String, Object> map) {
        return new JsonString(map.get("stringValue").toString());
    }

    @Override
    public void writeJson(JsonWriter writer) throws IOException {
        writer.value(getValue());
    }

    public String getValue() {
        return _value;
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> theSingleValue = new HashMap<String, Object>();
        theSingleValue.put("stringValue", _value);
        return theSingleValue;
    }

    @Override
    public String toString() {
        return _value;
    }
}

/**
 * Internal class: Represents a component of a JSON-serializable
 * {@link MessageUtil}.
 */
final class MessagePart implements JsonRepresentedObject, ConfigurationSerializable, Cloneable {

    static final BiMap<ChatColor, String> stylesToNames;

    static {
        ImmutableBiMap.Builder<ChatColor, String> builder = ImmutableBiMap.builder();
        for (final ChatColor style : ChatColor.values()) {
            if (!style.isFormat()) {
                continue;
            }

            String styleName;
            switch (style) {
                case MAGIC:
                    styleName = "obfuscated";
                    break;
                case UNDERLINE:
                    styleName = "underlined";
                    break;
                default:
                    styleName = style.name().toLowerCase();
                    break;
            }

            builder.put(style, styleName);
        }
        stylesToNames = builder.build();
    }
    ChatColor color = ChatColor.WHITE;
    ArrayList<ChatColor> styles = new ArrayList<ChatColor>();
    String clickActionName = null, clickActionData = null,
            hoverActionName = null;
    JsonRepresentedObject hoverActionData = null;
    TextualComponent text = null;

    MessagePart(final TextualComponent text) {
        this.text = text;
    }

    MessagePart() {
        this.text = null;
    }

    @SuppressWarnings("unchecked")
    public static MessagePart deserialize(Map<String, Object> serialized) {
        MessagePart part = new MessagePart((TextualComponent) serialized.get("text"));
        part.styles = (ArrayList<ChatColor>) serialized.get("styles");
        part.color = ChatColor.getByChar(serialized.get("color").toString());
        part.hoverActionName = serialized.get("hoverActionName").toString();
        part.hoverActionData = (JsonRepresentedObject) serialized.get("hoverActionData");
        part.clickActionName = serialized.get("clickActionName").toString();
        part.clickActionData = serialized.get("clickActionData").toString();
        return part;
    }

    static {
        ConfigurationSerialization.registerClass(MessagePart.class);
    }

    boolean hasText() {
        return text != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MessagePart clone() throws CloneNotSupportedException {
        MessagePart obj = (MessagePart) super.clone();
        obj.styles = (ArrayList<ChatColor>) styles.clone();
        if (hoverActionData instanceof JsonString) {
            obj.hoverActionData = new JsonString(((JsonString) hoverActionData).getValue());
        } else if (hoverActionData instanceof MessageUtil) {
            obj.hoverActionData = ((MessageUtil) hoverActionData).clone();
        }
        return obj;

    }

    public void writeJson(JsonWriter json) {
        try {
            json.beginObject();
            text.writeJson(json);
            json.name("color").value(color.name().toLowerCase());
            for (final ChatColor style : styles) {
                json.name(stylesToNames.get(style)).value(true);
            }
            if (clickActionName != null && clickActionData != null) {
                json.name("clickEvent")
                        .beginObject()
                        .name("action").value(clickActionName)
                        .name("value").value(clickActionData)
                        .endObject();
            }
            if (hoverActionName != null && hoverActionData != null) {
                json.name("hoverEvent")
                        .beginObject()
                        .name("action").value(hoverActionName)
                        .name("value");
                hoverActionData.writeJson(json);
                json.endObject();
            }
            json.endObject();
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "A problem occured during writing of JSON string", e);
        }
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("text", text);
        map.put("styles", styles);
        map.put("color", color.getChar());
        map.put("hoverActionName", hoverActionName);
        map.put("hoverActionData", hoverActionData);
        map.put("clickActionName", clickActionName);
        map.put("clickActionData", clickActionData);
        return map;
    }

}

/**
 * Represents a textual component of a message part. This can be used to not
 * only represent string literals in a JSON message, but also to represent
 * localized strings and other text values.
 */
abstract class TextualComponent implements Cloneable {

    static {
        ConfigurationSerialization.registerClass(TextualComponent.ArbitraryTextTypeComponent.class);
        ConfigurationSerialization.registerClass(TextualComponent.ComplexTextTypeComponent.class);
    }

    static TextualComponent deserialize(Map<String, Object> map) {
        if (map.containsKey("key") && map.size() == 2 && map.containsKey("value")) {
            // Arbitrary text component
            return ArbitraryTextTypeComponent.deserialize(map);
        } else if (map.size() >= 2 && map.containsKey("key") && !map.containsKey("value") /* It contains keys that START WITH value */) {
            // Complex JSON object
            return ComplexTextTypeComponent.deserialize(map);
        }

        return null;
    }

    static boolean isTextKey(String key) {
        return key.equals("translate") || key.equals("text") || key.equals("score") || key.equals("selector");
    }

    /**
     * Create a textual component representing a string literal. This is the
     * default type of textual component when a single string literal is given
     * to a method.
     *
     * @param textValue The text which will be represented.
     * @return The text component representing the specified literal text.
     */
    public static TextualComponent rawText(String textValue) {
        return new ArbitraryTextTypeComponent("text", textValue);
    }

    /**
     * Create a textual component representing a localized string. The client
     * will see this text component as their localized version of the specified
     * string <em>key</em>, which can be overridden by a resource pack.
     * <p>
     * If the specified translation key is not present on the client resource
     * pack, the translation key will be displayed as a string literal to the
     * client.
     * </p>
     *
     * @param translateKey The string key which maps to localized text.
     * @return The text component representing the specified localized text.
     */
    public static TextualComponent localizedText(String translateKey) {
        return new ArbitraryTextTypeComponent("translate", translateKey);
    }

    private static void throwUnsupportedSnapshot() {
        throw new UnsupportedOperationException("This feature is only supported in snapshot releases.");
    }

    /**
     * Create a textual component representing a scoreboard value. The client
     * will see their own score for the specified objective as the text
     * represented by this component.
     * <p>
     * <b>This method is currently guaranteed to throw an
     * {@code UnsupportedOperationException} as it is only supported on snapshot
     * clients.</b>
     * </p>
     *
     * @param scoreboardObjective The name of the objective for which to display
     * the score.
     * @return The text component representing the specified scoreboard score
     * (for the viewing player), or {@code null} if an error occurs during JSON
     * serialization.
     */
    public static TextualComponent objectiveScore(String scoreboardObjective) {
        return objectiveScore("*", scoreboardObjective);
    }

    /**
     * Create a textual component representing a scoreboard value. The client
     * will see the score of the specified player for the specified objective as
     * the text represented by this component.
     * <p>
     * <b>This method is currently guaranteed to throw an
     * {@code UnsupportedOperationException} as it is only supported on snapshot
     * clients.</b>
     * </p>
     *
     * @param playerName The name of the player whos score will be shown. If
     * this string represents the single-character sequence "*", the viewing
     * player's score will be displayed. Standard minecraft selectors (@a, @p,
     * etc) are <em>not</em> supported.
     * @param scoreboardObjective The name of the objective for which to display
     * the score.
     * @return The text component representing the specified scoreboard score
     * for the specified player, or {@code null} if an error occurs during JSON
     * serialization.
     */
    public static TextualComponent objectiveScore(String playerName, String scoreboardObjective) {
        throwUnsupportedSnapshot(); // Remove this line when the feature is released to non-snapshot versions, in addition to updating ALL THE OVERLOADS documentation accordingly

        return new ComplexTextTypeComponent("score", ImmutableMap.<String, String>builder()
                .put("name", playerName)
                .put("objective", scoreboardObjective)
                .build());
    }

    /**
     * Create a textual component representing a player name, retrievable by
     * using a standard minecraft selector. The client will see the players or
     * entities captured by the specified selector as the text represented by
     * this component.
     * <p>
     * <b>This method is currently guaranteed to throw an
     * {@code UnsupportedOperationException} as it is only supported on snapshot
     * clients.</b>
     * </p>
     *
     * @param selector The minecraft player or entity selector which will
     * capture the entities whose string representations will be displayed in
     * the place of this text component.
     * @return The text component representing the name of the entities captured
     * by the selector.
     */
    public static TextualComponent selector(String selector) {
        throwUnsupportedSnapshot(); // Remove this line when the feature is released to non-snapshot versions, in addition to updating ALL THE OVERLOADS documentation accordingly

        return new ArbitraryTextTypeComponent("selector", selector);
    }

    @Override
    public String toString() {
        return getReadableString();
    }

    /**
     * @return The JSON key used to represent text components of this type.
     */
    public abstract String getKey();

    /**
     * @return A readable String
     */
    public abstract String getReadableString();

    /**
     * Clones a textual component instance. The returned object should not
     * reference this textual component instance, but should maintain the same
     * key and value.
     */
    @Override
    public abstract TextualComponent clone() throws CloneNotSupportedException;

    /**
     * Writes the text data represented by this textual component to the
     * specified JSON writer object. A new object within the writer is not
     * started.
     *
     * @param writer The object to which to write the JSON data.
     * @throws IOException If an error occurs while writing to the stream.
     */
    public abstract void writeJson(JsonWriter writer) throws IOException;

    /**
     * Internal class used to represent all types of text components. Exception
     * validating done is on keys and values.
     */
    private static final class ArbitraryTextTypeComponent extends TextualComponent implements ConfigurationSerializable {

        private String _key;
        private String _value;

        public ArbitraryTextTypeComponent(String key, String value) {
            setKey(key);
            setValue(value);
        }

        public static ArbitraryTextTypeComponent deserialize(Map<String, Object> map) {
            return new ArbitraryTextTypeComponent(map.get("key").toString(), map.get("value").toString());
        }

        @Override
        public String getKey() {
            return _key;
        }

        public void setKey(String key) {
            Preconditions.checkArgument(key != null && !key.isEmpty(), "The key must be specified.");
            _key = key;
        }

        public String getValue() {
            return _value;
        }

        public void setValue(String value) {
            Preconditions.checkArgument(value != null, "The value must be specified.");
            _value = value;
        }

        @Override
        public TextualComponent clone() throws CloneNotSupportedException {
            // Since this is a private and final class, we can just reinstantiate this class instead of casting super.clone
            return new ArbitraryTextTypeComponent(getKey(), getValue());
        }

        @Override
        public void writeJson(JsonWriter writer) throws IOException {
            writer.name(getKey()).value(getValue());
        }

        @SuppressWarnings("serial")
        public Map<String, Object> serialize() {
            return new HashMap<String, Object>() {
                {
                    put("key", getKey());
                    put("value", getValue());
                }
            };
        }

        @Override
        public String getReadableString() {
            return getValue();
        }
    }

    /**
     * Internal class used to represent a text component with a nested JSON
     * value. Exception validating done is on keys and values.
     */
    private static final class ComplexTextTypeComponent extends TextualComponent implements ConfigurationSerializable {

        private String _key;
        private Map<String, String> _value;

        public ComplexTextTypeComponent(String key, Map<String, String> values) {
            setKey(key);
            setValue(values);
        }

        public static ComplexTextTypeComponent deserialize(Map<String, Object> map) {
            String key = null;
            Map<String, String> value = new HashMap<String, String>();
            for (Map.Entry<String, Object> valEntry : map.entrySet()) {
                if (valEntry.getKey().equals("key")) {
                    key = (String) valEntry.getValue();
                } else if (valEntry.getKey().startsWith("value.")) {
                    value.put(((String) valEntry.getKey()).substring(6) /* Strips out the value prefix */, valEntry.getValue().toString());
                }
            }
            return new ComplexTextTypeComponent(key, value);
        }

        @Override
        public String getKey() {
            return _key;
        }

        public void setKey(String key) {
            Preconditions.checkArgument(key != null && !key.isEmpty(), "The key must be specified.");
            _key = key;
        }

        public Map<String, String> getValue() {
            return _value;
        }

        public void setValue(Map<String, String> value) {
            Preconditions.checkArgument(value != null, "The value must be specified.");
            _value = value;
        }

        @Override
        public TextualComponent clone() throws CloneNotSupportedException {
            // Since this is a private and final class, we can just reinstantiate this class instead of casting super.clone
            return new ComplexTextTypeComponent(getKey(), getValue());
        }

        @Override
        public void writeJson(JsonWriter writer) throws IOException {
            writer.name(getKey());
            writer.beginObject();
            for (Map.Entry<String, String> jsonPair : _value.entrySet()) {
                writer.name(jsonPair.getKey()).value(jsonPair.getValue());
            }
            writer.endObject();
        }

        @SuppressWarnings("serial")
        public Map<String, Object> serialize() {
            return new java.util.HashMap<String, Object>() {
                {
                    put("key", getKey());
                    for (Map.Entry<String, String> valEntry : getValue().entrySet()) {
                        put("value." + valEntry.getKey(), valEntry.getValue());
                    }
                }
            };
        }

        @Override
        public String getReadableString() {
            return getKey();
        }
    }
}

/**
 * Represents a wrapper around an array class of an arbitrary reference type,
 * which properly implements "value" hash code and equality functions.
 * <p>
 * This class is intended for use as a key to a map.
 * </p>
 *
 * @param <E> The type of elements in the array.
 * @author Glen Husman
 * @see Arrays
 */
final class ArrayWrapper<E> {

    private E[] _array;

    /**
     * Creates an array wrapper with some elements.
     *
     * @param elements The elements of the array.
     */
    public ArrayWrapper(@SuppressWarnings("unchecked") E... elements) {
        setArray(elements);
    }

    /**
     * Converts an iterable element collection to an array of elements. The
     * iteration order of the specified object will be used as the array element
     * order.
     *
     * @param list The iterable of objects which will be converted to an array.
     * @param c The type of the elements of the array.
     * @return An array of elements in the specified iterable.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Iterable<? extends T> list, Class<T> c) {
        int size = -1;
        if (list instanceof Collection<?>) {
            @SuppressWarnings("rawtypes")
            Collection coll = (Collection) list;
            size = coll.size();
        }

        if (size < 0) {
            size = 0;
            // Ugly hack: Count it ourselves
            for (@SuppressWarnings("unused") T element : list) {
                size++;
            }
        }

        T[] result = (T[]) Array.newInstance(c, size);
        int i = 0;
        for (T element : list) { // Assumes iteration order is consistent
            result[i++] = element; // Assign array element at index THEN increment counter
        }
        return result;
    }

    /**
     * Retrieves a reference to the wrapped array instance.
     *
     * @return The array wrapped by this instance.
     */
    public E[] getArray() {
        return _array;
    }

    /**
     * Set this wrapper to wrap a new array instance.
     *
     * @param array The new wrapped array.
     */
    public void setArray(E[] array) {
        Validate.notNull(array, "The array must not be null.");
        _array = array;
    }

    /**
     * Determines if this object has a value equivalent to another object.
     *
     * @see Arrays#equals(Object[], Object[])
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ArrayWrapper)) {
            return false;
        }
        return Arrays.equals(_array, ((ArrayWrapper) other)._array);
    }

    /**
     * Gets the hash code represented by this objects value.
     *
     * @return This object's hash code.
     * @see Arrays#hashCode(Object[])
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(_array);
    }
}

/**
 * A class containing static utility methods and caches which are intended as
 * reflective conveniences. Unless otherwise noted, upon failure methods will
 * return {@code null}.
 */
final class Reflection {

    /**
     * Stores loaded classes from the {@code net.minecraft.server} package.
     */
    private static final Map<String, Class<?>> _loadedNMSClasses = new HashMap<String, Class<?>>();
    /**
     * Stores loaded classes from the {@code org.bukkit.craftbukkit} package
     * (and subpackages).
     */
    private static final Map<String, Class<?>> _loadedOBCClasses = new HashMap<String, Class<?>>();
    private static final Map<Class<?>, Map<String, Field>> _loadedFields = new HashMap<Class<?>, Map<String, Field>>();
    /**
     * Contains loaded methods in a cache. The map maps [types to maps of
     * [method names to maps of [parameter types to method instances]]].
     */
    private static final Map<Class<?>, Map<String, Map<ArrayWrapper<Class<?>>, Method>>> _loadedMethods = new HashMap<Class<?>, Map<String, Map<ArrayWrapper<Class<?>>, Method>>>();
    private static String _versionString;

    private Reflection() {

    }

    /**
     * Gets the version string from the package name of the CraftBukkit server
     * implementation. This is needed to bypass the JAR package name changing on
     * each update.
     *
     * @return The version string of the OBC and NMS packages, <em>including the
     * trailing dot</em>.
     */
    public synchronized static String getVersion() {
        if (_versionString == null) {
            if (Bukkit.getServer() == null) {
                // The server hasn't started, static initializer call?
                return null;
            }
            String name = Bukkit.getServer().getClass().getPackage().getName();
            _versionString = name.substring(name.lastIndexOf('.') + 1) + ".";
        }

        return _versionString;
    }

    /**
     * Gets a {@link Class} object representing a type contained within the
     * {@code net.minecraft.server} versioned package. The class instances
     * returned by this method are cached, such that no lookup will be done
     * twice (unless multiple threads are accessing this method simultaneously).
     *
     * @param className The name of the class, excluding the package, within
     * NMS.
     * @return The class instance representing the specified NMS class, or
     * {@code null} if it could not be loaded.
     */
    public synchronized static Class<?> getNMSClass(String className) {
        if (_loadedNMSClasses.containsKey(className)) {
            return _loadedNMSClasses.get(className);
        }

        String fullName = "net.minecraft.server." + getVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
            _loadedNMSClasses.put(className, null);
            return null;
        }
        _loadedNMSClasses.put(className, clazz);
        return clazz;
    }

    /**
     * Gets a {@link Class} object representing a type contained within the
     * {@code org.bukkit.craftbukkit} versioned package. The class instances
     * returned by this method are cached, such that no lookup will be done
     * twice (unless multiple threads are accessing this method simultaneously).
     *
     * @param className The name of the class, excluding the package, within
     * OBC. This name may contain a subpackage name, such as
     * {@code inventory.CraftItemStack}.
     * @return The class instance representing the specified OBC class, or
     * {@code null} if it could not be loaded.
     */
    public synchronized static Class<?> getOBCClass(String className) {
        if (_loadedOBCClasses.containsKey(className)) {
            return _loadedOBCClasses.get(className);
        }

        String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
            _loadedOBCClasses.put(className, null);
            return null;
        }
        _loadedOBCClasses.put(className, clazz);
        return clazz;
    }

    /**
     * Attempts to get the NMS handle of a CraftBukkit object.
     * <p>
     * The only match currently attempted by this method is a retrieval by using
     * a parameterless {@code getHandle()} method implemented by the runtime
     * type of the specified object.
     * </p>
     *
     * @param obj The object for which to retrieve an NMS handle.
     * @return The NMS handle of the specified object, or {@code null} if it
     * could not be retrieved using {@code getHandle()}.
     */
    public synchronized static Object getHandle(Object obj) {
        try {
            return getMethod(obj.getClass(), "getHandle").invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a {@link Field} instance declared by the specified class with
     * the specified name. Java access modifiers are ignored during this
     * retrieval. No guarantee is made as to whether the field returned will be
     * an instance or static field.
     * <p>
     * A global caching mechanism within this class is used to store fields.
     * Combined with synchronization, this guarantees that no field will be
     * reflectively looked up twice.
     * </p>
     * <p>
     * If a field is deemed suitable for return,
     * {@link Field#setAccessible(boolean) setAccessible} will be invoked with
     * an argument of {@code true} before it is returned. This ensures that
     * callers do not have to check or worry about Java access modifiers when
     * dealing with the returned instance.
     * </p>
     *
     * @param clazz The class which contains the field to retrieve.
     * @param name The declared name of the field in the class.
     * @return A field object with the specified name declared by the specified
     * class.
     * @see Class#getDeclaredField(String)
     */
    public synchronized static Field getField(Class<?> clazz, String name) {
        Map<String, Field> loaded;
        if (!_loadedFields.containsKey(clazz)) {
            loaded = new HashMap<String, Field>();
            _loadedFields.put(clazz, loaded);
        } else {
            loaded = _loadedFields.get(clazz);
        }
        if (loaded.containsKey(name)) {
            // If the field is loaded (or cached as not existing), return the relevant value, which might be null
            return loaded.get(name);
        }
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            loaded.put(name, field);
            return field;
        } catch (Exception e) {
            // Error loading
            e.printStackTrace();
            // Cache field as not existing
            loaded.put(name, null);
            return null;
        }
    }

    /**
     * Retrieves a {@link Method} instance declared by the specified class with
     * the specified name and argument types. Java access modifiers are ignored
     * during this retrieval. No guarantee is made as to whether the field
     * returned will be an instance or static field.
     * <p>
     * A global caching mechanism within this class is used to store method.
     * Combined with synchronization, this guarantees that no method will be
     * reflectively looked up twice.
     * </p>
     * <p>
     * If a method is deemed suitable for return,
     * {@link Method#setAccessible(boolean) setAccessible} will be invoked with
     * an argument of {@code true} before it is returned. This ensures that
     * callers do not have to check or worry about Java access modifiers when
     * dealing with the returned instance.
     * </p>
     * <p/>
     * This method does <em>not</em> search superclasses of the specified type
     * for methods with the specified signature. Callers wishing this behavior
     * should use {@link Class#getDeclaredMethod(String, Class...)}.
     *
     * @param clazz The class which contains the method to retrieve.
     * @param name The declared name of the method in the class.
     * @param args The formal argument types of the method.
     * @return A method object with the specified name declared by the specified
     * class.
     */
    public synchronized static Method getMethod(Class<?> clazz, String name,
            Class<?>... args) {
        if (!_loadedMethods.containsKey(clazz)) {
            _loadedMethods.put(clazz, new HashMap<String, Map<ArrayWrapper<Class<?>>, Method>>());
        }

        Map<String, Map<ArrayWrapper<Class<?>>, Method>> loadedMethodNames = _loadedMethods.get(clazz);
        if (!loadedMethodNames.containsKey(name)) {
            loadedMethodNames.put(name, new HashMap<ArrayWrapper<Class<?>>, Method>());
        }

        Map<ArrayWrapper<Class<?>>, Method> loadedSignatures = loadedMethodNames.get(name);
        ArrayWrapper<Class<?>> wrappedArg = new ArrayWrapper<Class<?>>(args);
        if (loadedSignatures.containsKey(wrappedArg)) {
            return loadedSignatures.get(wrappedArg);
        }

        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name) && Arrays.equals(args, m.getParameterTypes())) {
                m.setAccessible(true);
                loadedSignatures.put(wrappedArg, m);
                return m;
            }
        }
        loadedSignatures.put(wrappedArg, null);
        return null;
    }

}
