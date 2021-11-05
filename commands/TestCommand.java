package flansapi.commands;

import flansapi.handlers.BulletHandler;
import flansapi.handlers.ItemHandler;
import flansapi.handlers.VehicleHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

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
			EntityPlayer p = (EntityPlayer) sender;
			BulletHandler bHandler = new BulletHandler();
			ItemHandler itemHandler = new ItemHandler();
			VehicleHandler vHandler = new VehicleHandler();
			//int id = Item.getIdFromItem(p.getHeldItem().getItem());			
			
		}
	}

}
