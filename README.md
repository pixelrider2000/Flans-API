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
