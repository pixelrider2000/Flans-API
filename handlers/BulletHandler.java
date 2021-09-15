package flansapi.handlers;

import java.util.ArrayList;
import java.util.List;

import com.flansmod.common.guns.EntityBullet;
import com.flansmod.common.guns.ItemBullet;
import com.flansmod.common.guns.ItemShootable;

import flansapi.main.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BulletHandler {
	
	public String getBulletOwner(String uuid) {
		Entity e = Main.entityFromUUID(uuid);
		if(e != null) {
			if(e instanceof EntityBullet) {
				EntityBullet bullet = (EntityBullet) e;
				if(bullet.owner instanceof EntityPlayer) {
					return ((EntityPlayer) bullet.owner).getDisplayName();
				}
			}
		}
		return null;
	}
	
	public void organizeAllAmmo(String playerName) {
		List<Integer> allAmmo = new ArrayList<Integer>();
		EntityPlayer p = Main.entityPlayerFromName(playerName);
		if(p == null) return;
		
		for(int i = 0; i < p.inventory.getSizeInventory(); i++) {
			if(p.inventory.getStackInSlot(i) == null || !(p.inventory.getStackInSlot(i).getItem() instanceof ItemBullet)) continue;
			int id = Item.getIdFromItem(p.inventory.getStackInSlot(i).getItem());
			if(!allAmmo.contains(id)) allAmmo.add(id);
		}
		
		for(int i = 0; i < allAmmo.size(); i++)
			organizeAmmo(playerName, allAmmo.get(i));		
	}
	
	public void organizeAmmo(String playerName, int ammoID) {
		EntityPlayer p = Main.entityPlayerFromName(playerName);
		if(p == null) return;
		
		int allRounds = 0, maxRounds = 0;
		ItemStack ammo = null;
		
		for(int i = 0; i < p.inventory.getSizeInventory(); i++) {
			if(p.inventory.getStackInSlot(i) == null) continue;
			ItemStack stack = p.inventory.getStackInSlot(i);
			Item item = stack.getItem();
			
			if(item instanceof ItemShootable && Item.getIdFromItem(item) == ammoID) {
				ammo = new ItemStack(item, 1);
				ItemBullet b = (ItemBullet)item;
				int currentRounds = b.type.roundsPerItem - stack.getItemDamage();
				currentRounds *= stack.stackSize;
				allRounds += currentRounds;
				maxRounds = b.type.roundsPerItem;

				p.inventory.setInventorySlotContents(i, null);
			}
		}	
		if(ammo == null || allRounds == 0 || maxRounds < 1) return;
		int rest = allRounds % maxRounds,
		amount = (int)((allRounds - rest) / maxRounds);
		
		for(int i = 0; i < amount; i++) {
			Main.putItemInPlayerTopInv(p, ammo.copy());
		}	
		if(rest > 0) {
			ammo.setItemDamage((maxRounds-rest));
			Main.putItemInPlayerTopInv(p, ammo);
		}
	}
	
}
