package Bammerbom.UltimateCore.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.MobData;
import Bammerbom.UltimateCore.Resources.MobType;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class CmdSpawnmob{
	static Plugin plugin;
	public CmdSpawnmob(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.default1 + "/spawnmob " + r.default2 + "<Mob>[,Mob][,Mob].. [Amount] [Specials]");
			sender.sendMessage(r.default1 + "Specials currently unavailable while using riding mobs");
			sender.sendMessage(r.default1 + "Typ: " + r.default2 + "/spawnmob list " + r.default1 + "for a list of mobtypes");
			sender.sendMessage(r.default1 + "Typ: " + r.default2 + "/spawnmob data " + r.default1 + "for a list of datatypes");
			return;
		}
		if(!r.isPlayer(sender)){
			return;
		}
		Player p = (Player) sender;
		if(!r.perm(sender, "uc.spawnmob", false, true)){
			return;
		}
		if(args[0].equalsIgnoreCase("list")){
			String message1 = "";
			String message2 = "";
			String message3 = "";
			for(MobType mob : MobType.values()){
				if(mob.type.equals(MobType.Enemies.ENEMY)){
					message3 = message3 + ", " + mob.name;
				}
				if(mob.type.equals(MobType.Enemies.NEUTRAL)){
					message2 = message2 + ", " + mob.name;
				}
				if(mob.type.equals(MobType.Enemies.FRIENDLY)){
					message1 = message1 + ", " + mob.name;
				}
			}
			message1 = message1.replaceFirst(", ", "");
			message2 = message2.replaceFirst(", ", "");
			message3 = message3.replaceFirst(", ", "");
			sender.sendMessage(r.default1 + "Friendly: " + r.default2 + message1);
			sender.sendMessage(r.default1 + "Neutral: " + r.default2 + message2);
			sender.sendMessage(r.default1 + "Enemy: " + r.default2 + message3);
			return;
		}
		if(args[0].equalsIgnoreCase("data")){
			for(MobType mob : MobType.values()){
				String mes = r.default1 + StringUtil.firstUpperCase(mob.getType().name().toLowerCase()) + ": " + r.default2;
			    StringBuilder b = new StringBuilder(mes);
			    Boolean a = false;
				for(String str : MobData.getValidHelp(mob.getType())){
					if(a) b.append(", ");
			    	 b.append(str);
			    	 a = true;
			     }
				if(a){
				sender.sendMessage(b.toString());
				}
			}
			StringBuilder b = new StringBuilder(r.default1 + "Baby: " + r.default2 + "Chicken, Cow, Horse, Pig, Sheep, Wolf, Villager, Mushroomcow, Ocelot, Zombie");
			sender.sendMessage(b.toString());
			return;
		}
		Location loc = p.getLocation();
		MobType mob = MobType.fromName(args[0]);
		Integer amount = 1;
		
		ArrayList<MobType> smob = new ArrayList<MobType>();
		if(r.checkArgs(args, 1) == true){
			if(r.isNumber(args[1])){
				amount = Integer.parseInt(args[1]);
			}
		}
		if(mob == null || mob.name == null || mob.name.equals("") || mob.getType() == null){
			if(!args[0].contains(",")){
				p.sendMessage(r.mes("Spawnmob.MobNotFound").replaceAll("%Mob", args[0]));
				return;
			}
			//Stacked
			//ArrayList<Mob> smob2 = new ArrayList<Mob>();
			ArrayList<SpawnKit> kits = new ArrayList<SpawnKit>();
			for(String string : args[0].split(",")){
				MobType mo1 = MobType.fromName(string);
				if(mo1 == null || mo1.name == null || mo1.name.equals("") || mo1.getType() == null){
					mo1 = MobType.fromName(string.split(":")[0]);
					if(mo1 == null || mo1.name == null || mo1.name.equals("") || mo1.getType() == null){
				        p.sendMessage(r.mes("Spawnmob.MobNotFound").replaceAll("%Mob", string));
					    return;
					}else{
						kits.add(new SpawnKit(mo1, string.split(":")[1]));
					}
				}else{
					kits.add(new SpawnKit(mo1, ""));
				}
			}
			smob.clear(); //Remove memory
			for (int i = 0; i < amount; i++){
			LivingEntity lastmob = null;
			for(SpawnKit kit : kits){
				EntityType type = kit.a().getType();
				LivingEntity en = (LivingEntity) loc.getWorld().spawnEntity(loc, type);
				if(kit.a().name().equals("witherskeleton")){
					Skeleton skel = (Skeleton) en;
					skel.setSkeletonType(SkeletonType.WITHER);
		    		 EntityEquipment invent = ((LivingEntity)skel).getEquipment();
			   	      invent.setItemInHand(new ItemStack(Material.STONE_SWORD, 1));
			   	      invent.setItemInHandDropChance(0.09F);
				}else if(kit.a().name().equalsIgnoreCase("skeleton")){
					Skeleton skel = (Skeleton) en;
					skel.setSkeletonType(SkeletonType.NORMAL);
					skel.getEquipment().setItemInHand(new ItemStack(Material.BOW));
					skel.getEquipment().setItemInHandDropChance(0.09F);
				}
				if(kit.a().name().equalsIgnoreCase("elderguardian")){
					Guardian g = (Guardian) en;
					g.setElder(true);
				}
				defaultMobData(type, en);
				//TODO Utilize(kit.b, mob, en, p);
				if(lastmob != null){
					lastmob.setPassenger(en);
				}
				lastmob = en;
			}
			}
			//End stacked
			return;
		}
		//Unstacked
		for (int i = 0; i < amount; i++){
			try{
			LivingEntity en = (LivingEntity) loc.getWorld().spawnEntity(loc, mob.getType());
			if(args[0].equals("witherskeleton")){
				Skeleton skel = (Skeleton) en;
				skel.setSkeletonType(SkeletonType.WITHER);
	    		 EntityEquipment invent = ((LivingEntity)skel).getEquipment();
		   	      invent.setItemInHand(new ItemStack(Material.STONE_SWORD, 1));
		   	      invent.setItemInHandDropChance(0.09F);
			}else{
				if(args[0].equalsIgnoreCase("skeleton")){
					Skeleton skel = (Skeleton) en;
					skel.setSkeletonType(SkeletonType.NORMAL);
					skel.getEquipment().setItemInHand(new ItemStack(Material.BOW));
					skel.getEquipment().setItemInHandDropChance(0.09F);
				}
			}
			defaultMobData(mob.getType(), en);
			Utilize(args, mob, en, p);
			}catch(ClassCastException ex){
				ex.printStackTrace();
			}
		}
		
	
	
	
	
	
	}
	static void horse(EntityType type, Entity spawned, String str){
		if(!type.equals(EntityType.HORSE)) return;
		Horse horse = (Horse) spawned;
		if(str.equalsIgnoreCase("donkey")){
			horse.setVariant(Variant.DONKEY);
		}else if(str.equalsIgnoreCase("mule")){
			horse.setVariant(Variant.MULE);
		}else if(str.equalsIgnoreCase("skeleton")){
			horse.setVariant(Variant.SKELETON_HORSE);
		}else if(str.equalsIgnoreCase("undead") || str.equalsIgnoreCase("zombie")){
			horse.setVariant(Variant.UNDEAD_HORSE);
		}else if(isHorseColor(str)){
			horse.setColor(getHorseColor(str));
		}else if(isHorseStyle(str)){
			horse.setStyle(getHorseStyle(str));
		}else if(str.equalsIgnoreCase("saddled")){
			horse.setTamed(true);
			horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
		}else if(str.equalsIgnoreCase("diamond") || str.equalsIgnoreCase("diamondarmor")){
			horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
		}else if(str.equalsIgnoreCase("iron") || str.equalsIgnoreCase("ironarmor")){
			horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
		}else if(str.equalsIgnoreCase("gold") || str.equalsIgnoreCase("goldarmor")){
			horse.getInventory().setArmor(new ItemStack(Material.GOLD_BARDING));
		}
		
		
		
		/*Color
		 * BLACK
		 * BROWN
		 * CHESTNUT
		 * CREAMY
		 * DARKBROWN
		 * GRAY
		 * WHITE
		 */
		/*Style
		 * BLACKDOTS
		 * NONE
		 * WHITE_LEGS
		 * WHITE_DOTS
		 * WHITEFIELD
		 */
	}
	@SuppressWarnings("unused")
	private static class SpawnKit{
		public MobType a;
		public String b;
		public SpawnKit(MobType a, String b){
			this.a = a;
			this.b = b;
		}
		public MobType a(){
			return a;
		}
		public void a(MobType a2){
			a = a2;
		}
		public String b(){
			return b;
		}
		public void b(String b2){
			b = b2;
		}
	}
	static void Utilize(String[] args, MobType mob, LivingEntity en, Player p){
		Utilize((r.getFinalArg(args, 1)), mob, en, p);
	}
	static void Utilize(String args, MobType mob, LivingEntity en, Player p){
		//TODO
		for(String str : args.split("[: ]")){
			MobData d = MobData.fromData(en, str);
			if(d != null){
				try {
					d.setData(en, p, str);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		
		/*
		for(String str : args.split("[: ]")){
			horse(mob.getType(), en, str);
			if(str.equalsIgnoreCase("baby")){
				if(!(en instanceof Ageable) && !(en instanceof Zombie)){
					p.sendMessage(r.mes("Spawnmob.PropertyNotCompitable").replaceAll("%Property", str).replaceAll("%Type", mob.getType().name().toLowerCase().replaceAll("_", "")));
					en.remove();
					return;
				}else if(en instanceof Ageable){
					Ageable en1 = (Ageable) en;
					en1.setBaby();
					en1.setAgeLock(true);
				}else if(en instanceof Zombie){
					Zombie en1 = (Zombie) en;
					en1.setBaby(true);
				}
			}else if(r.isNumber(str)){
			}else if(getDyeColor(str) != null){
				if(mob.getType().equals(EntityType.WOLF)){
					Wolf wolf = (Wolf) en;
					wolf.setCollarColor(getDyeColor(str));
				}
				else if(!(mob.getType().equals(EntityType.SHEEP))){
					if(!isHorseColor(str)){
					p.sendMessage(r.mes("Spawnmob.PropertyNotCompitable").replaceAll("%Property", str).replaceAll("%Type", mob.getType().name().toLowerCase().replaceAll("_", "")));
					en.remove();
					return;
					}
				}else{
					Sheep sheep = (Sheep) en;
					sheep.setColor(getDyeColor(str));
				}
			}else if(str.equalsIgnoreCase("sheared")){
				if(!(mob.getType().equals(EntityType.SHEEP))){
					p.sendMessage(r.mes("Spawnmob.PropertyNotCompitable").replaceAll("%Property", str).replaceAll("%Type", mob.getType().name().toLowerCase().replaceAll("_", "")));
					en.remove();
					return;
				}else{
					Sheep sheep = (Sheep) en;
					sheep.setSheared(true);
				}
			}else if(str.equalsIgnoreCase("diamondarmor") || str.equalsIgnoreCase("diamond")){
				if(mob.getType().equals(EntityType.ZOMBIE) || mob.getType().equals(EntityType.SKELETON)){
					EntityEquipment eq = en.getEquipment();
					eq.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
					eq.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
					eq.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
					eq.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
					eq.setBootsDropChance(0F);
					eq.setLeggingsDropChance(0F);
					eq.setChestplateDropChance(0F);
					eq.setHelmetDropChance(0F);
				}else{
					if(!mob.getType().equals(EntityType.HORSE)){
					p.sendMessage(r.mes("Spawnmob.PropertyNotCompitable").replaceAll("%Property", str).replaceAll("%Type", mob.getType().name().toLowerCase().replaceAll("_", "")));
					en.remove();
					return;
					}
				}
			}else if(str.equalsIgnoreCase("ironarmor") || str.equalsIgnoreCase("iron")){
				if(mob.getType().equals(EntityType.ZOMBIE) || mob.getType().equals(EntityType.SKELETON)){
					EntityEquipment eq = en.getEquipment();
					eq.setHelmet(new ItemStack(Material.IRON_HELMET));
					eq.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
					eq.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
					eq.setBoots(new ItemStack(Material.IRON_BOOTS));
					eq.setBootsDropChance(0F);
					eq.setLeggingsDropChance(0F);
					eq.setChestplateDropChance(0F);
					eq.setHelmetDropChance(0F);
				}else{
					if(!mob.getType().equals(EntityType.HORSE)){
					p.sendMessage(r.mes("Spawnmob.PropertyNotCompitable").replaceAll("%Property", str).replaceAll("%Type", mob.getType().name().toLowerCase().replaceAll("_", "")));
					en.remove();
					return;
					}
				}
			}else if(str.equalsIgnoreCase("goldarmor") || str.equalsIgnoreCase("gold")){
				if(mob.getType().equals(EntityType.ZOMBIE) || mob.getType().equals(EntityType.SKELETON)){
					EntityEquipment eq = en.getEquipment();
					eq.setHelmet(new ItemStack(Material.GOLD_HELMET));
					eq.setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
					eq.setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
					eq.setBoots(new ItemStack(Material.GOLD_BOOTS));
					eq.setBootsDropChance(0F);
					eq.setLeggingsDropChance(0F);
					eq.setChestplateDropChance(0F);
					eq.setHelmetDropChance(0F);
				}else{
					if(!mob.getType().equals(EntityType.HORSE)){
					p.sendMessage(r.mes("Spawnmob.PropertyNotCompitable").replaceAll("%Property", str).replaceAll("%Type", mob.getType().name().toLowerCase().replaceAll("_", "")));
					en.remove();
					return;
					}
				}
			}else if(str.equalsIgnoreCase("chainarmor") || str.equalsIgnoreCase("chain")){
				if(mob.getType().equals(EntityType.ZOMBIE) || mob.getType().equals(EntityType.SKELETON)){
					EntityEquipment eq = en.getEquipment();
					eq.setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
					eq.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
					eq.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
					eq.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
					eq.setBootsDropChance(0F);
					eq.setLeggingsDropChance(0F);
					eq.setChestplateDropChance(0F);
					eq.setHelmetDropChance(0F);
				}else{
					p.sendMessage(r.mes("Spawnmob.PropertyNotCompitable").replaceAll("%Property", str).replaceAll("%Type", mob.getType().name().toLowerCase().replaceAll("_", "")));
					en.remove();
					return;
				}
			}else if(str.equalsIgnoreCase("leatherarmor") || str.equalsIgnoreCase("leather")){
				if(mob.getType().equals(EntityType.ZOMBIE) || mob.getType().equals(EntityType.SKELETON)){
					EntityEquipment eq = en.getEquipment();
					eq.setHelmet(new ItemStack(Material.LEATHER_HELMET));
					eq.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
					eq.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
					eq.setBoots(new ItemStack(Material.LEATHER_BOOTS));
					eq.setBootsDropChance(0F);
					eq.setLeggingsDropChance(0F);
					eq.setChestplateDropChance(0F);
					eq.setHelmetDropChance(0F);
				}else{
					p.sendMessage(r.mes("Spawnmob.PropertyNotCompitable").replaceAll("%Property", str).replaceAll("%Type", mob.getType().name().toLowerCase().replaceAll("_", "")));
					en.remove();
					return;
				}
			}else if(str.equalsIgnoreCase("saddled")){
				if(en.getType().equals(EntityType.PIG)){
					Pig pig = (Pig) en;
					pig.setSaddle(true);
				}else{
					if(!mob.getType().equals(EntityType.HORSE)){
						p.sendMessage(r.mes("Spawnmob.PropertyNotCompitable").replaceAll("%Property", str).replaceAll("%Type", mob.getType().name().toLowerCase().replaceAll("_", "")));
						en.remove();
						return;
						}
				}
			}else if(str.equalsIgnoreCase("tamed")){
				if(en instanceof Tameable){
					Tameable tame = (Tameable) en;
					tame.setTamed(true);
					tame.setOwner(p);
				}else{
					p.sendMessage(r.mes("Spawnmob.PropertyNotCompitable").replaceAll("%Property", str).replaceAll("%Type", mob.getType().name().toLowerCase().replaceAll("_", "")));
					en.remove();
					return;
				}
			}else if(str.startsWith("size:")){
				if(en instanceof Slime || en instanceof MagmaCube){
					String sizes = str.replaceAll("size:", "");
					if(!r.isNumber(sizes)){
						en.remove();
						p.sendMessage(r.default1 + "size:[1-50]");
						return; 
					}
					Integer size = Integer.parseInt(sizes);
					if(size > 50) size = 50;
					if(size < 1) size = 1;
					if(en instanceof Slime){
						Slime slime = (Slime) en;
						slime.setSize(size);
					}else{
						MagmaCube cube = (MagmaCube) en;
						cube.setSize(size);
					}
					
				}else{
					p.sendMessage(r.mes("Spawnmob.PropertyNotCompitable").replaceAll("%Property", str).replaceAll("%Type", mob.getType().name().toLowerCase().replaceAll("_", "")));
					en.remove();
					return;
				}
			}else if(str.equalsIgnoreCase("charged") || str.equalsIgnoreCase("powered")){
				if(en instanceof Creeper){
					Creeper creeper = (Creeper) en;
					creeper.setPowered(true);
				}else{
					p.sendMessage(r.mes("Spawnmob.PropertyNotCompitable").replaceAll("%Property", str).replaceAll("%Type", mob.getType().name().toLowerCase().replaceAll("_", "")));
					en.remove();
					return;
				}
			}
			
		}
		*/
	}
	static boolean isHorseColor(String str){
		for(Color color : Color.values()){
			String colors = color.name().toLowerCase().replaceAll("_", "");
			String inputs = str.toLowerCase().replaceAll("_", "");
			if(colors.equals(inputs)){
				return true;
			}
		}
		return false;
	}
	static Color getHorseColor(String str){
		for(Color color : Color.values()){
			String colors = color.name().toLowerCase().replaceAll("_", "");
			String inputs = str.toLowerCase().replaceAll("_", "");
			if(colors.equals(inputs)){
				return color;
			}
		}
		return null;
	}
	static boolean isHorseStyle(String str){
		if(str.equalsIgnoreCase("whitelegs")){
			return true;
		}
		for(Style style : Style.values()){
			String styles = style.name().toLowerCase().replaceAll("white_", "").replaceAll("_", "");
			String inputs = str.toLowerCase().replaceAll("white_", "").replaceAll("_", "").replaceAll("whitelegs", "white");;
			if(styles.equals(inputs)){
				return true;
			}
		}
		return false;
	}
	static Style getHorseStyle(String str){
		if(str.equalsIgnoreCase("whitelegs")){
			return Style.WHITE;
		}
		for(Style style : Style.values()){
			String styles = style.name().toLowerCase().replaceAll("white_", "").replaceAll("_", "");
			String inputs = str.toLowerCase().replaceAll("white_", "").replaceAll("_", "").replaceAll("whitelegs", "white");
			if(styles.equals(inputs)){
				return style;
			}
		}
		return null;
	}
	  static void defaultMobData(EntityType type, Entity spawned)
	  {
	    if (type == EntityType.SKELETON)
	    {
	    	return;
	    }

	    if (type == EntityType.PIG_ZOMBIE)
	    {
	      EntityEquipment invent = ((LivingEntity)spawned).getEquipment();
	      invent.setItemInHand(new ItemStack(Material.GOLD_SWORD, 1));
	      invent.setItemInHandDropChance(0.05F);
	    }

	    if (type == EntityType.HORSE)
	    {
	      ((Horse)spawned).setJumpStrength(1.3D);
	    }
	  }
	  static DyeColor getDyeColor(String str){
		  for(DyeColor dye : DyeColor.values()){
			  if(dye.name().toLowerCase().replaceAll("_", "").equalsIgnoreCase(str)){
				  return dye;
			  }
		  }
		  return null;
	  }
}