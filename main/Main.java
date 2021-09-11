package flansapi.main;

import java.util.HashMap;
import java.util.List;

import com.flansmod.common.guns.EntityBullet;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.registry.GameRegistry;
import flansapi.commands.TestCommand;
import flansapi.handlers.BulletHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "fapi", name = "FlansAPI", version = "1.0")

public class Main {
	
	public static HashMap<EntityPlayer, String> lastSpawned = new HashMap<EntityPlayer, String>();
	
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

