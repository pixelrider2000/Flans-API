package flansapi.main;

import java.util.List;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import flansapi.commands.TestCommand;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

@Mod(modid = "fapi", name = "FlansAPI", version = "1.0")

public class Main {
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent e) {
		ServerCommandManager cm = (ServerCommandManager) e.getServer().getCommandManager();
		cm.registerCommand(new TestCommand());
	}
	
	public static Entity entityFromUUID(String uuid) {
		List<Entity> entityList = MinecraftServer.getServer().worldServerForDimension(0).loadedEntityList;
		System.out.println("Size: " + entityList.size());
		
		for(Entity e : entityList) {
			if(e.getUniqueID().toString().equals(uuid)) return e;
		}
		return null;
	}
	
}

