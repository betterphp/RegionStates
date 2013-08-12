package uk.co.jacekk.bukkit.regionstates;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.World;

import uk.co.jacekk.bukkit.baseplugin.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.scheduler.BaseTask;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionStates extends BasePlugin {
	
	public WorldGuardPlugin worldGuard;
	public WorldEditPlugin worldEdit;
	
	public ArrayList<RegionState> states;
	
	@Override
	public void onEnable(){
		super.onEnable(true);
		
		this.worldGuard = (WorldGuardPlugin) this.pluginManager.getPlugin("WorldGuard");
		this.worldEdit = (WorldEditPlugin) this.pluginManager.getPlugin("WorldEdit");
		
		this.permissionManager.registerPermissions(Permission.class);
		this.commandManager.registerCommandExecutor(new RegionStateCommandExecutor(this));
		
		this.states = new ArrayList<RegionState>();
		
		this.scheduler.runTask(this, new BaseTask<RegionStates>(this){
			
			@Override
			public void run(){
				for (File file : this.plugin.baseDir.listFiles()){
					this.plugin.states.add(new RegionState(this.plugin, file));
				}
			}
			
		});
	}
	
	public ArrayList<RegionState> getStates(World world, ProtectedRegion region){
		ArrayList<RegionState> states = new ArrayList<RegionState>();
		
		for (RegionState state : this.states){
			if (state.getWorld().equals(world) && state.getRegion().equals(region)){
				states.add(state);
			}
		}
		
		return states;
	}
	
	public RegionState getState(World world, ProtectedRegion region, String name){
		for (RegionState state : this.states){
			if (state.getWorld().equals(world) && state.getRegion().equals(region) && state.getName().equals(name)){
				return state;
			}
		}
		
		return null;
	}
	
}
