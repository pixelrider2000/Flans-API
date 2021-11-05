package flansapi.handlers;

import com.flansmod.common.guns.EntityGrenade;

import flansapi.main.Main;
import net.minecraft.entity.Entity;

public class GrenadeHandler {

	public boolean isGrenade(String uuid) {
		Entity e = Main.entityFromUUID(uuid);
		return (e instanceof EntityGrenade);
	}
	
}
