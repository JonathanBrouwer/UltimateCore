package Bammerbom.UltimateCore.Commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import Bammerbom.UltimateCore.r;

public class CmdMobTP implements Listener{
	static Plugin plugin;
	public CmdMobTP(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	Integer entitynumber = 0;
	public static HashMap<Integer, Entity> sticks = new HashMap<Integer, Entity>();
	public static void handle(CommandSender sender, String label, String[] args){
		if(!r.isPlayer(sender)) return;
		if(!r.perm(sender, "uc.mobtp", false, true)) return;
		ItemStack stick = new ItemStack(Material.STICK);
		ItemMeta meta = stick.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "MobTP: select an entity (right click)");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "" + ChatColor.UNDERLINE + "Find an entity to teleport, then right click to to pick it up.");
		lore.add(ChatColor.GRAY + "Then right click again to place the entity");
		meta.setLore(lore);
		stick.setItemMeta(meta);
		Player p = (Player) sender;
		p.getInventory().addItem(stick);
	}
	ArrayList<String> cantdrop = new ArrayList<String>();
	@EventHandler(priority = EventPriority.LOWEST)
	public void pickup(PlayerInteractEntityEvent e){
		if(cantdrop.contains(e.getPlayer().getName())) return;
		if(!(e.getRightClicked() instanceof LivingEntity)) return;
		if(e.getRightClicked() instanceof Player) return;
		if(!r.perm(e.getPlayer(), "uc.mobtp", false, false)) return;
		if(e.getPlayer().getItemInHand() == null || e.getPlayer().getItemInHand().getType() == null) return;
		if(e.getPlayer().getItemInHand().getItemMeta() == null || e.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
		if(!e.getPlayer().getItemInHand().getType().equals(Material.STICK)) return;
		if(!e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "MobTP: select an entity (right click)")) return;
		e.setCancelled(true);
		ItemStack stick = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = stick.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "MobTP: place entity (right click)");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Select an entity to teleport, then right click to to pick it up.");
		lore.add(ChatColor.GRAY + "" + ChatColor.UNDERLINE + "Then right click again to place the entity");
		lore.add(ChatColor.BLACK + "EntityNumber: " + entitynumber);
		Entity en = e.getRightClicked();
		sticks.put(entitynumber, en);
		meta.setLore(lore);
		stick.setItemMeta(meta);
		final Player p = e.getPlayer();
		p.setItemInHand(stick);
		entitynumber++;
		en.remove();
		cantdrop.add(e.getPlayer().getName());
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run() {
				cantdrop.remove(p.getName());
			}
		}, 20L);
	}
	@EventHandler
	public void place(PlayerInteractEvent e){
		if(cantdrop.contains(e.getPlayer().getName())) return;
		if(e.getPlayer().getItemInHand() == null || e.getPlayer().getItemInHand().getType() == null) return;
		if(e.getPlayer().getItemInHand().getItemMeta() == null || e.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
		if(!e.getPlayer().getItemInHand().getType().equals(Material.BLAZE_ROD)) return;
		if(!e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "MobTP: place entity (right click)")) return;
		if(!r.perm(e.getPlayer(), "uc.mobtp", false, false)) return;
		ItemStack old = e.getPlayer().getItemInHand();
		ItemStack stick = new ItemStack(Material.STICK);
		ItemMeta meta = stick.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "MobTP: select an entity (right click)");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "" + ChatColor.UNDERLINE + "Find an entity to teleport, then right click to to pick it up.");
		lore.add(ChatColor.GRAY + "Then right click again to place the entity");
		meta.setLore(lore);
		stick.setItemMeta(meta);
		Player p = e.getPlayer();
		p.getInventory().setItemInHand(stick);
		String id2 = old.getItemMeta().getLore().get(2);
		Integer id = Integer.parseInt(ChatColor.stripColor(id2.replaceAll("EntityNumber: ", "")));
		LivingEntity eo = (LivingEntity) sticks.get(id);
		if(eo == null) return;
		LivingEntity en = (LivingEntity) p.getLocation().getWorld().spawnEntity(p.getLocation(), eo.getType());
		en.setCanPickupItems(eo.getCanPickupItems());
		en.setCustomName(eo.getCustomName());
		en.setCustomNameVisible(eo.isCustomNameVisible());
		en.setFireTicks(eo.getFireTicks());
		en.setMaxHealth(((Damageable)eo).getMaxHealth());
		en.setHealth(((Damageable)eo).getHealth());
		en.setLastDamage(eo.getLastDamage());
		en.setLastDamageCause(eo.getLastDamageCause());
		en.setMaximumAir(eo.getMaximumAir());
		en.setMaximumNoDamageTicks(eo.getMaximumNoDamageTicks());
		en.setNoDamageTicks(eo.getNoDamageTicks());
		en.setPassenger(eo.getPassenger());
		en.setRemainingAir(eo.getRemainingAir());
		en.setRemoveWhenFarAway(eo.getRemoveWhenFarAway());
		en.setTicksLived(eo.getTicksLived());
		en.setVelocity(eo.getVelocity());
		en.getEquipment().setArmorContents(eo.getEquipment().getArmorContents());
		en.getEquipment().setItemInHandDropChance(eo.getEquipment().getItemInHandDropChance());
		for(PotionEffect ef : eo.getActivePotionEffects()){
			en.addPotionEffect(ef);
		}
		if(eo instanceof Ageable){
			Ageable eo2 = (Ageable) eo;
			Ageable en2 = (Ageable) en;
			en2.setAgeLock(eo2.getAgeLock());
			en2.setAge(eo2.getAge());
			en2.setBreed(eo2.canBreed());
			if(!eo2.isAdult()){
				en2.setBaby();
			}
		}
		if(eo instanceof Tameable){
			Tameable eo2 = (Tameable) eo;
			Tameable en2 = (Tameable) en;
			en2.setOwner(eo2.getOwner());
			en2.setTamed(eo2.isTamed());
		}
		if(eo instanceof Skeleton){
			Skeleton eo2 = (Skeleton) eo;
			Skeleton en2 = (Skeleton) en;
			en2.setSkeletonType(eo2.getSkeletonType());
		}
		if(eo instanceof Zombie){
			Zombie eo2 = (Zombie) eo;
			Zombie en2 = (Zombie) en;
			en2.setBaby(eo2.isBaby());
			en2.setVillager(eo2.isVillager());
		}
		if(eo instanceof Horse){
			Horse eo2 = (Horse) eo;
			Horse en2 = (Horse) en;
			en2.setColor(eo2.getColor());
			en2.setCarryingChest(eo2.isCarryingChest());
			en2.setDomestication(eo2.getDomestication());
			en2.setJumpStrength(eo2.getJumpStrength());
			en2.setMaxDomestication(eo2.getMaxDomestication());
			en2.setStyle(eo2.getStyle());
			en2.setVariant(eo2.getVariant());
			en2.getInventory().setSaddle(eo2.getInventory().getSaddle());
			en2.getInventory().setArmor(eo2.getInventory().getArmor());
		}
		if(eo instanceof Pig){
			Pig eo2 = (Pig) eo;
			Pig en2 = (Pig) en;
			en2.setSaddle(eo2.hasSaddle());
		}
		if(eo instanceof Sheep){
			Sheep eo2 = (Sheep) eo;
			Sheep en2 = (Sheep) en;
			en2.setColor(eo2.getColor());
			en2.setSheared(eo2.isSheared());
		}
		if(eo instanceof Wolf){
			Wolf eo2 = (Wolf) eo;
			Wolf en2 = (Wolf) en;
			en2.setCollarColor(eo2.getCollarColor());
		}
		if(eo instanceof Slime){
			Slime eo2 = (Slime) eo;
			Slime en2 = (Slime) en;
			en2.setSize(eo2.getSize());
		}
		if(eo instanceof MagmaCube){
			MagmaCube eo2 = (MagmaCube) eo;
			MagmaCube en2 = (MagmaCube) en;
			en2.setSize(eo2.getSize());
		}
		if(eo instanceof Creeper){
			Creeper eo2 = (Creeper) eo;
			Creeper en2 = (Creeper) en;
			en2.setPowered(eo2.isPowered());
		}
		if(eo instanceof Ocelot){
			Ocelot eo2 = (Ocelot) eo;
			Ocelot en2 = (Ocelot) en;
			en2.setCatType(eo2.getCatType());
		}
		if(eo instanceof Villager){
			Villager eo2 = (Villager) eo;
			Villager en2 = (Villager) en;
			en2.setProfession(eo2.getProfession());
		}
	}
}
