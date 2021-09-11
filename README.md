# Flans-API
This API is meant for an easy connection between Bukkit/Spiggot plugins and Flans Mod, but it's still very experimental.

# How does it work?
You must add the API to your libraries to gain access to all methods. You must also upload the API onto your minecraft server as well as the client ("mods"-folder).

# Example code
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
            //Checks if the damager is a bullet and then returns the name of the player who fired it
            
		BulletHandler bHandler = new BulletHandler();
		String owner = bHandler.getBulletOwner(e.getDamager().getUniqueId().toString());
            if(owner == null) return;
    	
		//Do stuff...
	}
	
> Example two
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("test")) {
					if(p.hasPermission("flansapi.test")) {
						VehicleHandler vHandler = new VehicleHandler();
						
						//The player is standing on a chest thus these coordinates are of the chest
						int cx = p.getLocation().getBlock().getX(),
						cy = p.getLocation().getBlock().getY(),
						cz = p.getLocation().getBlock().getZ();
						
						vHandler.spawnVehicle(p.getLocation().getWorld().getName(), cx, cy, cz, "vehicle", (cx+15), (cy+10), cz);
						p.sendMessage("§6You successfully spawned a vehicle!");
					}
				}
			} 
		} 
		return false;
	}

# Explanation of all methods
- BulletHandler:
  - getBulletOwner(String uuid)
    - uuid : UUID of the bullet / entity that might be a bullet
    
- VehicleHandler:
  - putPlayerInDriveable(String playerUUID, String vehicleUUID)
    - playerUUID : UUID of player
    - vehicleUUID : UUID of vehicle to put the player in
      
  - spawnDriveable(String worldName, int cx, int cy, int cz, String type, int sx, int sy, int sz, float rotation)
    > Explanation: When this method is called it will check if there is a chest at the given coordinates and if so graps the first item out of it that isn't null 
    - worldName : name of the world to spawn the driveable in
    -  cx : x coordinate of chest
    -  cy : y coordinate of chest
    -  cz : z coordinate of chest
    -  type : (either "vehicle" or "plane")
    -  sx : x coordinate of where to spawn the driveable
    -  sy : y coordinate of where to spawn the driveable
    -  sz : z coordinate of where to spawn the driveable
    - rotation : rotation of the spawned driveable
