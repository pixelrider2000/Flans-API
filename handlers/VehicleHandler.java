package flansapi.handlers;

import java.util.ArrayList;
import java.util.List;

import com.flansmod.common.driveables.DriveableData;
import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.EntityPlane;
import com.flansmod.common.driveables.EntitySeat;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.driveables.ItemPlane;
import com.flansmod.common.driveables.ItemVehicle;

import flansapi.main.Main;
import flansapi.main.Main.InventorySection;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class VehicleHandler {
	
	public void removePlayerFromDriveable(String playerName) {
		EntityPlayer p = Main.entityPlayerFromName(playerName);
		p.mountEntity(null);
	}
	
	public void removeAllFromDriveable(String driveableUUID) {
		if(!isDriveable(driveableUUID)) return;
		EntityDriveable d = (EntityDriveable) Main.entityFromUUID(driveableUUID);
		
		for(int i = 0; i < d.seats.length; i++) {
			EntitySeat s = d.seats[i];
			
			if(s.riddenByEntity != null) 
				s.riddenByEntity.mountEntity(null);		
		}
	}
	
	public String seatFromDriveable(String driveableUUID) {
		if(!isDriveable(driveableUUID)) return null;
		return ((EntityDriveable) Main.entityFromUUID(driveableUUID)).seats[0].getUniqueID().toString();
	}
	
	public String driveableFromSeat(String seatUUID) {
		if(!isSeat(seatUUID)) return null;
		EntitySeat seat = (EntitySeat) Main.entityFromUUID(seatUUID);
		return seat.driveable.getUniqueID().toString();
	}
	
	public void setFuelInTank(String seatUUID, float fuelAmount) {
		if(!isSeat(seatUUID)) return;
		EntitySeat s = (EntitySeat)Main.entityFromUUID(seatUUID);
		s.driveable.getDriveableData().fuelInTank = fuelAmount;
	}
	
	public float getFuelInTank(String seatUUID) {
		if(!isSeat(seatUUID)) return 0.0f;
		EntitySeat s = (EntitySeat)Main.entityFromUUID(seatUUID);
		return s.driveable.getDriveableData().fuelInTank;
	}
	
	public float getFuelTankSize(String seatUUID) {
		if(!isSeat(seatUUID)) return 0.0f;
		EntitySeat s = (EntitySeat)Main.entityFromUUID(seatUUID);
		return s.driveable.getDriveableType().fuelTankSize;
	}
	
	public void putItemsInDriveable(String seatUUID, int[][][] ids, InventorySection inventorySection, boolean replaceExistingItems) {
		if(!isSeat(seatUUID)) return;
		DriveableData data = (((EntitySeat)Main.entityFromUUID(seatUUID)).driveable).driveableData;
		
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		for(int[][] id : ids) 
			stacks.add(new ItemStack(Item.getItemById(id[0][0]), id[0][2], id[0][1]));
		
		ItemStack[] container = null;
		int maxSize = 0;
		
		if(inventorySection == InventorySection.GUNS) {
			container = data.ammo;
			maxSize = data.numGuns;
		} else if(inventorySection == InventorySection.MINES) {
			container = data.bombs;
			maxSize = data.numBombs;
		} else if(inventorySection == InventorySection.SHELLS) {
			container = data.missiles;
			maxSize = data.numMissiles;
		} else {
			container = data.cargo;
			maxSize = data.numCargo;
		}
		int posInStacks = 0;
		for(int i = 0; i < maxSize; i++) {		
			if(posInStacks == stacks.size()) return;
			
			if(replaceExistingItems) {
				container[i] = stacks.get(posInStacks);
				posInStacks++;
			} else {
				if(container[i] == null) {
					container[i] = stacks.get(posInStacks);
					posInStacks++;
				}
			}
		}
	}
	
	public boolean isSeat(String entityUUID) {
		Entity e = Main.entityFromUUID(entityUUID);
		return (e instanceof EntitySeat);
	}
	
	public boolean isDriveable(String entityUUID) {
		Entity e = Main.entityFromUUID(entityUUID);
		return (e instanceof EntityDriveable);
	}
	
	public String[] getPassengers(String driveableUUID) {
		if(!isDriveable(driveableUUID)) return null;
		EntityDriveable d = (EntityDriveable) Main.entityFromUUID(driveableUUID);
		
		List<String> riders = new ArrayList<String>();
		
		for(int i = 0; i < d.seats.length; i++) {
			if(d.seats[i].riddenByEntity != null) {
				riders.add(d.seats[i].riddenByEntity.getUniqueID().toString());
			}
		}
		return riders.toArray(new String[0]);
	}
	
	public void killDriveable(String entityUUID) {
		if(isSeat(entityUUID)) {
			((EntitySeat)Main.entityFromUUID(entityUUID)).driveable.setDead();
		} else if(isDriveable(entityUUID)) {
			((EntityDriveable)Main.entityFromUUID(entityUUID)).setDead();
		}		
	}
	
	public String getRiddenEntity(String playerName) {
		EntityPlayer p = Main.entityPlayerFromName(playerName);
		if(p == null) return null;
		if(p.ridingEntity == null) return null;
		return p.ridingEntity.getUniqueID().toString();
	}
	
	public void putPlayerInDriveable(String playerUUID, String vehicleUUID) {
		Entity e = Main.entityFromUUID(vehicleUUID);
		if(e == null) return;
		if(!(e instanceof EntityDriveable)) return;
		
		EntityDriveable driveable = (EntityDriveable) e;
		if(driveable.seats == null) return;
		EntitySeat[] seats = driveable.seats;	
		if(seats.length == 0) return;
		if(seats[0].isDead || seats[0].riddenByEntity != null) return;
		
		EntityPlayer p = (EntityPlayer) Main.entityFromUUID(playerUUID);
		p.mountEntity(seats[0]);
	}
	
	public String spawnDriveable(int itemID, String worldName, int sx, int sy, int sz, float rotation) {
		World world = null;
		rotation += 90;
		
		for(World w : MinecraftServer.getServer().worldServers) {
			if(w.getWorldInfo().getWorldName().equals(worldName)) {				
				world = w;
				break;
			}
		}
		if(world == null) return null;	
		Item i = Item.getItemById(itemID);
		ItemStack is = new ItemStack(i);
		
		if(i instanceof ItemVehicle) {
			ItemVehicle vehicle = (ItemVehicle)i;
			EntityVehicle entVehicle = (EntityVehicle) vehicle.spawnVehicle(world, sx, sy, sz, is);
			entVehicle.rotateYaw(rotation);
			return entVehicle.getUniqueID().toString();
		} else if(i instanceof ItemPlane) {
			ItemPlane plane = (ItemPlane)i;
			EntityPlane entPlane = (EntityPlane) plane.spawnPlane(world, sx, sy, sz, is);
			entPlane.rotateYaw(rotation);
			return entPlane.getUniqueID().toString();
		}
		return null;
	}
	
	public String spawnDriveable(String worldName, int cx, int cy, int cz, String type, int sx, int sy, int sz, float rotation) {
		World world = null;
		rotation += 90;
		
		for(World w : MinecraftServer.getServer().worldServers) {
			if(w.getWorldInfo().getWorldName().equals(worldName)) {				
				world = w;
				break;
			}
		}
		if(world == null) return null;
		
		if(world.getBlock(cx, cy, cz) instanceof BlockChest) {
			TileEntityChest te = (TileEntityChest) world.getTileEntity(cx, cy, cz);
			
			for (int i = 0; i < te.getSizeInventory(); i++) {
				ItemStack is = te.getStackInSlot(i);
				if (is != null) {
					if(type.equalsIgnoreCase("vehicle")) {
						ItemVehicle vehicle = (ItemVehicle)is.getItem();
						EntityVehicle entVehicle = (EntityVehicle) vehicle.spawnVehicle(world, sx, sy, sz, is);
						entVehicle.rotateYaw(rotation);
						return entVehicle.getUniqueID().toString();
					} else if(type.equalsIgnoreCase("plane")) {
						ItemPlane plane = (ItemPlane)is.getItem();
						EntityPlane entPlane = (EntityPlane) plane.spawnPlane(world, sx, sy, sz, is);
						entPlane.rotateYaw(rotation);
						return entPlane.getUniqueID().toString();
					}
					break;
				}
			}
		} else 
			System.out.println("Block not chest!");
		return null;
	}
	
	public void setRotation(String entityUUID, float rotation) {
		Entity e = Main.entityFromUUID(entityUUID);
		if(isDriveable(entityUUID)) {
			((EntityDriveable)e).rotateYaw(rotation);;
		}
	}
	
	public float getRotation(String entityUUID) {
		Entity e = Main.entityFromUUID(entityUUID);
		if(isDriveable(entityUUID)) {
			return ((EntityDriveable)e).axes.getYaw();
		}
		return 0.0f;
	}
	
}
