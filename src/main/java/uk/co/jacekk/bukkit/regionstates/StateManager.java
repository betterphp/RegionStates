package uk.co.jacekk.bukkit.regionstates;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.World;

import uk.co.jacekk.bukkit.baseplugin.BaseObject;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class StateManager extends BaseObject<RegionStates> {
	
	private HashMap<UUID, HashMap<String, HashMap<String, RegionState>>> states;
	
	public StateManager(RegionStates plugin){
		super(plugin);
		
		this.states = new HashMap<UUID, HashMap<String,HashMap<String,RegionState>>>();
	}
	
	public void loadAll(World world){
		File worldDir = new File(this.plugin.getBaseDir(), world.getUID().toString());
		
		if (worldDir.exists()){
			HashMap<String, HashMap<String, RegionState>> regions = this.states.get(world.getUID());
			
			if (regions == null){
				regions = new HashMap<String, HashMap<String,RegionState>>();
				this.states.put(world.getUID(), regions);
			}
			
			for (String regionID : worldDir.list()){
				File regionDir = new File(worldDir, regionID);
				ProtectedRegion region = this.plugin.getWorldGuard().getRegionManager(world).getRegion(regionID);
				
				if (region == null){
					continue;
				}
				
				HashMap<String, RegionState> states = regions.get(regionID);
				
				if (states == null){
					states = new HashMap<String, RegionState>();
					regions.put(regionID, states);
				}
				
				for (String fileName : regionDir.list()){
					String name = fileName.substring(0, fileName.length() - 10);
					states.put(name, new RegionState(this.plugin, world, region, name));
				}
			}
		}
	}
	
	public void add(RegionState state) throws IOException, DataException {
		HashMap<String, HashMap<String, RegionState>> regions = this.states.get(state.getWorld().getUID());
		
		if (regions == null){
			regions = new HashMap<String, HashMap<String,RegionState>>();
			this.states.put(state.getWorld().getUID(), regions);
		}
		
		HashMap<String, RegionState> states = regions.get(state.getRegion().getId());
		
		if (states == null){
			states = new HashMap<String, RegionState>();
			regions.put(state.getRegion().getId(), states);
		}
		
		EditSession session = new EditSession(new BukkitWorld(state.getWorld()), Integer.MAX_VALUE);
		CuboidClipboard clipboard = new CuboidClipboard(state.getRegion().getMaximumPoint().subtract(state.getRegion().getMinimumPoint()).add(1, 1, 1), state.getRegion().getMinimumPoint());
		clipboard.copy(session);
		
		state.getSchematicFile().getParentFile().mkdirs();
		SchematicFormat.MCEDIT.save(clipboard, state.getSchematicFile());
		
		states.put(state.getName(), state);
	}
	
	public void remove(RegionState state){
		HashMap<String, HashMap<String, RegionState>> regions = this.states.get(state.getWorld().getUID());
		
		if (regions == null){
			return;
		}
		
		HashMap<String, RegionState> states = regions.get(state.getRegion().getId());
		
		if (states == null){
			return;
		}
		
		state.getSchematicFile().delete();
		
		states.remove(state.getName());
	}
	
	public List<RegionState> getStates(World world, ProtectedRegion region){
		HashMap<String, HashMap<String, RegionState>> regions = this.states.get(world.getUID());
		
		if (regions == null){
			return Collections.emptyList();
		}
		
		HashMap<String, RegionState> states = regions.get(region.getId());
		
		if (states == null){
			return Collections.emptyList();
		}
		
		return new ArrayList<RegionState>(states.values());
	}
	
	public RegionState getState(World world, ProtectedRegion region, String name){
		HashMap<String, HashMap<String, RegionState>> regions = this.states.get(world.getUID());
		
		if (regions == null){
			return null;
		}
		
		HashMap<String, RegionState> states = regions.get(region.getId());
		
		if (states == null){
			return null;
		}
		
		return states.get(name);
	}
	
}
