package flansapi.handlers;

import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.EntityPlane;
import com.flansmod.common.driveables.EntitySeat;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.driveables.ItemPlane;
import com.flansmod.common.driveables.ItemVehicle;

import flansapi.main.Main;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class VehicleHandler {

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
	
	public String spawnDriveable(String worldName, int cx, int cy, int cz, String type, int sx, int sy, int sz, float rotation) {
		World world = null;
		
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
	
}
