package flansapi.handlers;

import java.util.ArrayList;
import java.util.List;

import com.flansmod.common.guns.GunType;
import com.flansmod.common.guns.ItemBullet;
import com.flansmod.common.guns.ItemGrenade;
import com.flansmod.common.guns.ItemGun;
import com.flansmod.common.guns.ShootableType;
import com.flansmod.common.paintjob.Paintjob;

import flansapi.main.Main;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHandler {
	
	public int getMaxPaintJobs(int gunID) {
		if(!isGun(gunID)) return 0;
		return ((ItemGun)Item.getItemById(gunID)).type.paintjobs.size();
	}
	
	public void changePaintjob(String playerName, int slotOfGun, int paintJobID) {
		EntityPlayer p = Main.entityPlayerFromName(playerName);
		if(p == null || slotOfGun >= p.inventory.getSizeInventory() || slotOfGun < 0) return;
		ItemStack stack = p.inventory.getStackInSlot(slotOfGun);
		if(stack == null) return;
		Item item = stack.getItem();
		
		if(item instanceof ItemGun) {
			ItemGun gun = (ItemGun) item;			
			Paintjob pj = gun.type.getPaintjob(paintJobID);				
			stack.setItemDamage(pj.ID);
		}
	}
	
	public boolean isAmmo(int itemID) {
		Item item = Item.getItemById(itemID);  		
		return (item != null && item instanceof ItemBullet);
	}
	
	public boolean isGun(int itemID) {
		Item item = Item.getItemById(itemID);  		
		return (item != null && item instanceof ItemGun);
	}
	
	public Integer[] getAllAmmoForGun(int itemID) {
		if(!isGun(itemID)) return null;
		List<ShootableType> allAmmo = (((ItemGun)Item.getItemById(itemID)).type).ammo;
		
		List<Integer> ammoIDs = new ArrayList<Integer>();
		for(int i = 0; i < allAmmo.size(); i++) 
			ammoIDs.add(Item.getIdFromItem(allAmmo.get(i).getItem()));
		
		return ammoIDs.toArray(new Integer[0]);
	}
	
	public int getMaxAmmoItems(int itemID) {
		if(!isGun(itemID)) return 0;
		return (((ItemGun)Item.getItemById(itemID)).type).getNumAmmoItemsInGun(new ItemStack(((ItemGun)Item.getItemById(itemID))));
	}
	
	public int getMaxRounds(int itemID) {
		if(!isAmmo(itemID)) return 0;
		return (((ItemBullet)Item.getItemById(itemID)).type).roundsPerItem;
	}
	
	public void setAmmoAmount(String playerName, int slotOfGun, int rounds, int maxSeperateMagazines, int ammoID) {
		EntityPlayer p = Main.entityPlayerFromName(playerName);
		if(p == null || slotOfGun >= p.inventory.getSizeInventory() || slotOfGun < 0) return;
		ItemStack stack = p.inventory.getStackInSlot(slotOfGun);
		if(stack == null) return;
		Item item = stack.getItem();
		
		if(item instanceof ItemGun) {
			ItemGun gun = (ItemGun) item;
			GunType type = gun.type;
			List<ShootableType> possibleBullets = type.ammo;
			if(possibleBullets.size() == 0) return;
			
			ItemStack ammo = new ItemStack(possibleBullets.get(0).getItem());
			if(ammoID > -1) {
				for(ShootableType st : possibleBullets) {
					if(Item.getIdFromItem(st.getItem()) == ammoID) {
						ammo = new ItemStack(st.getItem());
						break;
					}
				}
			} 
			
			ShootableType bType = null;
			if(ammo.getItem() instanceof ItemBullet)
				bType = ((ItemBullet)ammo.getItem()).type;
			else if(ammo.getItem() instanceof ItemGrenade)
				bType = ((ItemGrenade)ammo.getItem()).type;
			else
				return;

			if(rounds > 0 && bType.roundsPerItem >= rounds) ammo.setItemDamage((bType.roundsPerItem-rounds));
			
			for(int i = 0; i < type.getNumAmmoItemsInGun(stack); i++) {
				if(i == maxSeperateMagazines) 
					ammo.setItemDamage(bType.roundsPerItem);				
				gun.setBulletItemStack(stack, ammo, i);
			}
		}
	}
	
}
