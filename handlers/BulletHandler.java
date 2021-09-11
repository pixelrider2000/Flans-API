package flansapi.handlers;

import java.util.HashMap;
import java.util.List;

import com.flansmod.common.FlansMod;
import com.flansmod.common.guns.BulletType;
import com.flansmod.common.guns.EntityBullet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import flansapi.main.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

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
	
}
