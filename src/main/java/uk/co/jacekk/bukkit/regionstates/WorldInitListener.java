package uk.co.jacekk.bukkit.regionstates;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;

import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;

public class WorldInitListener extends BaseListener<RegionStates> {
	
	public WorldInitListener(RegionStates plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onWorldInit(WorldInitEvent event){
		this.plugin.getStateManager().loadAll(event.getWorld());
	}
	
}
