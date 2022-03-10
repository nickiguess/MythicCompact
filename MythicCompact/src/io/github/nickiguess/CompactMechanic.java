package io.github.nickiguess;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;


public class CompactMechanic extends SkillMechanic implements ITargetedEntitySkill
{
    protected String compactedItem;
    protected String baseItem;
    protected int giveAmount;
    protected int removeAmount;
    
    public CompactMechanic(MythicLineConfig config) 
    {
        super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);

        this.baseItem = config.getString(new String[] {"baseItem", "bI"}, "");
        this.compactedItem = config.getString(new String[] {"compactedItem", "cI"}, "");
        this.giveAmount = config.getInteger(new String[] {"giveAmount", "gA"}, 1);
        this.removeAmount = config.getInteger(new String[] {"removeAmount", "rA"}, 64);
        
    }
    
    public void CompactMechanicCast(Player player, ItemStack[] materialList, ItemStack baseItemStack, ItemStack compactedItemStack) 
    {
		Material cM;
		
		{
			if (removeAmount < giveAmount) 
			{
				int isEmpty = player.getInventory().firstEmpty();
				for (int i = 0; i < 36; i++) 
				{
					if (isEmpty == -1) 
					{
						break;
					}
					if (materialList[i] == null) 
					{
						continue;
					}
					cM = materialList[i].getType();
					
					while (cM == baseItemStack.getType())
					{
						if (isEmpty == -1) 
						{
							break;
						}
						if (materialList[i].getAmount() >= removeAmount) 
						{
							player.getInventory().removeItem(baseItemStack);
							player.getInventory().addItem(compactedItemStack);
						} else 
						{
							break;
						}

						materialList = player.getInventory().getContents();
						
						if (materialList[i] == null) 
						{
							break;
						}

						cM = materialList[i].getType();
						isEmpty = player.getInventory().firstEmpty();
					}
					isEmpty = player.getInventory().firstEmpty();
				}
			} else 
			{
				for (int i = 0; i < 36; i++) 
				{
					if (materialList[i] == null) 
					{
						continue;
					}
					cM = materialList[i].getType();
					
					while (cM == baseItemStack.getType())
					{
						if (materialList[i].getAmount() >= removeAmount) 
						{
							player.getInventory().removeItem(baseItemStack);
							player.getInventory().addItem(compactedItemStack);
						} else 
						{
							break;
						}

						materialList = player.getInventory().getContents();
						
						if (materialList[i] == null) 
						{
							break;
						}

						cM = materialList[i].getType();
					}
				}
			}
		} 
    }
    
    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) 
    {
		
    	if (target.isPlayer() == true) 
    	{
    		Player player = (Player) target.getBukkitEntity();
    		
    		ItemStack baseItemStack = new ItemStack(MythicMobs.inst().getItemManager().getItemStack(baseItem));
			baseItemStack.setAmount(removeAmount);
    		ItemStack compactedItemStack = MythicMobs.inst().getItemManager().getItemStack(compactedItem);
			compactedItemStack.setAmount(giveAmount);

			ItemStack[] materialList = player.getInventory().getContents();

			if (player.getInventory().containsAtLeast(baseItemStack, removeAmount)) 
			{
				CompactMechanicCast(player, materialList, baseItemStack, compactedItemStack);
			}
    	}
		return false;
    }
}
