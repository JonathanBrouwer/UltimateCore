package Bammerbom.UltimateCore.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.AttributeUtil;
import Bammerbom.UltimateCore.Resources.Utils.AttributeUtil.Attribute;
import Bammerbom.UltimateCore.Resources.Utils.AttributeUtil.AttributeType;
import Bammerbom.UltimateCore.Resources.Utils.FormatUtil;

import com.google.common.base.Joiner;

public class MetaItemStack
{
private static final Map<String, DyeColor> colorMap = new HashMap<String, DyeColor>();
private static final Map<String, FireworkEffect.Type> fireworkShape = new HashMap<String, FireworkEffect.Type>();

  private final transient Pattern splitPattern = Pattern.compile("[:+',;.]");
  private ItemStack stack;
  private FireworkEffect.Builder builder = FireworkEffect.builder();
  private PotionEffectType pEffectType;
  private PotionEffect pEffect;
  private boolean validFirework = false;
  private boolean validPotionEffect = false;
  private boolean validPotionDuration = false;
  private boolean validPotionPower = false;
  private boolean completePotion = false;
  private int power = 1;
  private int duration = 120;
  
  static{
	    for (DyeColor color : DyeColor.values())
	    {
	      colorMap.put(color.name(), color);
	    }
	    for (FireworkEffect.Type type : FireworkEffect.Type.values())
	    {
	      fireworkShape.put(type.name(), type);
	    }
  }
  
  public MetaItemStack(ItemStack stack)
  {
    this.stack = stack.clone();
  }

  public ItemStack getItemStack()
  {
    return this.stack;
  }

  public boolean isValidFirework()
  {
    return this.validFirework;
  }

  public boolean isValidPotion()
  {
    return (this.validPotionEffect) && (this.validPotionDuration) && (this.validPotionPower);
  }

  public FireworkEffect.Builder getFireworkBuilder()
  {
    return this.builder;
  }

  public PotionEffect getPotionEffect()
  {
    return this.pEffect;
  }

  public boolean completePotion()
  {
    return this.completePotion;
  }

  private void resetPotionMeta()
  {
    this.pEffect = null;
    this.pEffectType = null;
    this.validPotionEffect = false;
    this.validPotionDuration = false;
    this.validPotionPower = false;
    this.completePotion = true;
  }

  @SuppressWarnings("deprecation")
public void parseStringMeta(CommandSender sender, boolean allowUnsafe, String[] string, int fromArg)
  {
    if (string[fromArg].startsWith("{"))
    {
        this.stack = Bukkit.getServer().getUnsafe().modifyItemStack(this.stack, Joiner.on(' ').join(Arrays.asList(string).subList(fromArg, string.length)));
    }
    else
    {
      for (int i = fromArg; i < string.length; i++)
      {
    	  addStringMeta(sender, allowUnsafe, string[i]);
      }
      if (this.validFirework)
      {
        FireworkEffect effect = this.builder.build();
        FireworkMeta fmeta = (FireworkMeta)this.stack.getItemMeta();
        fmeta.addEffect(effect);
        this.stack.setItemMeta(fmeta);
      }
    }
  }

public void addStringMeta(CommandSender sender, boolean allowUnsafe, String string)
  {
    String[] split = this.splitPattern.split(string, 2);
    if (split.length < 1)
    {
      return;
    }

    if ((split.length > 1) && (split[0].equalsIgnoreCase("name")))
    {
      String displayName = ChatColor.translateAlternateColorCodes('&', split[1].replace('_', ' '));
      ItemMeta meta = this.stack.getItemMeta();
      meta.setDisplayName(displayName);
      this.stack.setItemMeta(meta);
    }
    else if((split.length > 1) && (split[0].equalsIgnoreCase("maxhealth") || split[0].equalsIgnoreCase("health"))){
    	if(r.isDouble(split[1])){
    	Double max = Double.parseDouble(split[1]);
    	max = r.normalize(max, 0.0, 2147483647.0);
    	AttributeUtil attributes = new AttributeUtil(stack);
    	attributes.add(Attribute.newBuilder().name("Health").type(AttributeType.GENERIC_MAX_HEALTH).amount(max).build());
        stack = attributes.getStack();
    	}
    }
    else if((split.length > 1) && (split[0].equalsIgnoreCase("damage") || split[0].equalsIgnoreCase("attack") || split[0].equalsIgnoreCase("attackdamage"))){
    	if(r.isDouble(split[1])){
    	Double max = Double.parseDouble(split[1]);
    	max = r.normalize(max, 0.0, Double.MAX_VALUE);
    	AttributeUtil attributes = new AttributeUtil(stack);
    	attributes.add(Attribute.newBuilder().name("Attack Damage").type(AttributeType.GENERIC_ATTACK_DAMAGE).amount(max).build());
        stack = attributes.getStack();
    	}
    }
    else if((split.length > 1) && (split[0].equalsIgnoreCase("speed") || split[0].equalsIgnoreCase("movementspeed") || split[0].equalsIgnoreCase("swiftness"))){
    	if(r.isDouble(split[1])){
    	Double max = Double.parseDouble(split[1]);
    	max = max / 50;
    	max = r.normalize(max, 0.0, Double.MAX_VALUE);
    	AttributeUtil attributes = new AttributeUtil(stack);
    	attributes.add(Attribute.newBuilder().name("Speed").type(AttributeType.GENERIC_MOVEMENT_SPEED).amount(max).build());
        stack = attributes.getStack();
    	}
    }
    else if((split.length > 1) && (split[0].equalsIgnoreCase("knockback") || split[0].equalsIgnoreCase("knockbackresistance") || split[0].equalsIgnoreCase("resistance"))){
    	if(r.isDouble(split[1])){
    	Double max = Double.parseDouble(split[1]);
    	max = r.normalize(max, 0.0, 100.0);
    	max = max / 100.0;
    	AttributeUtil attributes = new AttributeUtil(stack);
    	attributes.add(Attribute.newBuilder().name("Knockback Resistance").type(AttributeType.GENERIC_KNOCKBACK_RESISTANCE).amount(max).build());
        stack = attributes.getStack();
    	}
    }
    else if ((split.length > 1) && ((split[0].equalsIgnoreCase("lore")) || (split[0].equalsIgnoreCase("desc"))))
    {
      List<String> lore = new ArrayList<String>();
      for (String line : split[1].split("\\|"))
      {
        lore.add(FormatUtil.replaceFormat(line.replace('_', ' ')));
      }
      ItemMeta meta = this.stack.getItemMeta();
      meta.setLore(lore);
      this.stack.setItemMeta(meta);
    }
    else if ((split.length > 1) && ((split[0].equalsIgnoreCase("player")) || (split[0].equalsIgnoreCase("owner"))) && (this.stack.getType() == Material.SKULL_ITEM))
    {
      if (this.stack.getDurability() == 3)
      {
        String owner = split[1];
        SkullMeta meta = (SkullMeta)this.stack.getItemMeta();
        meta.setOwner(owner);
        this.stack.setItemMeta(meta);
      }
      else
      {
      }
    }
    else if ((split.length > 1) && (split[0].equalsIgnoreCase("author")) && (this.stack.getType() == Material.WRITTEN_BOOK))
    {
      String author = FormatUtil.replaceFormat(split[1]);
      BookMeta meta = (BookMeta)this.stack.getItemMeta();
      meta.setAuthor(author);
      this.stack.setItemMeta(meta);
    }
    else if ((split.length > 1) && (split[0].equalsIgnoreCase("title")) && (this.stack.getType() == Material.WRITTEN_BOOK))
    {
      String title = FormatUtil.replaceFormat(split[1].replace('_', ' '));
      BookMeta meta = (BookMeta)this.stack.getItemMeta();
      meta.setTitle(title);
      this.stack.setItemMeta(meta);
    }
    else if ((split.length > 1) && (split[0].equalsIgnoreCase("power")) && (this.stack.getType() == Material.FIREWORK))
    {
      int power = r.isNumber(split[1]) ? Integer.parseInt(split[1]) : 0;
      FireworkMeta meta = (FireworkMeta)this.stack.getItemMeta();
      meta.setPower(power > 3 ? 4 : power);
      this.stack.setItemMeta(meta);
    }
    else if (this.stack.getType() == Material.FIREWORK)
    {
      try {
		addFireworkMeta(sender, false, string);
	} catch (Exception e) {
		e.printStackTrace();
	}
    }
    else if (this.stack.getType() == Material.POTION)
    {
      try {
		addPotionMeta(sender, false, string);
	} catch (Exception e) {
		e.printStackTrace();
	}
    }
    else if ((split.length > 1) && ((split[0].equalsIgnoreCase("color")) || (split[0].equalsIgnoreCase("colour"))) && ((this.stack.getType() == Material.LEATHER_BOOTS) || (this.stack.getType() == Material.LEATHER_CHESTPLATE) || (this.stack.getType() == Material.LEATHER_HELMET) || (this.stack.getType() == Material.LEATHER_LEGGINGS)))
    {
      String[] color = split[1].split("(\\||,)");
      if (color.length == 3)
      {
        int red = r.isNumber(color[0]) ? Integer.parseInt(color[0]) : 0;
        int green = r.isNumber(color[1]) ? Integer.parseInt(color[1]) : 0;
        int blue = r.isNumber(color[2]) ? Integer.parseInt(color[2]) : 0;
        LeatherArmorMeta meta = (LeatherArmorMeta)this.stack.getItemMeta();
        meta.setColor(Color.fromRGB(red, green, blue));
        this.stack.setItemMeta(meta);
      }
      else
      {
      }
    }
    else
    {
      try {
		parseEnchantmentStrings(sender, allowUnsafe, split);
	} catch (Exception e) {
		e.printStackTrace();
	}
    }
    
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
public void addFireworkMeta(CommandSender sender, boolean allowShortName, String string) throws Exception
  {
    if (this.stack.getType() == Material.FIREWORK)
    {
      String[] split = this.splitPattern.split(string, 2);

      if (split.length < 2)
      {
        return;
      }

      if ((split[0].equalsIgnoreCase("color")) || (split[0].equalsIgnoreCase("colour")) || ((allowShortName) && (split[0].equalsIgnoreCase("c"))))
      {
        if (this.validFirework)
        {
          FireworkEffect effect = this.builder.build();
          FireworkMeta fmeta = (FireworkMeta)this.stack.getItemMeta();
          fmeta.addEffect(effect);
          this.stack.setItemMeta(fmeta);
          this.builder = FireworkEffect.builder();
        }

        List primaryColors = new ArrayList();
        String[] colors = split[1].split(",");
        for (String color : colors)
        {
          if (colorMap.containsKey(color.toUpperCase()))
          {
            this.validFirework = true;
            primaryColors.add(((DyeColor)colorMap.get(color.toUpperCase())).getFireworkColor());
          }
          else
          {
            throw new Exception("Invalid format");
          }
        }
        this.builder.withColor(primaryColors);
      }
      else if ((split[0].equalsIgnoreCase("shape")) || (split[0].equalsIgnoreCase("type")) || ((allowShortName) && ((split[0].equalsIgnoreCase("s")) || (split[0].equalsIgnoreCase("t")))))
      {
        FireworkEffect.Type finalEffect = null;
        split[1] = (split[1].equalsIgnoreCase("large") ? "BALL_LARGE" : split[1]);
        if (fireworkShape.containsKey(split[1].toUpperCase()))
        {
          finalEffect = (FireworkEffect.Type)fireworkShape.get(split[1].toUpperCase());
        }
        else
        {
          throw new Exception("");
        }
        if (finalEffect != null)
        {
          this.builder.with(finalEffect);
        }
      }
      else if ((split[0].equalsIgnoreCase("fade")) || ((allowShortName) && (split[0].equalsIgnoreCase("f"))))
      {
        List fadeColors = new ArrayList();
        String[] colors = split[1].split(",");
        for (String color : colors)
        {
          if (colorMap.containsKey(color.toUpperCase()))
          {
            fadeColors.add(((DyeColor)colorMap.get(color.toUpperCase())).getFireworkColor());
          }
          else
          {
        	  throw new Exception("");
          }
        }
        if (!fadeColors.isEmpty())
        {
          this.builder.withFade(fadeColors);
        }
      }
      else if ((split[0].equalsIgnoreCase("effect")) || ((allowShortName) && (split[0].equalsIgnoreCase("e"))))
      {
        String[] effects = split[1].split(",");
        for (String effect : effects)
        {
          if (effect.equalsIgnoreCase("twinkle"))
          {
            this.builder.flicker(true);
          }
          else if (effect.equalsIgnoreCase("trail"))
          {
            this.builder.trail(true);
          }
          else
          {
        	  throw new Exception("");
          }
        }
      }
    }
  }

  public void addPotionMeta(CommandSender sender, boolean allowShortName, String string) throws Exception
  {
    if (this.stack.getType() == Material.POTION)
    {
      String[] split = this.splitPattern.split(string, 2);

      if (split.length < 2)
      {
        return;
      }

      if ((split[0].equalsIgnoreCase("effect")) || ((allowShortName) && (split[0].equalsIgnoreCase("e"))))
      {
        this.pEffectType = MapPotionEffects.getByName(split[1]);
        if ((this.pEffectType != null) && (this.pEffectType.getName() != null))
        {
           this.validPotionEffect = true;
        }
        else
        {
        	throw new Exception("");
        }
      }
      else if ((split[0].equalsIgnoreCase("power")) || ((allowShortName) && (split[0].equalsIgnoreCase("p"))))
      {
        if (r.isNumber(split[1]))
        {
          this.validPotionPower = true;
          this.power = Integer.parseInt(split[1]);
          if ((this.power > 0) && (this.power < 4))
          {
            this.power -= 1;
          }
        }
        else
        {
        	throw new Exception("");
        }
      }
      else if ((split[0].equalsIgnoreCase("duration")) || ((allowShortName) && (split[0].equalsIgnoreCase("d"))))
      {
        if (r.isNumber(split[1]))
        {
          this.validPotionDuration = true;
          this.duration = (Integer.parseInt(split[1]) * 20);
        }
        else
        {
        	throw new Exception("");
        }
      }

      if (isValidPotion())
      {
        PotionMeta pmeta = (PotionMeta)this.stack.getItemMeta();
        this.pEffect = this.pEffectType.createEffect(this.duration, this.power);
        pmeta.addCustomEffect(this.pEffect, true);
        this.stack.setItemMeta(pmeta);
        resetPotionMeta();
      }
    }
  }

  private void parseEnchantmentStrings(CommandSender sender, boolean allowUnsafe, String[] split) throws Exception
  {
    Enchantment enchantment = MapEnchantments.getByName(split[0]);
    if ((enchantment == null))
    {
      return;
    }

    int level = -1;
    if (split.length > 1)
    {
      try
      {
        level = Integer.parseInt(split[1]);
      }
      catch (NumberFormatException ex)
      {
        level = -1;
      }
    }

    if ((level < 0) || ((!allowUnsafe) && (level > enchantment.getMaxLevel())))
    {
      level = enchantment.getMaxLevel();
    }
    addEnchantment(sender, allowUnsafe, enchantment, level);
  }

  public void addEnchantment(CommandSender sender, boolean allowUnsafe, Enchantment enchantment, int level)
  {
    if (enchantment == null)
    {
    	return;
    }
    try
    {
      if (this.stack.getType().equals(Material.ENCHANTED_BOOK))
      {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta)this.stack.getItemMeta();
        if (level == 0)
        {
          meta.removeStoredEnchant(enchantment);
        }
        else
        {
          meta.addStoredEnchant(enchantment, level, allowUnsafe);
        }
        this.stack.setItemMeta(meta);
      }
      else if (level == 0)
      {
        this.stack.removeEnchantment(enchantment);
      }
      else if (allowUnsafe)
      {
        this.stack.addUnsafeEnchantment(enchantment, level);
      }
      else
      {
        this.stack.addEnchantment(enchantment, level);
      }

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public Enchantment getEnchantment(Player user, String name) throws Exception
  {
    Enchantment enchantment = MapEnchantments.getByName(name);
    return enchantment;
  }
}