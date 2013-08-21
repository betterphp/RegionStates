package uk.co.jacekk.bukkit.regionstates;

import org.bukkit.World;

import uk.co.jacekk.bukkit.baseplugin.BasePlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class RegionStates extends BasePlugin {
	
	private WorldGuardPlugin worldGuard;
	private WorldEditPlugin worldEdit;
	
	private StateManager stateManager;
	
	@Override
	public void onEnable(){
		super.onEnable(true);
		
		this.worldGuard = (WorldGuardPlugin) this.pluginManager.getPlugin("WorldGuard");
		this.worldEdit = (WorldEditPlugin) this.pluginManager.getPlugin("WorldEdit");
		
		this.stateManager = new StateManager(this);
		
		this.permissionManager.registerPermissions(Permission.class);
		this.commandManager.registerCommandExecutor(new RegionStateCommandExecutor(this));
		
		for (World world : this.server.getWorlds()){
			this.stateManager.loadAll(world);
		}
		
		this.pluginManager.registerEvents(new WorldInitListener(this), this);
	}
	
	public WorldGuardPlugin getWorldGuard(){
		return this.worldGuard;
	}
	
	public WorldEditPlugin getWorldEdit(){
		return this.worldEdit;
	}
	
	public StateManager getStateManager(){
		return this.stateManager;
	}
	
}
