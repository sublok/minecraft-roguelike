package greymerk.roguelike.treasure.loot;

import greymerk.roguelike.config.RogueConfig;
import greymerk.roguelike.util.TextFormat;

import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.EnchantmentData;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.Entity;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.World;

public enum Loot {

	WEAPON, ARMOUR, BLOCK, JUNK, ORE, TOOL, POTION, FOOD;
	
	public static ItemStack getLootByCategory(Loot category, Random rand, int rank){
		return getLootByCategory(category, rand, rank, true);
	}
	
	public static ItemStack getLootByCategory(Loot category, Random rand, int rank, boolean enchant){
		
		switch(category){
		case WEAPON: return ItemWeapon.getRandom(rand, rank, enchant);
		case ARMOUR: return ItemArmour.getRandom(rand, rank, enchant);
		case BLOCK: return ItemBlock.getRandom(rand, rank);
		case JUNK: return ItemJunk.getRandom(rand, rank);
		case ORE: return ItemOre.getRandom(rand, rank);
		case TOOL: return ItemTool.getRandom(rand, rank, enchant);
		case POTION: return ItemPotion.getRandom(rand, rank);
		case FOOD: return ItemFood.getRandom(rand, rank);
		default: return null;		
		}
	}
	
	public static ItemStack getEquipmentBySlot(Random rand, Slot slot, int rank, boolean enchant){
		
		ItemStack item;
		
		if(slot == Slot.WEAPON){
			return ItemWeapon.getRandom(rand, rank, enchant);
		}
		
		return ItemArmour.getRandom(rand, rank, slot, enchant);
		
	}
	
	public static ItemStack getEnchantedBook(Random rand, int rank){
		
		ItemStack book = new ItemStack(Item.book);
		
		enchantItem(book, rand, getEnchantLevel(rank));
		
		return book;
	}
	
	public static ItemStack getSupplyItem(Random rand, int rank){

		if(rand.nextInt(200) == 0){
			ItemNovelty[] items = {
					ItemNovelty.GUUDE,
					ItemNovelty.BAJ,
					ItemNovelty.JOHNNYRAGGOT,
					ItemNovelty.FOURLES
			};
			
			return ItemNovelty.getItem(items[rand.nextInt(items.length)]);
		}
		
		if(rand.nextInt(10) == 0){
			return pickRecord(rand);
		}
		
		if(rand.nextInt(10) == 0){
			return ItemBlock.getRandom(rand, rank);
		}
		
		return pickSupplyItem(rand);
	}
	
	private static ItemStack pickRecord(Random rand){
		return new ItemStack(Item.record13.itemID + rand.nextInt(12), 1, 0);
	}

	private static ItemStack pickSupplyItem(Random rand) {

		switch(rand.nextInt(7)){
		
		case 0:
			switch(rand.nextInt(4)){
			case 0: return new ItemStack(Item.seeds, rand.nextInt(8) + 1);
			case 1: return new ItemStack(Item.pumpkinSeeds, rand.nextInt(8) + 1);
			case 2: return new ItemStack(Item.melonSeeds, rand.nextInt(8) + 1);
			case 3: return new ItemStack(Block.sapling);
			}			
		case 1:
			return new ItemStack(Item.wheat, rand.nextInt(8) + 1);
		case 2:
			// name tag
			return new ItemStack(421, 1, 0);
		case 3:
			return new ItemStack(Block.torchWood, 10 + rand.nextInt(10));
		case 4:
			if(rand.nextBoolean()){
				return new ItemStack(Item.paper, rand.nextInt(8) + 1);
			}
			return new ItemStack(Item.book, rand.nextInt(4) + 1);
		case 5:
			return new ItemStack(Item.saddle);
		case 6:
			// diamond horse armour
			if(rand.nextInt(10) == 0){
				return new ItemStack(419, 1, 0);
			}
			
			// gold horse armour
			if(rand.nextInt(5) == 0){
				return new ItemStack(418, 1, 0);
			}
			
			// iron horse armour
			return new ItemStack(417, 1, 0);
		default:
			return new ItemStack(Item.stick, 1);
		}
	}

	public static int getEnchantLevel(int rank) {

		switch(rank){
		case 3: return 30;
		case 2: return 20;
		case 1: return 10;
		case 0: return 1;
		default: return 1;
		}
	}

	public static void enchantItem(ItemStack item, Random rand, int enchantLevel) {
		
        List enchants = EnchantmentHelper.buildEnchantmentList(rand, item, enchantLevel);
        boolean canEnchant = enchants != null;
        boolean isABook = item.itemID == Item.book.itemID;

        if (!canEnchant){
        	return;
        }
        
        if (isABook){
            item.itemID = Item.enchantedBook.itemID;
        }

        int var6 = isABook ? rand.nextInt(enchants.size()) : -1;

        for (int i = 0; i < enchants.size(); ++i)
        {
            EnchantmentData enchantData = (EnchantmentData)enchants.get(i);

            if (!isABook || i == var6)
            {
                if (isABook)
                {
                    Item.enchantedBook.addEnchantment(item, enchantData);
                }
                else
                {
                    item.addEnchantment(enchantData.enchantmentobj, enchantData.enchantmentLevel);
                }
            }
        }
	}
	

	
    public static void setItemLore(ItemStack item, String loreText){
    	
        if (item.stackTagCompound == null)
        {
            item.stackTagCompound = new NBTTagCompound("tag");
        }

        if (!item.stackTagCompound.hasKey("display"))
        {
            item.stackTagCompound.setCompoundTag("display", new NBTTagCompound());
        }
        
        NBTTagCompound display = item.stackTagCompound.getCompoundTag("display");
        
        if (!(display.hasKey("Lore")))
        {
        	display.setTag("Lore", new NBTTagList());
        }
        
        NBTTagList lore = display.getTagList("Lore");
        
        NBTTagString toAdd = new NBTTagString("", loreText);
        
        lore.appendTag(toAdd);
        
        display.setTag("Lore", lore);   
    }
    
    public static void setItemLore(ItemStack item, String loreText, TextFormat option){
    	setItemLore(item, TextFormat.apply(loreText, option));
    }
    
    public static void setItemName(ItemStack item, String name, TextFormat option){
    	item.setItemName(TextFormat.apply(name, option));
    }
    
    public static void addEquipment(World world, int rank, Entity mob){
			
		Random rand = world.rand;
				
		int difficulty = world.difficultySetting;
		
		boolean enchant = difficulty == 3 ? true : false;
		
		ItemStack weapon;
		
		// zombie gets a sword
		if(mob instanceof EntityZombie){
			
			if(((EntityZombie)mob).isChild() && enchant && rand.nextInt(100) == 0){
				weapon = ItemNovelty.getItem(ItemNovelty.ASHLEA);
			} else if(rand.nextInt(5) == 0){
				weapon = ItemWeapon.getSword(rand, rank, enchant);
			} else {
				weapon = ItemTool.getRandom(rand, rank, enchant);
			}
			
			mob.setCurrentItemOrArmor(0, weapon);
		}
		
		// skelly gets a bow
		if(mob instanceof EntitySkeleton){
			
			if(rand.nextInt(10) == 0 && rank > 1){
				((EntitySkeleton) mob).setSkeletonType(1);
				mob.setCurrentItemOrArmor(0, ItemWeapon.getSword(rand, rank, enchant));
			} else {
				if(rand.nextInt(20) == 0){
					mob.setCurrentItemOrArmor(0, ItemWeapon.getSword(rand, rank, enchant));
				} else {
					mob.setCurrentItemOrArmor(0, ItemWeapon.getBow(rand, rank, enchant));
				}
			}
		}
		
		// put on some armour
		for(int i = 1; i < 5; i++){
			
			int chance = 5 - rank + ((3 - difficulty) * 2);
			if (difficulty == 3 || rank == 3 || rand.nextInt(chance) == 0){
				mob.setCurrentItemOrArmor(i, Loot.getEquipmentBySlot(rand, Slot.getSlotByNumber(i), rank, enchant));
			}
		}
    }

	
}
