package Bammerbom.UltimateCore.Resources.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class InventoryUtil implements Listener{
	static Plugin plugin;
	public InventoryUtil(Plugin instance){
		plugin = instance;
	}
	
	//Equiped Armor
	private static HashMap<String, ItemStack[]> pArmor = new HashMap<String, ItemStack[]>();
	//Inventory
	private static HashMap<String, ItemStack[]> pItems = new HashMap<String, ItemStack[]>();

	public static void restore(Player p){
		InventoryUtil.clearInventory(p);
		p.getInventory().setArmorContents(pArmor.get(p.getName()));
		p.getInventory().setContents(pItems.get(p.getName()));
		pArmor.remove(p.getName());
		pArmor.remove(p.getName());
	}

	public static void saveInv(Player p, Boolean override){
		if(pArmor.containsKey(p.getName()) && !override){
			restore(p);
		}
		pArmor.put(p.getName(), p.getInventory().getArmorContents());
		pItems.put(p.getName(), p.getInventory().getContents());
		InventoryUtil.clearInventory(p);
	}
	
	@EventHandler
	public void quitRestore(PlayerQuitEvent e){
		restore(e.getPlayer());
	}
	
	@EventHandler
	public void kickRestore(PlayerKickEvent e){
		restore(e.getPlayer());
	}
	
	public static void addItem(Inventory inv, ItemStack item){
		Integer amount = item.getAmount();
		while(item.getAmount() >= 64 && !isFullInventory(inv)){
			ItemStack item2 = item.clone();
			item2.setAmount(64);
			inv.addItem(item2);
			item.setAmount(item.getAmount() - 64);
			amount = amount - 64;
		}
		if(!isFullInventory(inv)){
			if(amount == 0) return;
			inv.addItem(item);
		}
		
	}
    public static void clearInventory(final Player p)
    {
    	p.setItemInHand(new ItemStack(Material.AIR));
      PlayerInventory inv = p.getInventory();
      inv.clear();
      inv.setHelmet(null);
      inv.setChestplate(null);
      inv.setLeggings(null);
      inv.setBoots(null);
      InventoryView view = p.getOpenInventory();
      if (view != null) {
        view.setCursor(null);
        Inventory i = view.getTopInventory();
        if (i != null)
          i.clear();
      }
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				p.updateInventory();
				
			}
		}, 1L);
    }
	public static boolean isFullInventory(Inventory inv)
    {
      ItemStack[] inventory = inv.getContents();

      for (ItemStack stack : inventory) {
        if (stack == null || stack.getType().equals(Material.AIR)) {
          return false;
        }
      }
      return true;
    }
    @SuppressWarnings("deprecation")
	public static boolean isEmptyInventory(PlayerInventory inv)
    {
      ItemStack[] inventory = inv.getContents();
      ItemStack[] armor = inv.getArmorContents();

      for (ItemStack stack : inventory) {
        if (stack != null) {
          return false;
        }
      }

      for (ItemStack stack : armor) {
        if (stack.getTypeId() != 0) {
          return false;
        }
      }
      return true;
    }
    @SuppressWarnings("deprecation")
	public static String InventoryToString (Inventory invInventory)
    {
        String serialization = invInventory.getSize() + ";";
        for (int i = 0; i < invInventory.getSize(); i++)
        {
            ItemStack is = invInventory.getItem(i);
            if (is != null)
            {
                String serializedItemStack = new String();
               
                String isType = String.valueOf(is.getType().getId());
                serializedItemStack += "t@" + isType;
               
                if (is.getDurability() != 0)
                {
                    String isDurability = String.valueOf(is.getDurability());
                    serializedItemStack += ":d@" + isDurability;
                }
               
                if (is.getAmount() != 1)
                {
                    String isAmount = String.valueOf(is.getAmount());
                    serializedItemStack += ":a@" + isAmount;
                }
               
                Map<Enchantment,Integer> isEnch = is.getEnchantments();
                if (isEnch.size() > 0)
                {
                    for (Entry<Enchantment,Integer> ench : isEnch.entrySet())
                    {
                        serializedItemStack += ":e@" + ench.getKey().getId() + "@" + ench.getValue();
                    }
                }
               
                serialization += i + "#" + serializedItemStack + ";";
            }
        }
        return serialization;
    }
   
    @SuppressWarnings("deprecation")
	public static Inventory StringToInventory (String invString)
    {
        String[] serializedBlocks = invString.split(";");
        String invInfo = serializedBlocks[0];
        Inventory deserializedInventory = Bukkit.getServer().createInventory(null, Integer.valueOf(invInfo));
       
        for (int i = 1; i < serializedBlocks.length; i++)
        {
            String[] serializedBlock = serializedBlocks[i].split("#");
            int stackPosition = Integer.valueOf(serializedBlock[0]);
           
            if (stackPosition >= deserializedInventory.getSize())
            {
                continue;
            }
           
            ItemStack is = null;
            Boolean createdItemStack = false;
           
            String[] serializedItemStack = serializedBlock[1].split(":");
            for (String itemInfo : serializedItemStack)
            {
                String[] itemAttribute = itemInfo.split("@");
                if (itemAttribute[0].equals("t"))
                {
                    is = new ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])));
                    createdItemStack = true;
                }
                else if (itemAttribute[0].equals("d") && createdItemStack)
                {
                    is.setDurability(Short.valueOf(itemAttribute[1]));
                }
                else if (itemAttribute[0].equals("a") && createdItemStack)
                {
                    is.setAmount(Integer.valueOf(itemAttribute[1]));
                }
                else if (itemAttribute[0].equals("e") && createdItemStack)
                {
                    is.addEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
                }
            }
            deserializedInventory.setItem(stackPosition, is);
        }
       
        return deserializedInventory;
    }
}
