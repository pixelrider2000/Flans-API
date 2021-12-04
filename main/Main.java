package flansapi.main;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import flansapi.commands.TestCommand;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

@Mod(modid = Main.MODID, name = "FlansAPI", version = "1.3")

public class Main {
	
	public enum InventorySection {
		CARGO, GUNS, SHELLS, MINES;
	}
	
	public static final String MODID = "flansapi";
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		//MinecraftForge.EVENT_BUS.register(new EntityHandler());
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent e) {
		ServerCommandManager cm = (ServerCommandManager) e.getServer().getCommandManager();
		cm.registerCommand(new TestCommand());
	}
	
	public static EntityPlayer entityPlayerFromName(String playerName) {
		for(World w : MinecraftServer.getServer().worldServers) {
			if(w.getPlayerEntityByName(playerName) != null) {
				return w.getPlayerEntityByName(playerName);
			}
		}
		return null;
	}
	
	public static Entity entityFromUUID(String uuid) {
		for(World w : MinecraftServer.getServer().worldServers) {		
			for(Object o: w.loadedEntityList) {
				if(o instanceof Entity) {
					Entity e = (Entity) o;
					if(e.getUniqueID().toString().equals(uuid)) return e;
				}
			}
		}		
		return null;
	}
	
	public static void putItemInPlayerTopInv(EntityPlayer p, ItemStack item) {
		for(int j = 9; j < p.inventory.getSizeInventory(); j++) {
			ItemStack stack = p.inventory.getStackInSlot(j);
			if(stack == null) {
				p.inventory.setInventorySlotContents(j, item);
				break;
			} else if(stack.getItem().equals(item.getItem()) && stack.getMaxStackSize() > stack.stackSize && stack.getItemDamage() == item.getItemDamage()) {
				stack.stackSize++;
				p.inventory.setInventorySlotContents(j, stack);
				break;
			}
		}
	}
	
}

