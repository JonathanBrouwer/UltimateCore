package Bammerbom.UltimateCore.Resources.Utils;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.Statistic.Type;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MessageUtil {

	private final List<MessagePart> messageParts;
	private String jsonString;
	private boolean dirty;

	private Class<?> nmsChatSerializer = Reflection.getNMSClass("ChatSerializer");
	private Class<?> nmsTagCompound = Reflection.getNMSClass("NBTTagCompound");
	private Class<?> nmsPacketPlayOutChat = Reflection.getNMSClass("PacketPlayOutChat");
	private Class<?> nmsAchievement = Reflection.getNMSClass("Achievement");
	private Class<?> nmsStatistic = Reflection.getNMSClass("Statistic");
	private Class<?> nmsItemStack = Reflection.getNMSClass("ItemStack");

	private Class<?> obcStatistic = Reflection.getOBCClass("CraftStatistic");
	private Class<?> obcItemStack = Reflection.getOBCClass("inventory.CraftItemStack");

	public MessageUtil(final String firstPartText) {
		messageParts = new ArrayList<MessagePart>();
		messageParts.add(new MessagePart(firstPartText));
		jsonString = null;
		dirty = false;
	}

	public MessageUtil() {
		messageParts = new ArrayList<MessagePart>();
		messageParts.add(new MessagePart());
		jsonString = null;
		dirty = false;
	}

	public MessageUtil text(String text) {
		MessagePart latest = latest();
		if (latest.hasText()) {
			throw new IllegalStateException("text for this message part is already set");
		}
		latest.text = text;
		dirty = true;
		return this;
	}

	public MessageUtil color(final ChatColor color) {
		if (!color.isColor()) {
			throw new IllegalArgumentException(color.name() + " is not a color");
		}
		latest().color = color;
		dirty = true;
		return this;
	}

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

	public MessageUtil file(final String path) {
		onClick("open_file", path);
		return this;
	}

	public MessageUtil link(final String url) {
		onClick("open_url", url);
		return this;
	}

	public MessageUtil suggest(final String command) {
		onClick("suggest_command", command);
		return this;
	}

	public MessageUtil command(final String command) {
		onClick("run_command", command);
		return this;
	}

	public MessageUtil achievementTooltip(final String name) {
		onHover("show_achievement", "achievement." + name);
		return this;
	}

	public MessageUtil achievementTooltip(final Achievement which) {
		try {
			Object achievement = Reflection.getMethod(obcStatistic, "getNMSAchievement").invoke(null, which);
			return achievementTooltip((String) Reflection.getField(nmsAchievement, "name").get(achievement));
		} catch (Exception e) {
			e.printStackTrace();
			return this;
		}
	}

	public MessageUtil statisticTooltip(final Statistic which) {
		Type type = which.getType();
		if (type != Type.UNTYPED) {
			throw new IllegalArgumentException("That statistic requires an additional " + type + " parameter!");
		}
		try {
			Object statistic = Reflection.getMethod(obcStatistic, "getNMSStatistic").invoke(null, which);
			return achievementTooltip((String) Reflection.getField(nmsStatistic, "name").get(statistic));
		} catch (Exception e) {
			e.printStackTrace();
			return this;
		}
	}

	public MessageUtil statisticTooltip(final Statistic which, Material item) {
		Type type = which.getType();
		if (type == Type.UNTYPED) {
			throw new IllegalArgumentException("That statistic needs no additional parameter!");
		}
		if ((type == Type.BLOCK && item.isBlock()) || type == Type.ENTITY) {
			throw new IllegalArgumentException("Wrong parameter type for that statistic - needs " + type + "!");
		}
		try {
			Object statistic = Reflection.getMethod(obcStatistic, "getMaterialStatistic").invoke(null, which, item);
			return achievementTooltip((String) Reflection.getField(nmsStatistic, "name").get(statistic));
		} catch (Exception e) {
			e.printStackTrace();
			return this;
		}
	}

	public MessageUtil statisticTooltip(final Statistic which, EntityType entity) {
		Type type = which.getType();
		if (type == Type.UNTYPED) {
			throw new IllegalArgumentException("That statistic needs no additional parameter!");
		}
		if (type != Type.ENTITY) {
			throw new IllegalArgumentException("Wrong parameter type for that statistic - needs " + type + "!");
		}
		try {
			Object statistic = Reflection.getMethod(obcStatistic, "getEntityStatistic").invoke(null, which, entity);
			return achievementTooltip((String) Reflection.getField(nmsStatistic, "name").get(statistic));
		} catch (Exception e) {
			e.printStackTrace();
			return this;
		}
	}

	public MessageUtil itemTooltip(final String itemJSON) {
		onHover("show_item", itemJSON);
		return this;
	}

	public MessageUtil itemTooltip(final ItemStack itemStack) {
		try {
			Object nmsItem = Reflection.getMethod(obcItemStack, "asNMSCopy", ItemStack.class).invoke(null, itemStack);
			return itemTooltip(Reflection.getMethod(nmsItemStack, "save").invoke(nmsItem, nmsTagCompound.newInstance()).toString());
		} catch (Exception e) {
			e.printStackTrace();
			return this;
		}
	}

	public MessageUtil tooltip(final String text) {
		return tooltip(text.split("\\n"));
	}

	public MessageUtil tooltip(final List<String> lines) {
		return tooltip((String[])lines.toArray());
	}

	public MessageUtil tooltip(final String... lines) {
		if (lines.length == 1) {
			onHover("show_text", lines[0]);
		} else {
			itemTooltip(makeMultilineTooltip(lines));
		}
		return this;
	}

	public MessageUtil then(final Object obj) {
		if (!latest().hasText()) {
			throw new IllegalStateException("previous message part has no text");
		}
		messageParts.add(new MessagePart(obj.toString()));
		dirty = true;
		return this;
	}

	public MessageUtil then() {
		if (!latest().hasText()) {
			throw new IllegalStateException("previous message part has no text");
		}
		messageParts.add(new MessagePart());
		dirty = true;
		return this;
	}

	public String toJSONString() {
		if (!dirty && jsonString != null) {
			return jsonString;
		}
		StringWriter string = new StringWriter();
		JsonWriter json = new JsonWriter(string);
		try {
			if (messageParts.size() == 1) {
				latest().writeJson(json);
			} else {
				json.beginObject().name("text").value("").name("extra").beginArray();
				for (final MessagePart part : messageParts) {
					part.writeJson(json);
				}
				json.endArray().endObject();
				json.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("invalid message");
		}
		jsonString = string.toString();
		dirty = false;
		return jsonString;
	}

	public void send(Player player){
		try {
			Object handle = Reflection.getHandle(player);
			Object connection = Reflection.getField(handle.getClass(), "playerConnection").get(handle);
			Object serialized = Reflection.getMethod(nmsChatSerializer, "a", String.class).invoke(null, toJSONString());
			Object packet = nmsPacketPlayOutChat.getConstructor(Reflection.getNMSClass("IChatBaseComponent")).newInstance(serialized);
			Reflection.getMethod(connection.getClass(), "sendPacket").invoke(connection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(CommandSender sender) {
		if (sender instanceof Player) {
			send((Player) sender);
		} else {
			sender.sendMessage(toOldMessageFormat());
		}
	}

	public void send(final Iterable<CommandSender> senders) {
		for (final CommandSender sender : senders) {
			send(sender);
		}
	}

	public String toOldMessageFormat() {
		StringBuilder result = new StringBuilder();
		for (MessagePart part : messageParts) {
			result.append(part.color).append(part.text);
		}
		return result.toString();
	}

	private MessagePart latest() {
		return messageParts.get(messageParts.size() - 1);
	}

	private String makeMultilineTooltip(final String[] lines) {
		StringWriter string = new StringWriter();
		JsonWriter json = new JsonWriter(string);
		try {
			json.beginObject().name("id").value(1);
			json.name("tag").beginObject().name("display").beginObject();
			json.name("Name").value("\\u00A7f" + lines[0].replace("\"", "\\\""));
			json.name("Lore").beginArray();
			for (int i = 1; i < lines.length; i++) {
				final String line = lines[i];
				json.value(line.isEmpty() ? " " : line.replace("\"", "\\\""));
			}
			json.endArray().endObject().endObject().endObject();
			json.close();
		} catch (Exception e) {
			throw new RuntimeException("invalid tooltip");
		}
		return string.toString();
	}

	private void onClick(final String name, final String data) {
		final MessagePart latest = latest();
		latest.clickActionName = name;
		latest.clickActionData = data;
		dirty = true;
	}

	private void onHover(final String name, final String data) {
		final MessagePart latest = latest();
		latest.hoverActionName = name;
		latest.hoverActionData = data;
		dirty = true;
	}

}

final class MessagePart {

	ChatColor color = ChatColor.WHITE;
	ArrayList<ChatColor> styles = new ArrayList<ChatColor>();
	String clickActionName = null, clickActionData = null,
		   hoverActionName = null, hoverActionData = null;
	String text = null;

	MessagePart(final String text) {
		this.text = text;
	}

	MessagePart() {}

	boolean hasText() {
		return text != null;
	}

	JsonWriter writeJson(JsonWriter json) {
		try {
			json.beginObject().name("text").value(text);
			json.name("color").value(color.name().toLowerCase());
			for (final ChatColor style : styles) {
				String styleName;
				switch (style) {
				case MAGIC:
					styleName = "obfuscated"; break;
				case UNDERLINE:
					styleName = "underlined"; break;
				default:
					styleName = style.name().toLowerCase(); break;
				}
				json.name(styleName).value(true);
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
					.name("value").value(hoverActionData)
					.endObject();
			}
			return json.endObject();
		} catch(Exception e){
			e.printStackTrace();
			return json;
		}
	}

}
class Reflection {

	public static String getVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		return version;
	}

	public static Class<?> getNMSClass(String className) {
		String fullName = "net.minecraft.server." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Class<?> getOBCClass(String className) {
		String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Object getHandle(Object obj) {
		try {
			return getMethod(obj.getClass(), "getHandle").invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Field getField(Class<?> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Method getMethod(Class<?> clazz, String name,
			Class<?>... args) {
		for (Method m : clazz.getMethods())
			if (m.getName().equals(name) && (args.length == 0 || ClassListEqual(args, m.getParameterTypes()))) {
				m.setAccessible(true);
				return m;
			}
		return null;
	}

	public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
		boolean equal = true;
		if (l1.length != l2.length)
			return false;
		for (int i = 0; i < l1.length; i++)
			if (l1[i] != l2[i]) {
				equal = false;
				break;
			}
		return equal;
	}

}