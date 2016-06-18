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
package bammerbom.ultimatecore.spongeapi.resources.classes;

import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.databases.EffectDatabase;
import bammerbom.ultimatecore.spongeapi.resources.databases.EnchantmentDatabase;
import bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil;
import com.google.common.base.Joiner;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.FireworkEffect;
import org.spongepowered.api.item.FireworkShape;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.service.profile.GameProfileResolver;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandSource;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class MetaItemStack {

    private static final Map<String, DyeColor> colorMap = new HashMap<>();
    private static final Map<String, FireworkShape> fireworkShape = new HashMap<>();
    private final transient Pattern splitPattern = Pattern.compile("[:+',;.]");
    private ItemStack stack;
    private FireworkEffectBuilder builder = r.getRegistry().createFireworkEffectBuilder();
    private PotionEffectType pEffectType;
    private PotionEffect pEffect;
    private boolean validFirework = false;
    private boolean validPotionEffect = false;
    private boolean validPotionDuration = false;
    private boolean validPotionPower = false;
    private boolean completePotion = false;
    private int power = 1;
    private int duration = 120;

    public MetaItemStack(ItemStack stack) {
        this.stack = stack.copy();
    }

    public static void start() {
        for (DyeColor color : r.getRegistry().getAllOf(CatalogTypes.DYE_COLOR)) {
            colorMap.put(color.getName(), color);
        }
        for (FireworkShape type : r.getRegistry().getAllOf(CatalogTypes.FIREWORK_SHAPE)) {
            fireworkShape.put(type.getName(), type);
        }
    }

    public ItemStack getItemStack() {
        return this.stack;
    }

    public boolean isValidFirework() {
        return this.validFirework;
    }

    public boolean isValidPotion() {
        return (this.validPotionEffect) && (this.validPotionDuration) && (this.validPotionPower);
    }

    public FireworkEffectBuilder getFireworkBuilder() {
        return this.builder;
    }

    public PotionEffect getPotionEffect() {
        return this.pEffect;
    }

    public boolean completePotion() {
        return this.completePotion;
    }

    private void resetPotionMeta() {
        this.pEffect = null;
        this.pEffectType = null;
        this.validPotionEffect = false;
        this.validPotionDuration = false;
        this.validPotionPower = false;
        this.completePotion = true;
    }

    public void parseStringMeta(CommandSource sender, boolean allowUnsafe, String[] string, int fromArg) throws Exception {
        if (string[fromArg].startsWith("{")) {
            this.stack = Bukkit.getServer().getUnsafe().modifyItemStack(this.stack, Joiner.on(' ').join(Arrays.asList(string).subList(fromArg, string.length))); //TODO
        } else {
            for (int i = fromArg;
                 i < string.length;
                 i++) {
                addStringMeta(sender, allowUnsafe, string[i]);
            }
            if (this.validFirework) {
                FireworkEffect effect = this.builder.build();
                stack.offer(Keys.FIREWORK_EFFECTS, Arrays.asList(effect));
            }
        }
    }

    public void addStringMeta(CommandSource cs, boolean allowUnsafe, String string) throws Exception {
        String[] split = this.splitPattern.split(string, 2);
        if (split.length < 1) {
            return;
        }

        if ((split.length > 1) && (split[0].equalsIgnoreCase("name"))) {
            stack.offer(Keys.DISPLAY_NAME, r.translateAlternateColorCodes('&', Texts.of(split[1])));
        } else if ((split.length > 1) && split[0].equalsIgnoreCase("durability") && r.isInt(split[1]) && stack.supports(Keys.ITEM_DURABILITY)))
        {
            stack.offer(Keys.ITEM_DURABILITY, Integer.parseInt(split[1]));
        }else
        if ((split.length > 1) && (split[0].equalsIgnoreCase("maxhealth") || split[0].equalsIgnoreCase("health"))) {
            if (r.isDouble(split[1])) {
                Double max = Double.parseDouble(split[1]);
                max = r.normalize(max, 0.0, 2147483647.0);
                AttributeUtil attributes = new AttributeUtil(stack);
                attributes.add(Attribute.newBuilder().name("Health").type(AttributeType.GENERIC_MAX_HEALTH).amount(max).build());
                stack = attributes.getStack();
            }
        } else if ((split.length > 1) && (split[0].equalsIgnoreCase("damage") || split[0].equalsIgnoreCase("attack") || split[0].equalsIgnoreCase("attackdamage"))) {
            if (r.isDouble(split[1])) {
                Double max = Double.parseDouble(split[1]);
                max = r.normalize(max, 0.0, Double.MAX_VALUE);
                AttributeUtil attributes = new AttributeUtil(stack);
                attributes.add(Attribute.newBuilder().name("Attack Damage").type(AttributeType.GENERIC_ATTACK_DAMAGE).amount(max).build());
                stack = attributes.getStack();
            }
        } else if ((split.length > 1) && (split[0].equalsIgnoreCase("speed") || split[0].equalsIgnoreCase("movementspeed") || split[0].equalsIgnoreCase("swiftness"))) {
            if (r.isDouble(split[1])) {
                Double max = Double.parseDouble(split[1]);
                max = max / 50;
                max = r.normalize(max, 0.0, Double.MAX_VALUE);
                AttributeUtil attributes = new AttributeUtil(stack);
                attributes.add(Attribute.newBuilder().name("Speed").type(AttributeType.GENERIC_MOVEMENT_SPEED).amount(max).build());
                stack = attributes.getStack();
            }
        } else if ((split.length > 1) && (split[0].equalsIgnoreCase("knockbackres") || split[0].equalsIgnoreCase("knockbackresistance") || split[0].equalsIgnoreCase("kres"))) {
            if (r.isDouble(split[1])) {
                Double max = Double.parseDouble(split[1]);
                max = r.normalize(max, 0.0, 100.0);
                max = max / 100.0;
                AttributeUtil attributes = new AttributeUtil(stack);
                attributes.add(Attribute.newBuilder().name("Knockback Resistance").type(AttributeType.GENERIC_KNOCKBACK_RESISTANCE).amount(max).build());
                stack = attributes.getStack();
            }
        } else if ((split.length > 1) && ((split[0].equalsIgnoreCase("lore")) || (split[0].equalsIgnoreCase("desc")))) {
            List<Text> lore = new ArrayList<>();
            for (String line : split[1].split("\\|")) {
                lore.add(r.translateAlternateColorCodes('&', Texts.of(line.replace('_', ' '))));
            }
            stack.offer(Keys.ITEM_LORE, lore);
        } else if ((split.length > 1) && ((split[0].equalsIgnoreCase("player")) || (split[0].equalsIgnoreCase("owner"))) && (this.stack.getItem() == ItemTypes.SKULL)) {
            if (stack.get(Keys.SKULL_TYPE).get().equals(SkullTypes.PLAYER)) {
                String owner = split[1];
                stack.offer(Keys.REPRESENTED_PLAYER, Sponge.getGame().getServiceManager().provide(GameProfileResolver.class).get().get(UUID.fromString(owner)).get());
            }

        } else if ((split.length > 1) && (split[0].equalsIgnoreCase("author")) && (this.stack.getItem().equals(ItemTypes.WRITTEN_BOOK))) {
            Text author = r.translateAlternateColorCodes('&', Texts.of(split[1]));
            stack.offer(Keys.BOOK_AUTHOR, author);
        } else if ((split.length > 1) && (split[0].equalsIgnoreCase("title")) && (this.stack.getItem().equals(ItemTypes.WRITTEN_BOOK))) {
            stack.offer(Keys.DISPLAY_NAME, r.translateAlternateColorCodes('&', Texts.of(split[1])));
        } else if ((split.length > 1) && (split[0].equalsIgnoreCase("power")) && (this.stack.getItem().equals(ItemTypes.FIREWORKS))) {
            int power = r.isInt(split[1]) ? Integer.parseInt(split[1]) : 0;
            stack.offer(Keys.FIREWORK_FLIGHT_MODIFIER, power);
        } else if (this.stack.getItem().equals(ItemTypes.FIREWORKS)) {
            addFireworkMeta(false, string);
        } else if (this.stack.getItem().equals(ItemTypes.POTION)) {
            addPotionMeta(false, string);
        } else if ((split.length > 1) && ((split[0].equalsIgnoreCase("color")) || (split[0].equalsIgnoreCase("colour"))) && stack.supports(Keys.COLOR)) {
            String[] color = split[1].split("(\\||,)");
            if (color.length == 3) {
                int red = r.isInt(color[0]) ? Integer.parseInt(color[0]) : 0;
                int green = r.isInt(color[1]) ? Integer.parseInt(color[1]) : 0;
                int blue = r.isInt(color[2]) ? Integer.parseInt(color[2]) : 0;
                stack.offer(Keys.COLOR, new Color(red, green, blue));
            } else {
                try {
                    if (r.getRegistry().getType(CatalogTypes.DYE_COLOR, split[1]).isPresent()) {
                        stack.offer(Keys.COLOR, r.getRegistry().getType(CatalogTypes.DYE_COLOR, split[1]).get().getColor());
                    }
                } catch (Exception e) {
                }
            }
        } else if (split[0].equalsIgnoreCase("glow") || split[0].equalsIgnoreCase("glowing")) {
            ItemUtil.addGlow(stack);
        } else if (split[0].equalsIgnoreCase("unbreakable")) {
            stack.offer(Keys.UNBREAKABLE, true);
        } else if (split.length > 1 && (split[0].equalsIgnoreCase("canplaceon"))) {
            Set<BlockType> c = new HashSet<>();
            if (split[1].contains(",")) {
                for (String s : split[1].split(",")) {
                    BlockType i = ItemUtil.searchBlock(s);
                    if (i == null) {
                        if (cs != null) {
                            r.sendMes(cs, "giveItemNotFound", "%Item", s);
                        }
                        return;
                    }
                    c.add(i);
                }
            } else {
                BlockType i = ItemUtil.searchBlock(split[1]);
                if (i == null) {
                    if (cs != null) {
                        r.sendMes(cs, "giveItemNotFound", "%Item", split[1]);
                    }
                    return;
                }
                c.add(i);
            }
            stack.offer(Keys.PLACEABLE_BLOCKS, c);
        } else if (split.length > 1 && (split[0].equalsIgnoreCase("candestroy") || split[0].equalsIgnoreCase("canbreak"))) {
            Set<BlockType> c = new HashSet<>();
            if (split[1].contains(",")) {
                for (String s : split[1].split(",")) {
                    BlockType i = ItemUtil.searchBlock(s);
                    if (i == null) {
                        if (cs != null) {
                            r.sendMes(cs, "giveItemNotFound", "%Item", s);
                        }
                        return;
                    }
                    c.add(i);
                }
            } else {
                BlockType i = ItemUtil.searchBlock(split[1]);
                if (i == null) {
                    if (cs != null) {
                        r.sendMes(cs, "giveItemNotFound", "%Item", split[1]);
                    }
                    return;
                }
                c.add(i);
            }
            stack.offer(Keys.BREAKABLE_BLOCK_TYPES, c);
        } else if (split.length > 1 && (split[0].equalsIgnoreCase("hidetags")) || split[0].equalsIgnoreCase("hideflags")) {
            Integer i = 0;
            if (split[1].contains(",")) {
                for (String s : split[1].split(",")) {
                    if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("enchants") || s.equalsIgnoreCase("enchantments")) {
                        i += 1;
                    } else if (s.equalsIgnoreCase("2") || s.equalsIgnoreCase("attributes") || s.equalsIgnoreCase("attributemodifiers")) {
                        i += 2;
                    } else if (s.equalsIgnoreCase("4") || s.equalsIgnoreCase("unbreakable")) {
                        i += 4;
                    } else if (s.equalsIgnoreCase("8") || s.equalsIgnoreCase("candestroy")) {
                        i += 8;
                    } else if (s.equalsIgnoreCase("16") || s.equalsIgnoreCase("canplaceon")) {
                        i += 16;
                    } else if (s.equalsIgnoreCase("32") || s.equalsIgnoreCase("other") || s.equalsIgnoreCase("others")) {
                        i += 32;
                    } else if (r.isInt(s)) {
                        i += Integer.parseInt(s);
                    }
                }
            } else {
                if (split[1].equalsIgnoreCase("1") || split[1].equalsIgnoreCase("enchants") || split[1].equalsIgnoreCase("enchantments")) {
                    i += 1;
                } else if (split[1].equalsIgnoreCase("2") || split[1].equalsIgnoreCase("attributes") || split[1].equalsIgnoreCase("attributemodifiers")) {
                    i += 2;
                } else if (split[1].equalsIgnoreCase("4") || split[1].equalsIgnoreCase("unbreakable")) {
                    i += 4;
                } else if (split[1].equalsIgnoreCase("8") || split[1].equalsIgnoreCase("candestroy")) {
                    i += 8;
                } else if (split[1].equalsIgnoreCase("16") || split[1].equalsIgnoreCase("canplaceon")) {
                    i += 16;
                } else if (split[1].equalsIgnoreCase("32") || split[1].equalsIgnoreCase("other") || split[1].equalsIgnoreCase("others")) {
                    i += 32;
                } else if (r.isInt(split[1])) {
                    i += Integer.parseInt(split[1]);
                }
            }
            ItemMeta meta = stack.getItemMeta();
            Map<String, Object> m = (Map<String, Object>) ReflectionUtil.execute("unhandledTags", meta).fetch();
            ReflectionObject nc = ReflectionUtil.executeStatic("parse({1})", ReflectionStatic.fromNMS("MojangsonParser"), "{HideFlags:" + i + "}");
            m.put("HideFlags", ReflectionUtil.execute("get({1})", nc.fetch(), "HideFlags").fetch());
            ReflectionObject ro = new ReflectionObject(meta);
            ro.set("unhandledTags", m);
            stack.setItemMeta(ro.fetchAs(ItemMeta.class));
        } else if (split[0].equalsIgnoreCase("size") && split.length > 1 && r.isInt(split[1])) {
            stack.setQuantity(Integer.parseInt(split[1]));
        } else {
            parseEnchantmentStrings(cs, allowUnsafe, split);
        }

    }

    public void addFireworkMeta(boolean allowShortName, String string) throws Exception {
        if (this.stack.getItem().equals(ItemTypes.FIREWORKS) || this.stack.getItem().equals(ItemTypes.FIREWORK_CHARGE)) {
            String[] split = this.splitPattern.split(string, 2);

            if (split.length < 2) {
                return;
            }

            if ((split[0].equalsIgnoreCase("color")) || (split[0].equalsIgnoreCase("colour")) || ((allowShortName) && (split[0].equalsIgnoreCase("c")))) {
                List<Color> primaryColors = new ArrayList<>();
                String[] colors = split[1].split(",");
                for (String color : colors) {
                    if (colorMap.containsKey(color.toUpperCase())) {
                        this.validFirework = true;
                        primaryColors.add(colorMap.get(color.toUpperCase()).getColor());
                    } else {
                        throw new Exception("Invalid format");
                    }
                }
                this.builder.colors(primaryColors);
            } else if ((split[0].equalsIgnoreCase("shape")) || (split[0].equalsIgnoreCase("type")) || ((allowShortName) && ((split[0].equalsIgnoreCase("s")) || (split[0].equalsIgnoreCase("t"))))) {
                FireworkShape finalEffect;
                split[1] = (split[1].equalsIgnoreCase("large") ? "BALL_LARGE" : split[1]);
                if (fireworkShape.containsKey(split[1].toUpperCase())) {
                    finalEffect = fireworkShape.get(split[1].toUpperCase());
                } else {
                    throw new Exception("");
                }
                if (finalEffect != null) {
                    this.builder.shape(finalEffect);
                }
            } else if ((split[0].equalsIgnoreCase("fade")) || ((allowShortName) && (split[0].equalsIgnoreCase("f")))) {
                List<Color> fadeColors = new ArrayList<>();
                String[] colors = split[1].split(",");
                for (String color : colors) {
                    if (colorMap.containsKey(color.toUpperCase())) {
                        fadeColors.add(colorMap.get(color.toUpperCase()).getColor());
                    } else {
                        throw new Exception("");
                    }
                }
                if (!fadeColors.isEmpty()) {
                    this.builder.colors(fadeColors);
                }
            } else if ((split[0].equalsIgnoreCase("effect")) || ((allowShortName) && (split[0].equalsIgnoreCase("e")))) {
                String[] effects = split[1].split(",");
                for (String effect : effects) {
                    if (effect.equalsIgnoreCase("twinkle")) {
                        this.builder.flicker(true);
                    } else if (effect.equalsIgnoreCase("trail")) {
                        this.builder.trail(true);
                    } else {
                        throw new Exception("");
                    }
                }
            } else if ((split[0].equalsIgnoreCase("power")) || ((allowShortName) && (split[0].equalsIgnoreCase("p")))) {
                Integer power = Integer.parseInt(split[1]);
                stack.offer(Keys.FIREWORK_FLIGHT_MODIFIER, power);
            }
        }
    }

    public void addPotionMeta(boolean allowShortName, String string) throws Exception {
        if (this.stack.getItem().equals(ItemTypes.POTION)) {
            String[] split = this.splitPattern.split(string, 2);

            if (split.length < 2) {
                return;
            }

            if ((split[0].equalsIgnoreCase("effect")) || ((allowShortName) && (split[0].equalsIgnoreCase("e")))) {
                this.pEffectType = EffectDatabase.getByName(split[1]);
                if ((this.pEffectType != null) && (this.pEffectType.getName() != null)) {
                    this.validPotionEffect = true;
                } else {
                    throw new Exception("");
                }
            } else if ((split[0].equalsIgnoreCase("power")) || ((allowShortName) && (split[0].equalsIgnoreCase("p")))) {
                if (r.isInt(split[1])) {
                    this.validPotionPower = true;
                    this.power = Integer.parseInt(split[1]);
                    if ((this.power > 0) && (this.power < 4)) {
                        this.power -= 1;
                    }
                } else {
                    throw new Exception("");
                }
            } else if ((split[0].equalsIgnoreCase("duration")) || ((allowShortName) && (split[0].equalsIgnoreCase("d")))) {
                if (r.isInt(split[1])) {
                    this.validPotionDuration = true;
                    this.duration = (Integer.parseInt(split[1]) * 20);
                } else {
                    throw new Exception("");
                }
            }

            if (isValidPotion()) {
                List<PotionEffect> efs = stack.get(Keys.POTION_EFFECTS).get();
                efs.add(r.getRegistry().createPotionEffectBuilder().potionType(pEffectType).duration(this.duration).amplifier(this.power).build());
                stack.offer(Keys.POTION_EFFECTS, efs);
                resetPotionMeta();
            }
        }
    }

    private void parseEnchantmentStrings(CommandSource cs, boolean allowUnsafe, String[] split) throws Exception {
        Enchantment enchantment = EnchantmentDatabase.getByName(split[0]);
        if ((enchantment == null)) {
            return;
        }

        int level = -1;
        if (split.length > 1) {
            try {
                level = Integer.parseInt(split[1]);
            } catch (NumberFormatException ex) {
                level = -1;
            }
        }

        if ((level < 0) || ((!allowUnsafe) && (level > enchantment.getMaximumLevel()))) {
            level = enchantment.getMaximumLevel();
        }
        addEnchantment(cs, allowUnsafe, enchantment, level);
    }

    public void addEnchantment(CommandSource cs, boolean allowUnsafe, Enchantment enchantment, int level) {
        if (enchantment == null) {
            return;
        }

        if (this.stack.getItem().equals(ItemTypes.ENCHANTED_BOOK)) {
            List<ItemEnchantment> enchl = stack.get(Keys.STORED_ENCHANTMENTS).get();
            if (level == 0) {
                ItemEnchantment cench = null;
                for (ItemEnchantment ench : enchl) {
                    if (ench.getEnchantment().equals(enchantment)) {
                        cench = ench;
                    }
                }
                if (cench != null) {
                    enchl.remove(cench);
                }
            } else {
                enchl.add(new ItemEnchantment(enchantment, level));
            }
            stack.offer(Keys.STORED_ENCHANTMENTS, enchl);
        } else if (level == 0) {
            List<ItemEnchantment> enchl = stack.get(Keys.ITEM_ENCHANTMENTS).get();
            ItemEnchantment cench = null;
            for (ItemEnchantment ench : enchl) {
                if (ench.getEnchantment().equals(enchantment)) {
                    cench = ench;
                }
            }
            if (cench != null) {
                enchl.remove(cench);
            }
            stack.offer(Keys.ITEM_ENCHANTMENTS, enchl);
        } else {
            List<ItemEnchantment> enchl = stack.get(Keys.ITEM_ENCHANTMENTS).get();
            enchl.add(new ItemEnchantment(enchantment, level));
            stack.offer(Keys.ITEM_ENCHANTMENTS, enchl);
        }
    }

}
