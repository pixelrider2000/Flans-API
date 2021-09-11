package flansapi.commands;

import flansapi.handlers.VehicleHandler;
import flansapi.main.Main;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class TestCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "test";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/test";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(sender instanceof EntityPlayer) {			
			VehicleHandler vHandler =  new VehicleHandler();
			EntityPlayer p = (EntityPlayer) sender;	
			
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("put")) {
					vHandler.putPlayerInDriveable(p.getUniqueID().toString(), Main.lastSpawned.get(p));
					p.addChatMessage(new ChatComponentText("You should be sitting now!"));
				}
			} else {							
				int cx = p.getPlayerCoordinates().posX, 
				cy = p.getPlayerCoordinates().posY, 
				cz = p.getPlayerCoordinates().posZ;
							
				String uuid = vHandler.spawnDriveable(p.getEntityWorld().getWorldInfo().getWorldName(), cx, (cy-1), cz, "vehicle", (cx+15), cy, cz, 90);
				p.addChatMessage(new ChatComponentText("You spawned a vehicle!"));
				Main.lastSpawned.put(p, uuid);	
			}
		}
	}

}
